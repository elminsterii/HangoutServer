package com.fff.hos.database;

//TODO - multi-thread and thread-safe.

import com.fff.hos.log.HoSLogger;
import com.google.apphosting.api.ApiProxy;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CloudSQLManager {

    private static final String LOG_TAG = "[CloudSQLManager]";

    private static CloudSQLManager m_instance = null;
    private Connection conn;

    private CloudSQLManager() {

    }

    public CloudSQLManager getInstance() {
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

        String url = hostname.contains("localhost:")
                ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
        HoSLogger.info(LOG_TAG, "connecting to: " + url);
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            HoSLogger.error(LOG_TAG, "Unable to connect to Cloud SQL" + e.getMessage());
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
        String createPersonSql = "INSERT INTO persons (ts,account) VALUES (?,Jimmy);";
        String createTableSql = "CREATE TABLE IF NOT EXISTS persons ( "
                + "account SERIAL NOT NULL, ts timestamp NOT NULL, "
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
                    HoSLogger.info(LOG_TAG, "Account: " + strAccount);
                }
            }
        } catch (SQLException e) {
            HoSLogger.error(LOG_TAG, "SQL error" + e.getMessage());
        }
        HoSLogger.info(LOG_TAG, "Query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void query() {

    }
}
