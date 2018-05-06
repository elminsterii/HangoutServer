package com.fff.hos.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBCtrlActivity {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlActivity.class.getName());

    private final String TABLE_NAME = "activities";
    private Connection conn;

    DBCtrlActivity(Connection conn) {
        this.conn = conn;
        createTable();
    }

    private void createTable() {

        String strCreateTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + "id SERIAL NOT NULL, "
                + "ts timestamp NOT NULL, "
                + "email VARCHAR(128) NOT NULL, "
                + "userpassword VARCHAR(128) NOT NULL, "
                + "displayname VARCHAR(64) NOT NULL, "
                + "age INTEGER NOT NULL, "
                + "gender VARCHAR(16) NOT NULL, "
                + "interests VARCHAR(512), "
                + "description VARCHAR(512), "
                + "location VARCHAR(128), "
                + "joinactivities VARCHAR(1024), "
                + "holdactivities VARCHAR(1024), "
                + "goodmember INTEGER, "
                + "goodleader INTEGER, "
                + "online INTEGER, "
                + "PRIMARY KEY (email) );";

        try {
            conn.createStatement().execute(strCreateTableSQL);
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }
    }
}
