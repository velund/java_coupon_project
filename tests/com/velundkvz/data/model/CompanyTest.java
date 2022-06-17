package com.velundkvz.data.model;

import com.velundkvz.definitions.schema.model.Company;
import com.velundkvz.exceptions.InvalidCompanyBuildException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest
{
    @Test
    @DisplayName("when company created it is not null")
    public void whenCustomerCreatedItIsNotNull()
    {
        Company c = new Company.CompanyBuilder().build();
        assertTrue(Objects.nonNull(c));
    }
    @Test
    @DisplayName("when default company created all getters work")
    public void whenDefaultCustomerCreatedAllGettersWork()
    {
        Company c = new Company.CompanyBuilder().build();
        assertAll
                (
                        ()->assertEquals(0, c.getId()),
                        ()->assertEquals(null, c.getName()),
                        ()->assertEquals(null, c.getEmail()),
                        ()->assertEquals(null, c.getPassword())
                );
    }
    @Test
    @DisplayName("when company created with illegal id then throws InvalidCouponBuildException")
    public void whenCustomerCreatedWithIllegalIdThenThrowsInvalidCouponBuildException()
    {
        InvalidCompanyBuildException exc = assertThrows( InvalidCompanyBuildException.class, ()->new Company.CompanyBuilder().id(-1).build() );
        assertEquals("invalid id", exc.getMessage());
    }
}