package com.velundkvz.data.database.dao;

import com.velundkvz.data.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO
{
    void add(Customer customer);
    boolean remove(long id);
    boolean updateEmail(long id, String email);
    Optional<Customer> findById(long id);
    List<Customer> getAll();
    boolean purchase(long couponId, long customerId);
    Optional<Customer> findByEmailAndPassword(String email, String password);
    long count();
    long getMaxId();
}
