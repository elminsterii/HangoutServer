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

@WebServlet(name = "HttpServiceUnregister", value = "/unregister")
public class HttpServiceUnregister extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceUnregister.class.getName());

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
            //check user exist.
            if (CloudSQLManager.getInstance().queryPersonsByEmail(person.getEmail()) == null) {
                jsonObj.addProperty("email", person.getEmail());
                jsonObj.addProperty("status", "fail, user not exist");
            } else {
                if (CloudSQLManager.getInstance().deletePersonByEmail(person.getEmail())) {
                    jsonObj.addProperty("email", person.getEmail());
                    jsonObj.addProperty("status", "success");
                } else {
                    jsonObj.addProperty("status", "fail, unregister fail");
                }
            }
        } else {
            jsonObj.addProperty("status", "fail, unregister fail");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
