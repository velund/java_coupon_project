package com.velundkvz.data.database.dao;

import com.velundkvz.data.model.Coupon;
import com.velundkvz.data.test_utils.Utils;

import static com.velundkvz.data.DefaultModels.test_dflt_not_expired_coupon;
import static com.velundkvz.data.test_utils.Utils.*;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CouponMySQLDAOTest {

    private static CouponMySQLDAO couponDAO;

    @BeforeAll
    private static void setup()
    {
        resetAllTables();
        couponDAO = new CouponMySQLDAO();
    }

    @AfterEach
    void tearDown()
    {
        resetAllTables();
    }

    @Nested
    @DisplayName("when Call Add: ")
    class WhenCallAdd
    {
        @Test
        @DisplayName("Then Does Not Throw Any Exception")
        public void ThenDoesNotThrowAnyException()
        {
            Utils.insertDfltCompanyToCompanyTbl();
            test_dflt_not_expired_coupon.setCompany_id(getMaxIdcompany());
            assertDoesNotThrow( ()-> couponDAO.add(test_dflt_not_expired_coupon) );
        }

        @Test
        @DisplayName("Then One Entry Added On coupon Tbl and index incremented")
        public void ThenOneEntryAddedOnCouponTbl()
        {
            Utils.insertDfltCompanyToCompanyTbl();
            long lastId = getMaxIdCoupon();
            long lastCount = countCouponTbl();
            test_dflt_not_expired_coupon.setCompany_id(getMaxIdcompany());
            couponDAO.add(test_dflt_not_expired_coupon);
            long newtId = getMaxIdCoupon();
            long newCount = countCouponTbl();
            assertAll
                    (
                            ()->assertTrue(newtId != -1),
                            ()->assertTrue(newCount != -1),
                            ()->assertEquals(lastId + 1, newtId),
                            ()->assertEquals(lastCount +1, newCount)
                    );
        }

        @Test
        @DisplayName("Then correct coupon returned")
        public void AddedOnCouponTbl()
        {
            Utils.insertDfltCompanyToCompanyTbl();
            long lastCompanyId = getMaxIdcompany();
            test_dflt_not_expired_coupon.setCompany_id(lastCompanyId);
            Coupon returnedCoupon = couponDAO.add(test_dflt_not_expired_coupon);
            assertAll
            (
                    ()->assertEquals(1, returnedCoupon.getId()),
                    ()->assertEquals(lastCompanyId, returnedCoupon.getCompany_id()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getAmount(), returnedCoupon.getAmount()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getCategory(), returnedCoupon.getCategory()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getPrice(), returnedCoupon.getPrice()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getTitle(), returnedCoupon.getTitle()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getDescription(), returnedCoupon.getDescription()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getStart_date(), returnedCoupon.getStart_date()),
                    ()->assertEquals(test_dflt_not_expired_coupon.getEnd_date(), returnedCoupon.getEnd_date())
            );
        }


    }
    @Nested
    @DisplayName("when Call Get All: ")
    class WhenCallGetAll
    {
        @Test
        @DisplayName("then if no coupons, then  get empty list")
        public void ThenGetEmptyListIfNoCoupons()
        {
            assertEquals(0,  couponDAO.getAll().size() );
        }
        @Test
        @DisplayName("then if 3 cp. in table, get list of 3 correct cpns")
        public void ThenGetListOfSize3If3couponsInTable()
        {
            for (int i = 0; i < 3; i++)
            {
                Utils.insertDfltCompanyToCompanyTbl();
                test_dflt_not_expired_coupon.setCompany_id(getMaxIdcompany());
                couponDAO.add(test_dflt_not_expired_coupon);
            }
            List<Coupon> returnedList = couponDAO.getAll();
            assertEquals(3, returnedList.size());
        }
    }
    @Nested
    @DisplayName("when Call Get All customer coupons: ")
    class WhenCallGetAllCustomerCoupons
    {
        @Test
        @DisplayName("then if no coupons, then  get empty list")
        public void ThenGetEmptyListIfNoCoupons()
        {
            assertEquals(0,  couponDAO.getAll().size() );
        }
        @Test
        @DisplayName("then if customer owns 2 cpns. then get list of 2 correct cpns")
        public void ThenGetListOfSize2If2coupons()
        {
            fill2EntriesCouponIdCustomerIdRelations();
            List<Coupon> returnedList = couponDAO.getAllCustomerCoupons(getMaxIdCust());
            assertEquals(2, returnedList.size());
            for (Coupon coup: returnedList)
            {
                assertTrue(getMaxIdCoupon() == coup.getId() || getMinIdCoupon() == coup.getId() );
            }
        }
    }
    @Nested
    @DisplayName("when Call Get All expired: ")
    class WhenCallGetAllExpired
    {
        @Test
        @DisplayName("then if no expired coupons, then  get empty list")
        public void ThenGetEmptyListIfNoCoupons()
        {
            fillNotExpiredCoupons(3);
            assertEquals(0,  couponDAO.getAllExpired().size() );
        }
        @Test
        @DisplayName("then if 3 expired coupons, then get list of 3")
        public void andOnly3ExpiredThenGetListOf3()
        {
            fillExpiredCoupons(3);
            List<Coupon> returnedList = couponDAO.getAllExpired();
            assertEquals(3, returnedList.size());
        }
        @Test
        @DisplayName("then if 3 expired coupons, among 6, then get list of 3")
        public void and3Expiredamong6ThenGetListOf3()
        {
            fillExpiredCoupons(3);
            fillNotExpiredCoupons(3);
            List<Coupon> returnedList = couponDAO.getAllExpired();
            assertEquals(3, returnedList.size());
        }
    }


}