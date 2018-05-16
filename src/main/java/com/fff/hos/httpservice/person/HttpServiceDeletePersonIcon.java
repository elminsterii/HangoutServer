package com.fff.hos.httpservice.person;

import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.gcs.CloudStorageManager;
import com.fff.hos.json.HttpJsonToJsonObj;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceDeletePersonIcon", value = "/deletepersonicon")
public class HttpServiceDeletePersonIcon extends HttpServlet {

    private static final String TAG_EMAIL = "email";
    private static final String TAG_USERPASSWORD = "userpassword";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToJsonObj jsonToJsonObj = new HttpJsonToJsonObj();
        JsonObject jsonDataObj = jsonToJsonObj.parse(request);

        StringTool stringTool = new StringTool();
        JsonObject jsonObj = new JsonObject();

        if(jsonDataObj == null) {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "register fail, JSON format wrong");
        } else {
            JsonElement jsonEmail = jsonDataObj.get(TAG_EMAIL);
            JsonElement jsonUserPassword = jsonDataObj.get(TAG_USERPASSWORD);
            if(jsonEmail == null
                    || jsonUserPassword == null
                    || !stringTool.checkStringNotNull(jsonEmail.getAsString())
                    || !stringTool.checkStringNotNull(jsonUserPassword.getAsString())) {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "delete fail, email or password is empty?");
            } else {
                String strEmail = jsonEmail.getAsString();
                String strUserPassword = jsonUserPassword.getAsString();

                CloudSQLManager sqlManager = new CloudSQLManager();
                if(!sqlManager.checkPersonValid(strEmail, strUserPassword)) {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "delete fail, invalid user");
                } else {
                    JsonElement jsonIcons = jsonDataObj.get("icons");

                    //delete all belong the user.
                    if(jsonIcons == null || jsonIcons.getAsString().isEmpty()) {
                        CloudStorageManager csManager = new CloudStorageManager();
                        csManager.deletePersonIcons(strEmail);

                        jsonObj.addProperty("statuscode", 0);

                        //delete icons by icon name.
                    } else {
                        String strIcons = jsonIcons.getAsString();

                        if(stringTool.checkStringNotNull(strIcons)) {
                            String[] arrIcons = strIcons.split(",");

                            if(arrIcons.length > 0) {
                                CloudStorageManager csManager = new CloudStorageManager();

                                for(String strIcon : arrIcons) {
                                    csManager.deletePersonIcon(strIcon);
                                }
                            }
                            jsonObj.addProperty("statuscode", 0);
                        } else {
                            jsonObj.addProperty("statuscode", 1);
                            jsonObj.addProperty("status", "delete fail, icons is empty?");
                        }
                    }
                }
            }
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
