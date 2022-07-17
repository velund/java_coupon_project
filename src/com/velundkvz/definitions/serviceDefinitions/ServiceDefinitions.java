package com.velundkvz.definitions.serviceDefinitions;

public class ServiceDefinitions
{
    public static final int MIN_CUSTOMER_PSW_LENGTH = 8;

    public static final String AT_LEAST_ONE_DIGIT_RGX = "^.*\\d.*$";
    public static final String AT_LEAST_ONE_UPPER_CASE_LETTER_RGX = "^.*[A-Z].*$";
    public static final String AT_LEAST_ONE_LOWER_CASE_LETTER_RGX = "^.*[a-z].*$";
    private ServiceDefinitions()
    {
        throw new AssertionError("constants container not for instantiating");
    }
}
