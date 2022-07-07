package com.velundkvz.data.database.dao;

import com.velundkvz.data.test_utils.Utils;
import static com.velundkvz.data.test_utils.Utils.*;

import com.velundkvz.data.model.Customer;
import static com.velundkvz.data.DefaultModels.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertAll;

class CustomerMySQLDAOTest
{
    private static CustomerMySQLDAO custDAO;

    @BeforeAll
    private static void setup()
    {
        resetAllTables();
        custDAO = new CustomerMySQLDAO();
    }
    @AfterEach
    private void tearDown()
    {
        resetAllTables();
    }
    @Test
    @DisplayName("when call add then does not throw any exception")
    public void whenCallAddThenDoesNotThrowAnyException()
    {
        assertDoesNotThrow( ()-> new CustomerMySQLDAO().add(test_dflt_cust) );
    }
    @Test
    @DisplayName("when Call add One Entry Added On Customer Tbl and index incremented")
    public void whenCallAddOneEntryAddedOnCustomerTbl()
    {
        long lastId = getMaxIdCust();
        long lastCount = countCustTbl();
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        assertFalse( custDAO.remove(2) );
    }
    @Test
    @DisplayName("when Call remove existing id from Customer Tbl then return true")
    public void whenCallRemoveExistingIdFromCustomerTblThenReturnTrue()
    {
        custDAO.add(test_dflt_cust);
        long id = getMaxIdCust();
        assertTrue(id != 0);
        assertTrue( custDAO.remove( id ) );
    }
    @Test
    @DisplayName("when Call remove existing id from Customer Tbl then Count Decrements")
    public void whenCallRemoveExistingIdFromCustomerTblThenCountDecrements()
    {
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
        custDAO.remove(1);
        assertFalse(custDAO.remove(1));
    }
    @Test
    @DisplayName("when Call update email existing id from Customer Tbl then Return True")
    public void whenCallUpdateEmailExistingIdFromCustomerTblTrue()
    {
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
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
            custDAO.add(test_dflt_cust);
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
            custDAO.add(test_dflt_cust);
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
            custDAO.add(test_dflt_cust);
        }
        assertEquals(i, custDAO.getMaxId());
    }
    @Test
    @DisplayName("when getMaxId after removes and adds then return correct or 0 if empty")
    public void whengetMAxIdAfterRemovesAndAddsThenReturnCorrectOr0IfEmpty()
    {
        assertEquals(0, custDAO.getMaxId());
        custDAO.add(test_dflt_cust);
        assertEquals(1, custDAO.getMaxId());
        custDAO.add(test_dflt_cust);
        assertEquals(2, custDAO.getMaxId());
        custDAO.remove(2);
        assertEquals(1, custDAO.getMaxId());
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
        assertEquals(4, custDAO.getMaxId());
    }
    @Test
    @DisplayName("when purchase then added one correct entry in couponId_customerId table")
    public void whenPurchaseThenAddedOneCorrectEntryInCouponId_customerIdTable()
    {
        custDAO.add(test_dflt_cust);
        custDAO.add(test_dflt_cust);
        Utils.insertDfltCompanyToCompanyTbl();
        Utils.insertNotExpiredCouponToCouponTbl(getMaxIdcompany());
        assertTrue( custDAO.purchase(getMaxIdCoupon(), getMaxIdCust()) );
        assertEquals(1, countCouponCustTbl());
    }

    @Test
    @DisplayName("when purchase not existing customer ID then purchase returns false")
    public void whenPurchaseNotExistingCustomerIDThenThrowNotExistingCustomerException()
    {
        assertFalse( custDAO.purchase(1, 1));
    }
    @Test
    @DisplayName("when purchase not existing coupon ID then throw SQLIntegrityConstraintViolationException")
    public void whenPurchaseNotExistingCouponIDThenThrowNotNotExistingCouponException()
    {
        custDAO.add(test_dflt_cust);
        assertFalse( custDAO.purchase(1, 1));
    }


}



