package com.fff.hos.database;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Comment;
import com.fff.hos.data.Person;

import java.util.List;

public class DatabaseManager {

    private DBCtrlPerson m_dbCtrlPerson = null;
    private DBCtrlActivity m_dbCtrlActivity = null;
    private DBCtrlComment m_dbCtrlComment = null;
    private DBCtrlVerifyEmail m_dbCtrlVerifyEmail = null;

    public DatabaseManager() { }

    // --------------------------------- Person control functions ---------------------------------
    public boolean register(Person person) {
        return getDBCtrlPerson().insert(person);
    }

    public boolean unregister(Person person) {
        return getDBCtrlPerson().delete(person);
    }

    public Person queryPerson(String strId, String strEmail) {
        return getDBCtrlPerson().query(strId, strEmail);
    }

    public Person queryPerson(Person person) {
        return getDBCtrlPerson().query(person);
    }

    public boolean updatePerson(Person person) {
        return getDBCtrlPerson().update(person);
    }

    public boolean deemPerson(String strEmail, Integer iDeem, Integer iDeemRb) {
        return getDBCtrlPerson().deem(strEmail, iDeem, iDeemRb);
    }

    public Person login(Person person) {
        DBCtrlPerson dbCtrlPerson = getDBCtrlPerson();
        Person resPerson = null;

        if (dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = dbCtrlPerson.query(null, person.getEmail());
            resPerson.setOnline(1);
            dbCtrlPerson.update(resPerson);
        }
        return resPerson;
    }

    public boolean logout(Person person) {
        DBCtrlPerson dbCtrlPerson = getDBCtrlPerson();
        Person resPerson = null;

        if (dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = dbCtrlPerson.query(null, person.getEmail());
            resPerson.setOnline(0);
            dbCtrlPerson.update(resPerson);
        }
        return resPerson != null;
    }

    public boolean checkPersonValid(String strEmail, String strUserPassword) {
        return getDBCtrlPerson().checkPersonValid(strEmail, strUserPassword);
    }

    public boolean checkPersonValid(Person person) {
        return getDBCtrlPerson().checkPersonValid(person.getEmail(), person.getUserPassword());
    }

    public boolean checkPersonExist(Person person) {
        return getDBCtrlPerson().checkPersonExist(person.getEmail());
    }

    public boolean checkPersonExist(String strEmail) {
        return getDBCtrlPerson().checkPersonExist(strEmail);
    }


    // --------------------------------- Activity control functions ---------------------------------
    public String createActivity(Activity activity) {
        return getDBCtrlActivity().insert(activity);
    }

    public boolean deleteActivity(Activity activity) {
        return getDBCtrlActivity().delete(activity);
    }

    public List<Activity> queryActivityByIds(String strIds) {
        return getDBCtrlActivity().queryByIds(strIds);
    }

    public List<Activity> queryAll() {
        return getDBCtrlActivity().queryAll();
    }

    public List<String> queryActivity(Activity activity) {
        return getDBCtrlActivity().query(activity);
    }

    public boolean updateActivity(Activity activity) {
        return getDBCtrlActivity().update(activity);
    }

    public boolean attendActivity(String strActivityId, Integer iAttend, String strPersonId) {
        return getDBCtrlActivity().attend(strActivityId, iAttend, strPersonId);
    }

    public boolean deemActivity(String strActivityId, Integer iDeem, Integer iDeemRb) {
        return getDBCtrlActivity().deem(strActivityId, iDeem, iDeemRb);
    }

    public boolean checkActivityExist(Activity activity) {
        return getDBCtrlActivity().checkActivityExist(activity.getId());
    }

    public boolean checkActivityExist(String strId) {
        return getDBCtrlActivity().checkActivityExist(strId);
    }


    // --------------------------------- Comment control functions ---------------------------------
    public String createComment(Comment comment) {
        return getDBCtrlComment().insert(comment);
    }

    public boolean deleteComment(Comment comment) {
        return getDBCtrlComment().delete(comment);
    }

    public List<Comment> queryCommentByIds(String strIds) {
        return getDBCtrlComment().queryByIds(strIds);
    }

    public List<String> queryComment(Comment comment) {
        return getDBCtrlComment().query(comment);
    }

    public boolean updateComment(Comment comment) {
        return getDBCtrlComment().update(comment);
    }


    // --------------------------------- Comment control functions ---------------------------------
    public boolean createVerifyEmail(String strEmail, String strCode) {
        return getDBCtrlVerifyEmail().insert(strEmail, strCode);
    }

    public boolean deleteVerifyEmail(String strEmail) {
        return getDBCtrlVerifyEmail().delete(strEmail);
    }

    public boolean clearVerifyEmails() {
        return getDBCtrlVerifyEmail().deleteAll();
    }

    public boolean isVerifyEmailExist(String strEmail) {
        return getDBCtrlVerifyEmail().isEmailExist(strEmail);
    }

    public boolean updateVerifyEmail(String strEmail, String strCode) {
        return getDBCtrlVerifyEmail().update(strEmail, strCode);
    }

    public boolean verifyEmail(String strEmail, String strCode) {
        return getDBCtrlVerifyEmail().verify(strEmail, strCode);
    }


    // ---------------------------- Database Controller getter functions ----------------------------
    private DBCtrlPerson getDBCtrlPerson() {
        if(m_dbCtrlPerson == null)
            m_dbCtrlPerson = new DBCtrlPerson();
        return m_dbCtrlPerson;
    }

    private DBCtrlActivity getDBCtrlActivity() {
        if(m_dbCtrlActivity == null)
            m_dbCtrlActivity = new DBCtrlActivity();
        return m_dbCtrlActivity;
    }

    private DBCtrlComment getDBCtrlComment() {
        if(m_dbCtrlComment == null)
            m_dbCtrlComment = new DBCtrlComment();
        return m_dbCtrlComment;
    }

    private DBCtrlVerifyEmail getDBCtrlVerifyEmail() {
        if(m_dbCtrlVerifyEmail == null)
            m_dbCtrlVerifyEmail = new DBCtrlVerifyEmail();
        return m_dbCtrlVerifyEmail;
    }
}
