package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceUpdatePerson", value = "/updateperson")
public class HttpServiceUpdatePerson extends HttpServlet {

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

            if (sqlManager.checkPersonValid(person)) {
                //change password?
                StringTool stringTool = new StringTool();
                if(stringTool.checkStringNotNull(person.getNewUserPassword()))
                    person.setUserPassword(person.getNewUserPassword());

                if (sqlManager.updatePerson(person)) {
                    jsonObj.addProperty("statuscode", 0);
                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "update fail");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "update fail, user is not exist or password wrong");
            }
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "update fail, JSON format wrong or missing email");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
