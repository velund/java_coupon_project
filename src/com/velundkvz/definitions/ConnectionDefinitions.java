package com.velundkvz.definitions;

public final class ConnectionDefinitions
{

    public static final int MAX_COMPANY_CONNECTIONS  = 5;
    public static final int MAX_CLIENT_CONNECTIONS  = 20;
    public static final int MAX_ADMIN_CONNECTIONS  = 5;

    public static final String COMPANY_USER  = "company";
    public static final String CLIENT_USER  = "client";
    public static final String ADMIN_USER  = "krill_admin";
    public static final String PASSWORD  = "15kvz61982!";
    public static final String PORT  = "3306";
    public static final String DB  = "coupon_system";
    public static final String URL = "jdbc:mysql://localhost:" + PORT;
    public static final int WAIT_WHEN_CLOSE_CONNECTIONS = 1;

    public static final String CONNECTION_FAILED_FORMAT_MSG = "failed to connect to db as %s";

    public static final String NUM_OF_ACTIVE_CONNECTIONS_FORMAT_MSG = "some connections failed, num of active: %d";
    public static final String MAX_NEW_INSTANCES_REACHED_FORMAT_MSG = "maximum %d instance of %s pool allowed";
    public static final String GET_CONNECTION_INTERRUPTED_MSG = "interrupted while waiting to get connection from pool";
    public static final String INTERRUPTED_DURING_CLOSE_ALL_CONNECTIONS_MSG = "interrupted during close all connetctions";
    public static final String CLOSE_CONNECTION_EXC_MSG = "problem occurred while closing one of pool connections";
    public static final String POOL_NOT_EXISTS_EXC_MSG = "pool not exists";
    public static final String CONNECTION_NOT_ADDED_MSG = "connection not added";
    public static final String NOT_APPROPRIATE_CONNECTION_TYPE = "tried to put not appropriate connection type for this pool";

    private ConnectionDefinitions()
    {

    }
}
