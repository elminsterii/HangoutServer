package com.fff.hos.database;

import com.fff.hos.data.Comment;
import com.fff.hos.tools.StringTool;
import com.google.common.base.Stopwatch;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class DBCtrlComment {
    private static final Logger LOGGER = Logger.getLogger(DBCtrlComment.class.getName());

    DBCtrlComment() {
        createTable();
    }

    private void createTable() {

        if(!checkTableExist()) {
            Connection conn = DBConnection.getConnection();

            StringBuffer strCreateTableSQL = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
            strCreateTableSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" ( ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_ID).append(" SERIAL NOT NULL, ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_TS).append(" timestamp NOT NULL, ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_PUBLISHEREMAIL).append(" VARCHAR(128) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_DISPLAYNAME).append(" VARCHAR(64) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_ACTIVITYID).append(" VARCHAR(16) NOT NULL, ");
            strCreateTableSQL.append(DBConstants.COMMENT_COL_CONTENT).append(" VARCHAR(512) NOT NULL, ");
            strCreateTableSQL.append("PRIMARY KEY (").append(DBConstants.COMMENT_COL_ID).append(") );");

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
            ResultSet rs = metaData.getTables(null, null, DBConstants.TABLE_NAME_COMMENT,
                    new String[]{"TABLE"});
            bIsExist = rs.first();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return bIsExist;
    }

    String insert(Comment comment) {
        return insert(comment.getPublisherEmail(), comment.getDisplayName(), comment.getActivityId(), comment.getContent());
    }

    @SuppressWarnings("Duplicates")
    private String insert(String strPublisherEmail, String strDisplayName, String strActivityId, String strContent) {

        String strResId = null;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strPublisherEmail)
                || !stringTool.checkStringNotNull(strDisplayName)
                || !stringTool.checkStringNotNull(strActivityId)
                || !stringTool.checkStringNotNull(strContent))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strCreateCommentSQL = new StringBuilder( "INSERT INTO ");
        strCreateCommentSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" (");
        strCreateCommentSQL.append(DBConstants.COMMENT_COL_TS).append(",");
        strCreateCommentSQL.append(DBConstants.COMMENT_COL_PUBLISHEREMAIL).append(",");
        strCreateCommentSQL.append(DBConstants.COMMENT_COL_DISPLAYNAME).append(",");
        strCreateCommentSQL.append(DBConstants.COMMENT_COL_ACTIVITYID).append(",");
        strCreateCommentSQL.append(DBConstants.COMMENT_COL_CONTENT).append(") VALUES (?,");
        strCreateCommentSQL.append("\"").append(strPublisherEmail).append("\",");
        strCreateCommentSQL.append("\"").append(strDisplayName).append("\",");
        strCreateCommentSQL.append("\"").append(strActivityId).append("\",");
        strCreateCommentSQL.append("\"").append(strContent).append("\");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementCreateComment = conn.prepareStatement(strCreateCommentSQL.toString()
                , new String[]{DBConstants.COMMENT_COL_ID})) {
            statementCreateComment.setTimestamp(1, new Timestamp(new Date().getTime()));

            if(statementCreateComment.executeUpdate() > 0) {
                ResultSet rs = statementCreateComment.getGeneratedKeys();
                String strNewCommentID = "";
                if(rs.next())
                    strNewCommentID = rs.getString(1);

                strResId = strNewCommentID;
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("insert time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return strResId;
    }

    boolean delete(Comment comment) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(comment.getId())
                && !stringTool.checkStringNotNull(comment.getActivityId())
                && !stringTool.checkStringNotNull(comment.getPublisherEmail()))
            return false;

        Connection conn = DBConnection.getConnection();
        StringBuilder strDeleteCommentSQL = new StringBuilder("DELETE FROM ");
        strDeleteCommentSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" WHERE ");

        if(stringTool.checkStringNotNull(comment.getId())) {
            strDeleteCommentSQL.append(DBConstants.COMMENT_COL_ID).append("=\"").append(comment.getId());
            comment.setId(null);

            if(comment.checkMembersStillHaveValue())
                strDeleteCommentSQL.append("\" AND ");
        }

        if(stringTool.checkStringNotNull(comment.getActivityId())) {
            strDeleteCommentSQL.append(DBConstants.COMMENT_COL_ACTIVITYID).append("=\"").append(comment.getActivityId());
            comment.setActivityId(null);

            if(comment.checkMembersStillHaveValue())
                strDeleteCommentSQL.append("\" AND ");
        }

        if(stringTool.checkStringNotNull(comment.getPublisherEmail()))
            strDeleteCommentSQL.append(DBConstants.COMMENT_COL_PUBLISHEREMAIL).append("=\"").append(comment.getPublisherEmail());

        strDeleteCommentSQL.append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementDeleteComment = conn.prepareStatement(strDeleteCommentSQL.toString())) {
            bRes = statementDeleteComment.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("delete time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    List<String> query(Comment comment) {
        List<String> lsIds = new ArrayList<>();

        if (comment == null)
            return lsIds;

        StringTool stringTool = new StringTool();

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder();
        strSelectSQL.append("SELECT * FROM ").append(DBConstants.TABLE_NAME_COMMENT).append(" WHERE ");

        if(stringTool.checkStringNotNull(comment.getPublisherEmail())) {
            strSelectSQL.append(DBConstants.COMMENT_COL_PUBLISHEREMAIL).append("=\"").append(comment.getPublisherEmail()).append("\"");
            comment.setPublisherEmail(null);

            if(comment.checkMembersStillHaveValue())
                strSelectSQL.append(" AND ");
        }

        if(stringTool.checkStringNotNull(comment.getActivityId()))
            strSelectSQL.append(DBConstants.COMMENT_COL_ACTIVITYID).append("=\"").append(comment.getActivityId()).append("\"");

        strSelectSQL.append(";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                lsIds.add(rs.getString(DBConstants.COMMENT_COL_ID));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return lsIds;
    }

    List<Comment> queryByIds(String strIds) {
        List<Comment> lsComments = new ArrayList<>();
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strIds))
            return lsComments;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" WHERE ");
        strSelectSQL.append(DBConstants.COMMENT_COL_ID).append(" IN (").append(strIds).append(");");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getString(DBConstants.COMMENT_COL_ID));
                comment.setTs(rs.getString(DBConstants.COMMENT_COL_TS));
                comment.setPublisherEmail(rs.getString(DBConstants.COMMENT_COL_PUBLISHEREMAIL));
                comment.setDisplayName(rs.getString(DBConstants.COMMENT_COL_DISPLAYNAME));
                comment.setActivityId(rs.getString(DBConstants.COMMENT_COL_ACTIVITYID));
                comment.setContent(rs.getString(DBConstants.COMMENT_COL_CONTENT));
                lsComments.add(comment);
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return lsComments;
    }

    private Comment queryById(String strId) {
        Comment comment = null;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strId))
            return null;

        Connection conn = DBConnection.getConnection();
        StringBuilder strSelectSQL = new StringBuilder("SELECT * FROM ");
        strSelectSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" WHERE ");
        strSelectSQL.append(DBConstants.COMMENT_COL_ID).append("=\"").append(strId).append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (ResultSet rs = conn.prepareStatement(strSelectSQL.toString()).executeQuery()) {
            stopwatch.stop();

            while (rs.next()) {
                comment = new Comment();
                comment.setId(rs.getString(DBConstants.COMMENT_COL_ID));
                comment.setTs(rs.getString(DBConstants.COMMENT_COL_TS));
                comment.setPublisherEmail(rs.getString(DBConstants.COMMENT_COL_PUBLISHEREMAIL));
                comment.setDisplayName(rs.getString(DBConstants.COMMENT_COL_DISPLAYNAME));
                comment.setActivityId(rs.getString(DBConstants.COMMENT_COL_ACTIVITYID));
                comment.setContent(rs.getString(DBConstants.COMMENT_COL_CONTENT));
            }
        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("query time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return comment;
    }

    boolean update(Comment comment) {
        boolean bRes = false;
        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(comment.getId()))
            return false;

        Comment oldComment = queryById(comment.getId());

        if (oldComment == null)
            return false;

        fillUpdateCommentIfNull(oldComment, comment);

        Connection conn = DBConnection.getConnection();
        StringBuilder strUpdateSQL = new StringBuilder("UPDATE ");
        strUpdateSQL.append(DBConstants.TABLE_NAME_COMMENT).append(" SET ");
        strUpdateSQL.append(DBConstants.COMMENT_COL_TS).append("=\"").append(new Timestamp(new Date().getTime()).toString()).append("\",");
        strUpdateSQL.append(DBConstants.COMMENT_COL_CONTENT).append("=\"").append(comment.getContent()).append("\"");
        strUpdateSQL.append(" WHERE ").append(DBConstants.COMMENT_COL_ID).append("=\"").append(comment.getId());
        strUpdateSQL.append("\";");

        Stopwatch stopwatch = Stopwatch.createStarted();
        try (PreparedStatement statementUpdateComment = conn.prepareStatement(strUpdateSQL.toString())) {
            bRes = statementUpdateComment.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.warning("SQL erro, " + e.getMessage());
        }

        LOGGER.info("update time (ms):" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return bRes;
    }

    private void fillUpdateCommentIfNull(Comment oldComment, Comment newComment) {
        if (newComment.getId() == null)
            newComment.setId(oldComment.getId());
        if (newComment.getPublisherEmail() == null)
            newComment.setPublisherEmail(oldComment.getPublisherEmail());
        if (newComment.getDisplayName() == null)
            newComment.setDisplayName(oldComment.getDisplayName());
        if (newComment.getActivityId() == null)
            newComment.setActivityId(oldComment.getActivityId());
        if (newComment.getContent() == null)
            newComment.setContent(oldComment.getContent());
    }
}
