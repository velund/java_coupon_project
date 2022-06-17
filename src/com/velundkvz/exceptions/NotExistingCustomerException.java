package com.velundkvz.exceptions;

public class NotExistingCustomerException extends RuntimeException
{
    public NotExistingCustomerException() {
        super();
    }

    public NotExistingCustomerException(String message) {
        super(message);
    }
}
