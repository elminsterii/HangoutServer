package com.fff.hos.json;

import com.fff.hos.data.Comment;
import com.fff.hos.tools.HttpTool;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpJsonToComment {
    private static final Logger LOGGER = Logger.getLogger(HttpJsonToComment.class.getName());

    public Comment parse(HttpServletRequest request) {
        Comment comment = null;
        String strBody = "";

        HttpTool httpTool = new HttpTool();
        try {
            strBody = httpTool.getBody(request);
            comment = new Gson().fromJson(strBody, Comment.class);
        } catch (IllegalStateException e) {
            LOGGER.warning(e.getMessage());
            LOGGER.warning("Illegal data : " + strBody);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        return comment;
    }
}
