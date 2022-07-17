package com.velundkvz.exceptions;

public class CouponNotExistsInDBException extends RuntimeException
{
    public CouponNotExistsInDBException(String msg){ super(msg); }
}
