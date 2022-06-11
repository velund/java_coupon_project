package com.velundkvz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionsImplTest
{
    @Nested
    @DisplayName("when create pool:")
    public class WhenCreatePool
    {
        @Test
        @DisplayName("once, then not throwing IllegalStateException")
        public void whenCreatePoolOnceThenNotThrowsIllegalStateException()
        {
            assertAll
            (
                ()-> assertDoesNotThrow(()->{DBConnections.CLIENT_CONNECTIONS.getPool();}),
                ()-> assertDoesNotThrow(()->{DBConnections.ADMIN_CONNECTIONS.getPool();}),
                ()-> assertDoesNotThrow(()->{DBConnections.COMPANY_CONNECTIONS.getPool();})
            );
        }
    }
}