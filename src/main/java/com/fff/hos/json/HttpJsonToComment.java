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
        Comment comment;
        String strBody = "";

        HttpTool httpTool = new HttpTool();
        try {
            strBody = httpTool.getBody(request);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }

        comment = new Gson().fromJson(strBody, Comment.class);

        return comment;
    }
}
