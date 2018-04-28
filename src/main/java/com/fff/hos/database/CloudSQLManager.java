package com.fff.hos.database;

import com.fff.hos.tools.SysRunTool;
import com.google.apphosting.api.ApiProxy;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//TODO - multi-thread and thread-safe.
public class CloudSQLManager {
    private static final Logger LOGGER = Logger.getLogger(CloudSQLManager.class.getName());

    private static CloudSQLManager m_instance = null;
    private Connection conn;

    private CloudSQLManager() {

    }

    public static CloudSQLManager getInstance() {
        if (m_instance == null) {
            m_instance = new CloudSQLManager();
            m_instance.initialize();
        }
        return m_instance;
    }

    private void initialize() {
        ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
        Map<String, Object> attr = env.getAttributes();
        String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

        String strURL;
        if (hostname.contains("localhost:")) {
            SysRunTool.sysRunMySQL();

            strURL = System.getProperty("sqlLocal")
                    + System.getProperty("sqlDBName") + "?"
                    + "useSSL=false&"
                    + "user=" + System.getProperty("sqlUserName")
                    + "&password=" + System.getProperty("sqlUserPassword");
        } else {
            strURL = System.getProperty("sqlCloud")
                    + System.getProperty("sqlInsConnName") + "/"
                    + System.getProperty("sqlDBName")
                    + "?user=" + System.getProperty("sqlUserName")
                    + "&amp;password=" + System.getProperty("sqlUserPassword");
        }

        LOGGER.info("connecting to: " + strURL);
        try {
            conn = DriverManager.getConnection(strURL);
        } catch (SQLException e) {
            LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
        }
    }

    public void update() {

    }

    public void insert() {

    }

    public void delete() {

    }

    public void getAccounts() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String createPersonSql = "INSERT INTO persons (ts,account) VALUES (?,\"Jimmy\");";
        String createTableSql = "CREATE TABLE IF NOT EXISTS persons ( "
                + "id SERIAL NOT NULL, "
                + "ts timestamp NOT NULL, "
                + "account VARCHAR(128) NOT NULL, "
                + "PRIMARY KEY (account) );";
        String selectSql = "SELECT * FROM persons ORDER BY ts DESC "
                + "LIMIT 10;";

        try (PreparedStatement statementCreatePerson = conn.prepareStatement(createPersonSql)) {
            conn.createStatement().executeUpdate(createTableSql);
            statementCreatePerson.setTimestamp(1, new Timestamp(new Date().getTime()));
            statementCreatePerson.executeUpdate();

            try (ResultSet rs = conn.prepareStatement(selectSql).executeQuery()) {
                stopwatch.stop();
                while (rs.next()) {
                    String strAccount = rs.getString("account");
                    LOGGER.info("Account: " + strAccount);
                }
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }
        LOGGER.info("Query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void query() {

    }
}
