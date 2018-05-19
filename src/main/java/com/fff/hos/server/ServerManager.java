package com.fff.hos.server;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Person;
import com.fff.hos.database.DatabaseManager;
import com.fff.hos.gcs.StorageManager;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private DatabaseManager m_dbMgr = null;
    private StorageManager m_csMgr = null;

    // --------------------------------- Person control functions ---------------------------------
    public ServerResponse register(Person person) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(person != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if (!dbMgr.checkPersonExist(person)) {
                if (dbMgr.register(person)) {
                    //create storage on GCS for store user's icons.
                    StorageManager csMgr = getStorageManager();
                    csMgr.createPersonStorage(person.getEmail());

                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_EXIST;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse unregister(Person person) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(person != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if (dbMgr.checkPersonValid(person)) {
                if (dbMgr.unregister(person)) {
                    //delete all icons obelong the user on GCS after unregister success.
                    StorageManager csManager = getStorageManager();
                    csManager.deletePersonIcons(person.getEmail(), true);

                    //delete all images belong the activity after unregister success.
                    Activity activity = new Activity();
                    activity.setPublisherEmail(person.getEmail());
                    List<String> lsIds = dbMgr.queryActivity(activity);

                    if(lsIds != null && !lsIds.isEmpty()) {
                        for(String strId : lsIds)
                            csManager.deleteActivityImages(strId, true);
                    }

                    //delete all activities belong the user after unregister success.
                    dbMgr.deleteActivity(activity);

                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse updatePerson(Person person) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(person != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if (dbMgr.checkPersonValid(person)) {
                //change password?
                StringTool stringTool = new StringTool();
                if(stringTool.checkStringNotNull(person.getNewUserPassword()))
                    person.setUserPassword(person.getNewUserPassword());

                if (dbMgr.updatePerson(person)) {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse queryPerson(JsonObject jsonSource) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if (jsonSource != null) {
            final String TAG_EMAILS = "emails";
            JsonElement jsElement = jsonSource.get(TAG_EMAILS);

            if (jsElement != null) {
                String strEmails = jsElement.getAsString();
                StringTool stringTool = new StringTool();

                if(stringTool.checkStringNotNull(strEmails)) {
                    String[] arrEmails = strEmails.split(",");

                    DatabaseManager dbMgr = getDatabaseManager();
                    List<Person> lsPerson = new ArrayList<>();

                    for(String strEmail : arrEmails) {
                        Person person = dbMgr.queryPerson(strEmail);
                        if(person != null)
                            lsPerson.add(person);
                    }

                    serverResp.setContent(lsPerson);
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse logout(Person person) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(person != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if (dbMgr.logout(person)) {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse login(Person person) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(person != null) {
            DatabaseManager dbMgr = getDatabaseManager();
            Person personLogin = dbMgr.login(person);

            if (personLogin != null) {
                serverResp.setContent(personLogin);
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse deletePersonIcon(JsonObject jsonSource) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(jsonSource != null) {
            final String TAG_EMAIL = "email";
            final String TAG_USERPASSWORD = "userpassword";
            JsonElement jsonEmail = jsonSource.get(TAG_EMAIL);
            JsonElement jsonUserPassword = jsonSource.get(TAG_USERPASSWORD);

            StringTool stringTool = new StringTool();

            if(jsonEmail != null
                    && jsonUserPassword != null
                    && stringTool.checkStringNotNull(jsonEmail.getAsString())
                    && stringTool.checkStringNotNull(jsonUserPassword.getAsString())) {

                String strEmail = jsonEmail.getAsString();
                String strUserPassword = jsonUserPassword.getAsString();

                DatabaseManager dbMgr = getDatabaseManager();
                if(dbMgr.checkPersonValid(strEmail, strUserPassword)) {
                    final String TAG_ICONS = "icons";
                    JsonElement jsonIcons = jsonSource.get(TAG_ICONS);

                    //delete all belong the user.
                    if(jsonIcons == null || jsonIcons.getAsString().isEmpty()) {
                        StorageManager csMgr = getStorageManager();
                        csMgr.deletePersonIcons(strEmail, false);

                    //delete icons by icon name.
                    } else {
                        String[] arrIcons = jsonIcons.getAsString().split(",");
                        StorageManager csMgr = getStorageManager();

                        for(String strIcon : arrIcons) {
                            csMgr.deletePersonIcon(strIcon);
                        }
                    }

                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse uploadPersonIcon(String strOwnerName, String strFileName, InputStream is) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StringTool stringTool = new StringTool();

        if(is != null
                && stringTool.checkStringNotNull(strOwnerName)
                && stringTool.checkStringNotNull(strFileName)) {

            DatabaseManager dbMgr = getDatabaseManager();

            //TODO - There has a concern that is no verify user password
            if(dbMgr.checkPersonExist(strOwnerName)) {
                StorageManager csManager = new StorageManager();

                if(csManager.uploadPersonIcon(strFileName, is))
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                else
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_IO_ERROR;

            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse downloadPersonIcon(String strOwnerName, String strFileName, OutputStream os) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StringTool stringTool = new StringTool();

        if(os != null
                && stringTool.checkStringNotNull(strOwnerName)
                && stringTool.checkStringNotNull(strFileName)) {

            String strFullName = strOwnerName + "/" + strFileName;

            StorageManager csMgr = getStorageManager();
            if(csMgr.downloadPersonIcon(strFullName, os)) {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    @SuppressWarnings("Duplicates")
    public ServerResponse listPersonIcons(String strOwnerName) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StorageManager csMgr = getStorageManager();
        List<String> lsIcons =  csMgr.listPersonIcons(strOwnerName, false);

        if(lsIcons != null && !lsIcons.isEmpty()) {
            StringTool stringTool = new StringTool();
            String strIcons = stringTool.ListStringToString(lsIcons, ',');

            if(!strIcons.isEmpty()) {
                serverResp.setContent(strIcons);
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    // --------------------------------- Activity control functions ---------------------------------
    public ServerResponse createActivity(Activity activity) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(activity != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if(dbMgr.checkPersonValid(activity.getPublisherEmail(), activity.getPublisherUserPassword())) {
                String strResId = dbMgr.createActivity(activity);

                StringTool stringTool = new StringTool();
                if (stringTool.checkStringNotNull(strResId)) {
                    //create storage on GCS for store activity images.
                    StorageManager csMgr = getStorageManager();
                    csMgr.createActivityStorage(strResId);

                    serverResp.setContent(strResId);
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse deleteActivity(Activity activity) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(activity != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if(dbMgr.checkPersonValid(activity.getPublisherEmail(), activity.getPublisherUserPassword())) {
                StorageManager csMgr = getStorageManager();

                if(activity.getId() == null || activity.getId().isEmpty()) {
                    //delete all activities by publisher email.
                    List<String> lsIds = dbMgr.queryActivity(activity);

                    if (dbMgr.deleteActivity(activity)) {
                        if (lsIds != null && !lsIds.isEmpty()) {
                            for (String strId : lsIds)
                                csMgr.deleteActivityImages(strId, true);
                        }

                        resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                    } else {
                        resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
                    }
                } else {
                    //delete one activity by id.
                    if (dbMgr.deleteActivity(activity)) {
                        csMgr.deleteActivityImages(activity.getId(), true);

                        resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                    } else {
                        resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
                    }
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse queryActivity(JsonObject jsonSource) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if (jsonSource != null) {
            final String TAG_IDS = "ids";
            JsonElement jsElement = jsonSource.get(TAG_IDS);

            if (jsElement != null) {
                DatabaseManager dbMgr = getDatabaseManager();

                String strIDs = jsElement.getAsString();
                List<Activity> lsActicities = dbMgr.queryActivityByIds(strIDs);

                if(lsActicities != null && lsActicities.size() > 0) {
                    serverResp.setContent(lsActicities);
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse queryActivityIdBy(Activity activity) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if (activity != null) {
            DatabaseManager dbMgr = getDatabaseManager();
            List<String> lsIds = dbMgr.queryActivity(activity);

            if(lsIds != null && lsIds.size() > 0) {
                StringTool stringTool = new StringTool();
                String strIds = stringTool.ListStringToString(lsIds, ',');

                serverResp.setContent(strIds);
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse updateActivity(Activity activity) {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if (activity != null) {
            DatabaseManager dbMgr = getDatabaseManager();

            if(dbMgr.checkPersonValid(activity.getPublisherEmail(), activity.getPublisherUserPassword())) {
                if (dbMgr.checkActivityExist(activity)) {
                    if (dbMgr.updateActivity(activity)) {
                        resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                    } else {
                        resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
                    }
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse deleteActivityImage(JsonObject jsonSource) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(jsonSource != null) {
            final String TAG_EMAIL = "email";
            final String TAG_USERPASSWORD = "userpassword";

            JsonElement jsonEmail = jsonSource.get(TAG_EMAIL);
            JsonElement jsonUserPassword = jsonSource.get(TAG_USERPASSWORD);

            StringTool stringTool = new StringTool();

            if(jsonEmail != null
                    && jsonUserPassword != null
                    && stringTool.checkStringNotNull(jsonEmail.getAsString())
                    && stringTool.checkStringNotNull(jsonUserPassword.getAsString())) {

                String strEmail = jsonEmail.getAsString();
                String strUserPassword = jsonUserPassword.getAsString();

                DatabaseManager dbMgr = getDatabaseManager();

                if(dbMgr.checkPersonValid(strEmail, strUserPassword)) {
                    final String TAG_ID = "id";
                    final String TAG_IMAGES = "images";

                    JsonElement jsonId = jsonSource.get(TAG_ID);
                    JsonElement jsonImages = jsonSource.get(TAG_IMAGES);

                    if(jsonImages != null && !jsonImages.getAsString().isEmpty()) {
                        //delete images by activity image name.
                        String strImages = jsonImages.getAsString();

                        if(stringTool.checkStringNotNull(strImages)) {
                            String[] arrImages = strImages.split(",");

                            if(arrImages.length > 0) {
                                StorageManager csMgr = getStorageManager();

                                for(String strImage : arrImages) {
                                    csMgr.deleteActivityImage(strImage);
                                }
                            }
                            resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                        } else {
                            resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
                        }
                    } else {
                        //delete all belong the activity.
                        if(jsonId != null && !jsonId.getAsString().isEmpty()) {
                            String strId = jsonId.getAsString();
                            StorageManager csMgr = getStorageManager();
                            csMgr.deleteActivityImages(strId, false);

                            resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                        } else {
                            resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
                        }
                    }
                } else {
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
                }
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_JSON_FORMAT_WRONG;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse uploadActivityImage(String strActivityId, String strFileName, InputStream is) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StringTool stringTool = new StringTool();

        if(is != null
                && stringTool.checkStringNotNull(strActivityId)
                && stringTool.checkStringNotNull(strFileName)) {

            DatabaseManager dbMgr = getDatabaseManager();

            //TODO - There has a concern that is no verify publisher password
            if(dbMgr.checkActivityExist(strActivityId)) {
                StorageManager csManager = new StorageManager();

                if(csManager.uploadActivityImage(strFileName, is))
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
                else
                    resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_IO_ERROR;

            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_ACTIVITY_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    public ServerResponse downloadActivityImage(String strActivityId, String strFileName, OutputStream os) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StringTool stringTool = new StringTool();

        if(os != null
                && stringTool.checkStringNotNull(strActivityId)
                && stringTool.checkStringNotNull(strFileName)) {

            String strFullName = strActivityId + "/" + strFileName;

            StorageManager csMgr = getStorageManager();
            if(csMgr.downloadActivityImage(strFullName, os)) {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_MISSING_NECESSARY;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    @SuppressWarnings("Duplicates")
    public ServerResponse listActivityImage(String strActivityId) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StorageManager csMgr = getStorageManager();

        List<String> lsImages =  csMgr.listActivityImages(strActivityId, false);

        if(lsImages != null && !lsImages.isEmpty()) {
            StringTool stringTool = new StringTool();
            String strImages = stringTool.ListStringToString(lsImages, ',');

            if(!strImages.isEmpty()) {
                serverResp.setContent(strImages);
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_FILE_NOT_FOUND;
        }

        serverResp.setStatus(resCode);
        return serverResp;
    }

    private DatabaseManager getDatabaseManager() {
        if(m_dbMgr == null)
            m_dbMgr = new DatabaseManager();
        return m_dbMgr;
    }

    private StorageManager getStorageManager() {
        if(m_csMgr == null)
            m_csMgr = new StorageManager();
        return m_csMgr;
    }
}
