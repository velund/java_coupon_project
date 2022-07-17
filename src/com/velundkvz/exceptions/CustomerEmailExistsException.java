package com.velundkvz.exceptions;

public class CustomerEmailExistsException extends RuntimeException
{
    public CustomerEmailExistsException(String message)
    {
        super(message);
    }
}
