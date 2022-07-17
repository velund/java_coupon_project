package com.velundkvz.data.model;

import com.velundkvz.exceptions.InvalidCustomerBuildException;
import com.velundkvz.exceptions.InvalidCustomerParametersException;

import java.util.List;

import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_ID_EXC_MSG;

public class Customer
{
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private List<Coupon> coupons;
    public Customer(CustomerBuilder cb)
    {
        this.id = cb.id;
        this.first_name = cb.first_name;
        this.last_name = cb.last_name;
        this.email = cb.email;
        this.password = cb.password;
    }

    private void validate() throws InvalidCustomerParametersException
    {
        validateId();
    }

    private void validateId()
    {
        if (id < 0)
        {
            throw new InvalidCustomerParametersException(INVALID_ID_EXC_MSG);
        }
    }

    public void setCoupons(List<Coupon> allCustomerCoupons)
    {
        coupons = allCustomerCoupons;
    }

    public static class CustomerBuilder
    {
        private long id;
        private String first_name;
        private String last_name;
        private String email;
        private String password;

        public CustomerBuilder(){}



        public CustomerBuilder id(long id)
        {
            this.id = id;
            return this;
        }
        public CustomerBuilder first_name(String first_name)
        {
            this.first_name = first_name;
            return this;
        }
        public CustomerBuilder last_name(String last_name)
        {
            this.last_name = last_name;
            return this;
        }
        public CustomerBuilder email(String email)
        {
            this.email = email;
            return this;
        }
        public CustomerBuilder password(String password)
        {
            this.password = password;
            return this;
        }
        public Customer build()
        {
            Customer customer = new Customer(this);
            try {
                customer.validate();
            } catch (InvalidCustomerParametersException e) {
                throw new InvalidCustomerBuildException(e.getMessage());
            }
            return customer;
        }
    }

    public List<Coupon> getCoupons()
    {
        return coupons;
    }

    public long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
