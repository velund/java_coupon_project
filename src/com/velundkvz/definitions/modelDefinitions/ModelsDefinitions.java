package com.velundkvz.definitions.modelDefinitions;

public final class ModelsDefinitions
{
    public static final byte UNKNOWN_ID = -1;
    public static final String INVALID_ID_EXC_MSG = "invalid id";
    public static final String INVALID_COUPON_COMPANY_ID_EXC_MSG = "invalid company id";
    public static final String INVALID_COUPON_AMOUNT_EXC_MSG = "invalid amount";
    public static final String INVALID_COUPON_PRICE_EXC_MSG = "invalid price";

    public static final String CUSTOMER_EMAIL_ALREDY_EXISTS_EXC_FORMAT_MSG = "customer with %s email already exists";
    public static final String COUPON_ALREADY_OWNED_EXC_FORMAT_MSG = "customer id: %d already owns coupon id: %d";
    public static final String CUSTOMER_PASSWORD_TOO_WEAK_EXC_FORMAT_MSG = "customer password: %s is too weak";
    public static final String COUPON_AMOUNT_ZERO_EXC = "coupon amount zero or invalid";
    public static final String NO_SUCH_COUPON_ID_IN_DB_EXC_FRMT_MSG = "coupon id %d, not exists in db";
    public static final String NO_SUCH_CUSTOMER_ID_IN_DB_EXC_FRMT_MSG = "customer id %d, not exists in db";

    private ModelsDefinitions()
    {
        throw new AssertionError("constants container not for implementing");
    }


}
