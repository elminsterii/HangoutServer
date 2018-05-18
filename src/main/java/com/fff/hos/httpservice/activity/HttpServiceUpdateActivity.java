package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.DatabaseManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceUpdateActivity", value = "/updateactivity")
public class HttpServiceUpdateActivity extends HttpServlet {

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
            DatabaseManager sqlManager = new DatabaseManager();

            if(sqlManager.checkPersonValid(activity.getPublisherEmail(), activity.getPublisherUserPassword())) {
                if (sqlManager.checkActivityExist(activity)) {
                    if (sqlManager.updateActivity(activity)) {
                        jsonObj.addProperty("statuscode", 0);
                    } else {
                        jsonObj.addProperty("statuscode", 1);
                        jsonObj.addProperty("status", "update fail, activity ID wrong or you are not owner");
                    }
                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "update fail, activity is not exist");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "update fail, invalid user");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "update fail, JSON format wrong or missing email");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
