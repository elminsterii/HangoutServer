package com.fff.hos.database;

import com.fff.hos.data.Activity;
import com.fff.hos.tools.StringTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class DBCtrlActivity {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlActivity.class.getName());

    DBCtrlActivity() {
        createTable();
    }

    private void createTable() {

        if(!checkTableExist()) {
            Connection conn = DBConnection.getConnection();

            StringBuilder strCreateTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            strCreateTableSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" ( ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_ID).append(" SERIAL NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_TS).append(" timestamp NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append(" VARCHAR(128) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_PUBLISHBEGIN).append(" VARCHAR(32) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEND).append(" VARCHAR(32) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_EARLYBIRD).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append(" VARCHAR(64) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_DATEBEGIN).append(" VARCHAR(32) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_DATEEND).append(" VARCHAR(32) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_LOCATION).append(" VARCHAR(128), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_STATUS).append(" CHAR(8), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_DESCRIPTION).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_TAGS).append(" VARCHAR(128), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_GOODACTIVITY).append(" INT NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_ATTENTION).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append(" VARCHAR(4096), ");
            strCreateTableSQL.append("PRIMARY KEY (").append(DBConstants.ACTIVITY_COL_ID).append(") );");

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
            ResultSet rs = metaData.getTables(null, null, DBConstants.TABLE_NAME_ACTIVITY,
                    new String[]{"TABLE"});
            bIsExist = rs.first();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return bIsExist;
    }

    String insert(Activity activity) {
        return insert(activity.getPublisherEmail(), activity.getPublishBegin(), activity.getPublishEnd()
                , activity.getLargeActivity(), activity.getEarlyBird(), activity.getDisplayName(), activity.getDateBegin()
                , activity.getDateEnd(), activity.getLocation(), activity.getStatus(), activity.getDescription()
                , activity.getTags(), activity.getGoodActivity(), activity.getAttention(), activity.getAttendees());
    }

    private String insert(String strPublisherEmail, String strPublishBegin, String strPublishEnd, Integer iLargeActivity
            , Integer iEarlyBird,String strDisplayName, String strDateBegin, String strDateEnd, String strLocation
            , String strStatus, String strDescription, String strTags, Integer iGoodActivity, Integer iAttention
            , String strAttendees) {

        String strResId = null;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strPublisherEmail)
                || !stringTool.checkStringNotNull(strPublishBegin)
                || !stringTool.checkStringNotNull(strPublishEnd)
                || (iLargeActivity == null)
                || (iEarlyBird == null)
                || !stringTool.checkStringNotNull(strDisplayName)
                || !stringTool.checkStringNotNull(strDateBegin)
                || !stringTool.checkStringNotNull(strDateEnd)
                || !stringTool.checkStringNotNull(strLocation))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strCreateActivitySQL = new StringBuilder( "INSERT INTO ");
        strCreateActivitySQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" (");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_TS).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_PUBLISHBEGIN).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_PUBLISHEND).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_EARLYBIRD).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_DATEBEGIN).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_DATEEND).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_LOCATION).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_STATUS).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_DESCRIPTION).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_TAGS).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_GOODACTIVITY).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_ATTENTION).append(",");
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append(") VALUES (?,");
        strCreateActivitySQL.append("\"").append(strPublisherEmail).append("\",");
        strCreateActivitySQL.append("\"").append(strPublishBegin).append("\",");
        strCreateActivitySQL.append("\"").append(strPublishEnd).append("\",");
        strCreateActivitySQL.append(iLargeActivity).append(",");
        strCreateActivitySQL.append(iEarlyBird).append(",");
        strCreateActivitySQL.append("\"").append(strDisplayName).append("\",");
        strCreateActivitySQL.append("\"").append(strDateBegin).append("\",");
        strCreateActivitySQL.append("\"").append(strDateEnd).append("\",");
        strCreateActivitySQL.append("\"").append(strLocation).append("\",");
        strCreateActivitySQL.append("\"").append(strStatus == null ? "" : strStatus).append("\",");
        strCreateActivitySQL.append("\"").append(strDescription == null ? "" : strDescription).append("\",");
        strCreateActivitySQL.append("\"").append(strTags == null ? "" : strTags).append("\",");
        strCreateActivitySQL.append(iGoodActivity == null ? 0 : iGoodActivity).append(",");
        strCreateActivitySQL.append(iAttention == null ? 0 : iAttention).append(",");
        strCreateActivitySQL.append("\"").append(strAttendees == null ? "" : strAttendees).append("\");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementCreateActivity = conn.prepareStatement(strCreateActivitySQL.toString()
                , new String[]{DBConstants.ACTIVITY_COL_ID})) {
            statementCreateActivity.setTimestamp(1, new Timestamp(new Date().getTime()));

            if(statementCreateActivity.executeUpdate() > 0) {
                ResultSet rs = statementCreateActivity.getGeneratedKeys();
                String strNewActivityID = "";
                if(rs.next())
                    strNewActivityID = rs.getString(1);

                strResId = strNewActivityID;
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return strResId;
    }

    boolean delete(Activity activity) {
        if (activity == null)
            return false;

        return delete(activity.getId(), activity.getPublisherEmail());
    }

    private boolean delete(String strId, String strPublisherEmail) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strId)
                && !stringTool.checkStringNotNull(strPublisherEmail))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strDeleteActivitySQL = new StringBuilder("DELETE FROM ");
        strDeleteActivitySQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");

        if(stringTool.checkStringNotNull(strId))
            strDeleteActivitySQL.append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strId).append("\" AND ");

        if(stringTool.checkStringNotNull(strPublisherEmail))
            strDeleteActivitySQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append("=\"").append(strPublisherEmail).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeleteActivity = conn.prepareStatement(strDeleteActivitySQL.toString())) {
            bRes = statementDeleteActivity.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean checkActivityExist(String strId) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strId))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");
        strSelectSQL.append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strId).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementSelectActivity = conn.prepareStatement(strSelectSQL.toString())) {
            bRes = statementSelectActivity.executeQuery().first();
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("check time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    List<String> query(Activity activity) {
        List<String> lsIds = new ArrayList<>();

        if (activity == null)
            return lsIds;

        StringTool stringTool = new StringTool();

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder();
        strSelectSQL.append("SELECT * FROM ").append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");

        if(stringTool.checkStringNotNull(activity.getPublisherEmail()))
            strSelectSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL ).append("=\"").append(activity.getPublisherEmail()).append("\"");

        if(activity.getLargeActivity() != null)
            strSelectSQL.append(" AND ").append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append("=").append(activity.getLargeActivity());

        if(activity.getEarlyBird() != null)
            strSelectSQL.append(" AND ").append(DBConstants.ACTIVITY_COL_EARLYBIRD).append("=").append(activity.getEarlyBird());

        if(stringTool.checkStringNotNull(activity.getDisplayName()))
            strSelectSQL.append(" AND ").append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append("=\"").append(activity.getDisplayName()).append("\"");

        if(stringTool.checkStringNotNull(activity.getTags())) {
            String strRegExp = stringTool.strTagsToRegExp(activity.getTags());
            strSelectSQL.append(" AND ").append(DBConstants.ACTIVITY_COL_TAGS).append(" REGEXP \'").append(strRegExp).append("\'");
        }

        strSelectSQL.append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                lsIds.add(rs.getString(DBConstants.ACTIVITY_COL_ID));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return lsIds;
    }

    List<Activity> queryByIds(String strIds) {
        List<Activity> lsActivities = new ArrayList<>();
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strIds))
            return lsActivities;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");
        strSelectSQL.append(DBConstants.ACTIVITY_COL_ID).append(" IN (").append(strIds).append(");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                Activity activity = new Activity();
                activity.setId(rs.getString(DBConstants.ACTIVITY_COL_ID));
                activity.setPublisherEmail(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL));
                activity.setPublishBegin(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHBEGIN));
                activity.setPublishEnd(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHEND));
                activity.setLargeActivity(rs.getInt(DBConstants.ACTIVITY_COL_LARGEACTIVITY));
                activity.setEarlyBird(rs.getInt(DBConstants.ACTIVITY_COL_EARLYBIRD));
                activity.setDisplayName(rs.getString(DBConstants.ACTIVITY_COL_DISPLAYNAME));
                activity.setDateBegin(rs.getString(DBConstants.ACTIVITY_COL_DATEBEGIN));
                activity.setDateEnd(rs.getString(DBConstants.ACTIVITY_COL_DATEEND));
                activity.setLocation(rs.getString(DBConstants.ACTIVITY_COL_LOCATION));
                activity.setStatus(rs.getString(DBConstants.ACTIVITY_COL_STATUS));
                activity.setDescription(rs.getString(DBConstants.ACTIVITY_COL_DESCRIPTION));
                activity.setTags(rs.getString(DBConstants.ACTIVITY_COL_TAGS));
                activity.setGoodActivity(rs.getInt(DBConstants.ACTIVITY_COL_GOODACTIVITY));
                activity.setAttention(rs.getInt(DBConstants.ACTIVITY_COL_ATTENTION));
                activity.setAttendees(rs.getString(DBConstants.ACTIVITY_COL_ATTENDEES));
                lsActivities.add(activity);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return lsActivities;
    }

    private Activity queryById(String strId) {
        Activity activity = null;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strId))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");
        strSelectSQL.append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strId).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                activity = new Activity();
                activity.setId(rs.getString(DBConstants.ACTIVITY_COL_ID));
                activity.setPublisherEmail(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL));
                activity.setPublishBegin(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHBEGIN));
                activity.setPublishEnd(rs.getString(DBConstants.ACTIVITY_COL_PUBLISHEND));
                activity.setLargeActivity(rs.getInt(DBConstants.ACTIVITY_COL_LARGEACTIVITY));
                activity.setEarlyBird(rs.getInt(DBConstants.ACTIVITY_COL_EARLYBIRD));
                activity.setDisplayName(rs.getString(DBConstants.ACTIVITY_COL_DISPLAYNAME));
                activity.setDateBegin(rs.getString(DBConstants.ACTIVITY_COL_DATEBEGIN));
                activity.setDateEnd(rs.getString(DBConstants.ACTIVITY_COL_DATEEND));
                activity.setLocation(rs.getString(DBConstants.ACTIVITY_COL_LOCATION));
                activity.setStatus(rs.getString(DBConstants.ACTIVITY_COL_STATUS));
                activity.setDescription(rs.getString(DBConstants.ACTIVITY_COL_DESCRIPTION));
                activity.setTags(rs.getString(DBConstants.ACTIVITY_COL_TAGS));
                activity.setGoodActivity(rs.getInt(DBConstants.ACTIVITY_COL_GOODACTIVITY));
                activity.setAttention(rs.getInt(DBConstants.ACTIVITY_COL_ATTENTION));
                activity.setAttendees(rs.getString(DBConstants.ACTIVITY_COL_ATTENDEES));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return activity;
    }

    boolean update(Activity activity) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(activity.getId()))
            return false;

        Activity oldActivity = queryById(activity.getId());

        if (oldActivity == null)
            return false;

        fillUpdateActivityIfNull(oldActivity, activity);

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" SET ");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append("=\"").append(activity.getPublisherEmail()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_PUBLISHBEGIN).append("=\"").append(activity.getPublishBegin()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEND).append("=\"").append(activity.getPublishEnd()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append("=\"").append(activity.getLargeActivity()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_EARLYBIRD).append("=\"").append(activity.getEarlyBird()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append("=\"").append(activity.getDisplayName()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DATEBEGIN).append("=\"").append(activity.getDateBegin()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DATEEND).append("=\"").append(activity.getDateEnd()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_LOCATION).append("=\"").append(activity.getLocation()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_STATUS).append("=\"").append(activity.getStatus()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DESCRIPTION).append("=\"").append(activity.getDescription()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_TAGS).append("=\"").append(activity.getTags()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_GOODACTIVITY).append("=\"").append(activity.getGoodActivity()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_ATTENTION).append("=\"").append(activity.getAttention()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append("=\"").append(activity.getAttendees()).append("\"");
        strUpdateSQL.append(" WHERE ").append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(activity.getId());
        strUpdateSQL.append("\" AND ").append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append("=\"").append(activity.getPublisherEmail());
        strUpdateSQL.append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdateActivity = conn.prepareStatement(strUpdateSQL.toString())) {
            bRes = statementUpdateActivity.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    private void fillUpdateActivityIfNull(Activity oldActivity, Activity newActivity) {
        if (newActivity.getId() == null)
            newActivity.setId(oldActivity.getId());
        if (newActivity.getPublisherEmail() == null)
            newActivity.setPublisherEmail(oldActivity.getPublisherEmail());
        if (newActivity.getPublishBegin() == null)
            newActivity.setPublishBegin(oldActivity.getPublishBegin());
        if (newActivity.getPublishEnd() == null)
            newActivity.setPublishEnd(oldActivity.getPublishEnd());
        if (newActivity.getLargeActivity() == null)
            newActivity.setLargeActivity(oldActivity.getLargeActivity());
        if (newActivity.getEarlyBird() == null)
            newActivity.setEarlyBird(oldActivity.getEarlyBird());
        if (newActivity.getDisplayName() == null)
            newActivity.setDisplayName(oldActivity.getDisplayName());
        if (newActivity.getDateBegin() == null)
            newActivity.setDateBegin(oldActivity.getDateBegin());
        if (newActivity.getDateEnd() == null)
            newActivity.setDateEnd(oldActivity.getDateEnd());
        if (newActivity.getLocation() == null)
            newActivity.setLocation(oldActivity.getLocation());
        if (newActivity.getStatus() == null)
            newActivity.setStatus(oldActivity.getStatus());
        if (newActivity.getDescription() == null)
            newActivity.setDescription(oldActivity.getDescription());
        if (newActivity.getTags() == null)
            newActivity.setTags(oldActivity.getTags());
        if (newActivity.getGoodActivity() == null)
            newActivity.setGoodActivity(oldActivity.getGoodActivity());
        if (newActivity.getAttention() == null)
            newActivity.setAttention(oldActivity.getAttention());
        if (newActivity.getAttendees() == null)
            newActivity.setAttendees(oldActivity.getAttendees());
    }
}
