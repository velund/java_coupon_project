package com.velundkvz.exceptions;

public class BadTypeConnectionException extends RuntimeException {
    public BadTypeConnectionException(String descr)
    {
        super(descr);
    }
}
