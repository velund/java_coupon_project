package com.velundkvz.exceptions;

public class PoolNotExistsException extends RuntimeException
{
    public PoolNotExistsException(String descr)
    {
        super(descr);
    }

}
