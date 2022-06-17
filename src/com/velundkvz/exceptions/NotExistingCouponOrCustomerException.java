package com.velundkvz.exceptions;

public class NotExistingCouponOrCustomerException extends RuntimeException
{
    public NotExistingCouponOrCustomerException() {
        super();
    }

    public NotExistingCouponOrCustomerException(String message) {
        super(message);
    }
}
