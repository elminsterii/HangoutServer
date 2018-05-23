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
            strCreateTableSQL.append(DBConstants.PERSON_COL_AGE).append(" TINYINT UNSIGNED NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_GENDER).append(" CHAR(8) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_INTERESTS).append(" VARCHAR(512), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_DESCRIPTION).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_LOCATION).append(" VARCHAR(128), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_SAVEACTIVITIES).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_GOOD).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_NOGOOD).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_ONLINE).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.PERSON_COL_ANONYMOUS).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
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

        return insert(person.getEmail(), person.getUserPassword(), person.getDisplayName(), person.getAge(), person.getGender()
                , person.getInterests(), person.getDescription(), person.getLocation(), person.getSaveActivities()
                , person.getGood(), person.getNoGood(), person.getOnline(), person.getAnonymous());
    }

    private boolean insert(String strEmail, String strUserPassword, String strDisplayName, Integer iAge, String strGender, String strInterests
            , String strDescription, String strLocation, String strSaveActivities
            , Integer iGood, Integer iNoGood, Integer bOnline, Integer bAnonymous) {

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
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_AGE);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_GENDER);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_INTERESTS);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_DESCRIPTION);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_LOCATION);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_SAVEACTIVITIES);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_GOOD);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_NOGOOD);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_ONLINE);
        strCreatePersonSQL.append(",").append(DBConstants.PERSON_COL_ANONYMOUS).append(") ");
        strCreatePersonSQL.append("VALUES (?");
        strCreatePersonSQL.append(",\"").append(strEmail).append("\"");
        strCreatePersonSQL.append(",\"").append(strUserPassword).append("\"");
        strCreatePersonSQL.append(",\"").append(strDisplayName).append("\"");
        strCreatePersonSQL.append(",\"").append(iAge).append("\"");
        strCreatePersonSQL.append(",\"").append(strGender).append("\"");
        strCreatePersonSQL.append(",\"").append(strInterests == null ? "" : strInterests).append("\"");
        strCreatePersonSQL.append(",\"").append(strDescription == null ? "" : strDescription).append("\"");
        strCreatePersonSQL.append(",\"").append(strLocation == null ? "" : strLocation).append("\"");
        strCreatePersonSQL.append(",\"").append(strSaveActivities == null ? "" : strSaveActivities).append("\"");
        strCreatePersonSQL.append(",\"").append(iGood == null ? 0 : iGood).append("\"");
        strCreatePersonSQL.append(",\"").append(iNoGood == null ? 0 : iNoGood).append("\"");
        strCreatePersonSQL.append(",\"").append(bOnline == null ? 0 : bOnline).append("\"");
        strCreatePersonSQL.append(",\"").append(bAnonymous == null ? 0 : bAnonymous).append("\"");
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
        return query(person.getId(), person.getEmail());
    }

    Person query(String strId, String strEmail) {
        Person person = null;
        StringTool stringTool = new StringTool();

        if ((!stringTool.checkStringNotNull(strEmail)
                && !stringTool.checkStringNotNull(strId))
                || (stringTool.checkStringNotNull(strEmail)
                && stringTool.checkStringNotNull(strId)))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_PERSON).append(" WHERE ");

        if (stringTool.checkStringNotNull(strId))
            strSelectSQL.append(DBConstants.PERSON_COL_ID).append("=\"").append(strId).append("\";");
        else
            strSelectSQL.append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                person = new Person();
                person.setId(rs.getString(DBConstants.PERSON_COL_ID));
                person.setEmail(rs.getString(DBConstants.PERSON_COL_EMAIL));
                person.setUserPassword(rs.getString(DBConstants.PERSON_COL_USERPASSWORD));
                person.setDisplayName(rs.getString(DBConstants.PERSON_COL_DISPLAYNAME));
                person.setAge(rs.getInt(DBConstants.PERSON_COL_AGE));
                person.setGender(rs.getString(DBConstants.PERSON_COL_GENDER));
                person.setInterests(rs.getString(DBConstants.PERSON_COL_INTERESTS));
                person.setDescription(rs.getString(DBConstants.PERSON_COL_DESCRIPTION));
                person.setLocation(rs.getString(DBConstants.PERSON_COL_LOCATION));
                person.setSaveActivities(rs.getString(DBConstants.PERSON_COL_SAVEACTIVITIES));
                person.setGood(rs.getInt(DBConstants.PERSON_COL_GOOD));
                person.setNoGood(rs.getInt(DBConstants.PERSON_COL_NOGOOD));
                person.setOnline(rs.getInt(DBConstants.PERSON_COL_ONLINE));
                person.setAnonymous(rs.getInt(DBConstants.PERSON_COL_ANONYMOUS));
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
                person.setId(rs.getString(DBConstants.PERSON_COL_ID));
                person.setEmail(rs.getString(DBConstants.PERSON_COL_EMAIL));
                person.setDisplayName(rs.getString(DBConstants.PERSON_COL_DISPLAYNAME));
                person.setAge(rs.getInt(DBConstants.PERSON_COL_AGE));
                person.setGender(rs.getString(DBConstants.PERSON_COL_GENDER));
                person.setInterests(rs.getString(DBConstants.PERSON_COL_INTERESTS));
                person.setDescription(rs.getString(DBConstants.PERSON_COL_DESCRIPTION));
                person.setLocation(rs.getString(DBConstants.PERSON_COL_LOCATION));
                person.setSaveActivities(rs.getString(DBConstants.PERSON_COL_SAVEACTIVITIES));
                person.setGood(rs.getInt(DBConstants.PERSON_COL_GOOD));
                person.setNoGood(rs.getInt(DBConstants.PERSON_COL_NOGOOD));
                person.setOnline(rs.getInt(DBConstants.PERSON_COL_ONLINE));
                person.setAnonymous(rs.getInt(DBConstants.PERSON_COL_ANONYMOUS));
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

        Person oldPerson = query(null, person.getEmail());

        if (oldPerson == null)
            return false;

        fillUpdatePersonIfNull(oldPerson, person);

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_PERSON).append(" SET ");
        strUpdateSQL.append(DBConstants.PERSON_COL_USERPASSWORD).append("=\"").append(person.getUserPassword()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_DISPLAYNAME).append("=\"").append(person.getDisplayName()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_AGE).append("=\"").append(person.getAge()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_GENDER).append("=\"").append(person.getGender()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_INTERESTS).append("=\"").append(person.getInterests()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_DESCRIPTION).append("=\"").append(person.getDescription()).append("\",");
        strUpdateSQL.append(DBConstants.PERSON_COL_LOCATION).append("=\"").append(person.getLocation()).append("\",");
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

    boolean deem(String strEmail, Integer iDeem, Integer iDeemRb) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || iDeem == null)
            return false;

        final int INT_DEEM_GOOD = 1;
        final int INT_DEEM_DO_ROLLBACK = 1;

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_PERSON).append(" SET ");

        if(iDeem == INT_DEEM_GOOD) {
            strUpdateSQL.append(DBConstants.PERSON_COL_GOOD).append("=");
            strUpdateSQL.append(DBConstants.PERSON_COL_GOOD).append(iDeemRb == INT_DEEM_DO_ROLLBACK ? "-1" : "+1");
        }
        else {
            strUpdateSQL.append(DBConstants.PERSON_COL_NOGOOD).append("=");
            strUpdateSQL.append(DBConstants.PERSON_COL_NOGOOD).append(iDeemRb == INT_DEEM_DO_ROLLBACK ? "-1" : "+1");
        }

        strUpdateSQL.append(" WHERE ").append(DBConstants.PERSON_COL_EMAIL).append("=\"").append(strEmail);
        strUpdateSQL.append("\";");

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
        if (newPerson.getId() == null)
            newPerson.setId(oldPerson.getId());
        if (newPerson.getUserPassword() == null)
            newPerson.setUserPassword(oldPerson.getUserPassword());
        if (newPerson.getDisplayName() == null)
            newPerson.setDisplayName(oldPerson.getDisplayName());
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
        if (newPerson.getSaveActivities() == null)
            newPerson.setSaveActivities(oldPerson.getSaveActivities());
        if (newPerson.getGood() == null)
            newPerson.setGood(oldPerson.getGood());
        if (newPerson.getNoGood() == null)
            newPerson.setNoGood(oldPerson.getNoGood());
        if (newPerson.getOnline() == null)
            newPerson.setOnline(oldPerson.getOnline());
        if (newPerson.getAnonymous() == null)
            newPerson.setAnonymous(oldPerson.getAnonymous());
    }
}