package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.fff.hos.tools.StringTool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceCreateActivity", value = "/createactivity")
public class HttpServiceCreateActivity extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToActivity jsonToActivity = new HttpJsonToActivity();
        Activity activity = jsonToActivity.parse(request);
        JsonObject jsonObj = new JsonObject();

        if (activity != null) {
            CloudSQLManager sqlManager = new CloudSQLManager();

            if(sqlManager.checkPersonValid(activity.getPublisherEmail(), activity.getPublisherUserPassword())) {
                Activity newActivity = sqlManager.createActivity(activity);

                if (newActivity != null) {
                    StringTool stringTool = new StringTool();

                    jsonObj.addProperty("statuscode", 0);
                    String strNewActivityJson = new Gson().toJson(newActivity);
                    strNewActivityJson = stringTool.addStatusCode(strNewActivityJson, 0);
                    jsonObj = new JsonParser().parse(strNewActivityJson).getAsJsonObject();
                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "create fail, missing necessary data?");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "create fail, invalid user");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "create fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
