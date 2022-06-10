package com.velundkvz;

public class Main {

    public static void main(String[] args)
    {
        ConnectionPool companyConnPool = DBConnections.COMPANY_CONNECTIONS.getInstance();
        ConnectionPool clientConnPool = DBConnections.CLIENT_CONNECTIONS.getInstance();
        ConnectionPool adminConnPool = DBConnections.ADMIN_CONNECTIONS.getInstance();
        System.out.println(DBConnections.COMPANY_CONNECTIONS.getNewInstancesCounter());
        System.out.println(DBConnections.CLIENT_CONNECTIONS.getNewInstancesCounter());
        System.out.println(DBConnections.ADMIN_CONNECTIONS.getNewInstancesCounter());
        DBConnections.CLIENT_CONNECTIONS.getInstance();
    }
}
