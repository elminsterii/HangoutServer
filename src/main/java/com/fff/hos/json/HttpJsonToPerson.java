package com.fff.hos.json;

import com.fff.hos.data.Person;
import com.fff.hos.tools.HttpTool;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToPerson {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToPerson.class.getName());

    public Person parse(HttpServletRequest request) {
        Person person = null;
        String strBody = "";

        HttpTool httpTool = new HttpTool();
        try {
            strBody = httpTool.getBody(request);
            person = new Gson().fromJson(strBody, Person.class);
        } catch (IllegalStateException e) {
            LOGGER.warning(e.getMessage());
            LOGGER.warning("Illegal data : " + strBody);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        return person;
    }
}
