package com.velundkvz;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;
import com.velundkvz.exceptions.BadTypeConnectionException;
import com.velundkvz.exceptions.PoolNotExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionsImplTest
{
    @Nested
    @DisplayName("when create pool:")
    public class WhenCreatePool
    {
        ConnectionPool companyConnPool;
        ConnectionPool clientConnPool;
        ConnectionPool adminConnPool;
        @AfterEach
        public void destroyPools()
        {
            DBConnections.CLIENT_CONNECTIONS.destroyPool();
            DBConnections.COMPANY_CONNECTIONS.destroyPool();
            DBConnections.ADMIN_CONNECTIONS.destroyPool();
        }
        @Test
        @DisplayName("once, then not throwing IllegalStateException")
        public void whenCreatePoolOnceThenNotThrowsIllegalStateException()
        {
            assertAll
            (
                ()-> assertDoesNotThrow(()->{clientConnPool = DBConnections.CLIENT_CONNECTIONS.getPool();}),
                ()-> assertDoesNotThrow(()->{adminConnPool = DBConnections.ADMIN_CONNECTIONS.getPool();}),
                ()-> assertDoesNotThrow(()->{companyConnPool = DBConnections.COMPANY_CONNECTIONS.getPool();})
            );
        }
        @Test
        @DisplayName("when Pool Created Twice Then Get Same Pool")
        public void whenPoolCreatedTwiceThenGetSamePool()
        {
            ConnectionPool pool1 = DBConnections.CLIENT_CONNECTIONS.getPool();
            ConnectionPool pool2 = DBConnections.CLIENT_CONNECTIONS.getPool();
            assertEquals(pool1, pool2);
        }
        @Test
        @DisplayName("when Pool Destroyed And Created Then Get Not Same Pool")
        public void whenPoolDestroyedAndCreatedThenGetNotSamePool()
        {
            ConnectionPool pool3 = DBConnections.CLIENT_CONNECTIONS.getPool();
            DBConnections.CLIENT_CONNECTIONS.destroyPool();
            ConnectionPool pool4 = DBConnections.CLIENT_CONNECTIONS.getPool();
            assertAll
            (
                ()->assertNotEquals(null, pool3),
                ()->assertNotEquals(null, pool4),
                ()->assertNotEquals(pool3, pool4)
            );
        }
        @Test
        @DisplayName("when Pool Exists Then Get Connection Does Not Throw PoolNotExists Exception")
        public void whenPoolExistsThenGetConnectionDoesNotThrowPoolNotExistsException()
        {
            ConnectionPool pool = DBConnections.CLIENT_CONNECTIONS.getPool();
            assertDoesNotThrow(pool::getConnection);
        }
        @Test
        @DisplayName("when Pool Destroyed Then Get Connection Throws Pool Not Exists Exception")
        public void whenPoolDestroyedThenGetConnectionThrowsPoolNotExistsException()
        {
            ConnectionPool pool = DBConnections.CLIENT_CONNECTIONS.getPool();
            DBConnections.CLIENT_CONNECTIONS.destroyPool();
            assertThrows(PoolNotExistsException.class, pool::getConnection);
        }
        @Test
        @DisplayName("when Pool Destroyed Then put Throws Pool Not Exists Exception")
        public void whenPoolDestroyedThenputThrowsPoolNotExistsException()
        {
            ConnectionPool pool = DBConnections.CLIENT_CONNECTIONS.getPool();
            Connection connection = pool.getConnection();
            DBConnections.CLIENT_CONNECTIONS.destroyPool();
            assertThrows(PoolNotExistsException.class, ()->pool.put(connection));
        }
        @Test
        @DisplayName("when Pool Destroyed Then put with validation Throws Pool Not Exists Exception")
        public void whenPoolDestroyedThenPutWithValidationThrowsPoolNotExistsException()
        {
            ConnectionPool pool = DBConnections.CLIENT_CONNECTIONS.getPool();
            Connection connection = pool.getConnection();
            DBConnections.CLIENT_CONNECTIONS.destroyPool();
            assertThrows(PoolNotExistsException.class, ()->pool.putWithValidation(connection));
        }
        @Test
        @DisplayName("when Put Connection Not Of The Same Type Then Throws BadTypeConnectionException")
        public void whenPutConnectionNotOfTheSameTypeThenThrowsBadTypeConnectionException()
        {
            ConnectionPool clientCP = DBConnections.CLIENT_CONNECTIONS.getPool();
            ConnectionPool companyCP = DBConnections.COMPANY_CONNECTIONS.getPool();
            Connection clientConnection = clientCP.getConnection();
            Connection companyConnection = companyCP.getConnection();
            assertAll
            (
                ()->assertThrows(BadTypeConnectionException.class, ()->clientCP.putWithValidation(companyConnection)),
                ()->assertDoesNotThrow(()->companyCP.putWithValidation(companyConnection)),
                ()->assertDoesNotThrow(()->clientCP.putWithValidation(clientConnection))
            );
        }
        @Test
        @DisplayName("when pool created, then valid connections numbers are:\n 5 for company" +
                " and admin and 20 for client pools")
        public void whenPoolCreatedThenValidConnectionsNumbersCorrect()
        {
            ConnectionPool p1 = DBConnections.CLIENT_CONNECTIONS.getPool();
            ConnectionPool p2 = DBConnections.ADMIN_CONNECTIONS.getPool();
            ConnectionPool p3 = DBConnections.COMPANY_CONNECTIONS.getPool();
            assertAll
            (
                    ()->assertEquals(20, p1.validConnections()),
                    ()->assertEquals(5, p2.validConnections()),
                    ()->assertEquals(5, p3.validConnections())
            );
        }

    }
}