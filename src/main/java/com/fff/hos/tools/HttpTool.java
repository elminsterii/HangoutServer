package com.fff.hos.tools;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpTool {

    //e.g. https://hangouttw.appspot.com/accesspersonicon/jimmy2@gmail.com/icon01.jpg
    //           //         0           /       1        /       2        /    3    /
    public String getOwnerName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        if(splits.length < 3)
            return null;

        return splits[2];
    }

    public String getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        if(splits.length < 4)
            return null;

        return splits[3];
    }

    public String getBody(HttpServletRequest request) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while ((line = reader.readLine()) != null)
            strBuilder.append(line);

        return strBuilder.toString();
    }
}
