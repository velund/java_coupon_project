package com.velundkvz.common.connection_pool;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.velundkvz.definitions.ConnectionDefinitions.*;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.exceptions.BadTypeConnectionException;
import com.velundkvz.exceptions.PoolNotExistsException;

public enum DBConnections
{
    COMPANY_CONNECTIONS(CONNECTION_TYPE.COMPANY),
    CLIENT_CONNECTIONS(CONNECTION_TYPE.CLIENT),
    ADMIN_CONNECTIONS(CONNECTION_TYPE.ADMIN);

    private CONNECTION_TYPE connectionType;
    private ConnectionPoolImpl connectionPool;

    DBConnections(CONNECTION_TYPE ct)
    {
        this.connectionType = ct;
    }
    public synchronized ConnectionPool getPool()
    {
        if (connectionPool == null)
        {
            connectionPool = new ConnectionPoolImpl(connectionType);
        }
        return connectionPool;
    }

    public synchronized void destroyPool()
    {
        if ( connectionPool == null )
        {
            return;
        }
        connectionPool.closeAllConnections();
        connectionPool.queue = null;
        connectionPool = null;
        System.gc();
    }

    private enum CONNECTION_TYPE
    {
        COMPANY(COMPANY_USER, PASSWORD, MAX_COMPANY_CONNECTIONS),
        CLIENT(CLIENT_USER,PASSWORD, MAX_CLIENT_CONNECTIONS),
        ADMIN(ADMIN_USER, PASSWORD, MAX_ADMIN_CONNECTIONS);
        private final int maxConnections;
        private final String name;
        private final String pswd;
        private Properties info;

        CONNECTION_TYPE(String name, String pswd, int maxConnections)
        {
            this.maxConnections = maxConnections;
            this.name = name;
            this.pswd = pswd;
        }

        private Connection createConnection() throws SQLException
        {
            setInfo(name, pswd);
            Connection c =  DriverManager.getConnection(URL + "/" + DB, info);
            c.setClientInfo(info);
            return c;
        }
        private void setInfo(String user, String password)
        {
            info = new Properties();
            info.put("user", user);
            info.put("password", password);
        }

    }

    private static final class ConnectionPoolImpl implements ConnectionPool
    {
        CONNECTION_TYPE connectionType;
        private BlockingQueue<Connection> queue;

        public ConnectionPoolImpl(CONNECTION_TYPE connectionType)
        {
            this.connectionType = connectionType;
            int failedConnections = 0;
            queue = new LinkedBlockingDeque<>(connectionType.maxConnections);

            for (int i = 0; i < connectionType.maxConnections; ++i)
            {
                try
                {
                    queue.add(connectionType.createConnection());
                } catch (SQLException e)
                {
                    System.err.printf((CONNECTION_FAILED_FORMAT_MSG) + "%n", connectionType.name);
                    e.printStackTrace();
                    ++failedConnections;
                }
            }
            if (failedConnections != 0)
            {
                System.err.printf((NUM_OF_ACTIVE_CONNECTIONS_FORMAT_MSG) + "%n", connectionType.maxConnections - failedConnections);
            }
        }

        @Override
        public synchronized Connection getConnection()
        {
            if ( queue == null )
            {
                throw new PoolNotExistsException(POOL_NOT_EXISTS_EXC_MSG);
            }
            try
            {
                return queue.take();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
                throw new RuntimeException(GET_CONNECTION_INTERRUPTED_MSG);
            }
        }
        @Override
        public synchronized void put(Connection connection)
        {
            if ( queue == null )
            {
                throw new PoolNotExistsException(POOL_NOT_EXISTS_EXC_MSG);
            }
            try
            {
                queue.put(connection);
            } catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        @Override
        public synchronized void putWithValidation(Connection connection)
        {
            if ( queue == null )
            {
                throw new PoolNotExistsException(POOL_NOT_EXISTS_EXC_MSG);
            }
            try
            {
                if (isValidconn(connection))
                {
                    queue.put(connection);
                }else
                {
                    System.err.println(CONNECTION_NOT_ADDED_MSG);
                    throw new BadTypeConnectionException(NOT_APPROPRIATE_CONNECTION_TYPE);
                }
            }catch (SQLException | InterruptedException e )
            {
                e.printStackTrace();
            }
        }

        public synchronized void closeAllConnections()
        {
            if ( queue == null )
            {
                throw new PoolNotExistsException(POOL_NOT_EXISTS_EXC_MSG);
            }
            Connection c;
            try
            {
                while ( (c = queue.poll(WAIT_WHEN_CLOSE_CONNECTIONS, TimeUnit.SECONDS))  != null )
                {
                    try
                    {
                        c.close();
                    } catch (SQLException e)
                    {
                        System.err.println(CLOSE_CONNECTION_EXC_MSG);
                    }
                }
            }catch (InterruptedException e)
            {
                System.err.println(INTERRUPTED_DURING_CLOSE_ALL_CONNECTIONS_MSG);
            }
        }

        public synchronized int validConnections()
        {
            int valids = 0;
            for (Connection c : queue)
            {
                try
                {
                    if ( c.isValid(0) )
                    {
                        ++valids;
                    }
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            return valids;
        }

        private boolean isValidconn(Connection connection) throws SQLException
        {
            return  !connection.isClosed() &&
                    connection.getClientInfo("user").equals(connectionType.name) &&
                    connection.getClientInfo("password").equals(connectionType.pswd);
        }
    }
}
