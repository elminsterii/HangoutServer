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
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_STATUS).append(" TINYINT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_DESCRIPTION).append(" VARCHAR(1024), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_TAGS).append(" VARCHAR(128), ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_GOOD).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_NOGOOD).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_ATTENTION).append(" INT UNSIGNED NOT NULL DEFAULT 0, ");
            strCreateTableSQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append(" VARCHAR(1024) DEFAULT \'\', ");
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
                , activity.getTags());
    }

    @SuppressWarnings("Duplicates")
    private String insert(String strPublisherEmail, String strPublishBegin, String strPublishEnd, Integer iLargeActivity
            , Integer iEarlyBird, String strDisplayName, String strDateBegin, String strDateEnd, String strLocation
            , Integer iStatus, String strDescription, String strTags) {

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
                || !stringTool.checkStringNotNull(strLocation)
                || (iStatus == null))
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
        strCreateActivitySQL.append(DBConstants.ACTIVITY_COL_TAGS);
        strCreateActivitySQL.append(") VALUES (?,");
        strCreateActivitySQL.append("\"").append(strPublisherEmail).append("\",");
        strCreateActivitySQL.append("\"").append(strPublishBegin).append("\",");
        strCreateActivitySQL.append("\"").append(strPublishEnd).append("\",");
        strCreateActivitySQL.append(iLargeActivity).append(",");
        strCreateActivitySQL.append(iEarlyBird).append(",");
        strCreateActivitySQL.append("\"").append(strDisplayName).append("\",");
        strCreateActivitySQL.append("\"").append(strDateBegin).append("\",");
        strCreateActivitySQL.append("\"").append(strDateEnd).append("\",");
        strCreateActivitySQL.append("\"").append(strLocation).append("\",");
        strCreateActivitySQL.append(iStatus).append(",");
        strCreateActivitySQL.append("\"").append(strDescription == null ? "" : strDescription).append("\",");
        strCreateActivitySQL.append("\"").append(strTags == null ? "" : strTags).append("\"");
        strCreateActivitySQL.append(");");

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

        if(stringTool.checkStringNotNull(strId)) {
            strDeleteActivitySQL.append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strId);

            if(stringTool.checkStringNotNull(strPublisherEmail))
                strDeleteActivitySQL.append("\" AND ");
        }

        if(stringTool.checkStringNotNull(strPublisherEmail))
            strDeleteActivitySQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append("=\"").append(strPublisherEmail);

        strDeleteActivitySQL.append("\";");

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

        if(stringTool.checkStringNotNull(activity.getPublisherEmail())) {
            strSelectSQL.append(DBConstants.ACTIVITY_COL_PUBLISHEREMAIL).append("=\"").append(activity.getPublisherEmail()).append("\"");
            activity.setPublisherEmail(null);

            if(activity.checkMembersStillHaveValue())
                strSelectSQL.append(" AND ");
        }

        if(activity.getLargeActivity() != null) {
            strSelectSQL.append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append("=").append(activity.getLargeActivity());
            activity.setLargeActivity(null);

            if(activity.checkMembersStillHaveValue())
                strSelectSQL.append(" AND ");
        }

        if(activity.getEarlyBird() != null) {
            strSelectSQL.append(DBConstants.ACTIVITY_COL_EARLYBIRD).append("=").append(activity.getEarlyBird());
            activity.setEarlyBird(null);

            if(activity.checkMembersStillHaveValue())
                strSelectSQL.append(" AND ");
        }

        if(stringTool.checkStringNotNull(activity.getDisplayName())) {
            strSelectSQL.append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append("=\"").append(activity.getDisplayName()).append("\"");
            activity.setDisplayName(null);

            if(activity.checkMembersStillHaveValue())
                strSelectSQL.append(" AND ");
        }

        if(stringTool.checkStringNotNull(activity.getTags())) {
            String strRegExp = stringTool.strTagsToRegExp(activity.getTags());
            strSelectSQL.append(DBConstants.ACTIVITY_COL_TAGS).append(" REGEXP \'").append(strRegExp).append("\'");
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
                activity.setStatus(rs.getInt(DBConstants.ACTIVITY_COL_STATUS));
                activity.setDescription(rs.getString(DBConstants.ACTIVITY_COL_DESCRIPTION));
                activity.setTags(rs.getString(DBConstants.ACTIVITY_COL_TAGS));
                activity.setGood(rs.getInt(DBConstants.ACTIVITY_COL_GOOD));
                activity.setNoGood(rs.getInt(DBConstants.ACTIVITY_COL_NOGOOD));
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
                activity.setStatus(rs.getInt(DBConstants.ACTIVITY_COL_STATUS));
                activity.setDescription(rs.getString(DBConstants.ACTIVITY_COL_DESCRIPTION));
                activity.setTags(rs.getString(DBConstants.ACTIVITY_COL_TAGS));
                activity.setGood(rs.getInt(DBConstants.ACTIVITY_COL_GOOD));
                activity.setNoGood(rs.getInt(DBConstants.ACTIVITY_COL_NOGOOD));
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
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_LARGEACTIVITY).append("=").append(activity.getLargeActivity()).append(",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_EARLYBIRD).append("=").append(activity.getEarlyBird()).append(",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DISPLAYNAME).append("=\"").append(activity.getDisplayName()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DATEBEGIN).append("=\"").append(activity.getDateBegin()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DATEEND).append("=\"").append(activity.getDateEnd()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_LOCATION).append("=\"").append(activity.getLocation()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_STATUS).append("=").append(activity.getStatus()).append(",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_DESCRIPTION).append("=\"").append(activity.getDescription()).append("\",");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_TAGS).append("=\"").append(activity.getTags()).append("\"");
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

    boolean attend(String strActivityId, Integer iAttend, String strPersonId) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strActivityId)
                || iAttend == null
                || !stringTool.checkStringNotNull(strPersonId))
            return false;

        final int INT_ATTENDED = 1;

        String strNewAttendees = queryAttendees(strActivityId);

        //handle attendees string.
        if(iAttend == INT_ATTENDED) {
            if(stringTool.checkStringNotNull(strNewAttendees)) {
                String[] arrAttendees = strNewAttendees.split(",");
                for (String strAttendee : arrAttendees) {
                    if (strAttendee.equals(strPersonId))
                        return false;
                }
                strNewAttendees = strNewAttendees + "," + strPersonId;
            }
            else
                strNewAttendees = strPersonId;
        } else {
            if(stringTool.checkStringNotNull(strNewAttendees)) {
                String[] arrAttendees = strNewAttendees.split(",");
                for(int i=0; i< arrAttendees.length; i++) {
                    if(arrAttendees[i].equals(strPersonId)) {
                        arrAttendees[i] = null;
                        break;
                    }
                }
                strNewAttendees = stringTool.arrayStringToString(arrAttendees, ',');
            } else
                return false;
        }

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" SET ");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_ATTENTION).append("=").append(DBConstants.ACTIVITY_COL_ATTENTION);
        strUpdateSQL.append(iAttend == INT_ATTENDED ? "+1," : "-1,");
        strUpdateSQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append("=\"").append(strNewAttendees).append("\"");
        strUpdateSQL.append(" WHERE ").append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strActivityId);
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

    boolean deem(String strActivityId, Integer iDeem, Integer iDeemRb) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strActivityId)
                || iDeem == null)
            return false;

        final int INT_DEEM_GOOD = 1;
        final int INT_DEEM_DO_ROLLBACK = 1;

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" SET ");

        if(iDeem == INT_DEEM_GOOD) {
            strUpdateSQL.append(DBConstants.ACTIVITY_COL_GOOD).append("=");
            strUpdateSQL.append(DBConstants.ACTIVITY_COL_GOOD).append(iDeemRb == INT_DEEM_DO_ROLLBACK ? "-1" : "+1");
        }
        else {
            strUpdateSQL.append(DBConstants.ACTIVITY_COL_NOGOOD).append("=");
            strUpdateSQL.append(DBConstants.ACTIVITY_COL_NOGOOD).append(iDeemRb == INT_DEEM_DO_ROLLBACK ? "-1" : "+1");
        }

        strUpdateSQL.append(" WHERE ").append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strActivityId);
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

    private String queryAttendees(String strActivityId) {
        String strAttendees = "";

        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strActivityId))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT ");
        strSelectSQL.append(DBConstants.ACTIVITY_COL_ATTENDEES).append(" FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_ACTIVITY).append(" WHERE ");
        strSelectSQL.append(DBConstants.ACTIVITY_COL_ID).append("=\"").append(strActivityId).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                strAttendees = rs.getString(DBConstants.ACTIVITY_COL_ATTENDEES);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return strAttendees;
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
        if (newActivity.getGood() == null)
            newActivity.setGood(oldActivity.getGood());
        if (newActivity.getNoGood() == null)
            newActivity.setNoGood(oldActivity.getNoGood());
        if (newActivity.getAttention() == null)
            newActivity.setAttention(oldActivity.getAttention());
        if (newActivity.getAttendees() == null)
            newActivity.setAttendees(oldActivity.getAttendees());
    }
}
