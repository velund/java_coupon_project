package com.velundkvz;

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

    public int getNewInstancesCounter()
    {
        return newInstances;
    }
}
