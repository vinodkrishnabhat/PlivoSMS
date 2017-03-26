package com.vkb.plivosms.dao.postgres;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.dao.IPhoneNumberDao;
import com.vkb.plivosms.objects.PhoneNumberEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PostgresPhoneNumberDao extends PostgresDao implements IPhoneNumberDao {
    private PostgresPhoneNumberDao() {
    }

    private static PostgresPhoneNumberDao instance = new PostgresPhoneNumberDao();

    public static PostgresPhoneNumberDao getInstance() {
        return instance;
    }

    private static final Logger logger = Logger.getLogger(PostgresPhoneNumberDao.class);
    private static final String SEARCH_NUMBER_SQL = "select id, number, account_id from phone_number where number = ? and account_id = ?";

    public List<PhoneNumberEntity> searchPhoneNumberEntityByNumber(String number, Integer account_id) throws PlivoException {
        Connection connection = null;
        List<PhoneNumberEntity> retList = new LinkedList<PhoneNumberEntity>();

        try {
            connection = getConnection();

            PreparedStatement ps = connection.prepareStatement(SEARCH_NUMBER_SQL);
            ps.setString(1, number);
            ps.setInt(2, account_id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Integer dbId = rs.getInt(1);
                String dbNumber = rs.getString(2);
                Integer dbAccountId = rs.getInt(3);

                PhoneNumberEntity phonenumberEntity = new PhoneNumberEntity(dbId, dbNumber, dbAccountId);
                retList.add(phonenumberEntity);
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

        return retList;
    }
}
