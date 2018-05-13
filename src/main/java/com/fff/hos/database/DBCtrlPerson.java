package com.fff.hos.database;

import com.fff.hos.data.Person;
import com.fff.hos.tools.StringTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class DBCtrlPerson {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlPerson.class.getName());

    DBCtrlPerson() {
        createTable();
    }

    private void createTable() {

        if(!checkTableExist()) {
            Connection conn = DBConnection.getConnection();

            StringBuffer strCreateTableSQL = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
            strCreateTableSQL.append(DBConstants.TABLE_NAME_PERSON).append(" ( ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_ID).append(" SERIAL NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_TS).append(" timestamp NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_EMAIL).append(" VARCHAR(128) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append(" VARCHAR(64) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_DISPLAYNAME).append(" VARCHAR(64) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_ICON).append(" VARCHAR(4096), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_AGE).append(" TINYINT UNSIGNED NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_GENDER).append(" CHAR(8) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_INTERESTS).append(" VARCHAR(512), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_DESCRIPTION).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_LOCATION).append(" VARCHAR(128), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_JOINACTIVITIES).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_HOLDACTIVITIES).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_GOODMEMBER).append(" INT NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_GOODLEADER).append(" INT NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_ONLINE).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append("PRIMARY KEY (").append(DBConstants.PERSON_COL_EMAIL).append(") );");

            try {
                conn.createStatement().execute(strCreateTableSQL.toString());
            } catch (SQLException e) {
                LOGGER.warning("SQL erro, " + e.getMessage());
            }
        }
    }

    private boolean checkTableExist() {
        boolean bIsExist = false;

        Connection conn = DBConnection.getConnection();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, DBConstants.TABLE_NAME_PERSON,
                    new String[]{"TABLE"});
            bIsExist = rs.first();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return bIsExist;
    }

    boolean insert(Person person) {
        if (person == null)
            return false;

        return insert(person.getEmail(), person.getUserPassword(), person.getDisplayName(), person.getIcon(), person.getAge(), person.getGender()
                , person.getInterests(), person.getDescription(), person.getLocation(), person.getJoinActivities()
                , person.getHoldActivities(), person.getGoodMember(), person.getGoodLeader(), person.getOnline());
    }

    private boolean insert(String strEmail, String strUserPassword, String strDisplayName, String strIcon, Integer iAge, String strGender, String strInterests
            , String strDescription, String strLocation, String strJoinActivities, String strHoldActivities
            , Integer iGoodMember, Integer iGoodLeader, Integer bOnline) {

        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strUserPassword)
                || !stringTool.checkStringNotNull(strDisplayName)
                || (iAge == null)
                || !stringTool.checkStringNotNull(strGender))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strCreatePersonSQL = new StringBuilder("INSERT INTO ");
        strCreatePersonSQL.append(DBConstants.TABLE_NAME_PERSON);
        strCreatePersonSQL.append(" (").append(DBConstants.PERSON_COL_TS);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_EMAIL);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_USERPASSWORD);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_DISPLAYNAME);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_ICON);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_AGE);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_GENDER);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_INTERESTS);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_DESCRIPTION);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_LOCATION);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_JOINACTIVITIES);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_HOLDACTIVITIES);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_GOODMEMBER);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_GOODLEADER);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_ONLINE).append(") ");
        strCreatePersonSQL.append("VALUES (?");
        strCreatePersonSQL.append(",\"").append(strEmail).append("\"");
        strCreatePersonSQL.append(",\"").append(strUserPassword).append("\"");
        strCreatePersonSQL.append(",\"").append(strDisplayName).append("\"");
        strCreatePersonSQL.append(",\"").append(strIcon == null ? "" : strIcon).append("\"");
        strCreatePersonSQL.append(",\"").append(iAge).append("\"");
        strCreatePersonSQL.append(",\"").append(strGender).append("\"");
        strCreatePersonSQL.append(",\"").append(strInterests == null ? "" : strInterests).append("\"");
        strCreatePersonSQL.append(",\"").append(strDescription == null ? "" : strDescription).append("\"");
        strCreatePersonSQL.append(",\"").append(strLocation == null ? "" : strLocation).append("\"");
        strCreatePersonSQL.append(",\"").append(strJoinActivities == null ? "" : strJoinActivities).append("\"");
        strCreatePersonSQL.append(",\"").append(strHoldActivities == null ? "" : strHoldActivities).append("\"");
        strCreatePersonSQL.append(",\"").append(iGoodMember == null ? 0 : iGoodMember).append("\"");
        strCreatePersonSQL.append(",\"").append(iGoodLeader == null ? 0 : iGoodLeader).append("\"");
        strCreatePersonSQL.append(",\"").append(bOnline == null ? 0 : bOnline).append("\"");
        strCreatePersonSQL.append( ");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementCreatePerson = conn.prepareStatement(strCreatePersonSQL.toString())) {
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

    private boolean delete(String strEmail, String strUserPassword) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strUserPassword))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strDeletePersonSQL = new StringBuilder("DELETE FROM ");
        strDeletePersonSQL.append(DBConstants.TABLE_NAME_PERSON).append(" WHERE ");
        strDeletePersonSQL.append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail).append("\" AND ");
        strDeletePersonSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append("=\"").append(strUserPassword).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeletePerson = conn.prepareStatement(strDeletePersonSQL.toString())) {
            bRes = statementDeletePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean checkPersonExist(String strEmail) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_PERSON).append(" WHERE ");
        strSelectSQL.append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementSelectPerson = conn.prepareStatement(strSelectSQL.toString())) {
            bRes = statementSelectPerson.executeQuery().first();
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("check time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean checkPersonValid(String strEmail, String strUserPassword) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strUserPassword))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_PERSON).append(" WHERE ");
        strSelectSQL.append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail).append("\" AND ");
        strSelectSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append("=\"").append(strUserPassword).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementSelectPerson = conn.prepareStatement(strSelectSQL.toString())) {
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
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_PERSON).append(" WHERE ");
        strSelectSQL.append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                person = new Person();
                person.setEmail(rs.getString(DBConstants.PERSON_COL_EMAIL));
                person.setUserPassword(rs.getString(DBConstants.PERSON_COL_USERPASSWORD));
                person.setDisplayName(rs.getString(DBConstants.PERSON_COL_DISPLAYNAME));
                person.setIcon(rs.getString(DBConstants.PERSON_COL_ICON));
                person.setAge(rs.getInt(DBConstants.PERSON_COL_AGE));
                person.setGender(rs.getString(DBConstants.PERSON_COL_GENDER));
                person.setInterests(rs.getString(DBConstants.PERSON_COL_INTERESTS));
                person.setDescription(rs.getString(DBConstants.PERSON_COL_DESCRIPTION));
                person.setLocation(rs.getString(DBConstants.PERSON_COL_LOCATION));
                person.setJoinActivities(rs.getString(DBConstants.PERSON_COL_JOINACTIVITIES));
                person.setHoldActivities(rs.getString(DBConstants.PERSON_COL_HOLDACTIVITIES));
                person.setGoodMember(rs.getInt(DBConstants.PERSON_COL_GOODMEMBER));
                person.setGoodLeader(rs.getInt(DBConstants.PERSON_COL_GOODLEADER));
                person.setOnline(rs.getInt(DBConstants.PERSON_COL_ONLINE));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return person;
    }

    List<Person> queryAll(String strOrderBy, int iLimit) {
        List<Person> lsPersons = null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_PERSON).append(" ORDER BY ").append(strOrderBy);
        strSelectSQL.append(" DESC LIMIT").append(iLimit).append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();
            lsPersons = new ArrayList<>();

            while (rs.next()) {
                Person person = new Person();
                person.setEmail(rs.getString(DBConstants.PERSON_COL_EMAIL));
                person.setDisplayName(rs.getString(DBConstants.PERSON_COL_DISPLAYNAME));
                person.setIcon(rs.getString(DBConstants.PERSON_COL_ICON));
                person.setAge(rs.getInt(DBConstants.PERSON_COL_AGE));
                person.setGender(rs.getString(DBConstants.PERSON_COL_GENDER));
                person.setInterests(rs.getString(DBConstants.PERSON_COL_INTERESTS));
                person.setDescription(rs.getString(DBConstants.PERSON_COL_DESCRIPTION));
                person.setLocation(rs.getString(DBConstants.PERSON_COL_LOCATION));
                person.setJoinActivities(rs.getString(DBConstants.PERSON_COL_JOINACTIVITIES));
                person.setHoldActivities(rs.getString(DBConstants.PERSON_COL_HOLDACTIVITIES));
                person.setGoodMember(rs.getInt(DBConstants.PERSON_COL_GOODMEMBER));
                person.setGoodLeader(rs.getInt(DBConstants.PERSON_COL_GOODLEADER));
                person.setOnline(rs.getInt(DBConstants.PERSON_COL_ONLINE));
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
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(person.getEmail()))
            return false;

        Person oldPerson = query(person.getEmail());

        if (oldPerson == null)
            return false;

        fillUpdatePersonIfNull(oldPerson, person);

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_PERSON).append(" SET ");
        strUpdateSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append("=\"").append(person.getUserPassword()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_DISPLAYNAME).append("=\"").append(person.getDisplayName()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_ICON).append("=\"").append(person.getIcon()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_AGE).append("=\"").append(person.getAge()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_GENDER).append("=\"").append(person.getGender()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_INTERESTS).append("=\"").append(person.getInterests()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_DESCRIPTION).append("=\"").append(person.getDescription()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_LOCATION).append("=\"").append(person.getLocation()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_JOINACTIVITIES).append("=\"").append(person.getJoinActivities()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_HOLDACTIVITIES).append("=\"").append(person.getHoldActivities()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_GOODMEMBER).append("=\"").append(person.getGoodMember()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_GOODLEADER).append("=\"").append(person.getGoodLeader()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_ONLINE).append("=\"").append(person.getOnline()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append("=\"").append(person.getUserPassword()).append("\"");
        strUpdateSQL.append(" WHERE ").append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(person.getEmail()).append("\" ");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdatePerson = conn.prepareStatement(strUpdateSQL.toString())) {
            bRes = statementUpdatePerson.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    private void fillUpdatePersonIfNull(Person oldPerson, Person newPerson) {
        if (newPerson.getUserPassword() == null)
            newPerson.setUserPassword(oldPerson.getUserPassword());
        if (newPerson.getDisplayName() == null)
            newPerson.setDisplayName(oldPerson.getDisplayName());
        if (newPerson.getIcon() == null)
            newPerson.setIcon(oldPerson.getIcon());
        if (newPerson.getAge() == null)
            newPerson.setAge(oldPerson.getAge());
        if (newPerson.getGender() == null)
            newPerson.setGender(oldPerson.getGender());
        if (newPerson.getInterests() == null)
            newPerson.setInterests(oldPerson.getInterests());
        if (newPerson.getDescription() == null)
            newPerson.setDescription(oldPerson.getDescription());
        if (newPerson.getLocation() == null)
            newPerson.setLocation(oldPerson.getLocation());
        if (newPerson.getJoinActivities() == null)
            newPerson.setJoinActivities(oldPerson.getJoinActivities());
        if (newPerson.getHoldActivities() == null)
            newPerson.setHoldActivities(oldPerson.getHoldActivities());
        if (newPerson.getGoodMember() == null)
            newPerson.setGoodMember(oldPerson.getGoodMember());
        if (newPerson.getGoodLeader() == null)
            newPerson.setGoodLeader(oldPerson.getGoodLeader());
        if (newPerson.getOnline() == null)
            newPerson.setOnline(oldPerson.getOnline());
    }
}