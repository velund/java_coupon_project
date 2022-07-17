package com.velundkvz.data.database.dao;

import com.velundkvz.data.model.Coupon;

import java.sql.Date;
import java.util.List;

public interface CouponDAO
{
    Coupon add(Coupon coupon);
    List<Coupon> getAll();
    List<Coupon> getAllCustomerCoupons(long customerId);
    List<Coupon> getAllExpired();
    List<Coupon> getAllBefore(Date date);
    boolean isCouponOwned(long couponId, long customerId);
    int getAmount(long couponId);
    boolean updateCouponAmount(long couponId, int i);
}
