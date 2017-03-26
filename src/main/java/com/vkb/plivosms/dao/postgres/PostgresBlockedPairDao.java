package com.vkb.plivosms.dao.postgres;

import com.vkb.plivosms.dao.IBlockedPairDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.BlockedPairEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PostgresBlockedPairDao extends PostgresDao implements IBlockedPairDao {

    private static final int EXPIRY_MILLIS = 4 * 60 * 60 * 1000;

    private PostgresBlockedPairDao() {}

    private static PostgresBlockedPairDao instance = new PostgresBlockedPairDao();

    public static PostgresBlockedPairDao getInstance() {
        return instance;
    }

    private static final Logger logger = Logger.getLogger(PostgresBlockedPairDao.class);
    private static final String INSERT_SQL = "insert into blocked_pair (from_number, to_number, set_at_time) values (?,?, ?)";
    private static final String SEARCH_SQL = "select id, from_number, to_number, set_at_time from blocked_pair where from_number = ? and to_number = ? and set_at_time > ?";
    private static final String RESET_SQL = "update blocked_pair set set_at_time = ? where from_number = ? and to_number = ?";

    public void setBlockedPair(BlockedPairEntity blockedPair) throws PlivoException{
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
            ps.setString(1, blockedPair.getFrom());
            ps.setString(2, blockedPair.getTo());
            ps.setLong(3, blockedPair.getSetAtTime());

            ps.executeUpdate();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new PlivoException(e);
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    public List<BlockedPairEntity> getBlockedPair(String from, String to) throws PlivoException {
        Connection connection = null;
        List<BlockedPairEntity> retList = new LinkedList<BlockedPairEntity>();

        try {
            connection = getConnection();

            PreparedStatement ps = connection.prepareStatement(SEARCH_SQL);
            ps.setString(1, from);
            ps.setString(2, to);

            Long expiryTime = System.currentTimeMillis() - EXPIRY_MILLIS;
            ps.setLong(3, expiryTime);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Integer dbId = rs.getInt(1);
                String dbFrom = rs.getString(2);
                String dbTo = rs.getString(3);
                Long dbSetAtTime = rs.getLong(4);

                BlockedPairEntity blockedPairEntity = new BlockedPairEntity(dbId, dbFrom, dbTo, dbSetAtTime);
                retList.add(blockedPairEntity);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new PlivoException(e);
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return retList;
    }

    public void resetBlockedPair(String from, String to) throws PlivoException {
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            PreparedStatement ps = connection.prepareStatement(RESET_SQL);
            ps.setLong(1, System.currentTimeMillis() - EXPIRY_MILLIS - 1000);
            ps.setString(2, from);
            ps.setString(3, to);

            ps.executeUpdate();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new PlivoException(e);
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
