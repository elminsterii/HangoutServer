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

    private TableControlPerson m_tblPersonCtrler = null;

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

        m_tblPersonCtrler = new TableControlPerson();
    }

    public boolean register(Person person) {
        return m_tblPersonCtrler.insert(person);
    }

    public boolean unregister(Person person) {
        return m_tblPersonCtrler.delete(person);
    }

    public Person queryPerson(Person person) {
        return m_tblPersonCtrler.query(person);
    }

    public boolean updatePerson(Person person) {
        return m_tblPersonCtrler.update(person);
    }

    public Person login(Person person) {
        Person resPerson = null;

        if (m_tblPersonCtrler.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = m_tblPersonCtrler.query(person.getEmail());
            resPerson.setOnline(1);
            m_tblPersonCtrler.update(person.getEmail(), Person.DB_ONLINE, "1");
        }
        return resPerson;
    }

    public Person logout(Person person) {
        Person resPerson = null;

        if (m_tblPersonCtrler.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = m_tblPersonCtrler.query(person.getEmail());
            resPerson.setOnline(0);
            m_tblPersonCtrler.update(person.getEmail(), Person.DB_ONLINE, "0");
        }
        return resPerson;
    }

    public boolean checkPersonExist(Person person) {
        return m_tblPersonCtrler.checkPersonExist(person.getEmail());
    }

    class TableControlPerson {

        private final String TABLE_NAME = "persons";

        TableControlPerson() {
            createTable();
        }

        boolean createTable() {

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
                return false;
            }
            return true;
        }

        boolean insert(Person person) {
            if (person == null)
                return false;

            return insert(person.getEmail(), person.getUserPassword(), person.getDisplayName(), person.getAge(), person.getGender()
                    , person.getInterests(), person.getDescription(), person.getLocation(), person.getJoinActivities()
                    , person.getHoldActivities(), person.getGoodMember(), person.getGoodLeader(), person.getOnline());
        }

        boolean insert(String strEmail, String strUserPassword, String strDisplayName, int iAge, String strGender, String strInterests
                , String strDescription, String strLocation, String strJoinActivities, String strHoldActivities
                , int iGoodMember, int iGoodLeader, int bOnline) {

            boolean bRes = false;

            if (!DBTool.checkStringNotNull(strEmail)
                    || !DBTool.checkStringNotNull(strUserPassword))
                return bRes;

            String strCreatePersonSQL = "INSERT INTO " + TABLE_NAME + " (ts,email,userpassword,displayname,age,gender" +
                    ",interests,description,location,joinactivities,holdactivities,goodmember,goodleader,online) " +
                    "VALUES (?,\"" + strEmail + "\"" +
                    ",\"" + strUserPassword + "\"" +
                    ",\"" + strDisplayName + "\"" +
                    ",\"" + iAge + "\"" +
                    ",\"" + strGender + "\"" +
                    ",\"" + strInterests + "\"" +
                    ",\"" + strDescription + "\"" +
                    ",\"" + strLocation + "\"" +
                    ",\"" + strJoinActivities + "\"" +
                    ",\"" + strHoldActivities + "\"" +
                    ",\"" + iGoodMember + "\"" +
                    ",\"" + iGoodLeader + "\"" +
                    ",\"" + bOnline + "\"" +
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

        boolean delete(Person person) {
            if (person == null)
                return false;

            return delete(person.getEmail(), person.getUserPassword());
        }

        boolean delete(String strEmail, String strUserPassword) {
            boolean bRes = false;

            if (!DBTool.checkStringNotNull(strEmail)
                    || !DBTool.checkStringNotNull(strUserPassword))
                return bRes;

            String strDeletePersonSQL = "DELETE FROM " + TABLE_NAME + " WHERE email=\"" + strEmail
                    + "\" AND userpassword=\"" + strUserPassword + "\";";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (PreparedStatement statementDeletePerson = conn.prepareStatement(strDeletePersonSQL)) {
                bRes = statementDeletePerson.executeUpdate() > 0;

            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return bRes;
        }

        boolean checkPersonExist(String strEmail) {
            boolean bRes = false;

            if (!DBTool.checkStringNotNull(strEmail))
                return bRes;

            String strSelectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + Person.DB_EMAIL + "=\"" + strEmail + "\";";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (PreparedStatement statementSelectPerson = conn.prepareStatement(strSelectSQL)) {
                bRes = statementSelectPerson.executeQuery().first();
            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("check time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return bRes;
        }

        boolean checkPersonValid(String strEmail, String strUserPassword) {
            boolean bRes = false;

            if (!DBTool.checkStringNotNull(strEmail)
                    || !DBTool.checkStringNotNull(strUserPassword))
                return bRes;

            String strSelectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email=\"" + strEmail
                    + "\" AND userpassword=\"" + strUserPassword + "\";";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (PreparedStatement statementSelectPerson = conn.prepareStatement(strSelectSQL)) {
                bRes = statementSelectPerson.executeQuery().first();

            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("check time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return bRes;
        }


        Person query(Person person) {
            return query(person.getEmail());
        }

        Person query(String strEmail) {
            Person person = null;
            String strSelectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email=\"" + strEmail + "\";";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
                stopwatch.stop();

                while (rs.next()) {
                    person = new Person();
                    person.setEmail(rs.getString("email"));
                    person.setUserPassword(rs.getString("userpassword"));
                    person.setDisplayName(rs.getString("displayname"));
                    person.setAge(rs.getInt("age"));
                    person.setGender(rs.getString("gender"));
                    person.setInterests(rs.getString("interests"));
                    person.setDescription(rs.getString("description"));
                    person.setLocation(rs.getString("location"));
                    person.setJoinActivities(rs.getString("joinactivities"));
                    person.setHoldActivities(rs.getString("holdactivities"));
                    person.setGoodMember(rs.getInt("goodmember"));
                    person.setGoodLeader(rs.getInt("goodleader"));
                    person.setOnline(rs.getInt("online"));
                }
            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return person;
        }

        List<Person> queryAll(String strOrderBy, int iLimit) {
            List<Person> lsPersons = null;
            String strSelectSQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY "
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
                    person.setGender(rs.getString("gender"));
                    person.setInterests(rs.getString("interests"));
                    person.setDescription(rs.getString("description"));
                    person.setLocation(rs.getString("location"));
                    person.setJoinActivities(rs.getString("joinactivities"));
                    person.setHoldActivities(rs.getString("holdactivities"));
                    person.setGoodMember(rs.getInt("goodmember"));
                    person.setGoodLeader(rs.getInt("goodleader"));
                    person.setOnline(rs.getInt("online"));
                    lsPersons.add(person);
                }
            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return lsPersons;
        }

        boolean update(String strEmail, String strColume, String strData) {
            boolean bRes = false;

            if (!DBTool.checkStringNotNull(strEmail)
                    || !DBTool.checkStringNotNull(strColume)
                    || !DBTool.checkStringNotNull(strData))
                return bRes;

            String strUpdateSQL = "UPDATE " + TABLE_NAME + " SET " + strColume + "=\"" + strData + "\"" +
                    "WHERE email=\"" + strEmail + "\";";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (PreparedStatement statementUpdatePerson = conn.prepareStatement(strUpdateSQL)) {
                bRes = statementUpdatePerson.executeUpdate() > 0;

            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return bRes;
        }

        boolean update(Person person) {
            boolean bRes = false;

            if (!DBTool.checkStringNotNull(person.getEmail()))
                return bRes;

            String strUpdateSQL = "UPDATE " + TABLE_NAME + " SET "
                    + Person.DB_USERPASSWORD + "=\"" + person.getUserPassword() + "\","
                    + Person.DB_DISPLAYNAME + "=\"" + person.getDisplayName() + "\","
                    + Person.DB_AGE + "=\"" + person.getAge() + "\","
                    + Person.DB_GENDER + "=\"" + person.getDisplayName() + "\","
                    + Person.DB_INTERESTS + "=\"" + person.getInterests() + "\","
                    + Person.DB_DESCRIPTION + "=\"" + person.getDescription() + "\","
                    + Person.DB_LOCATION + "=\"" + person.getLocation() + "\","
                    + Person.DB_JOINACTIVITIES + "=\"" + person.getJoinActivities() + "\","
                    + Person.DB_HOLDACTIVITIES + "=\"" + person.getHoldActivities() + "\","
                    + Person.DB_GOODMEMBER + "=\"" + person.getGoodMember() + "\","
                    + Person.DB_GOODLEADER + "=\"" + person.getGoodLeader() + "\","
                    + Person.DB_ONLINE + "=\"" + person.getOnline() + "\" "
                    + "WHERE " + Person.DB_EMAIL + "=\"" + person.getEmail() + "\" ";

            Stopwatch stopwatch = Stopwatch.createStarted();
            try (PreparedStatement statementUpdatePerson = conn.prepareStatement(strUpdateSQL)) {
                bRes = statementUpdatePerson.executeUpdate() > 0;

            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }

            LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return bRes;
        }
    }
}
