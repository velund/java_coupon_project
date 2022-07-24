package com.velundkvz.exceptions;

public class CompanyNotExistsException extends RuntimeException
{
    public CompanyNotExistsException(String msg)
    {
        super(msg);
    }
}
