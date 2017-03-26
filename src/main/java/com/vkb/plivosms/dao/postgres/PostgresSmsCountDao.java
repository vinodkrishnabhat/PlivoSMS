package com.vkb.plivosms.dao.postgres;

import com.vkb.plivosms.constants.ConfigKeys;
import com.vkb.plivosms.dao.ISmsCountDao;
import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.objects.SMSCountEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class PostgresSmsCountDao extends PostgresDao implements ISmsCountDao{
    private static PostgresSmsCountDao ourInstance = new PostgresSmsCountDao();

    public static PostgresSmsCountDao getInstance() {
        return ourInstance;
    }

    private PostgresSmsCountDao() {
    }

    private static final int THROTTLE_LIMIT = new Integer(ResourceBundle.getBundle("config").getString(ConfigKeys.THROTTLE_LIMIT));

    private static final Logger logger = Logger.getLogger(PostgresSmsCountDao.class);

    private static final String SEARCH_SMS_COUNT_SQL = "select id, from_number, start_time, counter from sms_count where from_number = ?";
    private static final String INSERT_SQL = "insert into sms_count (from_number, start_time, counter) values (?, ?, ?)";
    private static final String INCREMENT_COUNTER_SQL = "update sms_count set counter = counter + 1 where id = ? and from_number = ? and counter < " + THROTTLE_LIMIT;
    private static final String RESET_COUNTER_SQL = "update sms_count set counter = 0, start_time = ? where id = ? and from_number = ?";

    public SMSCountEntity getSmsCountEntity(String number) throws PlivoException{
        Connection connection = null;
        SMSCountEntity entity = null;

        try {
            connection = getConnection();

            PreparedStatement ps = connection.prepareStatement(SEARCH_SMS_COUNT_SQL);
            ps.setString(1, number);

            ResultSet rs = ps.executeQuery();

            //Assume at max one result
            if(rs.next()) {
                Integer dbId = rs.getInt(1);
                String dbFromNumber = rs.getString(2);
                Timestamp dbStartTime = rs.getTimestamp(3);
                Integer dbCounter = rs.getInt(4);

                entity = new SMSCountEntity(dbId, dbFromNumber, dbStartTime, dbCounter);
            }

            rs.close();
            ps.close();
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

        return entity;
    }

    public void incrementCounter(Integer id, String fromNumber) throws PlivoException{
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            PreparedStatement ps = connection.prepareStatement(INCREMENT_COUNTER_SQL);
            ps.setInt(1, id);
            ps.setString(2, fromNumber);

            int affectedRows = ps.executeUpdate();
            if(affectedRows == 0) {
                throw new PlivoException(PlivoException.USER_LIMIT_REACHED);
            }
        }
        catch (PlivoException pe) {
            throw pe;
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

    public void resetCounter(Timestamp newStartTime, Integer id, String fromNumber) throws PlivoException{
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            PreparedStatement ps = connection.prepareStatement(RESET_COUNTER_SQL);
            ps.setTimestamp(1, newStartTime);
            ps.setInt(2, id);
            ps.setString(3, fromNumber);

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

    public void insertSmsCountEntity(String fromNumber) throws PlivoException{
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(true);

            PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
            ps.setString(1, fromNumber);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, 1);

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
