package com.fff.hos.httpservice.person;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.gcs.CloudStorageManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HttpServiceUnregister", value = "/unregister")
public class HttpServiceUnregister extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToPerson jsonToPerson = new HttpJsonToPerson();
        Person person = jsonToPerson.parse(request);
        JsonObject jsonObj = new JsonObject();

        if (person != null) {
            CloudSQLManager sqlManager = new CloudSQLManager();

            if (sqlManager.checkPersonValid(person)) {
                if (sqlManager.unregister(person)) {
                    //delete all icons obelong the user on GCS after unregister success.
                    CloudStorageManager csManager = new CloudStorageManager();
                    csManager.deletePersonIcons(person.getEmail());

                    //delete all images belong the activity after unregister success.
                    Activity activity = new Activity();
                    activity.setPublisherEmail(person.getEmail());
                    List<String> lsIds =  sqlManager.queryActivity(activity);

                    if(lsIds != null && !lsIds.isEmpty()) {
                        for(String strId : lsIds)
                            csManager.deleteActivityImages(strId);
                    }

                    //delete all activities belong the user after unregister success.
                    sqlManager.deleteActivity(activity);

                    jsonObj.addProperty("statuscode", 0);

                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "unregister fail, email or password wrong?");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "unregister fail, invalid user or user is not exist");
            }

        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "unregister fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
