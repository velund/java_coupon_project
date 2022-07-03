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
    public static Customer test_dflt_cust;
    public static Company test_dflt_company;
    public static Coupon test_dflt_not_expired_coupon;
    public static Coupon test_dflt_expired_coupon;

    public static final String f_name_cust = "default_f_name";
    public static final String l_name_cust = "default_l_name";
    public static final String email_cust = "default_mail.com";
    public static final String pswd_cust = "default_pswd";

    public static final String name_comp = "default_name";
    public static final String email_comp = "default_mail.com";
    public static final String pswd_comp = "default_pswd";

    public static final int category = 111;
    public static final String title = "default_title";
    public static final long price = 222;
    public static final int amount = 333;
    public static final String dflt_startDate_coup = "2022-07-03";
    public static final String dflt_not_expired_endDate_coup = "2022-12-30";
    public static final String dflt_expired_endDate_coup = "2022-05-28";


    static
    {
        createCompany();
        createCustomer();
        createNotExpiredCoupon();
        createExpiredCoupon();
    }

    private static void createNotExpiredCoupon()
    {
        test_dflt_not_expired_coupon = new CouponBuilder()
                .category(category)
                .title(title)
                .price(price)
                .amount(amount)
                .start_date(Date.valueOf(dflt_startDate_coup))
                .end_date(Date.valueOf(dflt_not_expired_endDate_coup))
                .build();
    }
    private static void createExpiredCoupon()
    {
        test_dflt_expired_coupon = new CouponBuilder()
                .category(category)
                .title(title)
                .price(price)
                .amount(amount)
                .start_date(Date.valueOf(dflt_startDate_coup))
                .end_date(Date.valueOf(dflt_expired_endDate_coup))
                .build();
    }

    private static void createCompany()
    {
        test_dflt_company = new CompanyBuilder()
                .first_name(name_comp)
                .email(email_comp)
                .password(pswd_comp)
                .build();
    }
    private static void createCustomer()
    {
        test_dflt_cust = new CustomerBuilder()
                .first_name(f_name_cust)
                .last_name(l_name_cust)
                .email(email_cust)
                .password(pswd_cust)
                .build();
    }
}
