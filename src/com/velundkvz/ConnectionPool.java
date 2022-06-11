package com.velundkvz;

import java.sql.Connection;

public interface ConnectionPool
{
    void closeAllConnections();
    Connection getConnection();
    void put(Connection connection);
    void putWithValidation(Connection connection);
}
