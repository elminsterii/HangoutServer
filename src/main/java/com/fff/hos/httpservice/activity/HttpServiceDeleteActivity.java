package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.DatabaseManager;
import com.fff.hos.gcs.StorageManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HttpServiceDeleteActivity", value = "/deleteactivity")
public class HttpServiceDeleteActivity extends HttpServlet {

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
                StorageManager csManager = new StorageManager();

                //delete all activities by publisher email.
                if(activity.getId() == null || activity.getId().isEmpty()) {
                    List<String> lsIds = sqlManager.queryActivity(activity);

                    if (sqlManager.deleteActivity(activity)) {
                        if (lsIds != null && !lsIds.isEmpty()) {
                            for (String strId : lsIds)
                                csManager.deleteActivityImages(strId);
                        }

                        jsonObj.addProperty("statuscode", 0);
                    } else {
                        jsonObj.addProperty("statuscode", 1);
                        jsonObj.addProperty("status", "delete fail, activity ID wrong or you are not owner");
                    }
                //delete one activity by id.
                } else {
                    if (sqlManager.deleteActivity(activity)) {
                        //delete all images belong the activity after delete activity success.
                        csManager.deleteActivityImages(activity.getId());

                        jsonObj.addProperty("statuscode", 0);
                    } else {
                        jsonObj.addProperty("statuscode", 1);
                        jsonObj.addProperty("status", "delete fail, activity ID wrong or you are not owner");
                    }
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "delete fail, invalid user");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "delete fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
