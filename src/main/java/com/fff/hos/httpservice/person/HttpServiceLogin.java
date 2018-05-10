package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.tools.StringTool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceLogin", value = "/login")
public class HttpServiceLogin extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceLogin.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Person person = HttpJsonToPerson.parse(request);
        JsonObject jsonObj = new JsonObject();

        if (person != null) {
            Person resPerson = CloudSQLManager.getInstance().login(person);

            if (resPerson != null) {
                resPerson.setUserPassword(null);
                String strPersonJson = new Gson().toJson(resPerson);
                strPersonJson = StringTool.addStatusCode(strPersonJson, 0);
                jsonObj = new JsonParser().parse(strPersonJson).getAsJsonObject();
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "login fail, email or password wrong?");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "login fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
