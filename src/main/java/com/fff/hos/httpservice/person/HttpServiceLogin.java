package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceLogin", value = "/login")
public class HttpServiceLogin extends HttpServlet {

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

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.login(person);
        String strResponse = errHandler.handleError(serverResp.getStatus());

        if(serverResp.getStatus() == ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
            Person personLogin = (Person)serverResp.getContent();
            personLogin.setUserPassword(null);

            String strJsonPerson = new Gson().toJson(personLogin);

            JsonArray resJsonArray = new JsonArray();
            resJsonArray.add(new JsonParser().parse(strResponse));
            resJsonArray.add(new JsonParser().parse(strJsonPerson).getAsJsonObject());

            strResponse = resJsonArray.toString();
        }

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
