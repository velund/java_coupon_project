package com.velundkvz.data;

import com.velundkvz.data.model.Company;
import com.velundkvz.data.model.Company.CompanyBuilder;
import com.velundkvz.data.model.Coupon;
import com.velundkvz.data.model.Coupon.CouponBuilder;
import com.velundkvz.data.model.Customer;
import com.velundkvz.data.model.Customer.CustomerBuilder;

public class DefaultModels
{
    public static Customer cust;
    public static Company company;
    public static Coupon coupon;

    public static final String f_name_cust = "default_f_name";
    public static final String l_name_cust = "default_l_name";
    public static final String email_cust = "default_mail.com";
    public static final String pswd_cust = "default_pswd";

    public static final String name_comp = "default_f_name";
    public static final String email_comp = "default_mail.com";
    public static final String pswd_comp = "default_pswd";

    public static final int category = 111;
    public static final String title = "default_title";
    public static final long price = 222;
    public static final int amount = 333;

    static
    {
        createCompany();
        createCustomer();
        createCoupon();
    }

    private static void createCoupon()
    {
        coupon = new CouponBuilder()
                .category(category)
                .title(title)
                .price(price)
                .amount(amount)
                .build();
    }

    private static void createCompany()
    {
        company = new CompanyBuilder()
                .first_name(name_comp)
                .email(email_comp)
                .password(pswd_comp)
                .build();
    }
    private static void createCustomer()
    {
        cust = new CustomerBuilder()
                .first_name(f_name_cust)
                .last_name(l_name_cust)
                .email(email_cust)
                .password(pswd_cust)
                .build();
    }
}
