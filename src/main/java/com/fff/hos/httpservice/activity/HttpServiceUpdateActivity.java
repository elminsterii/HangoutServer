package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceUpdateActivity", value = "/updateactivity")
public class HttpServiceUpdateActivity extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceUpdateActivity.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Activity activity = HttpJsonToActivity.parse(request);
        JsonObject jsonObj = new JsonObject();

        if (activity != null) {
            if (CloudSQLManager.getInstance().checkActivityExist(activity)) {
                if (CloudSQLManager.getInstance().updateActivity(activity)) {
                    jsonObj.addProperty("statuscode", 0);
                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "update fail");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "update fail, activity is not exist");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "update fail, JSON format wrong or missing email");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
