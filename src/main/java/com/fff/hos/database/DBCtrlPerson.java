package com.fff.hos.database;

import com.fff.hos.data.Person;
import com.fff.hos.tools.DBTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DBCtrlPerson {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlPerson.class.getName());

    private Connection conn;

    private final String DB_TABLE_NAME = "persons";
    private final static String DB_COL_TS = "ts";
    private final static String DB_COL_ID = "id";
    private final static String DB_COL_EMAIL = "email";
    private final static String DB_COL_USERPASSWORD = "userpassword";
    private final static String DB_COL_DISPLAYNAME = "displayname";
    private final static String DB_COL_ICON = "icon";
    private final static String DB_COL_AGE = "age";
    private final static String DB_COL_GENDER = "gender";
    private final static String DB_COL_INTERESTS = "interests";
    private final static String DB_COL_DESCRIPTION = "description";
    private final static String DB_COL_LOCATION = "location";
    private final static String DB_COL_JOINACTIVITIES = "joinactivities";
    private final static String DB_COL_HOLDACTIVITIES = "holdactivities";
    private final static String DB_COL_GOODMEMBER = "goodmember";
    private final static String DB_COL_GOODLEADER = "goodleader";
    private final static String DB_COL_ONLINE = "online";

    DBCtrlPerson(Connection conn) {
        this.conn = conn;
        createTable();
    }

    private void createTable() {
        String strCreateTableSQL = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + " ( "
                + DB_COL_ID + " SERIAL NOT NULL, "
                + DB_COL_TS + " timestamp NOT NULL, "
                + DB_COL_EMAIL + " VARCHAR(128) NOT NULL, "
                + DB_COL_USERPASSWORD + " VARCHAR(64) NOT NULL, "
                + DB_COL_DISPLAYNAME + " VARCHAR(64) NOT NULL, "
                + DB_COL_ICON + " VARCHAR(4096), "
                + DB_COL_AGE + " TINYINT UNSIGNED NOT NULL, "
                + DB_COL_GENDER + " CHAR(8) NOT NULL, "
                + DB_COL_INTERESTS + " VARCHAR(512), "
                + DB_COL_DESCRIPTION + " VARCHAR(512), "
                + DB_COL_LOCATION + " VARCHAR(128), "
                + DB_COL_JOINACTIVITIES + " VARCHAR(1024), "
                + DB_COL_HOLDACTIVITIES + " VARCHAR(1024), "
                + DB_COL_GOODMEMBER + " INT NOT NULL DEFAULT 0, "
                + DB_COL_GOODLEADER + " INT NOT NULL DEFAULT 0, "
                + DB_COL_ONLINE + " TINYINT UNSIGNED NOT NULL DEFAULT 0, "
                + "PRIMARY KEY (email) );";

        try {
            conn.createStatement().execute(strCreateTableSQL);
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }
    }

    boolean insert(Person person) {
        if (person == null)
            return false;

        return insert(person.getEmail(), person.getUserPassword(), person.getDisplayName(), person.getIcon(), person.getAge(), person.getGender()
                , person.getInterests(), person.getDescription(), person.getLocation(), person.getJoinActivities()
                , person.getHoldActivities(), person.getGoodMember(), person.getGoodLeader(), person.getOnline());
    }

    boolean insert(String strEmail, String strUserPassword, String strDisplayName, String icon, Integer iAge, String strGender, String strInterests
            , String strDescription, String strLocation, String strJoinActivities, String strHoldActivities
            , Integer iGoodMember, Integer iGoodLeader, Integer bOnline) {

        boolean bRes = false;

        if (!DBTool.checkStringNotNull(strEmail)
                || !DBTool.checkStringNotNull(strUserPassword)
                || !DBTool.checkStringNotNull(strDisplayName)
                || (iAge == null)
                || !DBTool.checkStringNotNull(strGender))
            return bRes;

        String strCreatePersonSQL = "INSERT INTO " + DB_TABLE_NAME +
                " (" + DB_COL_TS +
                "," + DB_COL_EMAIL +
                "," + DB_COL_USERPASSWORD +
                "," + DB_COL_DISPLAYNAME +
                "," + DB_COL_ICON +
                "," + DB_COL_AGE +
                "," + DB_COL_GENDER +
                "," + DB_COL_INTERESTS +
                "," + DB_COL_DESCRIPTION +
                "," + DB_COL_LOCATION +
                "," + DB_COL_JOINACTIVITIES +
                "," + DB_COL_HOLDACTIVITIES +
                "," + DB_COL_GOODMEMBER +
                "," + DB_COL_GOODLEADER +
                "," + DB_COL_ONLINE + ") " +
                "VALUES (?,\"" + strEmail + "\"" +
                ",\"" + strUserPassword + "\"" +
                ",\"" + strDisplayName + "\"" +
                ",\"" + icon + "\"" +
                ",\"" + iAge + "\"" +
                ",\"" + strGender + "\"" +
                ",\"" + strInterests + "\"" +
                ",\"" + strDescription + "\"" +
                ",\"" + strLocation + "\"" +
                ",\"" + strJoinActivities + "\"" +
                ",\"" + strHoldActivities + "\"" +
                ",\"" + (iGoodMember == null ? 0 : iGoodMember) + "\"" +
                ",\"" + (iGoodLeader == null ? 0 : iGoodLeader) + "\"" +
                ",\"" + (bOnline == null ? 0 : bOnline) + "\"" +
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

        String strDeletePersonSQL = "DELETE FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_EMAIL + "=\"" + strEmail
                + "\" AND " + DB_COL_USERPASSWORD + "=\"" + strUserPassword + "\";";

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

        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_EMAIL + "=\"" + strEmail + "\";";

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

        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_EMAIL + "=\"" + strEmail
                + "\" AND " + DB_COL_USERPASSWORD + "=\"" + strUserPassword + "\";";

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
        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_EMAIL + "=\"" + strEmail + "\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                person = new Person();
                person.setEmail(rs.getString("email"));
                person.setUserPassword(rs.getString("userpassword"));
                person.setDisplayName(rs.getString("displayname"));
                person.setIcon(rs.getString("icon"));
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
        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY "
                + strOrderBy + " DESC " + "LIMIT " + iLimit + ";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
            stopwatch.stop();
            lsPersons = new ArrayList<>();

            while (rs.next()) {
                Person person = new Person();
                person.setEmail(rs.getString("email"));
                person.setDisplayName(rs.getString("displayname"));
                person.setIcon(rs.getString("icon"));
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

    boolean update(Person person) {
        boolean bRes = false;

        if (!DBTool.checkStringNotNull(person.getEmail()))
            return bRes;

        Person oldPerson = query(person.getEmail());
        fillUpdatePersonIfNull(oldPerson, person);

        String strUpdateSQL = "UPDATE " + DB_TABLE_NAME + " SET "
                + DB_COL_USERPASSWORD + "=\"" + person.getUserPassword() + "\","
                + DB_COL_DISPLAYNAME + "=\"" + person.getDisplayName() + "\","
                + DB_COL_ICON + "=\"" + person.getIcon() + "\","
                + DB_COL_AGE + "=\"" + person.getAge() + "\","
                + DB_COL_GENDER + "=\"" + person.getDisplayName() + "\","
                + DB_COL_INTERESTS + "=\"" + person.getInterests() + "\","
                + DB_COL_DESCRIPTION + "=\"" + person.getDescription() + "\","
                + DB_COL_LOCATION + "=\"" + person.getLocation() + "\","
                + DB_COL_JOINACTIVITIES + "=\"" + person.getJoinActivities() + "\","
                + DB_COL_HOLDACTIVITIES + "=\"" + person.getHoldActivities() + "\","
                + DB_COL_GOODMEMBER + "=\"" + person.getGoodMember() + "\","
                + DB_COL_GOODLEADER + "=\"" + person.getGoodLeader() + "\","
                + DB_COL_ONLINE + "=\"" + person.getOnline() + "\" "
                + "WHERE " + DB_COL_EMAIL + "=\"" + person.getEmail() + "\" ";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdatePerson = conn.prepareStatement(strUpdateSQL)) {
            bRes = statementUpdatePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    private void fillUpdatePersonIfNull(Person oldPerson, Person updatePerson) {
        if (updatePerson.getUserPassword() == null)
            updatePerson.setUserPassword(oldPerson.getUserPassword());
        if (updatePerson.getDisplayName() == null)
            updatePerson.setDisplayName(oldPerson.getDisplayName());
        if (updatePerson.getIcon() == null)
            updatePerson.setIcon(oldPerson.getIcon());
        if (updatePerson.getAge() == null)
            updatePerson.setAge(oldPerson.getAge());
        if (updatePerson.getGender() == null)
            updatePerson.setGender(oldPerson.getGender());
        if (updatePerson.getInterests() == null)
            updatePerson.setInterests(oldPerson.getInterests());
        if (updatePerson.getDescription() == null)
            updatePerson.setDescription(oldPerson.getDescription());
        if (updatePerson.getLocation() == null)
            updatePerson.setLocation(oldPerson.getLocation());
        if (updatePerson.getJoinActivities() == null)
            updatePerson.setJoinActivities(oldPerson.getJoinActivities());
        if (updatePerson.getHoldActivities() == null)
            updatePerson.setHoldActivities(oldPerson.getHoldActivities());
        if (updatePerson.getGoodMember() == null)
            updatePerson.setGoodMember(oldPerson.getGoodMember());
        if (updatePerson.getGoodLeader() == null)
            updatePerson.setGoodLeader(oldPerson.getGoodLeader());
        if (updatePerson.getOnline() == null)
            updatePerson.setOnline(oldPerson.getOnline());
    }
}