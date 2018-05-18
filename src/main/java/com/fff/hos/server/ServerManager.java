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
                    csManager.deletePersonIcons(person.getEmail());

                    //delete all images belong the activity after unregister success.
                    Activity activity = new Activity();
                    activity.setPublisherEmail(person.getEmail());
                    List<String> lsIds = dbMgr.queryActivity(activity);

                    if(lsIds != null && !lsIds.isEmpty()) {
                        for(String strId : lsIds)
                            csManager.deleteActivityImages(strId);
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

    public ServerResponse queryPerson(JsonObject jsonObject) {
        final String TAG_EMAILS = "emails";

        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if (jsonObject != null) {
            JsonElement jsElement = jsonObject.get(TAG_EMAILS);

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

            if (dbMgr.login(person)) {
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
        final String TAG_EMAIL = "email";
        final String TAG_USERPASSWORD = "userpassword";
        final String TAG_ICONS = "icons";

        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        if(jsonSource != null) {
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
                    JsonElement jsonIcons = jsonSource.get(TAG_ICONS);

                    //delete all belong the user.
                    if(jsonIcons == null || jsonIcons.getAsString().isEmpty()) {
                        StorageManager csMgr = getStorageManager();
                        csMgr.deletePersonIcons(strEmail);

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

    public ServerResponse listPersonIcons(String strOwnerName) throws IOException {
        ServerResponse serverResp = new ServerResponse();
        ServerResponse.STATUS_CODE resCode;

        StringTool stringTool = new StringTool();

        if(stringTool.checkStringNotNull(strOwnerName)) {
            StorageManager csMgr = getStorageManager();
            List<String> lsIcons =  csMgr.listPersonIcons(strOwnerName);
            StringBuilder strIcons = new StringBuilder();

            int iMinLength = strOwnerName.length() + 1;
            if(lsIcons != null) {
                for (String strIconName : lsIcons) {
                    if(strIconName.length() > iMinLength)
                        strIcons.append(strIconName).append(",");
                }

                if(strIcons.length() > 0)
                    strIcons.deleteCharAt(strIcons.length() - 1);

                serverResp.setContent(strIcons.toString());
                resCode = ServerResponse.STATUS_CODE.ST_CODE_SUCCESS;
            } else {
                resCode = ServerResponse.STATUS_CODE.ST_CODE_INVALID_DATA;
            }
        } else {
            resCode = ServerResponse.STATUS_CODE.ST_CODE_USER_INVALID;
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
