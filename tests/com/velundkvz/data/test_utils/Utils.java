package com.velundkvz.data.test_utils;

import java.sql.*;
import java.util.Properties;

import static com.velundkvz.data.DefaultModels.*;
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
    public static void fill2EntriesCouponIdCustomerIdRelations()
    {
        insertCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertNotExpiredCouponToCouponTbl(getMaxIdcompany());
        }
        insertCustomerToCustomerTbl();
        insertToCouponIdCustomerIdTblTwoCouponsToMaxIdCust();
    }

    private static void insertToCouponIdCustomerIdTblTwoCouponsToMaxIdCust()
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into couponid_customerid(coupon_id, customer_id) values (?, ?), (?, ?)");
            ps.setLong(1, getMaxIdCoupon());
            ps.setLong(2, getMaxIdCust());
            ps.setLong(3, getMinIdCoupon());
            ps.setLong(4, getMaxIdCust());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void insertNotExpiredCouponToCouponTbl(long companyId)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into coupon(company_id, category, title, price, amount, start_date, end_date) values (?,?,?,?,?, ?, ?)");
            ps.setLong(1, companyId);
            ps.setInt(2, test_dflt_not_expired_coupon.getCategory());
            ps.setString(3, test_dflt_not_expired_coupon.getTitle());
            ps.setLong(4, test_dflt_not_expired_coupon.getPrice());
            ps.setInt(5, test_dflt_not_expired_coupon.getAmount());
            ps.setDate(6, test_dflt_not_expired_coupon.getStart_date());
            ps.setDate(7, test_dflt_not_expired_coupon.getEnd_date());

            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    public static void insertExpiredCouponToCouponTbl(long companyId)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into coupon(company_id, category, title, price, amount, start_date, end_date) values (?,?,?,?,?,?,?)");
            ps.setLong(1, companyId);
            ps.setInt(2, test_dflt_expired_coupon.getCategory());
            ps.setString(3, test_dflt_expired_coupon.getTitle());
            ps.setLong(4, test_dflt_expired_coupon.getPrice());
            ps.setInt(5, test_dflt_expired_coupon.getAmount());
            ps.setDate(6, test_dflt_expired_coupon.getStart_date());
            ps.setDate(7, test_dflt_expired_coupon.getEnd_date());

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
            ps.setString(1, test_dflt_company.getName());
            ps.setString(2, test_dflt_company.getPassword());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void insertCustomerToCustomerTbl()
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into customer values(null, ?, ?, ?, ?)");
            ps.setString(1, test_dflt_cust.getFirst_name());
            ps.setString(2, test_dflt_cust.getLast_name());
            ps.setString(3, test_dflt_cust.getEmail());
            ps.setString(4, test_dflt_cust.getPassword());
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
    public static long getMinIdCoupon()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select min(id) from coupon").executeQuery();
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
    public static long countCouponTbl()
    {
        long count = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select count(*) from coupon").executeQuery();
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
    public static void fillNotExpiredCoupons(int n)
    {
        insertCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertNotExpiredCouponToCouponTbl(getMaxIdcompany());
        }
    }
    public static void fillExpiredCoupons(int n)
    {
        insertCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertExpiredCouponToCouponTbl(getMaxIdcompany());
        }
    }
}
