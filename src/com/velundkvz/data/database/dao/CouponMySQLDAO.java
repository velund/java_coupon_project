package com.velundkvz.data.database.dao;

import com.velundkvz.common.connection_pool.ConnectionPool;
import com.velundkvz.common.connection_pool.DBConnections;
import com.velundkvz.data.model.Coupon;
import com.velundkvz.exceptions.CouponNotExistsInDBException;
import com.velundkvz.exceptions.CustomerNotExistsInDBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import static com.velundkvz.definitions.modelDefinitions.ModelsDefinitions.*;
import static com.velundkvz.definitions.schema.CouponTbl.*;

public class CouponMySQLDAO implements CouponDAO
{
    private static final ConnectionPool adminCP = DBConnections.ADMIN_CONNECTIONS.getPool();
    @Override
    public synchronized Coupon add(Coupon coupon)
    {
        Connection connection = adminCP.getConnection();
        long addedCouponId = 0;
        try
        {
            prepareCouponInsertStmt( coupon, connection ).executeUpdate();
            addedCouponId = getAddedCouponId(connection);
        } catch (SQLException e)
        {
                e.printStackTrace();
        }finally
        {
            adminCP.put(connection);
        }
        Coupon newCoupon = new Coupon(coupon);
        newCoupon.setId(addedCouponId);
        return newCoupon;
    }

