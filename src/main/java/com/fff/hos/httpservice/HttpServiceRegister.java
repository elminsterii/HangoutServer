package com.fff.hos.httpservice;

import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.person.Person;
import com.fff.hos.tools.DBTool;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceRegister", value = "/register")
public class HttpServiceRegister extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceRegister.class.getName());

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

        if (person != null || DBTool.checkStringNotNull(person.getEmail())) {
            //check user already exist.
            if (CloudSQLManager.getInstance().queryPersonsByEmail(person.getEmail()) != null) {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "fail, user already exist");
            } else {
                CloudSQLManager.getInstance().insertPerson(person);
                Person resPerson = CloudSQLManager.getInstance().queryPersonsByEmail(person.getEmail());

                if (resPerson != null) {
                    jsonObj.addProperty("statuscode", 0);
                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "fail, register fail");
                }
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
