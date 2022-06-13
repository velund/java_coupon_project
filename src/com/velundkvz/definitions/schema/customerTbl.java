package com.velundkvz.definitions.schema;

public final class customerTbl
{
    public static final String COL_ID = "id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_E_MAIL = "email";
    public static final String COL_PASSWORD = "password";

    private customerTbl()
    {
        throw new AssertionError("constants container not for implementing");
    }
}
