package com.fff.hos.json;

import com.fff.hos.tools.HttpTool;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToJsonObj {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToJsonObj.class.getName());

    public static JsonObject parse(HttpServletRequest request) {
        JsonObject jsonObj;
        JsonParser parser = new JsonParser();

        String strBody = "";
        try {
            strBody = HttpTool.getBody(request);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        jsonObj = parser.parse(strBody).getAsJsonObject();
        return jsonObj;
    }
}
