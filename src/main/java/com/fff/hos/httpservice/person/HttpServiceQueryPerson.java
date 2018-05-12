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

@WebServlet(name = "HttpServiceQueryPerson", value = "/queryperson")
public class HttpServiceQueryPerson extends HttpServlet {

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
            Person resPerson = sqlManager.queryPerson(person);

            if (resPerson != null) {
                StringTool stringTool = new StringTool();
                resPerson.setUserPassword(null);
                String strPersonJson = new Gson().toJson(resPerson);
                strPersonJson = stringTool.addStatusCode(strPersonJson, 0);
                jsonObj = new JsonParser().parse(strPersonJson).getAsJsonObject();
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "query fail, email is wrong?");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "query fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
