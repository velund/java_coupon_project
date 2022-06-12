package com.velundkvz.common.connection_pool;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws Exception
    {
        ConnectionPool companyConnPool = DBConnections.COMPANY_CONNECTIONS.getPool();
        ConnectionPool clientConnPool = DBConnections.CLIENT_CONNECTIONS.getPool();
        ConnectionPool adminConnPool = DBConnections.ADMIN_CONNECTIONS.getPool();
        Connection c = companyConnPool.getConnection();
        Connection a = adminConnPool.getConnection();
        companyConnPool.putWithValidation(c);
        companyConnPool.putWithValidation(a);
        DBConnections.COMPANY_CONNECTIONS.destroyPool();
        Connection d = companyConnPool.getConnection();


    }
}
