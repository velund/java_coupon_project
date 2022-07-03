package com.velundkvz.definitions.schema;

import static com.velundkvz.definitions.schema.CouponIdCustomerIdTbl.COL_COUPON_ID;
import static com.velundkvz.definitions.schema.CouponIdCustomerIdTbl.COL_CUSTOMER_ID;
import static com.velundkvz.definitions.schema.DBSchema.*;

public final class CouponTbl
{
    public static final String COL_ID = "id";
    public static final String COL_COMPANY_ID = "company_id";
    public static final String COL_CATEGORY = "category";
    public static final String COL_TITLE = "title";
    public static final String COL_PRICE = "price";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_END_DATE = "end_date";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_IMAGE_URL = "image_url";

    public static String INSERT_COUPON_SQL = "insert into " + COUPON_TBL + " values (null, ?,?,?,?,?,?,?,?,?)";
    public static String GET_LAST_ADDED_COUPON_ID_SQL = "select max(" +COL_ID+ ")" + " from " + COUPON_TBL;
    public static final String SELECT_ALL_COUPONS_SQL = "select * from " + COUPON_TBL;


    private static final String cust_cpns_cte_name = "coupons_of_customer";
    private static final String COUPONS_OF_CUSTOMER_CTE = "with " + cust_cpns_cte_name + " as " +
                                                        "(select " + COL_COUPON_ID + " from "
                                                        + COUPON_CUSTOMER_ID_TBL
                                                        + " where " + COL_CUSTOMER_ID + " = ?)";
    public static final String GET_ALL_CUSTOMER_COUPONS_SQL = COUPONS_OF_CUSTOMER_CTE + "select " + COUPON_TBL + ".* " + " from "
                                    + COUPON_TBL
                                    + " join " + cust_cpns_cte_name + " on " + COUPON_TBL +"." + COL_ID + " = "
                                     + cust_cpns_cte_name + "." + COL_COUPON_ID;
    private CouponTbl()
    {
        throw new AssertionError("constants container not for implementing");
    }

    public static final String GET_ALL_LESS_THAN_DATE_SQL = "select * from coupon where " +  COL_END_DATE + " < ?";

}
