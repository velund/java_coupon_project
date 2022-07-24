package com.velundkvz.data.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;
import com.velundkvz.data.model.Company;
import static com.velundkvz.definitions.schema.CompanyTbl.*;

public class CompanyMySQLDAO implements CompanyDAO
{
    private static final ConnectionPool companyCP = DBConnections.COMPANY_CONNECTIONS.getPool();
    @Override
    public void add(Company company)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            prepareCompanyInsertStmt( company, connection ).executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            companyCP.put(connection);
        }
    }

    @Override
    public boolean remove(long id)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            if ( prepareCompanyRemoveStmt( id, connection ).executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            companyCP.put(connection);
        }
        return false;
    }

    @Override
    public boolean updateEmail(long id, String email)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            if ( prepareUpdateCompanyEmailStatement(id, email, connection).executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            companyCP.put(connection);
        }
        return false;
    }

    @Override
    public boolean updatePassword(long id, String password)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            if ( prepareUpdateCompanyPasswordStatement(id, password, connection).executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            companyCP.put(connection);
        }
        return false;
    }

    @Override
    public Optional<Company> findById(long id)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareFindCompanyByIDStmt(id, connection).executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCompanyByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return Optional.empty();
        }finally
        {
            companyCP.put(connection);
        }
        return Optional.empty();
    }

    @Override
    public List<Company> getAll()
    {
        Connection connection = companyCP.getConnection();
        List<Company> companies = new ArrayList<>();
        try
        {
            ResultSet rs = prepareGetAllCompaniesStmt( connection).executeQuery();
            while ( rs.next() )
            {
                companies.add(createCompanyByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            companyCP.put(connection);
        }
        return companies;

    }

    @Override
    public Optional<Company> findByEmailAndPassword(String email, String password)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareFindCompanyByEmailAndPswdStmt(email, password, connection).executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCompanyByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            companyCP.put(connection);
        }
        return Optional.empty();
    }

    @Override
    public long count()
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareCountCompaniesStmt(connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getLong(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return  0;
        } finally
        {
            companyCP.put(connection);
        }
        return 0;
    }

    @Override
    public long getMaxId()
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareGetCompanyMaxIdStmt( connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getLong(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        } finally
        {
            companyCP.put(connection);
        }
        return 0;
    }

    @Override
    public boolean isExists(long id)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareIsCompanyExistsByIdStmt(id, connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getBoolean(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            companyCP.put(connection);
        }
        return false;
    }
    public boolean isExists(String email)
    {
        Connection connection = companyCP.getConnection();
        try
        {
            ResultSet rs = prepareIsCompanyExistsByEmailStmt(email, connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getBoolean(1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            companyCP.put(connection);
        }
        return false;
    }

    /* PRIVATE */
    private PreparedStatement prepareCompanyInsertStmt(Company company, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(INSERT_COMPANY_SQL, connection);
        try {
            ps.setString(1, company.getName());
            ps.setString(2, company.getEmail());
            ps.setString(3, company.getPassword());

        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement initPreparedStmt(String sql, Connection connection)
    {
        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ps;
    }
    private PreparedStatement prepareCompanyRemoveStmt(long id, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(DELETE_COMPANY_SQL, connection);
        ps.setLong(1, id);
        return ps;
    }
    private Company createCompanyByFullParameters(ResultSet rs) throws SQLException {
        return new Company.CompanyBuilder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .email(rs.getString(3))
                .password(rs.getString(4))
                .build();
    }
    private PreparedStatement prepareFindCompanyByIDStmt(long id, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(SELECT_COMPANY_BY_ID_SQL, connection);
        ps.setLong(1, id);
        return ps;
    }
    private PreparedStatement prepareGetAllCompaniesStmt(Connection connection)
    {
        return initPreparedStmt(SELECT_ALL_COMPANIES_SQL, connection);
    }
    private PreparedStatement prepareUpdateCompanyEmailStatement(long id, String email, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(UPDATE_COMPANY_EMAIL_BY_ID_SQL, connection);
        ps.setString(1, email);
        ps.setLong(2, id);
        return ps;
    }
    private PreparedStatement prepareUpdateCompanyPasswordStatement(long id, String email, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(UPDATE_COMPANY_PASSWORD_BY_ID_SQL, connection);
        ps.setString(1, email);
        ps.setLong(2, id);
        return ps;
    }

    private PreparedStatement prepareFindCompanyByEmailAndPswdStmt(String email, String password, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(SELECT_COMPANY_BY_EMAIL_AND_PSWD_SQL, connection);
        ps.setString(1, email);
        ps.setString(2, password);
        return ps;
    }
    private PreparedStatement prepareCountCompaniesStmt(Connection connection)
    {
        PreparedStatement ps = initPreparedStmt(COUNT_COMPANY_SQL, connection);
        return ps;
    }
    private PreparedStatement prepareGetCompanyMaxIdStmt(Connection connection)
    {
        return initPreparedStmt(GET_COMPANY_MAX_ID_SQL, connection);
    }
    private PreparedStatement prepareIsCompanyExistsByIdStmt(long id, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(IS_COMPANY_EXISTS_BY_ID_SQL, connection);
        ps.setLong(1, id);
        return ps;
    }
    private PreparedStatement prepareIsCompanyExistsByEmailStmt(String email, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(IS_COMPANY_EXISTS_BY_EMAIL_SQL, connection);
        ps.setString(1, email);
        return ps;
    }
}
