package com.velundkvz.exceptions;

public class InvalidCouponParametersException extends  RuntimeException
{
    public InvalidCouponParametersException()
    {
        super();
    }
    public InvalidCouponParametersException(String descr)
    {
        super(descr);
    }
}
