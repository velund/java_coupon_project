package com.velundkvz.data.database.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.velundkvz.data.model.Company;
import static com.velundkvz.data.DefaultModels.*;
import static com.velundkvz.data.test_utils.Utils.*;

class CompanyMySQLDAOTest
{
    private static CompanyMySQLDAO companyDAO;

    @BeforeAll
    private static void setup()
    {
        resetAllTables();
        companyDAO = new CompanyMySQLDAO();
    }
    @AfterEach
    private void tearDown()
    {
        resetAllTables();
    }
    @Nested
    @DisplayName("when Call add(): ")
    class WhenCallAdd
    {
        @Test
        @DisplayName("then company added")
        public void thenCompanyAdded()
        {
            companyDAO.add(test_dflt_company);
            assertEquals(1, countCompanies());
        }
        @Test
        @DisplayName("then company max id equals 1")
        public void thenCompanyMaxIDEquals1()
        {
            companyDAO.add(test_dflt_company);
            assertEquals(1, getMaxIdCompany());
        }
        @Test
        @DisplayName("then company exists intable")
        public void thenCompanyExistsInTable()
        {
            companyDAO.add(test_dflt_company);
            assertTrue( isCompanyIDExists(1) );
        }
        @Test
        @DisplayName("3 times, 3 companies addded and max id equals 3")
        public void threeTimesThen3CompaniesAddedAndMaxIDEquals3()
        {
            for (int i = 0; i < 3; i++)
            {
                companyDAO.add(test_dflt_company);
            }
            assertAll
            (
                ()->assertEquals(3, getMaxIdCompany()),
                ()->assertEquals(3, countCompanies()),
                ()->assertTrue(isCompanyIDExists(1)),
                ()->assertTrue(isCompanyIDExists(2)),
                ()->assertTrue(isCompanyIDExists(3))

            );
        }
    }
    @Nested
    @DisplayName("when Call remove(): ")
    class WhenCallRemove
    {
        @Test
        @DisplayName("existing company, then returns true")
        public void existingCompanyThenReturnsTrue()
        {
            companyDAO.add(test_dflt_company);
            assertTrue( companyDAO.remove(1) );
        }
        @Test
        @DisplayName("not existing company, then returns false")
        public void notExistingCompanyThenReturnsFalse()
        {
            companyDAO.add(test_dflt_company);
            assertFalse( companyDAO.remove(2) );
        }
        @Test
        @DisplayName("then company count decreased")
        public void thenCompanyCountDecreased()
        {
            companyDAO.add(test_dflt_company);
            companyDAO.remove(1);
            assertAll
            (
                ()->assertEquals(0, countCompanies())
            );
        }
        @Test
        @DisplayName("then company does not exists anymore in table")
        public void thenCompanyDoesNotExistInTableAnymore()
        {
            fillDefaultCompanies(5);
            companyDAO.remove(2);
            companyDAO.remove(3);
            assertAll
            (
                ()->assertEquals(3, countCompanies()),
                ()->assertTrue(isCompanyIDExists(1)),
                ()->assertTrue(isCompanyIDExists(4)),
                ()->assertTrue(isCompanyIDExists(5)),
                ()->assertFalse(isCompanyIDExists(2)),
                ()->assertFalse(isCompanyIDExists(3))
            );
        }
    }
    @Nested
    @DisplayName("when Call isExists(): ")
    class WhenCallIsExists
    {
        @BeforeEach
        private void prepareDB()
        {
            fillDefaultCompanies(2);
        }
        @Test
        @DisplayName("existing id, then returns true")
        public void existingIDThenReturnsTrue()
        {
            assertAll
            (
                ()->assertTrue(isCompanyIDExists(1)),
                ()->assertEquals(isCompanyIDExists(1), companyDAO.isExists(1))
            );
        }
        @Test
        @DisplayName("not existing id, then returns false")
        public void notExistingIDThenReturnsFalse()
        {
            assertAll
            (
                ()->assertFalse(isCompanyIDExists(5)),
                ()->assertEquals(isCompanyIDExists(5), companyDAO.isExists(5))
            );
        }
        @Test
        @DisplayName("existing EMAIL, then returns true")
        public void existingEMAILThenReturnsTrue()
        {
            assertAll
                    (
                            ()->assertTrue(isCompanyEMAILExists(dflt_email_comp)),
                            ()->assertEquals(isCompanyEMAILExists(dflt_email_comp), companyDAO.isExists(dflt_email_comp))
                    );
        }
        @Test
        @DisplayName("not existing email, then returns false")
        public void notExistingEMAILThenReturnsFalse()
        {
            String notExistingEmail = "some_mail.com";
            assertAll
            (
                ()->assertFalse(isCompanyEMAILExists(notExistingEmail)),
                ()->assertEquals(isCompanyEMAILExists(notExistingEmail), companyDAO.isExists(notExistingEmail))
            );
        }
    }
    @Nested
    @DisplayName("when Call updateEmail(): ")
    class WhenCallUpdateEmail
    {
        private final String updatedMail = "updateted@mail.com";
        @BeforeEach
        private void prepareDB()
        {
            fillDefaultCompanies(2);
        }
        @Test
        @DisplayName("if id exists returns true")
        public void ifIdExistsReturnsTrue()
        {
            assertTrue( isCompanyEMAILExists(dflt_email_comp) );
            assertTrue( companyDAO.updateEmail(1, updatedMail) );
        }
        @Test
        @DisplayName("if id not exists returns false")
        public void ifIdNotExistsReturnsFalse()
        {
            assertFalse( companyDAO.updateEmail(getMaxIdCompany() + 1, updatedMail) );
        }
        @Test
        @DisplayName(" mail updated ")
        public void mailUpdated()
        {
            companyDAO.updateEmail(1, updatedMail);
            assertTrue( isCompanyEMAILExists(updatedMail) );
        }
    }
    @Nested
    @DisplayName("when Call updatePassword(): ")
    class WhenCallUpdatePassword
    {
        private final String updatedMail = "updateted@mail.com";
        @BeforeEach
        private void prepareDB()
        {
            fillDefaultCompanies(2);
        }
        @Test
        @DisplayName("if id exists returns true")
        public void ifIdExistsReturnsTrue()
        {
            assertTrue( isCompanyPasswordExists(dflt_strong_pswd_comp) );
            assertTrue( companyDAO.updatePassword(1, updated_strong_pswd_comp) );
        }
        @Test
        @DisplayName("if id not exists returns false")
        public void ifIdNotExistsReturnsFalse()
        {
            assertFalse( companyDAO.updatePassword(getMaxIdCompany() + 1, updatedMail) );
        }
        @Test
        @DisplayName(" password updated ")
        public void passwordUpdated()
        {
            companyDAO.updatePassword(1, updated_strong_pswd_comp);
            assertTrue( isCompanyPasswordExists(updated_strong_pswd_comp) );
        }
    }
    @Nested
    @DisplayName("when Call findById(): ")
    class WhenCallFindById
    {
        @BeforeEach
        private void prepareDB()
        {
            fillDefaultCompanies(2);
        }
        @Test
        @DisplayName("if id exists - returns optional of company")
        public void ifIdExistsReturnsOptionalOfCompany()
        {
            assertTrue(companyDAO.findById(1).isPresent());
        }
        @Test
        @DisplayName("if id NOT exists - returns optional of company")
        public void ifIdNOTExistsReturnsOptionalOfCompany()
        {
            assertFalse(companyDAO.findById(5).isPresent());
        }
        @Test
        @DisplayName("if id exists - returns optional of correct company")
        public void ifIdNOTExistsReturnsOptionalOfCorrectCompany()
        {
            Optional<Company> optCompany = companyDAO.findById(1);
            Company company = optCompany.isPresent() ?  optCompany.get() : null;
            assertAll
            (
                ()->assertNotNull(company),
                ()->assertEquals(1, company.getId()),
                ()->assertEquals(dflt_name_comp, company.getName()),
                ()->assertEquals(dflt_email_comp, company.getEmail()),
                ()->assertEquals(dflt_strong_pswd_comp, company.getPassword())
            );
        }
    }
    @Nested
    @DisplayName("when Call findByEmailAndPassword(): ")
    class WhenCallFindByEmailAndPassword
    {
        @BeforeEach
        private void prepareDB()
        {
            fillDefaultCompanies(2);
        }
        @Test
        @DisplayName("if id exists - returns optional of company")
        public void ifIdExistsReturnsOptionalOfCompany()
        {
            assertTrue(companyDAO.findByEmailAndPassword(dflt_email_comp, dflt_strong_pswd_comp).isPresent());
        }
        @Test
        @DisplayName("if id NOT exists - returns optional of company")
        public void ifIdNOTExistsReturnsOptionalOfCompany()
        {
            assertFalse(companyDAO.findByEmailAndPassword("XXX", "XXX").isPresent());
        }
        @Test
        @DisplayName("if id exists - returns optional of correct company")
        public void ifIdNOTExistsReturnsOptionalOfCorrectCompany()
        {
            Optional<Company> optCompany = companyDAO.findByEmailAndPassword(dflt_email_comp, dflt_strong_pswd_comp);
            Company company = optCompany.orElse(null);
            assertAll
                    (
                            ()->assertNotNull(company),
                            ()->assertEquals(dflt_name_comp, company.getName()),
                            ()->assertEquals(dflt_email_comp, company.getEmail()),
                            ()->assertEquals(dflt_strong_pswd_comp, company.getPassword())
                    );
        }
    }
    @Nested
    @DisplayName("when Call getAll(): ")
    class WhenCallGetAll
    {
        @Test
        @DisplayName("if no companies - returns empty list")
        public void ifNoCompaniesReturnsEmptyList()
        {
            List<Company> compList = companyDAO.getAll();
            assertAll
            (
                ()->assertNotNull(compList),
                ()->assertEquals(0, compList.size())
            );
        }
        @ParameterizedTest
        @ValueSource(ints = {1, 3, 5, 8, 15})
        @DisplayName("if x companies - returns list of x")
        public void ifXCompaniesReturnsListOfX(int n)
        {
            fillDefaultCompanies(n);
            List<Company> compList = companyDAO.getAll();
            assertAll
            (
                ()->assertNotNull(compList),
                ()->assertEquals(n, compList.size())
            );
        }
        @Test
        @DisplayName("if 2 diff companies - returns listof 2 diff companies")
        public void if2DiffCompaniesReturnsListOf2DiffCompanies()
        {
            String name1 = "XXX", pass1 = "1234", email1 = "XXX@com";
            String name2 = "YYY", pass2 = "*****", email2 = "YYY@com";
            Company company1 = new Company.CompanyBuilder().name(name1).email(email1).password(pass1).build();
            Company company2 = new Company.CompanyBuilder().name(name2).email(email2).password(pass2).build();
            Company company3 = new Company.CompanyBuilder().name("name2").email("email2").password("pass2").build();
            insertCompanyToCompanyTbl( company1 );
            insertCompanyToCompanyTbl( company2 );
            List<Company> compList = companyDAO.getAll();
            assertAll
            (
                ()->assertEquals(2, compList.size()),
                ()->assertTrue( containsAllCompanies ( compList, Arrays.asList(company1, company2))),
                ()->assertFalse( containsAllCompanies ( compList, Arrays.asList(company1, company2, company3)))
            );
        }

    }
    @Nested
    @DisplayName("when Call count(): ")
    class WhenCallCount
    {
        @Test
        @DisplayName("if no companies - returns 0")
        public void ifNoCompaniesReturnsZero()
        {
            assertEquals(0, companyDAO.count());
        }
        @ParameterizedTest
        @ValueSource(ints = {4, 6, 9})
        @DisplayName("if x companies - returns x")
        public void ifXCompaniesReturnsX(int x)
        {
            fillDefaultCompanies(x);
            assertEquals(x, companyDAO.count());
        }
    }
    @Nested
    @DisplayName("when Call getMaxId(): ")
    class WhenCallGetMaxId
    {
        @Test
        @DisplayName("if no companies - returns 0")
        public void ifNoCompaniesReturnsZero()
        {
            assertEquals(0, companyDAO.getMaxId());
        }
        @ParameterizedTest
        @ValueSource(ints = {4, 6, 9})
        @DisplayName("if x companies - returns x")
        public void ifXCompaniesReturnsX(int x)
        {
            fillDefaultCompanies(x);
            assertEquals(x, companyDAO.getMaxId());
        }
    }

}