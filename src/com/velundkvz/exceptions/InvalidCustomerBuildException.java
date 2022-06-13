package com.velundkvz.exceptions;

public class InvalidCustomerBuildException extends  RuntimeException
{
    public InvalidCustomerBuildException(String descr)
    {
        super(descr);
    }
}
