package com.fff.hos.httpservice;

import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.person.Person;
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

@WebServlet(name = "HttpServiceQueryPerson", value = "/queryperson")
public class HttpServiceQueryPerson extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceQueryPerson.class.getName());

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
            Person resPerson = CloudSQLManager.getInstance().queryPersonsByEmail(person.getEmail());

            if (resPerson == null) {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "fail, user not exist");
            } else {
                String strPersonJson = new Gson().toJson(resPerson);
                strPersonJson = DBTool.addStatusCode(strPersonJson, 0);
                jsonObj = new JsonParser().parse(strPersonJson).getAsJsonObject();
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
