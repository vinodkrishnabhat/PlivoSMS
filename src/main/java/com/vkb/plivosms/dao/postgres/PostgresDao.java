package com.vkb.plivosms.dao.postgres;

import com.vkb.plivosms.constants.ConfigKeys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

abstract class PostgresDao {


    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    Connection getConnection() throws SQLException{

        String url = resourceBundle.getString(ConfigKeys.POSTGRES_URL);
        String username = resourceBundle.getString(ConfigKeys.POSTGRES_USERNAME);
        String password = resourceBundle.getString(ConfigKeys.POSTGRES_PASSWORD);

        return DriverManager.getConnection(url,username, password);
    }
}
