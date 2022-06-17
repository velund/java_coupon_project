package com.velundkvz.common.connection_pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import static com.velundkvz.definitions.ConnectionDefinitions.DB;
import static com.velundkvz.definitions.ConnectionDefinitions.URL;

public class Main {

    public static void main(String[] args) throws Exception
    {
        ConnectionPool clientConnPool = DBConnections.CLIENT_CONNECTIONS.getPool();
        Properties properties = new Properties();
        properties.put("user", "krill_admin");
        properties.put("password", "15kvz61982!");
        Connection c =DriverManager.getConnection(URL + "/" + DB, properties);
        PreparedStatement ps = c.prepareStatement("drop table if exists t");
         ps.executeUpdate();



    }
}
