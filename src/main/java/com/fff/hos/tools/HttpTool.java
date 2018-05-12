package com.fff.hos.tools;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpTool {

    public String getBody(HttpServletRequest request) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null)
            strBuilder.append(line);

        return strBuilder.toString();
    }
}
