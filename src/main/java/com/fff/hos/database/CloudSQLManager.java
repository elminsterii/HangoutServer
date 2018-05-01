package com.fff.hos.database;

import com.fff.hos.person.Person;
import com.fff.hos.tools.DBTool;
import com.fff.hos.tools.SysRunTool;
import com.google.apphosting.api.ApiProxy;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
                    + System.getProperty("sqlDBName") + "?"
                    + "user=" + System.getProperty("sqlUserName")
                    + "&amp;password=" + System.getProperty("sqlUserPassword");
        }

        LOGGER.info("connecting to: " + strURL);
        try {
            conn = DriverManager.getConnection(strURL);
        } catch (SQLException e) {
            LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
        }
    }

    public void createPersonsTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS persons ( "
                + "id SERIAL NOT NULL, "
                + "ts timestamp NOT NULL, "
                + "email VARCHAR(128) NOT NULL, "
                + "displayname VARCHAR(64) NOT NULL, "
                + "age INTEGER NOT NULL, "
                + "interests VARCHAR(512), "
                + "description VARCHAR(512), "
                + "location VARCHAR(128), "
                + "activities VARCHAR(512), "
                + "influence INTEGER, "
                + "PRIMARY KEY (email) );";

        try {
            conn.createStatement().executeUpdate(createTableSql);
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }
    }

    public void update() {

    }

    public boolean insert(String strEmail, String strDisplayName, int iAge, String strInterests
            , String strDescription, String strLocation, String strActivities, int iInfluence) {

        boolean bRes = false;

        if (DBTool.checkStringNotNull(strEmail)
                || DBTool.checkStringNotNull(strDisplayName))
            return bRes;

        String createPersonSql = "INSERT INTO persons (ts,email,displayname,age" +
                ",interests,description,location,activities,influence) " +
                "VALUES (?,\"" + strEmail + "\"" +
                ",\"" + strDisplayName + "\"" +
                ",\"" + iAge + "\"" +
                ",\"" + strInterests + "\"" +
                ",\"" + strDescription + "\"" +
                ",\"" + strLocation + "\"" +
                ",\"" + strActivities + "\"" +
                ",\"" + iInfluence + "\"" +
                ");";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementCreatePerson = conn.prepareStatement(createPersonSql)) {
            statementCreatePerson.setTimestamp(1, new Timestamp(new Date().getTime()));
            bRes = statementCreatePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    public void delete() {

    }

    public List<Person> getAccounts() {
        return queryPersonsOrderBy("id", 0);
    }

    private List<Person> queryPersonsOrderBy(String strOrderBy, int iLimit) {
        List<Person> lsPersons = null;
        String selectSql = "SELECT * FROM persons ORDER BY " + strOrderBy + "DESC "
                + "LIMIT " + iLimit + ";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(selectSql).executeQuery()) {
            stopwatch.stop();
            lsPersons = new ArrayList<>();

            while (rs.next()) {
                Person person = new Person();
                person.setEmail(rs.getString("email"));
                person.setDisplayName(rs.getString("displayname"));
                person.setAge(rs.getInt("age"));
                person.setInterests(rs.getString("interests"));
                person.setDescription(rs.getString("description"));
                person.setLocation(rs.getString("location"));
                person.setActivities(rs.getString("activities"));
                person.setInfluence(rs.getInt("influence"));
                lsPersons.add(person);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return lsPersons;
    }
}
