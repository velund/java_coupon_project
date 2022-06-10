package com.velundkvz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static definitions.ConnectionDefinitions.*;

public enum DBConnections
{
    COMPANY_CONNECTIONS(COMPANY_USER, PASSWORD, MAX_COMPANY_POOL_INSTANCES, MAX_COMPANY_CONNECTIONS),
    CLIENT_CONNECTIONS(CLIENT_USER,PASSWORD, MAX_CLIENT_POOL_INSTANCES, MAX_CLIENT_CONNECTIONS),
    ADMIN_CONNECTIONS(ADMIN_USER, PASSWORD,MAX_ADMIN_POOL_INSTANCES, MAX_ADMIN_CONNECTIONS);
    private final int maxPoolInstances;
    private final int maxConnections;
    public final String name;
    public final String pswd;
    private int newInstances = 0;
    DBConnections(String role_name, String password, int maxPoolInstances, int maxConnections)
    {
        name = role_name;
        pswd = password;
        this.maxPoolInstances = maxPoolInstances;
        this.maxConnections = maxConnections;
    }
    public synchronized ConnectionPool getInstance()
    {
        if (newInstances < maxPoolInstances)
        {
            ++newInstances;
            return new ConnectionPool(name, pswd, maxConnections);
        }
        throw new IllegalStateException(MAX_NEW_INSTANCES_REACHED_FORMAT_MSG.formatted(MAX_COMPANY_POOL_INSTANCES));
    }
    public int getNewInstancesCounter()
    {
        return newInstances;
    }
    public static class ConnectionPool
    {
        private static Properties info;
        private final BlockingQueue<Connection> queue;

        private ConnectionPool(String roleName, String password, int maxConnections)
        {
            int failedConnections = 0;
            queue = new LinkedBlockingDeque<>();
            setInfo(roleName, password);
            for (int i = 0; i < maxConnections; ++i)
            {
                try
                {
                    queue.add(DriverManager.getConnection(URL, info));
                } catch (SQLException e)
                {
                    System.err.printf((CONNECTION_FAILED_FORMAT_MSG) + "%n", roleName);
                    e.printStackTrace();
                    ++failedConnections;
                }
            }
            if (failedConnections != 0)
            {
                System.err.printf((NUM_OF_ACTIVE_CONNECTIONS_FORMAT_MSG) + "%n", MAX_COMPANY_CONNECTIONS - failedConnections);
            }
        }

        private void setInfo(String user, String password)
        {
            info = new Properties();
            info.put("user", user);
            info.put("password", password);
        }
    }
}
