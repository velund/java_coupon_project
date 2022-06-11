package com.velundkvz;

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
    }
}
