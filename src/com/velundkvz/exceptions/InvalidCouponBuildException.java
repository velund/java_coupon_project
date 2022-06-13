package com.velundkvz.exceptions;

public class InvalidCouponBuildException extends  RuntimeException
{
    public InvalidCouponBuildException()
    {
        super();
    }
    public InvalidCouponBuildException(String descr)
    {
        super(descr);
    }
}
