package com.velundkvz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static definitions.ConnectionDefinitions.*;
import static definitions.ConnectionDefinitions.MAX_COMPANY_CONNECTIONS;

public class ConnectionPool
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

    public synchronized ConnectionPool getInstance()
    {
        if (newInstances < maxPoolInstances)
        {
            ++newInstances;
            return new ConnectionPool(name, pswd, maxConnections);
        }
        throw new IllegalStateException(MAX_NEW_INSTANCES_REACHED_FORMAT_MSG.formatted(MAX_COMPANY_POOL_INSTANCES));
    }

    private void setInfo(String user, String password)
    {
        info = new Properties();
        info.put("user", user);
        info.put("password", password);
    }

}
