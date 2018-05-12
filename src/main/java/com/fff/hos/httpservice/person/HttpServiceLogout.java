package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceLogout", value = "/logout")
public class HttpServiceLogout extends HttpServlet {

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
            Person resPerson = sqlManager.logout(person);

            if (resPerson != null) {
                jsonObj.addProperty("statuscode", 0);
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "logout fail, email or password wrong?");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "login fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
