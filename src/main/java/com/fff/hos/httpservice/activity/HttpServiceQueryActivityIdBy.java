package com.fff.hos.httpservice.activity;


import com.fff.hos.data.Activity;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToActivity;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceQueryActivityIdBy", value = "/queryactivityidby")
public class HttpServiceQueryActivityIdBy extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceQueryActivityIdBy.class.getName());

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
            List<String> lsIds = CloudSQLManager.getInstance().queryActivity(activity);

            if(lsIds == null || lsIds.size() <= 0) {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "query fail, no any activities found");
            } else {
                jsonObj.addProperty("statuscode", 0);
                String strIds = StringTool.strListToJsonString(lsIds);
                jsonObj.addProperty("ids", strIds);
            }

        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "delete fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
