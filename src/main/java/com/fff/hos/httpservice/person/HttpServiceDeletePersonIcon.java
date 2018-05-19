package com.fff.hos.httpservice.person;

import com.fff.hos.json.HttpJsonToJsonObj;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceDeletePersonIcon", value = "/deletepersonicon")
public class HttpServiceDeletePersonIcon extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToJsonObj jsonToJsonObj = new HttpJsonToJsonObj();
        JsonObject jsonSource = jsonToJsonObj.parse(request);

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.deletePersonIcon(jsonSource);
        String strResponse = errHandler.handleError(serverResp.getStatus());

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
