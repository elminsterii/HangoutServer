package com.fff.hos.httpservice;

import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.person.Person;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        Person person = HttpJsonToPerson.parse(request);

        response.setContentType("application/json");

        if (person != null) {

            //CloudSQLManager.getInstance().getAccounts();

            Gson gson = new Gson();
            JsonParser gsonParser = new JsonParser();

            String strJson = gson.toJson(person);
            JsonObject jsonObj = gsonParser.parse(strJson).getAsJsonObject();
            jsonObj.addProperty("status", "success");
            strJson = jsonObj.toString();

            response.getWriter().print(strJson);
            response.flushBuffer();
        } else {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("status", "fail");

            response.getWriter().print(jsonObj.toString());
            response.flushBuffer();
        }
    }
}
