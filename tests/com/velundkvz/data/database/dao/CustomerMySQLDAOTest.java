package com.velundkvz.data.database.dao;

import com.velundkvz.exceptions.NotExistingCouponOrCustomerException;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static com.velundkvz.definitions.ConnectionDefinitions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.velundkvz.data.model.Customer;
import static com.velundkvz.data.DefaultModels.*;

class CustomerMySQLDAOTest
{
    private static Connection connection;
    private static CustomerMySQLDAO custDAO;

    @BeforeAll
    private static void setup()
    {
        setupConnection();
        resetAllTables();
        custDAO = new CustomerMySQLDAO();
    }
    @AfterEach
    private void setdown()
    {
        resetAllTables();
    }
    @Test
    @DisplayName("when call create then does not throw any exception")
    public void whenCallCreateThenDoesNotThrowAnyException()
    {
        assertDoesNotThrow( ()-> new CustomerMySQLDAO().create(cust) );
    }
    @Test
    @DisplayName("when Call Create One Entry Added On Customer Tbl and index incremented")
    public void whenCallCreateOneEntryAddedOnCustomerTbl()
    {
        long lastId = getMaxIdCust();
        long lastCount = countCustTbl();
        custDAO.create(cust);
        long newtId = getMaxIdCust();
        long newCount = countCustTbl();
        assertAll
        (
            ()->assertTrue(newtId != -1),
            ()->assertTrue(newCount != -1),
            ()->assertEquals(lastId + 1, newtId),
            ()->assertEquals(lastCount +1, newCount)
        );
    }
    @Test
    @DisplayName("when Call remove from empty Customer Tbl then return false")
    public void whenCallRemoveFromEmptyCustomerTblThenReturnFalse()
    {
        assertFalse( custDAO.remove(1) );
    }
    @Test
    @DisplayName("when Call remove not existing id from Customer Tbl then return false")
    public void whenCallRemoveNotExistingIdFromCustomerTblThenReturnFalse()
    {
        custDAO.create(cust);
        assertFalse( custDAO.remove(2) );
    }
    @Test
    @DisplayName("when Call remove existing id from Customer Tbl then return true")
    public void whenCallRemoveExistingIdFromCustomerTblThenReturnTrue()
    {
        custDAO.create(cust);
        long id = getMaxIdCust();
        assertTrue(id != 0);
        assertTrue( custDAO.remove( id ) );
    }
    @Test
    @DisplayName("when Call remove existing id from Customer Tbl then Count Decrements")
    public void whenCallRemoveExistingIdFromCustomerTblThenCountDecrements()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        long lastCount = countCustTbl();
        long id = getMaxIdCust();
        assertTrue(lastCount != 0);
        assertTrue( custDAO.remove( id ) );
        long newCount = countCustTbl();
        assertEquals(lastCount - 1, newCount);
    }
    @Test
    @DisplayName("when Call remove existing id from Customer Tbl twice then Return False")
    public void whenCallRemoveExistingIdFromCustomerTblTwiceThenReturnFalse()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        custDAO.remove(1);
        assertFalse(custDAO.remove(1));
    }
    @Test
    @DisplayName("when Call update email existing id from Customer Tbl then Return True")
    public void whenCallUpdateEmailExistingIdFromCustomerTblTrue()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        assertAll
        (
                ()->assertFalse(isEmailExistsCustTbl("new-Email.com")),
                ()->assertTrue( custDAO.updateEmail(2, "new-Email.com") ),
                ()->assertTrue(isEmailExistsCustTbl("new-Email.com"))
        );

    }
    @Test
    @DisplayName("when Call update email not existing id from Customer Tbl then Return False")
    public void whenCallUpdateEmailNotExistingIdFromCustomerTblFalse()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        assertAll
        (
            ()-> assertFalse(isEmailExistsCustTbl("new-Email.com")),
            ()-> assertFalse(isIdExistsCustTbl(3)),
            ()-> assertFalse( custDAO.updateEmail(3, "new-Email.com") ),
            ()-> assertFalse(isEmailExistsCustTbl("new-Email.com"))
        );
    }
    @Test
    @DisplayName("when Call Find By Id Existing From Customer Tbl then Return Optional Not Empty, contains the correct data")
    public void whenCallFindByIdExistingFromCustomerTblThenReturnOptionalNotEmpty()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        Optional<Customer> optcust = custDAO.findById(2);
        assertAll
        (
            ()-> assertTrue(isIdExistsCustTbl(2)),
            ()-> assertNotNull(optcust),
            ()-> assertFalse(optcust.isEmpty())
        );
        Customer c = optcust.get();
        assertAll
        (
            ()->assertEquals(f_name_cust , c.getFirst_name()),
            ()->assertEquals(l_name_cust , c.getLast_name()),
            ()->assertEquals(email_cust , c.getEmail()),
            ()->assertEquals(pswd_cust , c.getPassword())
        );
    }
    @Test
    @DisplayName("when Call Find By Id Not Existing From Customer Tbl then Return Optional Empty")
    public void whenCallFindByIdNotExistingFromCustomerTblThenReturnOptionalEmpty()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        assertFalse(isIdExistsCustTbl(3));
        Optional<Customer> optcust = custDAO.findById(3);
        assertAll
        (
            ()-> assertNotNull(optcust),
            ()-> assertTrue(optcust.isEmpty())
        );
    }

    @Test
    @DisplayName("when Call Find By Email And Pswd Existing From Customer Tbl then Return Optional Not Empty, contains the correct data")
    public void whenCallFindByEmailAndPswdExistingFromCustomerTblThenReturnOptionalNotEmpty()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        Optional<Customer> optcust = custDAO.findByEmailAndPassword(email_cust, pswd_cust);
        assertAll
                (
                        ()-> assertNotNull(optcust),
                        ()-> assertFalse(optcust.isEmpty())
                );
        Customer c = optcust.get();
        assertAll
                (
                        ()->assertEquals(f_name_cust , c.getFirst_name()),
                        ()->assertEquals(l_name_cust , c.getLast_name()),
                        ()->assertEquals(email_cust , c.getEmail()),
                        ()->assertEquals(pswd_cust , c.getPassword())
                );
    }
    @Test
    @DisplayName("when Call Find By email and pswd Not Existing From Customer Tbl then Return Optional Empty")
    public void whenCallFindByEmailAndPswdNotExistingFromCustomerTblThenReturnOptionalEmpty()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        Optional<Customer> optcust = custDAO.findByEmailAndPassword("XXXXXX", "XXXXXX");
        assertAll
                (
                        ()-> assertNotNull(optcust),
                        ()-> assertTrue(optcust.isEmpty())
                );
    }
    @Test
    @DisplayName("when Call get All From Empty Customer Tbl Then Return List Size 0")
    public void whenCallgetAllFromEmptyCustomerTblThenReturnListSize0()
    {
        List<Customer> list = custDAO.getAll();
        assertEquals(0, list.size());
    }
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,8,20})
    public void whenGetAllFromCustomerTblThenGetListOfCorrectSize(int i)
    {
        for (int j = 0; j < i; j++)
        {
            custDAO.create(cust);
        }
        assertAll
        (
            ()->assertEquals(countCustTbl(), i),
            ()->assertEquals(i, custDAO.getAll().size())
        );
    }
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,8,20})
    @DisplayName("when count then return correct or 0 if empty")
    public void whenCountThenReturnCorrectOr0IfEmpty(int i)
    {
        for (int j = 0; j < i; j++)
        {
            custDAO.create(cust);
        }
        assertEquals(i, custDAO.count());
    }
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,8,20})
    @DisplayName("when getMaxId then return correct or 0 if empty")
    public void whengetMAxIdThenReturnCorrectOr0IfEmpty(int i)
    {
        for (int j = 0; j < i; j++)
        {
            custDAO.create(cust);
        }
        assertEquals(i, custDAO.getMaxId());
    }
    @Test
    @DisplayName("when getMaxId after removes and adds then return correct or 0 if empty")
    public void whengetMAxIdAfterRemovesAndAddsThenReturnCorrectOr0IfEmpty()
    {
        assertEquals(0, custDAO.getMaxId());
        custDAO.create(cust);
        assertEquals(1, custDAO.getMaxId());
        custDAO.create(cust);
        assertEquals(2, custDAO.getMaxId());
        custDAO.remove(2);
        assertEquals(1, custDAO.getMaxId());
        custDAO.create(cust);
        custDAO.create(cust);
        assertEquals(4, custDAO.getMaxId());
    }
    @Test
    @DisplayName("when purchase then added one correct entry in couponId_customerId table")
    public void whenPurchaseThenAddedOneCorrectEntryInCouponId_customerIdTable()
    {
        custDAO.create(cust);
        custDAO.create(cust);
        insertCompanyToCompanyTbl();
        insertCouponToCouponTbl(getMaxIdcompany());
        assertTrue( custDAO.purchase(getMaxIdCoupon(), getMaxIdCust()) );
        assertEquals(1, countCouponCustTbl());
    }

    @Test
    @DisplayName("when purchase not existing customer ID then throw NotExistingCustomerException")
    @Disabled("undone")
    public void whenPurchaseNotExistingCustomerIDThenThrowNotExistingCustomerException()
    {
        assertThrows(NotExistingCouponOrCustomerException.class, ()->custDAO.purchase(1, 1));
    }
    @Test
    @DisplayName("when purchase not existing coupon ID then throw NotExistingCouponException")
    @Disabled("undone")
    public void whenPurchaseNotExistingCouponIDThenThrowNotNotExistingCouponException()
    {
        custDAO.create(cust);
        assertThrows(NotExistingCouponOrCustomerException.class, ()->custDAO.purchase(1, 1));
    }

    private static void setupConnection()
    {
        Properties properties = new Properties();
        properties.put("user", "krill_admin");
        properties.put("password", "15kvz61982!");
        try {
            connection = DriverManager.getConnection(URL + "/" + DB, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void resetAllTables()
    {
        try {

            connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            connection.prepareStatement("truncate coupon").executeUpdate();
            connection.prepareStatement("truncate customer").executeUpdate();
            connection.prepareStatement("truncate company").executeUpdate();
            connection.prepareStatement("truncate couponid_customerid").executeUpdate();
            connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static long getMaxIdCust()
    {
        long id = 0;
        try
        {
            ResultSet rs =  connection.prepareStatement("select max(id) from customer").executeQuery();
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
    private long getMaxIdCoupon()
    {
        long id = 0;
        try
        {
            ResultSet rs =  connection.prepareStatement("select max(id) from coupon").executeQuery();
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
    private long getMaxIdcompany()
    {
        long id = 0;
        try
        {
            ResultSet rs =  connection.prepareStatement("select max(id) from company").executeQuery();
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
    private static long countCustTbl()
    {
        long count = 0;
        try
        {
            ResultSet rs =  connection.prepareStatement("select count(*) from customer").executeQuery();
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
    private long countCouponCustTbl()
    {
        long count = 0;
        try
        {
            ResultSet rs =  connection.prepareStatement("select count(*) from couponid_customerid").executeQuery();
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
    private static boolean isEmailExistsCustTbl(String email)
    {
        boolean isExists = false;
        try
        {
            ResultSet rs =  connection.prepareStatement("select exists(select email from customer where email = " + "\"" + email + "\""  + ")").executeQuery();
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
    private boolean isIdExistsCustTbl(int id)
    {
        boolean isExists = false;
        try
        {
            ResultSet rs =  connection.prepareStatement("select exists(select id from customer where id = " + "\"" + id + "\""  + ")").executeQuery();
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
    private void insertCouponToCouponTbl(long companyId)
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("insert into coupon(company_id, category, title, price, amount) values (?,?,?,?,?)");
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

    private void insertCompanyToCompanyTbl()
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("insert into company(name, password) values(?,?)");
            ps.setString(1, company.getName());
            ps.setString(2, company.getPassword());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}



