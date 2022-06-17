package com.velundkvz.data.model;

import com.velundkvz.definitions.schema.model.Customer;
import com.velundkvz.exceptions.InvalidCustomerBuildException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest
{
    @Test
    @DisplayName("when customer created it is not null")
    public void whenCustomerCreatedItIsNotNull()
    {
        Customer c = new Customer.CustomerBuilder().build();
        assertTrue(Objects.nonNull(c));
    }
    @Test
    @DisplayName("when default customer created all getters work")
    public void whenDefaultCustomerCreatedAllGettersWork()
    {
        Customer c = new Customer.CustomerBuilder().build();
        assertAll
                (
                        ()->assertEquals(0, c.getId()),
                        ()->assertEquals(null, c.getFirst_name()),
                        ()->assertEquals(null, c.getLast_name()),
                        ()->assertEquals(null, c.getEmail()),
                        ()->assertEquals(null, c.getPassword())
                );
    }
    @Test
    @DisplayName("when customer created with illegal id then throws InvalidCouponBuildException")
    public void whenCustomerCreatedWithIllegalIdThenThrowsInvalidCouponBuildException()
    {
        InvalidCustomerBuildException exc = assertThrows( InvalidCustomerBuildException.class, ()->new Customer.CustomerBuilder().id(-1).build() );
        assertEquals("invalid id", exc.getMessage());
    }
}