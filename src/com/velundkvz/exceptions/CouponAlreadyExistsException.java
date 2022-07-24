package com.velundkvz.exceptions;

public class CouponAlreadyExistsException extends RuntimeException
{
    public CouponAlreadyExistsException(String msg)
    {
        super(msg);
    }
}
