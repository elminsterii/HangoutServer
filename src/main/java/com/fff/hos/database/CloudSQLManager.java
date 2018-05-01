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
                    + "&password=" + System.getProperty("sqlUserPassword");
        }

        LOGGER.warning("connecting to: " + strURL);
        try {
            conn = DriverManager.getConnection(strURL);
        } catch (SQLException e) {
            LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
        }
    }

    public boolean createPersonsTable() {

        String strCreateTableSQL = "CREATE TABLE IF NOT EXISTS persons ( "
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
            conn.createStatement().execute(strCreateTableSQL);
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
            return false;
        }
        return true;
    }

    public void update() {

    }

    public boolean insertPerson(Person person) {
        if (person == null)
            return false;

        return insertPerson(person.getEmail(), person.getDisplayName(), person.getAge()
                , person.getInterests(), person.getDescription(), person.getLocation()
                , person.getActivities(), person.getInfluence());
    }

    public boolean insertPerson(String strEmail, String strDisplayName, int iAge, String strInterests
            , String strDescription, String strLocation, String strActivities, int iInfluence) {

        boolean bRes = false;

        if (!DBTool.checkStringNotNull(strEmail)
                || !DBTool.checkStringNotNull(strDisplayName))
            return bRes;

        if (!createPersonsTable())
            return bRes;

        String strCreatePersonSQL = "INSERT INTO persons (ts,email,displayname,age" +
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
        try (PreparedStatement statementCreatePerson = conn.prepareStatement(strCreatePersonSQL)) {
            statementCreatePerson.setTimestamp(1, new Timestamp(new Date().getTime()));
            bRes = statementCreatePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    public boolean deletePersonByEmail(String strEmail) {
        boolean bRes = false;

        if (!DBTool.checkStringNotNull(strEmail))
            return bRes;

        String strDeletePersonSQL = "DELETE FROM persons WHERE email=\"" + strEmail + "\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeletePerson = conn.prepareStatement(strDeletePersonSQL)) {
            bRes = statementDeletePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    public List<Person> getAccounts() {
        return queryPersonsAndOrder("id", 0);
    }

    public Person queryPersonsByEmail(String strEmail) {
        Person person = null;
        String strSelectSQL = "SELECT * FROM persons WHERE email=\"" + strEmail + "\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                person = new Person();
                person.setEmail(rs.getString("email"));
                person.setDisplayName(rs.getString("displayname"));
                person.setAge(rs.getInt("age"));
                person.setInterests(rs.getString("interests"));
                person.setDescription(rs.getString("description"));
                person.setLocation(rs.getString("location"));
                person.setActivities(rs.getString("activities"));
                person.setInfluence(rs.getInt("influence"));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return person;
    }

    private List<Person> queryPersonsAndOrder(String strOrderBy, int iLimit) {
        List<Person> lsPersons = null;
        String strSelectSQL = "SELECT * FROM persons ORDER BY "
                + strOrderBy + " DESC " + "LIMIT " + iLimit + ";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
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
