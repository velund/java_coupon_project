package com.velundkvz.data.database.dao;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;
import com.velundkvz.data.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.velundkvz.definitions.schema.customerTbl.*;

public class CustomerMySQLDAO implements CustomerDAO
{
    private static final ConnectionPool clientCP = DBConnections.CLIENT_CONNECTIONS.getPool();

    @Override
    public synchronized void add(Customer customer)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            prepareCustomerInsertStmt( customer, connection ).executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            clientCP.put(connection);
        }
    }

    @Override
    public synchronized boolean remove(long id)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            if ( prepareCustomerRemoveStmt( id, connection ).executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            clientCP.put(connection);
        }
        return false;
    }

    @Override
    public synchronized boolean updateEmail(long id, String email)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            if ( prepareUpdateEmailStatement(id, email, connection).executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            clientCP.put(connection);
        }
        return false;
    }

    @Override
    public Optional<Customer> findById(long id)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            ResultSet rs = prepareFindByIDStmt(id, connection).executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCustomerByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            clientCP.put(connection);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByEmailAndPassword(String email, String password)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            ResultSet rs = prepareFindByEmailAndPswdStmt(email, password, connection).executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCustomerByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            clientCP.put(connection);
        }
        return Optional.empty();
    }

    @Override
    public long count()
    {
        Connection connection = clientCP.getConnection();
        try
        {
            ResultSet rs = prepareCountStmt(connection).executeQuery();
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
            clientCP.put(connection);
        }
        return 0;
    }

    @Override
    public long getMaxId()
    {
        Connection connection = clientCP.getConnection();
        try
        {
            ResultSet rs = prepareGetMaxIdStmt( connection).executeQuery();
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
            clientCP.put(connection);
        }
        return 0;
    }

    @Override
    public List<Customer> getAll()
    {
        Connection connection = clientCP.getConnection();
        List<Customer> custs = new ArrayList<>();
        try
        {
            ResultSet rs = prepareGetAllCustomersStmt( connection).executeQuery();
            while ( rs.next() )
            {
                custs.add(createCustomerByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            clientCP.put(connection);
        }
        return custs;
    }

    @Override
    public boolean purchase(long couponId, long customerId)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_COUPON_CUSTOMER_TBL_SQL);
            ps.setLong(1, couponId);
            ps.setLong(2, customerId);
            if (ps.executeUpdate() != 0)
            {
                return true;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            clientCP.put(connection);
        }
        return false;
    }
    /* PRIVATE */
    private Customer createCustomerByFullParameters(ResultSet rs) throws SQLException
    {
        return new Customer.CustomerBuilder()
                .id(rs.getLong(1))
                .first_name(rs.getString(2))
                .last_name(rs.getString(3))
                .email(rs.getString(4))
                .password(rs.getString(5))
                .build();
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
    private PreparedStatement prepareCustomerInsertStmt(Customer customer, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(INSERT_CUSTOMER_SQL, connection);
        try {
            ps.setString(1, customer.getFirst_name());
            ps.setString(2, customer.getLast_name());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPassword());
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareCustomerRemoveStmt(long id, Connection connection) throws  SQLException
    {
        PreparedStatement ps = initPreparedStmt(DELETE_SQL, connection);
        ps.setLong(1, id);
        return ps;
    }
    private PreparedStatement prepareCountStmt(Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(COUNT_CUSTOMER_SQL, connection);
        return ps;
    }
    private PreparedStatement prepareFindByEmailAndPswdStmt(String email, String password, Connection connection) throws  SQLException
    {
        PreparedStatement ps = initPreparedStmt(SELECT_BY_EMAIL_AND_PSWD_SQL, connection);
        ps.setString(1, email);
        ps.setString(2, password);
        return ps;
    }
    private PreparedStatement prepareFindByIDStmt(long id, Connection connection) throws  SQLException
    {
        PreparedStatement ps = initPreparedStmt(SELECT_BY_ID_SQL, connection);
        ps.setLong(1, id);
        return ps;
    }
    private PreparedStatement prepareUpdateEmailStatement(long id, String email, Connection connection) throws  SQLException
    {
        PreparedStatement ps = initPreparedStmt(UPDATE_EMAIL_BY_ID_SQL, connection);
        ps.setString(1, email);
        ps.setLong(2, id);
        return ps;
    }
    private PreparedStatement prepareGetMaxIdStmt( Connection connection)
    {
        return initPreparedStmt(GET_CUSTOMER_MAX_ID_SQL, connection);
    }
    private PreparedStatement prepareGetAllCustomersStmt(Connection connection)
    {
        return initPreparedStmt(SELECT_ALL_SQL, connection);
    }

}
