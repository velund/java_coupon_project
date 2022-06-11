package com.velundkvz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static definitions.ConnectionDefinitions.*;

public enum DBConnections
{
    COMPANY_CONNECTIONS(CONNECTION_OF.COMPANY),
    CLIENT_CONNECTIONS(CONNECTION_OF.CLIENT),
    ADMIN_CONNECTIONS(CONNECTION_OF.ADMIN);
    private CONNECTION_OF connectionOf;
    private ConnectionPool connectionPool;

    DBConnections(CONNECTION_OF ct)
    {
        this.connectionOf = ct;
    }
    public synchronized ConnectionPool getPool()
    {
        if (connectionPool == null)
        {
            connectionPool = new ConnectionPoolImpl(connectionOf);
        }
        return connectionPool;
    }

    public synchronized void destroyPool()
    {
        connectionPool.closeAllConnections();
        connectionPool = null;
        System.gc();
    }

    private enum CONNECTION_OF
    {
        COMPANY(COMPANY_USER, PASSWORD, MAX_COMPANY_CONNECTIONS),
        CLIENT(CLIENT_USER,PASSWORD, MAX_CLIENT_CONNECTIONS),
        ADMIN(ADMIN_USER, PASSWORD, MAX_ADMIN_CONNECTIONS);
        private final int maxConnections;
        private final String name;
        private final String pswd;
        private Properties info;

        CONNECTION_OF(String name, String pswd, int maxConnections)
        {
            this.maxConnections = maxConnections;
            this.name = name;
            this.pswd = pswd;
        }

        private Connection createConnection() throws SQLException
        {
            setInfo(name, pswd);
            Connection c =  DriverManager.getConnection(URL, info);
            c.setClientInfo(info);
            return c;
        }
        private void setInfo(String user, String password)
        {
            info = new Properties();
            info.put("user", user);
            info.put("password", password);
            info.put("databaseName", DB);
        }

    }

    private static final class ConnectionPoolImpl implements ConnectionPool
    {
        CONNECTION_OF connectionOf;
        private final BlockingQueue<Connection> queue;

        public ConnectionPoolImpl(CONNECTION_OF ct)
        {
            this.connectionOf = ct;
            int failedConnections = 0;
            queue = new ArrayBlockingQueue<>(ct.maxConnections);

            for (int i = 0; i < ct.maxConnections; ++i)
            {
                try
                {
                    queue.add(ct.createConnection());
                } catch (SQLException e)
                {
                    System.err.printf((CONNECTION_FAILED_FORMAT_MSG) + "%n", ct.name);
                    e.printStackTrace();
                    ++failedConnections;
                }
            }
            if (failedConnections != 0)
            {
                System.err.printf((NUM_OF_ACTIVE_CONNECTIONS_FORMAT_MSG) + "%n", MAX_COMPANY_CONNECTIONS - failedConnections);
            }
        }

        @Override
        public synchronized Connection getConnection()
        {
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
        public void put(Connection connection)
        {
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
            try
            {
                if (isValidconn(connection))
                {
                    queue.add(connection);
                    return;
                }else
                {
                    System.err.println("connection not added");
                }
            }catch (SQLException e )
            {
                e.printStackTrace();
            }
        }

        private boolean isValidconn(Connection connection) throws SQLException
        {
            return  !connection.isClosed() &&
                    connection.getClientInfo("user").equals(connectionOf.name) &&
                    connection.getClientInfo("password").equals(connectionOf.pswd) &&
                    connection.getClientInfo("databaseName").equals(DB);
        }

        public synchronized void closeAllConnections()
        {
            Connection c;
            try
            {
                while (  (c = queue.poll(TIME_OUT_TO_POLL_WHEN_CLOSE_CONNECTIONS, TimeUnit.SECONDS))  != null )
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
    }
}
