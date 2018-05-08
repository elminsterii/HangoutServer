package com.fff.hos.json;

import com.fff.hos.data.Activity;
import com.fff.hos.tools.HttpTool;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToActivity {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToActivity.class.getName());

    public static Activity parse(HttpServletRequest request) {
        Activity activity;
        String strBody = "";

        try {
            strBody = HttpTool.getBody(request);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        activity = new Gson().fromJson(strBody, Activity.class);

        return activity;
    }
}
