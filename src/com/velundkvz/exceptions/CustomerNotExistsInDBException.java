package com.velundkvz.exceptions;

public class CustomerNotExistsInDBException extends RuntimeException
{
    public CustomerNotExistsInDBException(String msg){ super(msg); }
}
