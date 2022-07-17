package com.velundkvz.data.service;

import com.velundkvz.data.database.dao.CouponDAO;
import com.velundkvz.data.database.dao.CouponMySQLDAO;
import com.velundkvz.data.database.dao.CustomerDAO;
import com.velundkvz.data.database.dao.CustomerMySQLDAO;
import com.velundkvz.data.model.Customer;
import com.velundkvz.exceptions.CouponAmountZeroException;
import com.velundkvz.exceptions.CouponOwnedByCustomerException;
import com.velundkvz.exceptions.CustomerEmailExistsException;
import com.velundkvz.exceptions.CustomerPasswordTooWeakException;

import java.util.Optional;

import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.*;
import static com.velundkvz.definitions.serviceDefinitions.ServiceDefinitions.*;

public class CustomerServiceImpl implements CustomerService
{
    CustomerDAO customerDAO;
    CouponDAO couponDAO;
    CustomerServiceImpl()
    {
        customerDAO = new CustomerMySQLDAO();
        couponDAO = new CouponMySQLDAO();
    }

    @Override
    public Optional<Customer> get(long id)
    {
        Optional<Customer> opt = customerDAO.findById(id);
        opt.ifPresent(customer -> customer.setCoupons(couponDAO.getAllCustomerCoupons(id)));
        return opt;
    }

    @Override
    public synchronized long insert(Customer customer) throws CustomerEmailExistsException, CustomerPasswordTooWeakException
    {
        validate(customer);
        customerDAO.add(customer);
        return getLastInsertedCustomerId();
    }

    @Override
    public void delete(long id)
    {
        customerDAO.remove(id);
    }

    @Override
    public void updateEmail(int id, String email) throws CustomerEmailExistsException
    {
        validateCustomerEmail(email);
        customerDAO.updateEmail(id, email);
    }

    @Override
    public void updatePassword(int id, String password) throws CustomerPasswordTooWeakException
    {
        validateCustomerPassword(password);
        customerDAO.updatePassword(id, password);
    }

    @Override
    public void purchase(long couponId, long customerId) throws CouponAmountZeroException, CouponOwnedByCustomerException
    {
        checkCanBePurchased(couponId, customerId);
        if ( customerDAO.purchase(couponId, customerId) )
        {
            decreaseCouponAmount(couponId);
        }
    }

    private void decreaseCouponAmount(long couponId)
    {
        couponDAO.updateCouponAmount(couponId, couponDAO.getAmount(couponId) - 1);
    }

    private void checkCanBePurchased(long couponId, long customerId)
    {
        checkCouponIsInStock(couponId);
        checkNotOwned(couponId, customerId);
    }

    private void checkNotOwned(long couponId, long customerId)
    {
        if ( couponDAO.isCouponOwned(couponId, customerId) )
        {
            throw new CouponOwnedByCustomerException(COUPON_ALREADY_OWNED_EXC_FORMAT_MSG.formatted(customerId, couponId));
        }
    }

    private void checkCouponIsInStock(long couponId)
    {
        if ( couponDAO.getAmount(couponId) <= 0 )
        {
            throw new CouponAmountZeroException(COUPON_AMOUNT_ZERO_EXC);
        }
    }

    /* PRIVATE */
    private long getLastInsertedCustomerId()
    {
        return customerDAO.getMaxId();
    }
    private void validate(Customer customer)
    {
        validateCustomerPassword(customer.getPassword());
        validateCustomerEmail(customer.getEmail());
    }

    private void validateCustomerPassword(String password)
    {
        if (tooShort(password) || !hasAtLeastOneDigit(password) ||
                !hasAtLeastOneUpperCaseLetter(password) ||
                !hasAtLeastOneLowerCaseLetter(password)
        )
        {
            throw new CustomerPasswordTooWeakException(CUSTOMER_PASSWORD_TOO_WEAK_EXC_FORMAT_MSG.formatted(password));
        }
    }

    private boolean hasAtLeastOneLowerCaseLetter(String str)
    {
        return str.matches(AT_LEAST_ONE_LOWER_CASE_LETTER_RGX);
    }

    private boolean hasAtLeastOneUpperCaseLetter(String str)
    {
        return str.matches(AT_LEAST_ONE_UPPER_CASE_LETTER_RGX);
    }
    private boolean hasAtLeastOneDigit(String str)
    {
        return str.matches(AT_LEAST_ONE_DIGIT_RGX);
    }

    private boolean tooShort(String str)
    {
        return str.length() < MIN_CUSTOMER_PSW_LENGTH;
    }

    private void validateCustomerEmail(String email)
    {
        if (customerDAO.isExists(email) )
        {
            throw new CustomerEmailExistsException(CUSTOMER_EMAIL_ALREDY_EXISTS_EXC_FORMAT_MSG.formatted(email));
        }
    }
}
