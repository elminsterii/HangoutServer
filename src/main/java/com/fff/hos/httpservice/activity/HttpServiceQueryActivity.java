package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.fff.hos.tools.DBTool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceQueryActivity", value = "/queryactivity")
public class HttpServiceQueryActivity extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceQueryActivity.class.getName());

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
            Activity resActivity = CloudSQLManager.getInstance().queryActivity(activity);

            if (resActivity != null) {
                String strActivityJson = new Gson().toJson(resActivity);
                strActivityJson = DBTool.addStatusCode(strActivityJson, 0);
                jsonObj = new JsonParser().parse(strActivityJson).getAsJsonObject();
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "query fail, activity ID is wrong or not exist");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "query fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
