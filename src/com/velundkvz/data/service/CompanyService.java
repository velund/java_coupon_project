package com.velundkvz.data.service;

import com.velundkvz.data.model.Company;
import com.velundkvz.data.model.Coupon;

import java.util.Optional;

public interface CompanyService
{
    Optional<Company> get(long id);
    long insert(Company company);
    boolean delete(long id);
    void updateEmail(int id, String email);
    void updatePassword(int id, String password);
    void createCoupon(Coupon coupon);
}
