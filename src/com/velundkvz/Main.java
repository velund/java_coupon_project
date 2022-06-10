package com.velundkvz;

public class Main {

    public static void main(String[] args)
    {
        DBConnections.ConnectionPool companyPool = DBConnections.COMPANY_CONNECTIONS.getInstance();
        DBConnections.CLIENT_CONNECTIONS.getInstance();
        DBConnections.ADMIN_CONNECTIONS.getInstance();
        System.out.println(DBConnections.COMPANY_CONNECTIONS.getNewInstancesCounter());
        System.out.println(DBConnections.CLIENT_CONNECTIONS.getNewInstancesCounter());
        System.out.println(DBConnections.ADMIN_CONNECTIONS.getNewInstancesCounter());
    }
}
