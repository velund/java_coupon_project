package com.velundkvz.data;

import com.velundkvz.data.model.Company;
import com.velundkvz.data.model.Company.CompanyBuilder;
import com.velundkvz.data.model.Coupon;
import com.velundkvz.data.model.Coupon.CouponBuilder;
import com.velundkvz.data.model.Customer;
import com.velundkvz.data.model.Customer.CustomerBuilder;

import java.sql.Date;

public class DefaultModels
{
    public static Customer test_dflt_cust_with_strong_password;
    public static Customer test_dflt_cust_with_weak_password;
    public static Company test_dflt_company;
    public static Coupon test_dflt_not_expired_coupon;
    public static Coupon test_dflt_expired_coupon;
    public static long not_existing_coupon_id = Integer.MIN_VALUE;

    public static final String dflt_f_name_cust = "default_f_name";
    public static final String dflt_l_name_cust = "default_l_name";
    public static final String dflt_email_cust = "default_mail.com";
    public static final String dflt_strong_pswd_cust = "default_pswdA1";
    public static final String dflt_weak_pswd_cust = "default_pswd";
    public static final String updated_email_cust = "udated_email_cust";
    public static final String updated_strong_pswd_cust = "updated_pswd_cust1Z";

    public static final String dflt_name_comp = "default_name";
    public static final String dflt_email_comp = "default_mail.com";
    public static final String updated_email_comp = "updated_mail_company.com";
    public static final String dflt_strong_pswd_comp = "default_pswd_companyZ1";
    public static final String dflt_weak_pswd_comp = "default_weak_pswd_company";
    public static final String updated_strong_pswd_comp = "updated_strong_pswd_companyZ1";

    public static final int dflt_category_coup = 111;
    public static final String dflt_title_coup = "default_title";
    public static final String new_title_coup = "new_title";
    public static final long dflt_price_coup = 222;
    public static final int dflt_coupon_amount = 333;
    public static final String dflt_startDate_coup = "2022-07-03";
    public static final String dflt_not_expired_endDate_coup = "2022-12-30";
    public static final String dflt_expired_endDate_coup = "2022-05-28";


    static
    {
        createCompany();
        createCustomerStrongPassword();
        createCustomerWeakPassword();
        createNotExpiredCoupon();
        createExpiredCoupon();
    }

    private static void createNotExpiredCoupon()
    {
        test_dflt_not_expired_coupon = new CouponBuilder()
                .category(dflt_category_coup)
                .title(dflt_title_coup)
                .price(dflt_price_coup)
                .amount(dflt_coupon_amount)
                .start_date(Date.valueOf(dflt_startDate_coup))
                .end_date(Date.valueOf(dflt_not_expired_endDate_coup))
                .build();
    }
    private static void createExpiredCoupon()
    {
        test_dflt_expired_coupon = new CouponBuilder()
                .category(dflt_category_coup)
                .title(dflt_title_coup)
                .price(dflt_price_coup)
                .amount(dflt_coupon_amount)
                .start_date(Date.valueOf(dflt_startDate_coup))
                .end_date(Date.valueOf(dflt_expired_endDate_coup))
                .build();
    }

    private static void createCompany()
    {
        test_dflt_company = new CompanyBuilder()
                .name(dflt_name_comp)
                .email(dflt_email_comp)
                .password(dflt_strong_pswd_comp)
                .build();
    }
    private static void createCustomerStrongPassword()
    {
        test_dflt_cust_with_strong_password = new CustomerBuilder()
                .first_name(dflt_f_name_cust)
                .last_name(dflt_l_name_cust)
                .email(dflt_email_cust)
                .password(dflt_strong_pswd_cust)
                .build();
    }
    private static void createCustomerWeakPassword()
    {
        test_dflt_cust_with_weak_password = new CustomerBuilder()
                .first_name(dflt_f_name_cust)
                .last_name(dflt_l_name_cust)
                .email(dflt_email_cust)
                .password(dflt_weak_pswd_cust)
                .build();
    }
}
