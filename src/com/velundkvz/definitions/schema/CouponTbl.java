package com.velundkvz.definitions.schema;

import static com.velundkvz.definitions.schema.CouponIdCustomerIdTbl.COL_COUPON_ID_COUP_CUST_TBL;
import static com.velundkvz.definitions.schema.CouponIdCustomerIdTbl.COL_CUSTOMER_ID_COUP_CUST_TBL;
import static com.velundkvz.definitions.schema.DBSchema.*;

public final class CouponTbl
{
    public static final String COL_ID_COUPON_TBL = "id";
    public static final String COL_COMPANY_ID_COUPON_TBL = "company_id";
    public static final String COL_CATEGORY_COUPON_TBL = "category";
    public static final String COL_TITLE_COUPON_TBL = "title";
    public static final String COL_PRICE_COUPON_TBL = "price";
    public static final String COL_START_DATE_COUPON_TBL = "start_date";
    public static final String COL_END_DATE_COUPON_TBL = "end_date";
    public static final String COL_AMOUNT_COUPON_TBL = "amount";
    public static final String COL_DESCRIPTION_COUPON_TBL = "description";
    public static final String COL_IMAGE_URL_COUPON_TBL = "image_url";

    public static String INSERT_COUPON_SQL = "insert into " + COUPON_TBL + " values (null, ?,?,?,?,?,?,?,?,?)";
    public static String UPDATE_COUPON_AMOUNT_SQL = "update " + COUPON_TBL + " set "+COL_AMOUNT_COUPON_TBL+" = ? where "+COL_ID_COUPON_TBL+" = ?";
    public static String GET_LAST_ADDED_COUPON_ID_SQL = "select max(" + COL_ID_COUPON_TBL + ")" + " from " + COUPON_TBL;
    public static final String SELECT_ALL_COUPONS_SQL = "select * from " + COUPON_TBL;
    public static final String GET_IS_OWNED_COUPONS_SQL = "select exists " +
            "(select "+COL_COUPON_ID_COUP_CUST_TBL+" from "+COUPON_CUSTOMER_ID_TBL+" where "
        +COL_COUPON_ID_COUP_CUST_TBL+" = ? and "+COL_CUSTOMER_ID_COUP_CUST_TBL+" = ? )";

    public static final String GET_IS_COUPON_EXISTS_SQL = "select exists" +
            "(select "+COL_ID_COUPON_TBL+" from "+COUPON_TBL+" where "+COL_ID_COUPON_TBL+" = ?)";

    public static final String GET_IS_COUPON_EXISTS_BY_COMP_ID_AND_TITLE_SQL = "select exists" +
            "(select "+COL_ID_COUPON_TBL+" from "+COUPON_TBL+" where "+COL_COMPANY_ID_COUPON_TBL+" = ? and "+COL_TITLE_COUPON_TBL+" = ?)";

    public static final String GET_AMOUNT_SQL = "select "+ COL_AMOUNT_COUPON_TBL +" from "+COUPON_TBL+" where "+COL_ID_COUPON_TBL+" = ?";

    private static final String cust_cpns_cte_name = "coupons_of_customer";
    private static final String COUPONS_OF_CUSTOMER_CTE = "with " + cust_cpns_cte_name + " as " +
                                                        "(select " + COL_COUPON_ID_COUP_CUST_TBL + " from "
                                                        + COUPON_CUSTOMER_ID_TBL
                                                        + " where " + COL_CUSTOMER_ID_COUP_CUST_TBL + " = ?)";
    public static final String GET_ALL_CUSTOMER_COUPONS_SQL = COUPONS_OF_CUSTOMER_CTE + "select " + COUPON_TBL + ".* " + " from "
                                    + COUPON_TBL
                                    + " join " + cust_cpns_cte_name + " on " + COUPON_TBL +"." + COL_ID_COUPON_TBL + " = "
                                     + cust_cpns_cte_name + "." + COL_COUPON_ID_COUP_CUST_TBL;
    private CouponTbl()
    {
        throw new AssertionError("constants container not for implementing");
    }

    public static final String GET_ALL_LESS_THAN_DATE_SQL = "select * from coupon where " + COL_END_DATE_COUPON_TBL + " < ?";

}
