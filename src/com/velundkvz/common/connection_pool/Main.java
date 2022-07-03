package com.velundkvz.common.connection_pool;

import com.velundkvz.data.database.dao.CouponDAO;
import com.velundkvz.data.database.dao.CouponMySQLDAO;
import com.velundkvz.data.database.dao.CustomerDAO;
import com.velundkvz.data.database.dao.CustomerMySQLDAO;
import com.velundkvz.data.model.Coupon;
import com.velundkvz.data.model.Customer;

import java.sql.*;
import java.util.Properties;

import static com.velundkvz.definitions.ConnectionDefinitions.DB;
import static com.velundkvz.definitions.ConnectionDefinitions.URL;

public class Main {

    private static Customer cust;
    private static Coupon test_dflt_coupon;
    private static Connection adminConnection;

    public static void main(String[] args) throws Exception
    {
        //resetAllTables();
        ConnectionPool clientConnPool = DBConnections.CLIENT_CONNECTIONS.getPool();
        CustomerDAO custDAO = new CustomerMySQLDAO();
        CouponDAO couponDAO = new CouponMySQLDAO();
        custDAO.add(cust);
        couponDAO.add(test_dflt_coupon);

       // resetAllTables();
    }
    static
    {
        createCustomer();
        createCoupon();
        setupAdminConnection();
    }
    private static void setupAdminConnection()
    {
        Properties properties = new Properties();
        properties.put("user", "krill_admin");
        properties.put("password", "15kvz61982!");
        try {
            adminConnection = DriverManager.getConnection(URL + "/" + DB, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createCustomer()
    {
        cust = new Customer.CustomerBuilder()
                .first_name("f_name_cust")
                .last_name("l_name_cust")
                .email("email_cust")
                .password("pswd_cust")
                .build();
    }
    private static void resetAllTables()
    {
        try {

            adminConnection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            adminConnection.prepareStatement("truncate coupon").executeUpdate();
            adminConnection.prepareStatement("truncate customer").executeUpdate();
            adminConnection.prepareStatement("truncate company").executeUpdate();
            adminConnection.prepareStatement("truncate couponid_customerid").executeUpdate();
            adminConnection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createCoupon()
    {
        test_dflt_coupon = new Coupon.CouponBuilder()
                .category(2)
                .title("title")
                .price(2)
                .amount(2)
                .build();
    }
}
