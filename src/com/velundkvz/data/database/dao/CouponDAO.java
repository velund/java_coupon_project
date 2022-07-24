package com.velundkvz.data.database.dao;


import java.sql.Date;
import java.util.List;

import com.velundkvz.data.model.Coupon;

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
    boolean isExists(long couponId);
    boolean isExistsByCompanyIdAndTitle(long companyId, String title);
}
