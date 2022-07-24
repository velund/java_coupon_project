package com.velundkvz.exceptions;

public class PasswordTooWeakException extends RuntimeException
{
    public PasswordTooWeakException(String message)
    {
        super(message);
    }
}
