package com.fff.hos.json;

import com.fff.hos.person.Person;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToPerson {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToPerson.class.getName());

    public static Person parse(HttpServletRequest request) {
        Person person;
        String strBody = "";

        try {
            strBody = getBody(request);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        person = new Gson().fromJson(strBody, Person.class);

        return person;
    }

    private static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null)
            buffer.append(line);

        return buffer.toString();
    }
}
