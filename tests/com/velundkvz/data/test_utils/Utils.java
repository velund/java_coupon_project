package com.velundkvz.data.test_utils;

import com.velundkvz.data.model.Company;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.velundkvz.data.DefaultModels.*;
import static com.velundkvz.definitions.ConnectionDefinitions.DB;
import static com.velundkvz.definitions.ConnectionDefinitions.URL;
import static com.velundkvz.definitions.schema.CompanyTbl.*;
import static com.velundkvz.definitions.schema.CouponTbl.COL_AMOUNT_COUPON_TBL;
import static com.velundkvz.definitions.schema.CouponTbl.COL_ID_COUPON_TBL;
import static com.velundkvz.definitions.schema.DBSchema.*;
import static com.velundkvz.definitions.schema.customerTbl.*;

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
        insertDfltCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertNotExpiredCouponToCouponTbl(getMaxIdcompany());
        }
        insertDfltCustomerToCustomerTbl();
        insertToCouponId_CustomerIdTblTwoCouponsToMaxIdCust();
    }

    public static void insertToCouponId_CustomerIdTblTwoCouponsToMaxIdCust()
    {
        insertCouponToCustomerId_CouponIdTbl(getMaxIdCoupon(), getMaxIdCust());
        insertCouponToCustomerId_CouponIdTbl(getMinIdCoupon(), getMaxIdCust());
    }
    public static void insertCouponToCustomerId_CouponIdTbl(long couponId, long customerId)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into couponid_customerid(coupon_id, customer_id) values (?, ?)");
            ps.setLong(1, couponId);
            ps.setLong(2, customerId);
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

    public static void insertDfltCompanyToCompanyTbl()
    {
        insertCompanyToCompanyTbl(test_dflt_company.getName(), test_dflt_company.getPassword(), test_dflt_company.getEmail());
    }
    public static void insertCompanyToCompanyTbl(Company company)
    {
        insertCompanyToCompanyTbl(company.getName(), company.getPassword(), company.getEmail());
    }
    public static void insertCompanyToCompanyTbl(String name, String pass, String email)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into company(" + COL_NAME + "," + COL_PASSWORD + "," + COL_E_MAIL_COMPANY_TBL + ") values(?,?,?)");
            ps.setString(1, name);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void insertCustomerToCustomerTbl(String f_name,String l_name,String email,String pass)
    {
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("insert into customer values(null, ?, ?, ?, ?)");
            ps.setString(1, f_name);
            ps.setString(2, l_name);
            ps.setString(3, email);
            ps.setString(4, pass);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void insertDfltCustomerToCustomerTbl()
    {
        insertCustomerToCustomerTbl(dflt_f_name_cust, dflt_l_name_cust, dflt_email_cust, dflt_strong_pswd_cust);
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
    public static String getCustEmail(long id)
    {
        String email = "not defined email";
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select " + COL_E_MAIL_CUSTOMER_TBL + " from customer where "+COL_ID_CUSTOMER_TBL+"="+id).executeQuery();
            if (rs.next())
            {
                email = rs.getString(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return email;
    }
    public static String getCustPassword(long id)
    {
        String email = "not defined email";
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select " + COL_PASSWORD_CUSTOMER_TBL + " from "+CUSTOMER_TBL+" where "+COL_ID_CUSTOMER_TBL+"="+id).executeQuery();
            if (rs.next())
            {
                email = rs.getString(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return email;
    }
    public static long getMaxIdCoupon()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select max(id) from " + COUPON_TBL).executeQuery();
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
    public static long getMaxIdCompany()
    {
        long id = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select max(id) from " + COMPANY_TBL).executeQuery();
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
    public static long countCompanies()
    {
        long count = 0;
        try
        {
            ResultSet rs =  adminConnection.prepareStatement("select count(*) from " + COMPANY_TBL).executeQuery();
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
    public static boolean isCompanyIDExists(long id)
    {
        boolean isExists = false;
        try
        {
            PreparedStatement ps =  adminConnection.prepareStatement("select exists (select " + COL_ID + " from " + COMPANY_TBL + " where " + COL_ID + " = ?)" );
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                isExists = rs.getBoolean(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return isExists;
    }
    public static boolean isCompanyEMAILExists(String email)
    {
        boolean isExists = false;
        try
        {
            PreparedStatement ps =  adminConnection.prepareStatement("select exists (select " + COL_E_MAIL_COMPANY_TBL + " from " + COMPANY_TBL + " where " + COL_E_MAIL_COMPANY_TBL + " = ?)" );
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                isExists = rs.getBoolean(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return isExists;
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
        insertDfltCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertNotExpiredCouponToCouponTbl(getMaxIdcompany());
        }
    }
    public static void fillExpiredCoupons(int n)
    {
        insertDfltCompanyToCompanyTbl();
        for (int i = 0; i < 3; i++)
        {
            insertExpiredCouponToCouponTbl(getMaxIdcompany());
        }
    }
    public static void fillDefaultCompanies(int n)
    {
        for (int i = 0; i < n; i++)
        {
            insertDfltCompanyToCompanyTbl();
        }
    }
    public static void fillDefaultCustomers(int n)
    {
        for (int i = 0; i < n; i++)
        {
            insertDfltCustomerToCustomerTbl();
        }
    }

    public static boolean containsAllCompanies(List<Company> origin, List<Company> comparingList )
    {
        for (Company compFromComparingList: comparingList)
        {
            if (origin.stream().noneMatch(compFromOrigList -> areEqualByEverythingExceptId(compFromOrigList, compFromComparingList) ))
            {
                return false;
            }
        }
        return true;
    }
    public static boolean areEqualByEverythingExceptId(Company c1, Company c2)
    {
        return Objects.equals(c1.getName(), c2.getName()) &&
                Objects.equals(c1.getEmail(), c2.getEmail()) &&
                Objects.equals(c1.getPassword(), c2.getPassword());
    }
    public static int getCoupAmount(long couponId)
    {
        int amount = 0;
        try
        {
            PreparedStatement ps = adminConnection.prepareStatement("select "+COL_AMOUNT_COUPON_TBL+" from "+COUPON_TBL+" where "+COL_ID_COUPON_TBL+" = ?");
            ps.setLong(1, couponId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                amount = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            amount = -1;
        }
        return amount;
    }

}
