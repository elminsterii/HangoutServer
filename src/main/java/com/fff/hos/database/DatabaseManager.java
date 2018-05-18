package com.fff.hos.database;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Person;

import java.util.List;

public class DatabaseManager {

    private DBCtrlPerson m_dbCtrlPerson = null;
    private DBCtrlActivity m_dbCtrlActivity = null;

    public DatabaseManager() { }

    // --------------------------------- Person control functions ---------------------------------
    public boolean register(Person person) {
        return getDBCtrlPerson().insert(person);
    }

    public boolean unregister(Person person) {
        return getDBCtrlPerson().delete(person);
    }

    public Person queryPerson(String strEmail) {
        return getDBCtrlPerson().query(strEmail);
    }

    public Person queryPerson(Person person) {
        return getDBCtrlPerson().query(person);
    }

    public boolean updatePerson(Person person) {
        return getDBCtrlPerson().update(person);
    }

    public boolean login(Person person) {
        DBCtrlPerson dbCtrlPerson = getDBCtrlPerson();
        Person resPerson = null;

        if (dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = dbCtrlPerson.query(person.getEmail());
            resPerson.setOnline(1);
            dbCtrlPerson.update(resPerson);
        }
        return resPerson != null;
    }

    public boolean logout(Person person) {
        DBCtrlPerson dbCtrlPerson = getDBCtrlPerson();
        Person resPerson = null;

        if (dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = dbCtrlPerson.query(person.getEmail());
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
    public Activity createActivity(Activity activity) {
        return getDBCtrlActivity().insert(activity);
    }

    public boolean deleteActivity(Activity activity) {
        return getDBCtrlActivity().delete(activity);
    }

    public List<Activity> queryActivityByIds(String strIds) {
        return getDBCtrlActivity().queryByIds(strIds);
    }

    public List<String> queryActivity(Activity activity) {
        return getDBCtrlActivity().query(activity);
    }

    public boolean updateActivity(Activity activity) {
        return getDBCtrlActivity().update(activity);
    }

    public boolean checkActivityExist(Activity activity) {
        return getDBCtrlActivity().checkActivityExist(activity.getId());
    }

    public boolean checkActivityExist(String strId) {
        return getDBCtrlActivity().checkActivityExist(strId);
    }

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
}
