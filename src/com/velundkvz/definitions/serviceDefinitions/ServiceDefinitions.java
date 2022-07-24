package com.velundkvz.definitions.serviceDefinitions;

public class ServiceDefinitions
{
    public static final int MIN_CUSTOMER_PSW_LENGTH = 8;

    public static final String AT_LEAST_ONE_DIGIT_RGX = "^.*\\d.*$";
    public static final String AT_LEAST_ONE_UPPER_CASE_LETTER_RGX = "^.*[A-Z].*$";
    public static final String AT_LEAST_ONE_LOWER_CASE_LETTER_RGX = "^.*[a-z].*$";


    public static final String COMPANY_ID_LESS_THAN_MIN_LEGAL_FRMT_EXC_MSG = "company id: %d, is less than %d, which minimum legal id";
    public static final String COMPANY_WITH_CURRENT_EMAIL_EXISTS_FRMT_EXC_MSG = "company with email: %s, already exists";
    public static final String COUPON_ALREADY_EXISTS_FRMT_EXC_MSG = "coupon with company id: %d, and title: %s already exists";
    public static final String NO_COMPANY_WITH_SUCH_ID_FRMT_EXC_MSG = "company with company id: %d, not exists";


    private ServiceDefinitions()
    {
        throw new AssertionError("constants container not for instantiating");
    }
}
