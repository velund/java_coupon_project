package com.velundkvz.data.database.dao;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;
import com.velundkvz.data.model.Customer;
import com.velundkvz.exceptions.NotExistingCouponOrCustomerException;

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
    public synchronized void create(Customer customer)
    {
        Connection connection = clientCP.getConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
            ps.setString(1, customer.getFirst_name());
            ps.setString(2, customer.getLast_name());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPassword());
            ps.executeUpdate();
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
            PreparedStatement ps = connection.prepareStatement(DELETE_SQL);
            ps.setLong(1, id);
            if ( ps.executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
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
            PreparedStatement ps = connection.prepareStatement(UPDATE_EMAIL_BY_ID_SQL);
            ps.setString(1, email);
            ps.setLong(2, id);
            if ( ps.executeUpdate() != 0)
            {
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
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
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCustomerByFull(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
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
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_EMAIL_AND_PSWD_SQL);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                return Optional.of(createCustomerByFull(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
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
            PreparedStatement ps = connection.prepareStatement(COUNT_CUSTOMER_SQL);
            ResultSet rs = ps.executeQuery();
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
            PreparedStatement ps = connection.prepareStatement(GET_CUSTOMER_MAX_ID_SQL);
            ResultSet rs = ps.executeQuery();
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
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);

            ResultSet rs = ps.executeQuery();
            while ( rs.next() )
            {
                custs.add(createCustomerByFull(rs));
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
            if (e.getErrorCode() == ER_NO_REFERENCED_ROW_FOREIGN_KEY_CONSTRAINT)
            {
                throw new NotExistingCouponOrCustomerException();
            }else
            {
                e.printStackTrace();
            }
            return false;
        } finally
        {
            clientCP.put(connection);
        }
        return false;
    }
    private Customer createCustomerByFull(ResultSet rs) throws SQLException
    {
        return new Customer.CustomerBuilder()
                .id(rs.getLong(1))
                .first_name(rs.getString(2))
                .last_name(rs.getString(3))
                .email(rs.getString(4))
                .password(rs.getString(5))
                .build();
    }
}
