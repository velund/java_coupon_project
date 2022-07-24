package com.velundkvz.exceptions;

public class CompanyEmailExistsException extends RuntimeException
{
    public CompanyEmailExistsException(String msg)
    {
        super(msg);
    }
}
