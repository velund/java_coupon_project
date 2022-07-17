package com.velundkvz.data.service;

import com.velundkvz.data.model.Customer;

import java.util.Optional;

public interface CustomerService
{
    Optional<Customer> get(long id);
    long insert(Customer customer);
    void delete(long id);
    void updateEmail(int id, String email);
    void updatePassword(int id, String email);
    void purchase(long customerId, long couponId);
}
