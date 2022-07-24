package com.velundkvz.data.database.dao;

import com.velundkvz.data.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDAO
{
    void add(Company company);
    boolean remove(long id);
    boolean updateEmail(long id, String email);
    boolean updatePassword(long id, String password);
    Optional<Company> findById(long id);
    List<Company> getAll();
    Optional<Company> findByEmailAndPassword(String email, String password);
    long count();
    long getMaxId();
    boolean isExists(long id);
    boolean isExists(String email);
}
