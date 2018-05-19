package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.json.HttpJsonToActivity;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;

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

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.updateActivity(activity);
        String strResponse = errHandler.handleError(serverResp.getStatus());

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
