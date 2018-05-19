package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.json.HttpJsonToActivity;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;
import com.google.gson.JsonArray;
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

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.createActivity(activity);
        String strResponse = errHandler.handleError(serverResp.getStatus());

        if(serverResp.getStatus() == ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
            final String TAG_ID = "id";
            String strNewId = (String)serverResp.getContent();
            JsonObject jsonNewId = new JsonObject();
            jsonNewId.addProperty(TAG_ID, strNewId);

            JsonArray resJsonArray = new JsonArray();
            resJsonArray.add(new JsonParser().parse(strResponse));
            resJsonArray.add(jsonNewId);

            strResponse = resJsonArray.toString();
        }

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
