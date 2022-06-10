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
       /* @Test
        @DisplayName("once, then not throwing IllegalStateException")
        public void whenCreatePoolOnceThenNotThrowsIllegalStateException()
        {
            assertDoesNotThrow(ConnectionPool::getInstance);
        }
        @Test
        @DisplayName("then self created counter increments")
        public void whenCreatePoolThenReferenceCounterIncrements()
        {
            assertEquals(1, ConnectionPool.getNewReferencesCounter());
        }
        @Test
        @DisplayName("twice, then throwing IllegalStateException")
        public void whenCreatePoolTwiceThenThrowIllegalStateException()
        {
            IllegalStateException thrown = assertThrows(IllegalStateException.class, ConnectionPool::getInstance);
            assertEquals("maximum 1 instance of company pool allowed", thrown.getMessage());
        }*/
    }


}