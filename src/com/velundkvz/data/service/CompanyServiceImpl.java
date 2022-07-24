package com.velundkvz.data.service;

import java.util.Optional;

import com.velundkvz.data.database.dao.CompanyDAO;
import com.velundkvz.data.database.dao.CompanyMySQLDAO;
import com.velundkvz.data.database.dao.CouponDAO;
import com.velundkvz.data.database.dao.CouponMySQLDAO;
import com.velundkvz.data.model.Company;
import com.velundkvz.data.model.Coupon;
import com.velundkvz.exceptions.CompanyEmailExistsException;
import com.velundkvz.exceptions.CompanyNotExistsException;
import com.velundkvz.exceptions.CouponAlreadyExistsException;
import com.velundkvz.exceptions.PasswordTooWeakException;
import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.PASSWORD_TOO_WEAK_EXC_FORMAT_MSG;
import static com.velundkvz.definitions.serviceDefinitions.ServiceDefinitions.*;
import static com.velundkvz.definitions.serviceDefinitions.ServiceDefinitions.MIN_CUSTOMER_PSW_LENGTH;

public class CompanyServiceImpl implements  CompanyService
{
    public static final int COMPANY_MIN_LEGAL_ID = 1;
    private CompanyDAO companyDAO;
    private CouponDAO couponDAO;
    public CompanyServiceImpl()
    {
        companyDAO = new CompanyMySQLDAO();
        couponDAO = new CouponMySQLDAO();
    }

    @Override
    public Optional<Company> get(long id)
    {
        validateId(id);
        return companyDAO.findById(id);
    }

    @Override
    public synchronized long insert(Company company)
    {
        validateCompanyBeforeInsertion(company);
        companyDAO.add(company);
        return getLastInsertedCompanyId();
    }

    @Override
    public boolean delete(long id)
    {
        validateId(id);
        return companyDAO.remove(id);
    }

    @Override
    public void updateEmail(int id, String email)
    {
        validateId(id);
        validateEmail(email);
        companyDAO.updateEmail(id, email);
    }

    @Override
    public void updatePassword(int id, String password)
    {
        validateId(id);
        validatecompanyPassword(password);
        companyDAO.updatePassword(id, password);
    }

    @Override
    public void createCoupon(Coupon coupon)
    {
        validateCompanyIdOfCoupon(coupon);
        validatecouponBeforeCreation(coupon);
        couponDAO.add(coupon);
    }

    private void validateCompanyIdOfCoupon(Coupon coupon)
    {
        long comp_id = coupon.getCompany_id();
        if (!companyDAO.isExists(comp_id))
        {
            throw new CompanyNotExistsException(NO_COMPANY_WITH_SUCH_ID_FRMT_EXC_MSG.formatted(comp_id));
        }
    }

    private void validatecouponBeforeCreation(Coupon coupon)
    {
        long id = coupon.getCompany_id();
        String title = coupon.getTitle();
        if ( couponDAO.isExistsByCompanyIdAndTitle(id, title) )
        {
            throw new CouponAlreadyExistsException(COUPON_ALREADY_EXISTS_FRMT_EXC_MSG.formatted(id, title));
        }
    }

    /*PRIVATE*/
    private long getLastInsertedCompanyId()
    {
        return companyDAO.getMaxId();
    }
    private void validateCompanyBeforeInsertion(Company company)
    {
        validateEmail(company.getEmail());
    }
    private void validateEmail(String email)
    {
        if (companyDAO.isExists(email))
        {
            throw new CompanyEmailExistsException(COMPANY_WITH_CURRENT_EMAIL_EXISTS_FRMT_EXC_MSG.formatted(email));
        }
    }
    private void validateId(long id)
    {
        if (id < COMPANY_MIN_LEGAL_ID)
        {
            throw new IllegalArgumentException(COMPANY_ID_LESS_THAN_MIN_LEGAL_FRMT_EXC_MSG.formatted(id, COMPANY_MIN_LEGAL_ID));
        }
    }
    private void validatecompanyPassword(String password)
    {
        if (tooShort(password) || !hasAtLeastOneDigit(password) ||
                !hasAtLeastOneUpperCaseLetter(password) ||
                !hasAtLeastOneLowerCaseLetter(password)
        )
        {
            throw new PasswordTooWeakException(PASSWORD_TOO_WEAK_EXC_FORMAT_MSG.formatted(password));
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

}
