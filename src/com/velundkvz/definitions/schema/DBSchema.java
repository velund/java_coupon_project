package com.velundkvz.definitions.schema;

public final class DBSchema
{
    public static final String DB_NAME = "coupon_system";
    public static final String COUPON_TBL = "coupon";
    public static final String CUSTOMER_TBL = "customer";
    public static final String COMPANY_TBL = "company";
    public static final String COUPON_CUSTOMER_ID_TBL = "couponid_customerid";


    private DBSchema()
    {
        throw new AssertionError("constants container not for implementing");
    }

}
