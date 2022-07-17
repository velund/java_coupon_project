package com.velundkvz.exceptions;

public class CouponOwnedByCustomerException extends RuntimeException
{
    public CouponOwnedByCustomerException(String msg){ super(msg); }
}
