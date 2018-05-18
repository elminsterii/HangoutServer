package com.fff.hos.httpservice.activity;
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

@WebServlet(name = "HttpServiceDeleteActivityImage", value = "/deleteactivityimage")
public class HttpServiceDeleteActivityImage extends HttpServlet {

    private static final String TAG_EMAIL = "email";
    private static final String TAG_USERPASSWORD = "userpassword";
    private static final String TAG_ID = "id";
    private static final String TAG_IMAGES = "images";

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
            jsonObj.addProperty("status", "delete fail, JSON format wrong");
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
                    JsonElement jsonId = jsonDataObj.get(TAG_ID);
                    JsonElement jsonImages = jsonDataObj.get(TAG_IMAGES);

                    //delete all belong the activity.
                    if(jsonImages == null || jsonImages.getAsString().isEmpty()) {

                        if(jsonId == null || jsonId.getAsString().isEmpty()) {
                            jsonObj.addProperty("statuscode", 1);
                            jsonObj.addProperty("status", "delete fail, activity id is empty?");
                        } else {
                            String strId = jsonId.getAsString();
                            CloudStorageManager csManager = new CloudStorageManager();
                            csManager.deleteActivityImages(strId);

                            jsonObj.addProperty("statuscode", 0);
                        }

                    //delete images by activity image name.
                    } else {
                        String strImages = jsonImages.getAsString();

                        if(stringTool.checkStringNotNull(strImages)) {
                            String[] arrImages = strImages.split(",");

                            if(arrImages.length > 0) {
                                CloudStorageManager csManager = new CloudStorageManager();

                                for(String strImage : arrImages) {
                                    csManager.deleteActivityImage(strImage);
                                }
                            }
                            jsonObj.addProperty("statuscode", 0);
                        } else {
                            jsonObj.addProperty("statuscode", 1);
                            jsonObj.addProperty("status", "delete fail, images is empty?");
                        }
                    }
                }
            }
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
