package com.fff.hos.json;

import com.fff.hos.data.Activity;
import com.fff.hos.tools.HttpTool;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToActivity {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToActivity.class.getName());

    public Activity parse(HttpServletRequest request) {
        Activity activity = null;
        String strBody = "";

        HttpTool httpTool = new HttpTool();
        try {
            strBody = httpTool.getBody(request);
            activity = new Gson().fromJson(strBody, Activity.class);
        } catch (IllegalStateException e) {
            LOGGER.warning(e.getMessage());
            LOGGER.warning("Illegal data : " + strBody);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        return activity;
    }
}
