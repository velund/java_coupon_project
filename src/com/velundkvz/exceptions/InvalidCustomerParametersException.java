package com.velundkvz.exceptions;

public class InvalidCustomerParametersException extends RuntimeException
{
    public InvalidCustomerParametersException(String invalidCouponPriceExcMsg)
    {
        super(invalidCouponPriceExcMsg);
    }

}
