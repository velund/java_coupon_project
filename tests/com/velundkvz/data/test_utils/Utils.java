package com.velundkvz.data.test_utils;

import java.sql.*;
import java.util.Properties;

import static com.velundkvz.data.DefaultModels.company;
import static com.velundkvz.data.DefaultModels.coupon;
import static com.velundkvz.definitions.ConnectionDefinitions.DB;
import static com.velundkvz.definitions.ConnectionDefinitions.URL;

public class Utils
{
    private static Connection adminConnection;
    static
    {
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
    public static void resetAllTables()
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
    public static void insertCouponToCouponTbl(long companyId)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into coupon(company_id, category, title, price, amount) values (?,?,?,?,?)");
            ps.setLong(1, companyId);
            ps.setInt(2, coupon.getCategory());
            ps.setString(3, coupon.getTitle());
            ps.setLong(4, coupon.getPrice());
            ps.setInt(5, coupon.getAmount());

            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public static void insertCompanyToCompanyTbl()
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into company(name, password) values(?,?)");
            ps.setString(1, company.getName());
            ps.setString(2, company.getPassword());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static long getMaxIdCust()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select max(id) from customer").executeQuery();
            if (rs.next())
            {
                id = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }
    public static long getMaxIdCoupon()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select max(id) from coupon").executeQuery();
            if (rs.next())
            {
                id = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }
    public static long getMaxIdcompany()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select max(id) from company").executeQuery();
            if (rs.next())
            {
                id = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }
    public static  long countCustTbl()
    {
        long count = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select count(*) from customer").executeQuery();
            if (rs.next())
            {
                count = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            count = -1;
        }
        return count;
    }
    public static long countCouponCustTbl()
    {
        long count = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select count(*) from couponid_customerid").executeQuery();
            if (rs.next())
            {
                count = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            count = -1;
        }
        return count;
    }
    public static  boolean isEmailExistsCustTbl(String email)
    {
        boolean isExists = false;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select exists(select email from customer where email = " + "\"" + email + "\""  + ")").executeQuery();
            if (rs.next())
            {
                isExists = rs.getBoolean(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            return isExists;
        }
    }
    public static boolean isIdExistsCustTbl(int id)
    {
        boolean isExists = false;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select exists(select id from customer where id = " + "\"" + id + "\""  + ")").executeQuery();
            if (rs.next())
            {
                isExists = rs.getBoolean(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            return isExists;
        }
    }
}
