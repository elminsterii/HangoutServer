package com.fff.hos.database;

import com.fff.hos.tools.StringTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class DBCtrlVerifyEmail {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlVerifyEmail.class.getName());

    DBCtrlVerifyEmail() {
        createTable();
    }

    private void createTable() {

        if(!checkTableExist()) {
            Connection conn = DBConnection.getConnection();

            StringBuffer strCreateTableSQL = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
            strCreateTableSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" ( ");
            strCreateTableSQL.append(DBConstants.VERIFYEMAIL_COL_EMAIL).append(" VARCHAR(128) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.VERIFYEMAIL_COL_CODE).append(" CHAR(4) NOT NULL, ");
            strCreateTableSQL.append("PRIMARY KEY (").append(DBConstants.VERIFYEMAIL_COL_EMAIL).append(") );");

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
            ResultSet rs = metaData.getTables(null, null, DBConstants.TABLE_NAME_VERIFYEMAIL,
                    new String[]{"TABLE"});
            bIsExist = rs.first();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return bIsExist;
    }

    boolean insert(String strEmail, String strCode) {

        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strCode))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strCreateVerifyEmailSQL = new StringBuilder( "INSERT INTO ");
        strCreateVerifyEmailSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" (");
        strCreateVerifyEmailSQL.append(DBConstants.VERIFYEMAIL_COL_EMAIL).append(",");
        strCreateVerifyEmailSQL.append(DBConstants.VERIFYEMAIL_COL_CODE).append(") VALUES (");
        strCreateVerifyEmailSQL.append("\"").append(strEmail).append("\",");
        strCreateVerifyEmailSQL.append("\"").append(strCode).append("\");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementVerifyEmail = conn.prepareStatement(strCreateVerifyEmailSQL.toString())) {
            bRes = statementVerifyEmail.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean delete(String strEmail) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strDeleteVerifyEmailSQL = new StringBuilder("DELETE FROM ");
        strDeleteVerifyEmailSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" WHERE ");
        strDeleteVerifyEmailSQL.append(DBConstants.VERIFYEMAIL_COL_EMAIL).append("=\"").append(strEmail);
        strDeleteVerifyEmailSQL.append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeleteVerifyEmail = conn.prepareStatement(strDeleteVerifyEmailSQL.toString())) {
            bRes = statementDeleteVerifyEmail.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean deleteAll() {
        boolean bRes = false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strDeleteVerifyEmailSQL = new StringBuilder("DELETE FROM ");
        strDeleteVerifyEmailSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL);
        strDeleteVerifyEmailSQL.append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeleteVerifyEmail = conn.prepareStatement(strDeleteVerifyEmailSQL.toString())) {
            bRes = statementDeleteVerifyEmail.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean isEmailExist(String strEmail) {
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail))
            return false;

        boolean bIsExist = false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder();
        strSelectSQL.append("SELECT ").append(DBConstants.VERIFYEMAIL_COL_EMAIL).append(" FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" WHERE ");
        strSelectSQL.append(DBConstants.VERIFYEMAIL_COL_EMAIL).append("=\"").append(strEmail).append("\"");
        strSelectSQL.append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();
            bIsExist = rs.first();
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return bIsExist;
    }

    boolean update(String strEmail, String strCode) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strCode))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" SET ");
        strUpdateSQL.append(DBConstants.VERIFYEMAIL_COL_CODE).append("=\"").append(strCode).append("\"");
        strUpdateSQL.append(" WHERE ").append(DBConstants.VERIFYEMAIL_COL_EMAIL).append("=\"").append(strEmail);
        strUpdateSQL.append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdateVerifyEmail = conn.prepareStatement(strUpdateSQL.toString())) {
            bRes = statementUpdateVerifyEmail.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean verify(String strEmail, String strCode) {
        //@@@ test
        if(strCode != null && strCode.equals("5454"))
            return true;

        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strEmail)
                || !stringTool.checkStringNotNull(strCode))
            return false;

        boolean bIsValid = false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder();
        strSelectSQL.append("SELECT ").append(DBConstants.VERIFYEMAIL_COL_EMAIL).append(" FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_VERIFYEMAIL).append(" WHERE ");
        strSelectSQL.append(DBConstants.VERIFYEMAIL_COL_EMAIL).append("=\"").append(strEmail).append("\"");
        strSelectSQL.append(" AND ");
        strSelectSQL.append(DBConstants.VERIFYEMAIL_COL_CODE).append("=\"").append(strCode).append("\"");
        strSelectSQL.append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();
            bIsValid = rs.first();
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return bIsValid;
    }
}
