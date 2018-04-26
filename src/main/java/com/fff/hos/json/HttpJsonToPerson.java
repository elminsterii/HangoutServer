package com.fff.hos.json;

import com.fff.hos.person.Person;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpJsonToPerson {
    public static Person parse(HttpServletRequest request) {
        String strBody;
        try {
            strBody = getBody(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Gson().fromJson(strBody, Person.class);
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
