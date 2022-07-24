package com.velundkvz.data.service;

import com.velundkvz.exceptions.CompanyEmailExistsException;
import com.velundkvz.exceptions.CompanyNotExistsException;
import com.velundkvz.exceptions.CouponAlreadyExistsException;
import com.velundkvz.exceptions.PasswordTooWeakException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.velundkvz.data.DefaultModels.*;
import static com.velundkvz.data.service.CompanyServiceImpl.COMPANY_MIN_LEGAL_ID;
import static com.velundkvz.data.test_utils.Utils.*;
import static com.velundkvz.definitions.serviceDefinitions.ServiceDefinitions.COMPANY_ID_LESS_THAN_MIN_LEGAL_FRMT_EXC_MSG;
import static org.junit.jupiter.api.Assertions.*;

class CompanyServiceImplTest
{
    private static CompanyServiceImpl compService;
    @BeforeAll
    public static void setup()
    {
        resetAllTables();
        compService = new CompanyServiceImpl();
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
        @ValueSource(ints = {0,-1,-2})
        @DisplayName("with id <=0, throws IllegalArgumentsException")
        public void withIdLEZeroThrowsIllegalArgumentsException(int id)
        {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()->compService.get(id));
            assertEquals(COMPANY_ID_LESS_THAN_MIN_LEGAL_FRMT_EXC_MSG.formatted(id, COMPANY_MIN_LEGAL_ID), e.getMessage());
        }
        @ParameterizedTest
        @ValueSource(ints = {1,2,Integer.MAX_VALUE})
        @DisplayName("with id <=0, throws IllegalArgumentsException")
        public void withIdGEOneDoesNotThrow(int id)
        {
            assertDoesNotThrow( ()->compService.get(id));
        }
        @Test
        @DisplayName("from empty, returns empty Optional")
        public void formEmptyReturnsEmtyOptional()
        {
            assertTrue( compService.get(1).isEmpty() );
        }
        @Test
        @DisplayName("not existing id, returns empty Optional")
        public void notExistingIdReturnsEmtyOptional()
        {
            insertDfltCompanyToCompanyTbl();
            assertTrue( compService.get(getMaxIdCompany()+1).isEmpty() );
        }
        @Test
        @DisplayName("existing id, returns not empty Optional")
        public void existingIdReturnsNotEmtyOptional()
        {
            insertDfltCompanyToCompanyTbl();
            assertTrue( compService.get(getMaxIdCompany()).isPresent() );
        }
    }
    @Nested
    @DisplayName("when call insert()")
    class WhenCallInsert
    {
        @Test
        @DisplayName("to empty, company count incremented")
        public void toEmptyCompanyCountIncremented()
        {
            assertEquals(0, countCompanies());
            compService.insert(test_dflt_company);
            assertEquals(1, countCompanies());
        }
        @Test
        @DisplayName("to empty, returns expected id")
        public void toEmptyReturnsExpextedId()
        {
            assertEquals(1,  compService.insert(test_dflt_company) );
        }
        @Test
        @DisplayName("with existing email, throws CompanyEmailExistsException")
        public void withExistingEmailThrowsCompanyEmailExistsException()
        {
            insertDfltCompanyToCompanyTbl();
            assertThrows(CompanyEmailExistsException.class, ()->compService.insert(test_dflt_company));
        }
        @Test
        @DisplayName("with not existing email, doesn't throw")
        public void withNotExistingEmailDoesNotThrow()
        {
            insertCompanyToCompanyTbl("name", "password", "email");
            assertDoesNotThrow(()->compService.insert(test_dflt_company));
        }
    }
    @Nested
    @DisplayName("when call UpdateEmail()")
    class WhenCallUpdateEmail
    {
        @Test
        @DisplayName("if email exists, throws CompanyEmailExistsException")
        public void ifEmailExistsThrowsCompanyEmailExistsException()
        {
            insertDfltCompanyToCompanyTbl();
            assertThrows(CompanyEmailExistsException.class, ()->compService.updateEmail(1, dflt_email_comp));
        }
        @ParameterizedTest
        @ValueSource(ints = {0, -1, -2})
        @DisplayName("with illegal id, throws IllegalArgumentException")
        public void withEllagalIdThrowsIllegalArgumentException(int id)
        {
            assertThrows(IllegalArgumentException.class, ()->compService.updateEmail(id, dflt_email_comp));
        }
        @Test
        @DisplayName("email of correct company updated")
        public void emailOfCorrectCompanyUpdated()
        {
            insertDfltCompanyToCompanyTbl();
            insertDfltCompanyToCompanyTbl();
            compService.updateEmail(2, updated_email_comp);
            assertAll
            (
                    ()->assertEquals(getCompanyEmail(2), updated_email_comp ),
                    ()->assertEquals(getCompanyEmail(1), dflt_email_comp ),
                    ()->assertNotEquals(getCompanyEmail(1), updated_email_comp )
            );
        }

    }
    @Nested
    @DisplayName("when call delete()")
    class WhenCallDelete
    {
        @ParameterizedTest
        @ValueSource(ints = {0, -1, -2})
        @DisplayName("not legal id, throws IllegalArgumentException")
        public void notLegalIdThrowsIllegalArgumentException(int id)
        {
            assertThrows( IllegalArgumentException.class, ()->compService.delete(id) );
        }
        @Test
        @DisplayName("existing id, count companies decremented")
        public void existingIdThenCountCompaniesDecremented()
        {
            insertDfltCompanyToCompanyTbl();
            long count = countCompanies();
            assertEquals(1, count);
            compService.delete(1);
            assertEquals(0, count-1);
        }
        @Test
        @DisplayName("existing id, returns true")
        public void existingIdThenreturnsTrue()
        {
            insertDfltCompanyToCompanyTbl();
            assertTrue( compService.delete(1) );
        }
        @Test
        @DisplayName("not existing id, returns false")
        public void notExistingIdThenreturnsFalse()
        {
            insertDfltCompanyToCompanyTbl();
            assertEquals(1, countCompanies());
            assertFalse( compService.delete(2) );
        }
        @Test
        @DisplayName("empty, returns false")
        public void emptyThenreturnsFalse()
        {
            assertFalse( compService.delete(2) );
        }
    }
    @Nested
    @DisplayName("when call updatePassword()")
    class WhenCallupdatePassword
    {
        @ParameterizedTest
        @ValueSource(ints = {0, -1, -2})
        @DisplayName("with illegal id throws IllegalArgumentException")
        public void withIllegalIdThrowsIllegalArgumentException(int id)
        {
            assertThrows( IllegalArgumentException.class, ()->  compService.updatePassword(id, dflt_strong_pswd_comp) );
        }
        @Test
        @DisplayName("with weak password, throws PasswordTooWeakException")
        public void withWeakPasswordThrowsPasswordTooWeakException()
        {
            insertDfltCompanyToCompanyTbl();
            assertThrows(PasswordTooWeakException.class, ()-> compService.updatePassword(1, dflt_weak_pswd_comp) );
        }
        @Test
        @DisplayName("updates password")
        public void updatesPassword()
        {
            insertDfltCompanyToCompanyTbl();
            assertEquals(dflt_strong_pswd_comp, getCompanyPassword(1));
            compService.updatePassword(1, updated_strong_pswd_comp);
            assertEquals(updated_strong_pswd_comp, getCompanyPassword(1));
        }

    }
    @Nested
    @DisplayName("when call createCoupon()")
    class WhenCallCreateCoupon
    {
        @Test
        @DisplayName("with not existing company id, throws CompanyNotExistsException")
        public void withNotExistingCompanyIdThrowsCompanyNotExistsException()
        {
            test_dflt_not_expired_coupon.setCompany_id(1);
            assertThrows(CompanyNotExistsException.class, ()->compService.createCoupon(test_dflt_not_expired_coupon) );
        }
        @Test
        @DisplayName("with same company id, and title throws CouponAlreadyExistsException")
        public void withSameCompanyIdAndTitleThrowsCouponAlreadyExistsException()
        {
            insertDfltCompanyToCompanyTbl();
            test_dflt_not_expired_coupon.setCompany_id(1);
            insertNotExpiredCouponToCouponTbl(1);
            assertThrows(CouponAlreadyExistsException.class, ()->compService.createCoupon(test_dflt_not_expired_coupon) );
        }
        @Test
        @DisplayName("with same company id, increments coupon count of the company")
        public void withSameCompanyIdIncrementscouponcoutOftheCompany()
        {
            insertDfltCompanyToCompanyTbl();
            test_dflt_not_expired_coupon.setCompany_id(1);
            assertEquals(0, countCouponTbl(1) );
            compService.createCoupon(test_dflt_not_expired_coupon);
            assertEquals(1, countCouponTbl(1) );
        }
        @Test
        @DisplayName("one entry created")
        public void oneEntryCreated()
        {
            fillDefaultCompanies(2);
            test_dflt_not_expired_coupon.setCompany_id(1);
            assertEquals(0, countCouponTbl(1) );
            assertEquals(0, countCouponTbl(2) );
            compService.createCoupon(test_dflt_not_expired_coupon);
            test_dflt_not_expired_coupon.setCompany_id(2);
            compService.createCoupon(test_dflt_not_expired_coupon);
            test_dflt_not_expired_coupon.settitle(new_title_coup);
            compService.createCoupon(test_dflt_not_expired_coupon);
            assertAll
            (
                ()->assertEquals(1, countCouponTbl(1, dflt_title_coup) ),
                ()->assertEquals(1, countCouponTbl(2, dflt_title_coup) ),
                ()->assertEquals(1, countCouponTbl(2, new_title_coup) ),
                ()->assertEquals(2, countCouponTbl(2) )
            );

        }
    }


}