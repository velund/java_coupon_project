package com.velundkvz.exceptions;

public class CustomerPasswordTooWeakException extends RuntimeException
{
    public CustomerPasswordTooWeakException(String message)
    {
        super(message);
    }
}
