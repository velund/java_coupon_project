package com.velundkvz.data.service;

import com.velundkvz.data.model.Coupon;
import com.velundkvz.data.model.Customer;
import com.velundkvz.exceptions.CouponNotExistsInDBException;
import com.velundkvz.exceptions.CustomerEmailExistsException;
import com.velundkvz.exceptions.CustomerPasswordTooWeakException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static com.velundkvz.data.DefaultModels.*;
import static com.velundkvz.data.test_utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest
{
    private static CustomerServiceImpl custService;
    @BeforeAll
    public static void setup()
    {
        resetAllTables();
        custService = new CustomerServiceImpl();
    }
    @AfterEach
    private void tearDown()
    {
        resetAllTables();
    }
    @Nested
    @DisplayName("when call get()")
    class WhenCallGet
    {
        @ParameterizedTest
        @ValueSource(ints = {0,1,2,3,4})
        @DisplayName("if table is empty, returns empty optional")
        public void ifTableIsEmptyReturnsEmptyOptional(int id)
        {
            assertTrue( custService.get(id).isEmpty() );
        }
        @ParameterizedTest
        @ValueSource(ints = {3,4, 0})
        @DisplayName("if not existing, returns empty optional")
        public void ifNotExistingReturnsEmptyOptional(int id)
        {
            fillDefaultCustomers(2);
            assertTrue( custService.get(id).isEmpty() );
        }
        @ParameterizedTest
        @ValueSource(ints = {1})
        @DisplayName("returns correct Customer optional")
        public void returnsCorrectCustomerOptional(int id)
        {
            insertDfltCustomerToCustomerTbl();
            assertAll
            (
                ()->assertTrue( custService.get(id).isPresent() ),
                ()->assertEquals(dflt_email_cust, custService.get(id).get().getEmail() ),
                ()->assertEquals(dflt_f_name_cust, custService.get(id).get().getFirst_name() ),
                ()->assertEquals( dflt_l_name_cust, custService.get(id).get().getLast_name() ),
                ()->assertEquals(dflt_strong_pswd_cust, custService.get(id).get().getPassword() )
            );
        }
        @ParameterizedTest
        @ValueSource(ints = {1})
        @DisplayName("if no coupons to customer, returns Customer optional, with empty coupons list")
        public void ifNoCouponsToCustomerReturnsCustomerOptionalWithEmptyCouponsList(int id)
        {
            insertDfltCustomerToCustomerTbl();
            Optional<Customer> optCust = custService.get(id);
            assertTrue( custService.get(id).isPresent() );
            Customer customer = optCust.get();
            assertAll
            (
                ()->assertEquals(dflt_email_cust, customer.getEmail() ),
                ()->assertEquals(dflt_f_name_cust, customer.getFirst_name() ),
                ()->assertEquals( dflt_l_name_cust, customer.getLast_name() ),
                ()->assertEquals(dflt_strong_pswd_cust, customer.getPassword() ),
                ()->assertEquals(0, customer.getCoupons().size())
            );
        }
        @ParameterizedTest
        @ValueSource(ints = {1})
        @DisplayName("returns Customer, with coupons list")
        public void CustomerReturnsCustomerOptionalWithCouponsList(int id)
        {
            insertDfltCustomerToCustomerTbl();
            insertDfltCompanyToCompanyTbl();
            insertNotExpiredCouponToCouponTbl(1);
            insertNotExpiredCouponToCouponTbl(1);
            insertToCouponId_CustomerIdTblTwoCouponsToMaxIdCust();
            Optional<Customer> optCust = custService.get(id);
            assertTrue( custService.get(id).isPresent() );
            Customer customer = optCust.get();
            List<Coupon> custCoupons = customer.getCoupons();
            assertAll
            (
                ()->assertEquals(dflt_email_cust, customer.getEmail() ),
                ()->assertEquals(dflt_f_name_cust, customer.getFirst_name() ),
                ()->assertEquals( dflt_l_name_cust, customer.getLast_name() ),
                ()->assertEquals(dflt_strong_pswd_cust, customer.getPassword() ),
                ()->assertEquals(2, custCoupons.size())
            );
        }

    }
    @Nested
    @DisplayName("when call insert()")
    class WhenCallInsert
    {
        @Test
        @DisplayName("customer added")
        public void customerAdded()
        {
            custService.insert(test_dflt_cust_with_strong_password);
            assertEquals(1, countCustTbl());
        }
        @Test
        @DisplayName("returned last (max) id")
        public void returnMaxId()
        {
            custService.insert(test_dflt_cust_with_strong_password);
            assertEquals(1, getMaxIdCust());
        }
        @Test
        @DisplayName("with existing email, throws CustomerEmailExistsException")
        public void withExistingEmailThrowsCustomerEmailExistsException()
        {
            insertDfltCustomerToCustomerTbl();
            assertThrows(CustomerEmailExistsException.class, ()->custService.insert(test_dflt_cust_with_strong_password));
        }
        @Test
        @DisplayName("with weak password, throws CustomerPasswordTooWeakException")
        public void withWeakPasswordThrowsCustomerPasswordTooWeakException()
        {
            assertThrows(CustomerPasswordTooWeakException.class, ()->custService.insert(test_dflt_cust_with_weak_password));
        }

    }

    @Nested
    @DisplayName("when call delete()")
    class WhenCallDelete
    {
        @ParameterizedTest
        @ValueSource(ints = {3})
        @DisplayName("customer count decremented")
        public void customerCountDecrements(int n)
        {
            for (int i = 0; i < n; i++)
            {
                insertDfltCustomerToCustomerTbl();
            }
            assertEquals(n, countCustTbl());
            custService.delete(getMaxIdCust());
            assertEquals(n-1, countCustTbl());
        }
        @ParameterizedTest
        @ValueSource(ints = {3})
        @DisplayName("customer with removed id not exists")
        public void customerWithRemovedIdNotExists(int n)
        {
            for (int i = 0; i < n; i++)
            {
                insertDfltCustomerToCustomerTbl();
            }
            assertTrue(isIdExistsCustTbl(2));
            custService.delete(2);
            assertFalse(isIdExistsCustTbl(2));
        }
    }
    @Nested
    @DisplayName("when call updateEmail()")
    class WhenCallUpdateEmail
    {
        @Test
        @DisplayName("mail changed")
        public void mailChanged()
        {
            insertDfltCustomerToCustomerTbl();
            assertEquals(dflt_email_cust, getCustEmail(1));
            custService.updateEmail(1, updated_email_cust);
            assertEquals(updated_email_cust, getCustEmail(1));

        }
    }
    @Nested
    @DisplayName("when call updatePassword()")
    class WhenCallUpdatePassword
    {
        @Test
        @DisplayName("password changed")
        public void mailChanged()
        {
            insertDfltCustomerToCustomerTbl();
            assertEquals(dflt_strong_pswd_cust, getCustPassword(1));
            custService.updatePassword(1, updated_strong_pswd_cust);
            assertEquals(updated_strong_pswd_cust, getCustPassword(1));
        }
    }
    @Nested
    @DisplayName("when call purchase()")
    class WhenCallPurchase
    {
        @Test
        @DisplayName("existing coupon, then customer coupons list not updated with correct coupon")
        public void existingCouponCustomerCouponListUpdated()
        {
            insertDfltCompanyToCompanyTbl();
            insertNotExpiredCouponToCouponTbl(1);
            insertDfltCustomerToCustomerTbl();
            assertEquals(0, getCustomersCouponsListSize(1));
            custService.purchase(1, 1);
            assertEquals(1, getCustomersCouponsListSize(1));
        }
        @Test
        @DisplayName("not existing coupon, then throws CouponNotExistsInDBException")
        public void notExistingThenThrowsCouponNotExistsInDBException()
        {
            insertDfltCompanyToCompanyTbl();
            insertNotExpiredCouponToCouponTbl(1);
            insertDfltCustomerToCustomerTbl();
            assertEquals(0, getCustomersCouponsListSize(1));
            assertThrows(CouponNotExistsInDBException.class, ()-> custService.purchase(not_existing_coupon_id, 1) );
            assertEquals(0, getCustomersCouponsListSize(1));
        }
        @Test
        @DisplayName("amount of coupon decreased")
        public void amountOfCouponDecreased()
        {
            insertDfltCompanyToCompanyTbl();
            insertNotExpiredCouponToCouponTbl(1);
            insertDfltCustomerToCustomerTbl();
            custService.purchase(1, 1);
            assertEquals(dflt_coupon_amount-1, getCoupAmount(1));
        }
        private long getCustomersCouponsListSize(long id)
        {
            Optional<Customer> optCus = custService.get(1);
            return optCus.map(customer -> customer.getCoupons().size()).orElse(0);
        }
    }
}