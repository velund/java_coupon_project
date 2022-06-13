package com.velundkvz.exceptions;

public class InvalidCompanyParametersException extends RuntimeException
{
    public InvalidCompanyParametersException(String descr)
    {
        super(descr);
    }
}
