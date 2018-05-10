package com.fff.hos.tools;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpTool {

    private static final Logger LOGGER = Logger.getLogger(HttpTool.class.getName());

    public static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null)
            buffer.append(line);

        return buffer.toString();
    }
}
