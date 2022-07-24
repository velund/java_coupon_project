package com.velundkvz.data.service;

import java.util.Optional;

import com.velundkvz.data.model.Customer;

public interface CustomerService
{
    Optional<Customer> get(long id);
    long insert(Customer customer);
    void delete(long id);
    void updateEmail(int id, String email);
    void updatePassword(int id, String email);
    void purchase(long customerId, long couponId);
}
