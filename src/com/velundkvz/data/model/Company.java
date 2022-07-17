package com.velundkvz.data.model;

import com.velundkvz.exceptions.InvalidCompanyBuildException;
import com.velundkvz.exceptions.InvalidCompanyParametersException;
import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.INVALID_ID_EXC_MSG;

public class Company
{
    private long id;
    private String name;
    private String email;
    private String password;

    public Company(CompanyBuilder cb)
    {
        this.id = cb.id;
        this.name = cb.name;
        this.email = cb.email;
        this.password = cb.password;
    }

    private void validate() throws InvalidCompanyParametersException
    {
        validateId();
    }

    private void validateId()
    {
        if (id < 0)
        {
            throw new InvalidCompanyParametersException(INVALID_ID_EXC_MSG);
        }
    }

    public static class CompanyBuilder
    {
        private long id;
        private String name;
        private String email;
        private String password;

        public CompanyBuilder(){}
        public CompanyBuilder id(long id)
        {
            this.id = id;
            return this;
        }
        public CompanyBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public CompanyBuilder email(String email)
        {
            this.email = email;
            return this;
        }
        public CompanyBuilder password(String password)
        {
            this.password = password;
            return this;
        }
        public Company build()
        {
            Company company = new Company(this);
            try {
                company.validate();
            } catch (InvalidCompanyParametersException e) {
                throw new InvalidCompanyBuildException(e.getMessage());
            }
            return company;
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