    @Override
    public List<Coupon> getAll()
    {
        List<Coupon> couponsList = new ArrayList<>();
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetAllCouponsStmt( connection).executeQuery();
            while ( rs.next() )
            {
                couponsList.add(createCouponByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            adminCP.put(connection);
        }
        return couponsList;
    }

    @Override
    public List<Coupon> getAllCustomerCoupons(long customerId)
    {
        List<Coupon> couponsList = new ArrayList<>();
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetAllByCustomerIdStmt(customerId, connection).executeQuery();
            while ( rs.next() )
            {
                couponsList.add(createCouponByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            adminCP.put(connection);
        }
        return couponsList;
    }

    @Override
    public List<Coupon> getAllExpired()
    {
        List<Coupon> couponsList = new ArrayList<>();
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetAllBeforeStmt(new Date(System.currentTimeMillis()), connection).executeQuery();
            while ( rs.next() )
            {
                couponsList.add(createCouponByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            adminCP.put(connection);
        }
        return couponsList;
    }

    @Override
    public List<Coupon> getAllBefore(Date date)
    {
        List<Coupon> couponsList = new ArrayList<>();
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetAllBeforeStmt(date, connection).executeQuery();
            while ( rs.next() )
            {
                couponsList.add(createCouponByFullParameters(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            adminCP.put(connection);
        }
        return couponsList;
    }

    public boolean isCouponOwned(long couponId, long customerId)
    {
        checkExistance(couponId, customerId);
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetIsOwnedStmt(couponId, customerId, connection).executeQuery();
            if ( rs.next() )
            {
                if (rs.getInt(1) == 0)
                {
                    return false;
                }
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            adminCP.put(connection);
        }
        return false;
    }

    public int getAmount(long couponId)
    {
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareGetAmountStmt(couponId, connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getInt(1);
            }else
            {
                throw new CouponNotExistsInDBException(NO_SUCH_COUPON_ID_IN_DB_EXC_FRMT_MSG.formatted(couponId));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            return  0;
        } finally
        {
            adminCP.put(connection);
        }
    }

    public boolean isExists(long couponId)
    {
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareIsCouponExistsByIDStmt(couponId, connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getInt(1) == 1;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            adminCP.put(connection);
        }
        return false;
    }
    public boolean updateCouponAmount(long couponId, int i)
    {
        Connection connection = adminCP.getConnection();
        try
        {
           return prepareCouponUpdateAmountStmt( couponId, i, connection ).executeUpdate() > 0;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            adminCP.put(connection);
        }
    }
    public boolean isExistsByCompanyIdAndTitle(long companyId, String title)
    {
        Connection connection = adminCP.getConnection();
        try
        {
            ResultSet rs = prepareIsCouponExistsByCompanyIdAndTitle(companyId, title, connection).executeQuery();
            if ( rs.next() )
            {
                return rs.getInt(1) == 1;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally
        {
            adminCP.put(connection);
        }
        return false;
    }
    /* PRIVATES */
    private PreparedStatement prepareIsCouponExistsByIDStmt(long couponId, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(GET_IS_COUPON_EXISTS_SQL, connection);
        try {
            ps.setLong(1, couponId);
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareIsCouponExistsByCompanyIdAndTitle(long companyId, String title, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(GET_IS_COUPON_EXISTS_BY_COMP_ID_AND_TITLE_SQL, connection);
        try {
            ps.setLong(1, companyId);
            ps.setString(2, title);
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareGetAllCouponsStmt(Connection connection)
    {
        return initPreparedStmt(SELECT_ALL_COUPONS_SQL, connection);
    }
    private PreparedStatement prepareGetIsOwnedStmt(long couponId, long customerId, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(GET_IS_OWNED_COUPONS_SQL, connection);
        try {
            ps.setLong(1, couponId);
            ps.setLong(2, customerId);
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareGetAmountStmt(long couponId, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(GET_AMOUNT_SQL, connection);
        try {
            ps.setLong(1, couponId);

        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareGetAllByCustomerIdStmt(long customerId, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(GET_ALL_CUSTOMER_COUPONS_SQL, connection);
        try {
            ps.setLong(1, customerId);
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareGetAllBeforeStmt(Date date, Connection connection) throws SQLException {
        PreparedStatement ps = initPreparedStmt(GET_ALL_LESS_THAN_DATE_SQL, connection);
        try {
            ps.setString(1, date.toString());
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private Coupon createCouponByFullParameters(ResultSet rs) throws SQLException {
        return new Coupon.CouponBuilder()
                .id(rs.getLong(1))
                .company_id(rs.getLong(2))
                .category(rs.getInt(3))
                .title(rs.getString(4))
                .price(rs.getLong(5))
                .start_date(rs.getDate(6))
                .end_date(rs.getDate(7))
                .amount(rs.getInt(8))
                .description(rs.getString(9))
                .image_url(rs.getURL(10))
                .build();
    }
    private long getAddedCouponId(Connection connection) throws SQLException
    {
        ResultSet rs = connection.prepareStatement(GET_LAST_ADDED_COUPON_ID_SQL).executeQuery();
        if (rs.next() )
        {
            return rs.getLong(1);
        }
        return UNKNOWN_ID;
    }
    private PreparedStatement prepareCouponInsertStmt(Coupon coupon, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(INSERT_COUPON_SQL, connection);

        try {
            ps.setLong(1, coupon.getCompany_id());
            ps.setInt(2, coupon.getCategory());
            ps.setString(3, coupon.getTitle());
            ps.setLong(4, coupon.getPrice());
            ps.setDate(5, coupon.getStart_date());
            ps.setDate(6, coupon.getEnd_date());
            ps.setInt(7, coupon.getAmount());
            ps.setString(8, coupon.getDescription());
            ps.setURL(9, coupon.getImage_url());
        } catch (SQLException e) {
            throw new SQLException("bad parameters, "  + e.getMessage());
        }
        return ps;
    }
    private PreparedStatement prepareCouponUpdateAmountStmt(long couponId, int amount, Connection connection) throws SQLException
    {
        PreparedStatement ps = initPreparedStmt(UPDATE_COUPON_AMOUNT_SQL, connection);
        ps.setInt(1, amount);
        ps.setLong(2, couponId);
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
    private void checkExistance(long couponId, long customerId)
    {
        if (!isExists(couponId))
        {
            throw new CouponNotExistsInDBException(NO_SUCH_COUPON_ID_IN_DB_EXC_FRMT_MSG.formatted(couponId));
        }

        if ( ! (new CustomerMySQLDAO().isExists(customerId)) )
        {
            throw new CustomerNotExistsInDBException(NO_SUCH_CUSTOMER_ID_IN_DB_EXC_FRMT_MSG);
        }
    }
}
