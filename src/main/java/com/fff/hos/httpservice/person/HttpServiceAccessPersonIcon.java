package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.gcs.CloudStorageManager;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HttpServiceAccessPersonIcon", urlPatterns = {"/accesspersonicon/*"})
public class HttpServiceAccessPersonIcon extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpg");

        String strOwnerName = getOwnerName(request);
        String strFileName = getFileName(request);
        String strFullName = strOwnerName + "/" + strFileName;

        CloudStorageManager csManager = new CloudStorageManager();
        if(!csManager.downloadPersonIcon(strFullName, response.getOutputStream())) {
            response.setContentType("application/json");
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "access fail, icon is not exist");
            response.getOutputStream().print(jsonObj.toString());
            response.flushBuffer();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JsonObject jsonObj = new JsonObject();

        String strOwnerName = getOwnerName(request);
        String strFileName = getFileName(request);
        String strFullName = strOwnerName + "/" + strFileName;

        CloudSQLManager sqlManager = new CloudSQLManager();

        //TODO - There has a concern that is no verify user password
        if(sqlManager.checkPersonExist(strOwnerName)) {
            CloudStorageManager csManager = new CloudStorageManager();

            if(csManager.uploadPersonIcon(strFullName, request.getInputStream())) {

                //update property "icon" in DB.
                List<String> lsIcons = csManager.listPersonIcons(strOwnerName);
                StringBuilder strIcons = new StringBuilder();

                if(lsIcons != null) {
                    for(String strIcon : lsIcons)
                        strIcons.append(strIcon).append(",");
                    strIcons.deleteCharAt(strIcons.length()-1);
                }

                Person person = new Person();
                person.setEmail(strOwnerName);
                person.setIcon(strIcons.toString());
                sqlManager.updatePerson(person);
            }

            jsonObj.addProperty("statuscode", 0);
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "access fail, user is not exist");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }

    //e.g. https://hangouttw.appspot.com/accesspersonicon/jimmy2@gmail.com/icon01.jpg
    //           //         0           /       1        /       2        /    3    /
    private String getOwnerName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        return splits[2];
    }

    private String getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        return splits[3];
    }
}
