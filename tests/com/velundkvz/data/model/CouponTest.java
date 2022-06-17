package com.velundkvz.data.model;

import com.velundkvz.definitions.schema.model.Coupon;
import com.velundkvz.exceptions.InvalidCouponBuildException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest
{
    @Test
    @DisplayName("when coupon created it is not null")
    public void whenCouponCreatedItIsNotNull()
    {
        Coupon c = new Coupon.CouponBuilder().build();
        assertTrue(Objects.nonNull(c));
    }
    @Test
    @DisplayName("when default coupon created all getters work")
    public void whenDefaultCouponCreatedAllGettersWork()
    {
        Coupon c = new Coupon.CouponBuilder().build();
        assertAll
        (
                ()->assertEquals(0, c.getId()),
                ()->assertEquals(0, c.getCompany_id()),
                ()->assertEquals(0, c.getCategory()),
                ()->assertEquals(0, c.getPrice()),
                ()->assertEquals(null, c.getTitle()),
                ()->assertEquals(null, c.getStart_date()),
                ()->assertEquals(null, c.getEnd_date()),
                ()->assertEquals(0, c.getAmount()),
                ()->assertEquals(null, c.getDescription()),
                ()->assertEquals(null, c.getImage_url())
        );
    }
    @Test
    @DisplayName("when coupon created with illegal id then throws InvalidCouponBuildException")
    public void whenCouponCreatedWithIllegalIdThenThrowsInvalidCouponBuildException()
    {
        InvalidCouponBuildException exc = assertThrows( InvalidCouponBuildException.class, ()->new Coupon.CouponBuilder().id(-1).build() );
        assertEquals("invalid id", exc.getMessage());
    }
    @Test
    @DisplayName("when coupon created with illegal CompanyId then throws InvalidCouponBuildException")
    public void whenCouponCreatedWithIllegalCompanyIdThenThrowsInvalidCouponBuildException()
    {
        InvalidCouponBuildException exc = assertThrows( InvalidCouponBuildException.class, ()->new Coupon.CouponBuilder().company_id(-1).build() );
        assertEquals("invalid company id", exc.getMessage());
    }
    @Test
    @DisplayName("when coupon created with illegal Price then throws InvalidCouponBuildException")
    public void whenCouponCreatedWithIllegalPriceThenThrowsInvalidCouponBuildException()
    {
        InvalidCouponBuildException exc = assertThrows( InvalidCouponBuildException.class, ()->new Coupon.CouponBuilder().price(-1).build() );
        assertEquals("invalid price", exc.getMessage());
    }
    @Test
    @DisplayName("when coupon created with illegal Amount then throws InvalidCouponBuildException")
    public void whenCouponCreatedWithIllegalAmountThenThrowsInvalidCouponBuildException()
    {
        InvalidCouponBuildException exc = assertThrows( InvalidCouponBuildException.class, ()->new Coupon.CouponBuilder().amount(-1).build() );
        assertEquals("invalid amount", exc.getMessage());
    }

}