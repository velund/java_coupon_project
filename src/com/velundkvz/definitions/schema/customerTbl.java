package com.velundkvz.definitions.schema;

import static com.velundkvz.definitions.schema.DBSchema.*;

public final class customerTbl
{
    public static final String COL_ID_CUSTOMER_TBL = "id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_E_MAIL_CUSTOMER_TBL = "email";
    public static final String COL_PASSWORD_CUSTOMER_TBL = "password";

    public static final String DELETE_CUSTOMER_SQL = "delete from " + CUSTOMER_TBL + " where " + COL_ID_CUSTOMER_TBL +  " = ?";
    public static String INSERT_CUSTOMER_SQL = "insert into " + CUSTOMER_TBL + " values (null, ?,?,?,?)";
    public static final String UPDATE_CUSTOMER_EMAIL_BY_ID_SQL = "update " + CUSTOMER_TBL + " set " + COL_E_MAIL_CUSTOMER_TBL+" = ?"+" where "+COL_ID_CUSTOMER_TBL+" = "+"?" ;
    public static final String UPDATE_CUSTOMER_PASSWORD_BY_ID_SQL = "update "+CUSTOMER_TBL+" set "+COL_PASSWORD_CUSTOMER_TBL+" = ?"+" where "+COL_ID_CUSTOMER_TBL+" = "+"?" ;
    public static final String SELECT_CUSTOMER_BY_ID_SQL = "select * from "+CUSTOMER_TBL+" where "+COL_ID_CUSTOMER_TBL+" = "+"?";
    public static final String SELECT_CUSTOMER_BY_EMAIL_SQL = "select email from "+CUSTOMER_TBL+" where "+COL_E_MAIL_CUSTOMER_TBL+" = "+"?";
    public static final String SELECT_CUSTOMER_BY_EMAIL_AND_PSWD_SQL = "select * from " + CUSTOMER_TBL + " where " + COL_E_MAIL_CUSTOMER_TBL + " = ?" + " and " + COL_PASSWORD_CUSTOMER_TBL + " = ?";
    public static final String SELECT_ALL_CUSTOMERS_SQL = "select * from "+CUSTOMER_TBL;
    public static final String COUNT_CUSTOMER_SQL = "select count(*) from "+CUSTOMER_TBL;
    public static final String GET_CUSTOMER_MAX_ID_SQL = "select max("+COL_ID_CUSTOMER_TBL+")"+" from "+CUSTOMER_TBL;
    public static final String INSERT_INTO_COUPON_CUSTOMER_TBL_SQL = "insert into "+COUPON_CUSTOMER_ID_TBL+" values "+"(?,?)";
    public static final String IS_CUSTOMER_EXISTS_BY_ID_SQL = "select exists" +
            "(select "+COL_ID_CUSTOMER_TBL+" from "+CUSTOMER_TBL+" where "+COL_ID_CUSTOMER_TBL+" = ?)";;


    private customerTbl()
    {
        throw new AssertionError("constants container not for implementing");
    }
}
