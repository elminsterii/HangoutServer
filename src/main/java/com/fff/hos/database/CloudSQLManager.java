package com.fff.hos.database;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Person;
import com.fff.hos.tools.SysRunTool;
import com.google.apphosting.api.ApiProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//TODO - multi-thread and thread-safe.
public class CloudSQLManager {
    private static final Logger LOGGER = Logger.getLogger(CloudSQLManager.class.getName());

    private static CloudSQLManager m_instance = null;
    private static Connection conn = null;

    private DBCtrlPerson m_dbCtrlPerson = null;
    private DBCtrlActivity m_dbCtrlActivity = null;

    private CloudSQLManager() {

    }

    public static CloudSQLManager getInstance() {
        if (m_instance == null) {
            m_instance = new CloudSQLManager();
            m_instance.initialize();
        }
        return m_instance;
    }

    static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
                Map<String, Object> attr = env.getAttributes();
                String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

                String strURL;
                if (hostname.contains("localhost:")) {
                    SysRunTool.sysRunMySQL();

                    strURL = System.getProperty("sqlLocal")
                            + System.getProperty("sqlDBName") + "?"
                            + "useSSL=false&"
                            + "user=" + System.getProperty("sqlUserName")
                            + "&password=" + System.getProperty("sqlUserPassword");
                } else {
                    strURL = System.getProperty("sqlCloud")
                            + System.getProperty("sqlInsConnName") + "/"
                            + System.getProperty("sqlDBName") + "?"
                            + "user=" + System.getProperty("sqlUserName")
                            + "&password=" + System.getProperty("sqlUserPassword");
                }

                LOGGER.warning("connecting to: " + strURL);
                try {
                    conn = DriverManager.getConnection(strURL);
                } catch (SQLException e) {
                    LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            LOGGER.warning("Unable to connect to Cloud SQL, " + e.getMessage());
        }

        return conn;
    }

    private void initialize() {
        m_dbCtrlPerson = new DBCtrlPerson();
        m_dbCtrlActivity = new DBCtrlActivity();
    }

    // ---------------------------- person control ----------------------------
    public boolean register(Person person) {
        return m_dbCtrlPerson.insert(person);
    }

    public boolean unregister(Person person) {
        return m_dbCtrlPerson.delete(person);
    }

    public Person queryPerson(Person person) {
        return m_dbCtrlPerson.query(person);
    }

    public boolean updatePerson(Person person) {
        return m_dbCtrlPerson.update(person);
    }

    public Person login(Person person) {
        Person resPerson = null;

        if (m_dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = m_dbCtrlPerson.query(person.getEmail());
            resPerson.setOnline(1);
            m_dbCtrlPerson.update(resPerson);
        }
        return resPerson;
    }

    public Person logout(Person person) {
        Person resPerson = null;

        if (m_dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword())) {
            resPerson = m_dbCtrlPerson.query(person.getEmail());
            resPerson.setOnline(0);
            m_dbCtrlPerson.update(resPerson);
        }
        return resPerson;
    }

    public boolean checkPersonValid(String strEmail, String strUserPassword) {
        return m_dbCtrlPerson.checkPersonValid(strEmail, strUserPassword);
    }

    public boolean checkPersonValid(Person person) {
        return m_dbCtrlPerson.checkPersonValid(person.getEmail(), person.getUserPassword());
    }

    public boolean checkPersonExist(Person person) {
        return m_dbCtrlPerson.checkPersonExist(person.getEmail());
    }

    // ---------------------------- Activity control ----------------------------
    public Activity createActivity(Activity activity) {
        return m_dbCtrlActivity.insert(activity);
    }

    public boolean deleteActivity(Activity activity) {
        return m_dbCtrlActivity.delete(activity);
    }

    public Activity queryActivityById(String strId) {
        return m_dbCtrlActivity.queryById(strId);
    }

    public List<Activity> queryActivityByIds(String strIds) {
        return m_dbCtrlActivity.queryByIds(strIds);
    }

//    public Activity queryActivity(Activity activity) {
//        return m_dbCtrlActivity.query(activity);
//    }

    public boolean updateActivity(Activity activity) {
        return m_dbCtrlActivity.update(activity);
    }

    public boolean checkActivityExist(Activity activity) {
        return m_dbCtrlActivity.checkActivityExist(activity.getId());
    }
}
