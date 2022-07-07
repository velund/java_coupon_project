package com.velundkvz.definitions.schema;

import static com.velundkvz.definitions.schema.DBSchema.COUPON_CUSTOMER_ID_TBL;
import static com.velundkvz.definitions.schema.DBSchema.CUSTOMER_TBL;

public final class customerTbl
{
    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_E_MAIL = "email";
    private static final String COL_PASSWORD = "password";

    public static final String DELETE_CUSTOMER_SQL = "delete from " + CUSTOMER_TBL + " where " + COL_ID +  " = ?";
    public static String INSERT_CUSTOMER_SQL = "insert into " + CUSTOMER_TBL + " values (null, ?,?,?,?)";
    public static final String UPDATE_CUSTOMER_EMAIL_BY_ID_SQL = "update " + CUSTOMER_TBL + " set " + COL_E_MAIL + " = ?"   + " where " + COL_ID + " = " + "?" ;
    public static final String SELECT_CUSTOMER_BY_ID_SQL = "select * from " + CUSTOMER_TBL + " where " + COL_ID + " = " + "?";
    public static final String SELECT_CUSTOMER_BY_EMAIL_AND_PSWD_SQL = "select * from " + CUSTOMER_TBL + " where " + COL_E_MAIL + " = ?" + " and " + COL_PASSWORD + " = ?";
    public static final String SELECT_ALL_CUSTOMERS_SQL = "select * from " + CUSTOMER_TBL;
    public static final String COUNT_CUSTOMER_SQL = "select count(*) from " + CUSTOMER_TBL;
    public static final String GET_CUSTOMER_MAX_ID_SQL = "select max(" + COL_ID + ")" + " from " +  CUSTOMER_TBL;
    public static final String INSERT_INTO_COUPON_CUSTOMER_TBL_SQL = "insert into " + COUPON_CUSTOMER_ID_TBL + " values " + "(?,?)";

    private customerTbl()
    {
        throw new AssertionError("constants container not for implementing");
    }
}
