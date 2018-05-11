package com.fff.hos.database;

import com.fff.hos.data.Activity;
import com.fff.hos.tools.DBTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DBCtrlActivity {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlActivity.class.getName());

    private final static String DB_TABLE_NAME = "activities";
    private final static String DB_COL_ID = "id";
    private final static String DB_COL_TS = "ts";
    private final static String DB_COL_PUBLISHEREMAIL = "publisheremail";
    private final static String DB_COL_PUBLISHBEGIN = "publishbegin";
    private final static String DB_COL_PUBLISHEND = "publishend";
    private final static String DB_COL_LARGEACTIVITY = "largeactivity";
    private final static String DB_COL_EARLYBIRD = "earlybird";
    private final static String DB_COL_DISPLAYNAME = "displayname";
    private final static String DB_COL_DATEBEGIN = "datebegin";
    private final static String DB_COL_DATEEND = "dateend";
    private final static String DB_COL_LOCATION = "location";
    private final static String DB_COL_STATUS = "status";
    private final static String DB_COL_IMAGE = "image";
    private final static String DB_COL_DESCRIPTION = "description";
    private final static String DB_COL_TAGS = "tags";
    private final static String DB_COL_GOODACTIVITY = "goodactivity";
    private final static String DB_COL_ATTENTION = "attention";
    private final static String DB_COL_ATTENDEES = "attendees";

    DBCtrlActivity() {
        createTable();
    }

    private void createTable() {
        Connection conn = CloudSQLManager.getConnection();

        String strCreateTableSQL = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + " ( "
                + DB_COL_ID + " SERIAL NOT NULL, "
                + DB_COL_TS + " timestamp NOT NULL, "
                + DB_COL_PUBLISHEREMAIL + " VARCHAR(128) NOT NULL, "
                + DB_COL_PUBLISHBEGIN + " VARCHAR(32) NOT NULL, "
                + DB_COL_PUBLISHEND + " VARCHAR(32) NOT NULL, "
                + DB_COL_LARGEACTIVITY + " TINYINT UNSIGNED NOT NULL DEFAULT 0, "
                + DB_COL_EARLYBIRD + " TINYINT UNSIGNED NOT NULL DEFAULT 0, "
                + DB_COL_DISPLAYNAME + " VARCHAR(64) NOT NULL, "
                + DB_COL_DATEBEGIN + " VARCHAR(32) NOT NULL, "
                + DB_COL_DATEEND + " VARCHAR(32) NOT NULL, "
                + DB_COL_LOCATION + " VARCHAR(128), "
                + DB_COL_STATUS + " CHAR(8), "
                + DB_COL_IMAGE + " VARCHAR(4096), "
                + DB_COL_DESCRIPTION + " VARCHAR(1024), "
                + DB_COL_TAGS + " VARCHAR(128), "
                + DB_COL_GOODACTIVITY + " INT NOT NULL DEFAULT 0, "
                + DB_COL_ATTENTION + " INT UNSIGNED NOT NULL DEFAULT 0, "
                + DB_COL_ATTENDEES + " VARCHAR(4096), "
                + "PRIMARY KEY (" + DB_COL_ID + ") );";

        try {
            conn.createStatement().execute(strCreateTableSQL);
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }
    }

    Activity insert(Activity activity) {
        Activity newActivity = insert(activity.getPublisherEmail(), activity.getPublishBegin(), activity.getPublishEnd()
                , activity.getLargeActivity(), activity.getEarlyBird(), activity.getDisplayName(), activity.getDateBegin()
                , activity.getDateEnd(), activity.getLocation(), activity.getStatus(), activity.getImage()
                , activity.getDescription(), activity.getTags(), activity.getGoodActivity(), activity.getAttention()
                , activity.getAttendees());

        if(newActivity != null)
            fillUpdateActivityIfNull(activity, newActivity);

        return newActivity;
    }

    private Activity insert(String strPublisherEmail, String strPublishBegin, String strPublishEnd, Integer iLargeActivity
            , Integer iEarlyBird,String strDisplayName, String strDateBegin, String strDateEnd, String strLocation
            , String strStatus, String strImage, String strDescription, String strTags, Integer iGoodActivity
            , Integer iAttention, String strAttendees) {

        Activity resActivity = null;

        if (!DBTool.checkStringNotNull(strPublisherEmail)
                || !DBTool.checkStringNotNull(strPublishBegin)
                || !DBTool.checkStringNotNull(strPublishEnd)
                || (iLargeActivity == null)
                || (iEarlyBird == null)
                || !DBTool.checkStringNotNull(strDisplayName)
                || !DBTool.checkStringNotNull(strDateBegin)
                || !DBTool.checkStringNotNull(strDateEnd)
                || !DBTool.checkStringNotNull(strLocation))
            return resActivity;

        Connection conn = CloudSQLManager.getConnection();
        String strCreateActivitySQL = "INSERT INTO " + DB_TABLE_NAME +
                " (" + DB_COL_TS +
                "," + DB_COL_PUBLISHEREMAIL +
                "," + DB_COL_PUBLISHBEGIN +
                "," + DB_COL_PUBLISHEND +
                "," + DB_COL_LARGEACTIVITY +
                "," + DB_COL_EARLYBIRD +
                "," + DB_COL_DISPLAYNAME +
                "," + DB_COL_DATEBEGIN +
                "," + DB_COL_DATEEND +
                "," + DB_COL_LOCATION +
                "," + DB_COL_STATUS +
                "," + DB_COL_IMAGE +
                "," + DB_COL_DESCRIPTION +
                "," + DB_COL_TAGS +
                "," + DB_COL_GOODACTIVITY +
                "," + DB_COL_ATTENTION +
                "," + DB_COL_ATTENDEES + ") " +
                "VALUES (?,\"" + strPublisherEmail + "\"" +
                ",\"" + strPublishBegin + "\"" +
                ",\"" + strPublishEnd + "\"" +
                ",\"" + iLargeActivity + "\"" +
                ",\"" + iEarlyBird + "\"" +
                ",\"" + strDisplayName + "\"" +
                ",\"" + strDateBegin + "\"" +
                ",\"" + strDateEnd + "\"" +
                ",\"" + strLocation + "\"" +
                ",\"" + strStatus + "\"" +
                ",\"" + strImage + "\"" +
                ",\"" + strDescription + "\"" +
                ",\"" + strTags + "\"" +
                ",\"" + (iGoodActivity == null ? 0 : iGoodActivity) + "\"" +
                ",\"" + (iAttention == null ? 0 : iAttention) + "\"" +
                ",\"" + strAttendees + "\"" +
                ");";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementCreateActivity = conn.prepareStatement(strCreateActivitySQL, new String[]{DB_COL_ID})) {
            statementCreateActivity.setTimestamp(1, new Timestamp(new Date().getTime()));

            if(statementCreateActivity.executeUpdate() > 0) {
                ResultSet rs = statementCreateActivity.getGeneratedKeys();
                String strNewActivityID = "";
                if(rs.next())
                    strNewActivityID = rs.getString(1);

                resActivity = new Activity();
                resActivity.setId(strNewActivityID);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return resActivity;
    }

    boolean delete(Activity activity) {
        if (activity == null)
            return false;

        return delete(activity.getId(), activity.getPublisherEmail());
    }

    boolean delete(String strId, String strPublisherEmail) {
        boolean bRes = false;

        if (!DBTool.checkStringNotNull(strId)
                || !DBTool.checkStringNotNull(strPublisherEmail))
            return bRes;

        Connection conn = CloudSQLManager.getConnection();
        String strDeleteActivitySQL = "DELETE FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_ID + "=\"" + strId
                + "\" AND " + DB_COL_PUBLISHEREMAIL + "=\"" + strPublisherEmail +"\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeleteActivity = conn.prepareStatement(strDeleteActivitySQL)) {
            bRes = statementDeleteActivity.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    boolean checkActivityExist(String strId) {
        boolean bRes = false;

        if (!DBTool.checkStringNotNull(strId))
            return bRes;

        Connection conn = CloudSQLManager.getConnection();
        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_ID + "=\"" + strId + "\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementSelectPerson = conn.prepareStatement(strSelectSQL)) {
            bRes = statementSelectPerson.executeQuery().first();
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("check time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    List<Activity> queryByIds(String strIds) {
        List<Activity> lsActivities = new ArrayList<>();

        if (!DBTool.checkStringNotNull(strIds))
            return lsActivities;

        Connection conn = CloudSQLManager.getConnection();
        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_ID + " IN (" + strIds + ");";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                Activity activity = new Activity();
                activity.setId(rs.getString(DB_COL_ID));
                activity.setPublisherEmail(rs.getString(DB_COL_PUBLISHEREMAIL));
                activity.setPublishBegin(rs.getString(DB_COL_PUBLISHBEGIN));
                activity.setPublishEnd(rs.getString(DB_COL_PUBLISHEND));
                activity.setLargeActivity(rs.getInt(DB_COL_LARGEACTIVITY));
                activity.setEarlyBird(rs.getInt(DB_COL_EARLYBIRD));
                activity.setDisplayName(rs.getString(DB_COL_DISPLAYNAME));
                activity.setDateBegin(rs.getString(DB_COL_DATEBEGIN));
                activity.setDateEnd(rs.getString(DB_COL_DATEEND));
                activity.setLocation(rs.getString(DB_COL_LOCATION));
                activity.setStatus(rs.getString(DB_COL_STATUS));
                activity.setImage(rs.getString(DB_COL_IMAGE));
                activity.setDescription(rs.getString(DB_COL_DESCRIPTION));
                activity.setTags(rs.getString(DB_COL_TAGS));
                activity.setGoodActivity(rs.getInt(DB_COL_GOODACTIVITY));
                activity.setAttention(rs.getInt(DB_COL_ATTENTION));
                activity.setAttendees(rs.getString(DB_COL_ATTENDEES));
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

        if (!DBTool.checkStringNotNull(strId))
            return null;

        Connection conn = CloudSQLManager.getConnection();
        String strSelectSQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COL_ID + "=\"" + strId + "\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                activity = new Activity();
                activity.setId(rs.getString(DB_COL_ID));
                activity.setPublisherEmail(rs.getString(DB_COL_PUBLISHEREMAIL));
                activity.setPublishBegin(rs.getString(DB_COL_PUBLISHBEGIN));
                activity.setPublishEnd(rs.getString(DB_COL_PUBLISHEND));
                activity.setLargeActivity(rs.getInt(DB_COL_LARGEACTIVITY));
                activity.setEarlyBird(rs.getInt(DB_COL_EARLYBIRD));
                activity.setDisplayName(rs.getString(DB_COL_DISPLAYNAME));
                activity.setDateBegin(rs.getString(DB_COL_DATEBEGIN));
                activity.setDateEnd(rs.getString(DB_COL_DATEEND));
                activity.setLocation(rs.getString(DB_COL_LOCATION));
                activity.setStatus(rs.getString(DB_COL_STATUS));
                activity.setImage(rs.getString(DB_COL_IMAGE));
                activity.setDescription(rs.getString(DB_COL_DESCRIPTION));
                activity.setTags(rs.getString(DB_COL_TAGS));
                activity.setGoodActivity(rs.getInt(DB_COL_GOODACTIVITY));
                activity.setAttention(rs.getInt(DB_COL_ATTENTION));
                activity.setAttendees(rs.getString(DB_COL_ATTENDEES));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return activity;
    }

    boolean update(Activity activity) {
        boolean bRes = false;

        if (!DBTool.checkStringNotNull(activity.getId()))
            return bRes;

        Activity oldActivity = queryById(activity.getId());

        if (oldActivity == null)
            return bRes;

        fillUpdateActivityIfNull(oldActivity, activity);

        Connection conn = CloudSQLManager.getConnection();
        String strUpdateSQL = "UPDATE " + DB_TABLE_NAME + " SET "
                + DB_COL_PUBLISHEREMAIL + "=\"" + activity.getPublisherEmail() + "\","
                + DB_COL_PUBLISHBEGIN + "=\"" + activity.getPublishBegin() + "\","
                + DB_COL_PUBLISHEND + "=\"" + activity.getPublishEnd() + "\","
                + DB_COL_LARGEACTIVITY + "=\"" + activity.getLargeActivity() + "\","
                + DB_COL_EARLYBIRD + "=\"" + activity.getEarlyBird() + "\","
                + DB_COL_DISPLAYNAME + "=\"" + activity.getDisplayName() + "\","
                + DB_COL_DATEBEGIN + "=\"" + activity.getDateBegin() + "\","
                + DB_COL_DATEEND + "=\"" + activity.getDateEnd() + "\","
                + DB_COL_LOCATION + "=\"" + activity.getLocation() + "\","
                + DB_COL_STATUS + "=\"" + activity.getStatus() + "\","
                + DB_COL_IMAGE + "=\"" + activity.getImage() + "\","
                + DB_COL_DESCRIPTION + "=\"" + activity.getDescription() + "\","
                + DB_COL_TAGS + "=\"" + activity.getTags() + "\","
                + DB_COL_GOODACTIVITY + "=\"" + activity.getGoodActivity() + "\","
                + DB_COL_ATTENTION + "=\"" + activity.getAttention() + "\","
                + DB_COL_ATTENDEES + "=\"" + activity.getAttendees() + "\" "
                + " WHERE " + DB_COL_ID + "=\"" + activity.getId()
                + "\" AND " + DB_COL_PUBLISHEREMAIL + "=\"" + activity.getPublisherEmail() +"\";";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdateActivity = conn.prepareStatement(strUpdateSQL)) {
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
        if (newActivity.getImage() == null)
            newActivity.setImage(oldActivity.getImage());
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
