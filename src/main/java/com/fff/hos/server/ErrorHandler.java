package com.fff.hos.server;

import com.google.gson.JsonObject;

public class ErrorHandler {

    private static final String FAIL_PROPERTY_TITLE = "status_code";
    private static final String FAIL_PROPERTY_CONTENT = "status_description";

    public String handleError(ServerResponse.STATUS_CODE stCode) {
        String strResponse;

        switch(stCode) {
            case ST_CODE_SUCCESS :
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Success, request has been done");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_USER_NOT_FOUND :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, user was not found");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_USER_EXIST :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, user was already exist");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_USER_INVALID :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, invalid user or user was not exist");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_ACTIVITY_NOT_FOUND :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, activity was not found or you are not owner");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_MISSING_NECESSARY :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, missing necessary data?");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_JSON_FORMAT_WRONG :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, json format wrong?");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_INVALID_DATA :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, invalid data input");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_FILE_IO_ERROR :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, file io error");
                strResponse = jsonObj.toString();
                break;

            case ST_CODE_FILE_NOT_FOUND :
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, file was not found");
                strResponse = jsonObj.toString();
                break;

            default:
                jsonObj = new JsonObject();
                jsonObj.addProperty(FAIL_PROPERTY_TITLE, stCode.ordinal());
                jsonObj.addProperty(FAIL_PROPERTY_CONTENT, "Fail, unknown fail happen");
                strResponse = jsonObj.toString();
                break;
        }

        return strResponse;
    }
}
