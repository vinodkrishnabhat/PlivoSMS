package com.vkb.plivosms.dao.postgres;

import com.vkb.plivosms.exception.PlivoException;
import com.vkb.plivosms.dao.IAccountDao;
import com.vkb.plivosms.objects.AccountEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresAccountDao extends PostgresDao implements IAccountDao{
    private PostgresAccountDao() {}

    private static PostgresAccountDao instance = new PostgresAccountDao();

    public static PostgresAccountDao getInstance() {
        return instance;
    }

    private static final Logger logger = Logger.getLogger(PostgresAccountDao.class);
    private static final String SEARCH_ACCOUNT_SQL = "select id, username, auth_id from account where username = ? and auth_id = ?";

    public AccountEntity searchAccountEntity(String username, String authId) throws PlivoException {
        Connection connection = null;
        AccountEntity accountEntity = null;

        try {
            connection = getConnection();

            PreparedStatement ps = connection.prepareStatement(SEARCH_ACCOUNT_SQL);
            ps.setString(1, username);
            ps.setString(2, authId);

            ResultSet rs = ps.executeQuery();

            //Assume at max one result
            if(rs.next()) {
                Integer dbId = rs.getInt(1);
                String dbUsername = rs.getString(2);
                String dbAuthId = rs.getString(3);

                accountEntity = new AccountEntity(dbId, dbAuthId, dbUsername);
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

        return accountEntity;
    }
}
