package com.fff.hos.database;

import com.fff.hos.tools.SysRunTool;
import com.google.apphosting.api.ApiProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static Connection conn = null;

    private DBConnection() {

    }

    static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
                Map<String, Object> attr = env.getAttributes();
                String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

                String strURL;
                if (hostname.contains("localhost:")) {
                    SysRunTool sysRunTool = new SysRunTool();
                    sysRunTool.sysRunMySQL();

                    strURL = System.getProperty("sqlLocal")
                            + System.getProperty("sqlDBName") + "?"
                            + "useSSL=false&"
                            + "user=" + System.getProperty("sqlUserName")
                            + "&password=" + System.getProperty("sqlUserPassword");
                } else {
                    strURL = System.getProperty("sqlCloud")
                            + System.getProperty("sqlInsConnName") + "/"
                            + System.getProperty("sqlDBName") + "?"
                            + "user=" + System.getProperty("sqlUserName")
                            + "&password=" + System.getProperty("sqlUserPassword");
                }

                LOGGER.warning("connecting to: " + strURL);
                try {
                    conn = DriverManager.getConnection(strURL);
                } catch (SQLException e) {
                    LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
        }
        return conn;
    }
}
