package com.fff.hos.httpservice.activity;

import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.gcs.CloudStorageManager;
import com.fff.hos.tools.HttpTool;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HttpServiceAccessActivityImage", urlPatterns = {"/accessactivityimage/*"})
public class HttpServiceAccessActivityImage extends HttpServlet {

    private static final String TAG_IMAGES = "images";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpg");

        HttpTool httpTool = new HttpTool();
        String strActivityId = httpTool.getOwnerName(request);
        String strFileName = httpTool.getFileName(request);

        StringTool stringTool = new StringTool();

        //invalid input...
        if(!stringTool.checkStringNotNull(strActivityId)) {
            FillFailResponseAndFlush(response, "access fail, activity id is empty?");

        //take image list...
        } else if(!stringTool.checkStringNotNull(strFileName)) {
            CloudStorageManager csManager = new CloudStorageManager();
            List<String> lsImages =  csManager.listActivityImages(strActivityId);
            StringBuilder strImages = new StringBuilder();

            int iMinLength = strActivityId.length() + 1;
            if(lsImages != null) {
                for (String strImageName : lsImages) {
                    if(strImageName.length() > iMinLength)
                        strImages.append(strImageName).append(",");
                }
            }

            if(strImages.length() > 0)
                strImages.deleteCharAt(strImages.length() - 1);

            response.setContentType("application/json");
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("statuscode", 0);
            jsonObj.addProperty(TAG_IMAGES, strImages.toString());
            response.getOutputStream().print(jsonObj.toString());
            response.flushBuffer();

        //download image...
        } else {
            String strFullName = strActivityId + "/" + strFileName;

            CloudStorageManager csManager = new CloudStorageManager();
            if(!csManager.downloadActivityImage(strFullName, response.getOutputStream())) {
                FillFailResponseAndFlush(response, "access fail, image is not exist");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JsonObject jsonObj = new JsonObject();
        HttpTool httpTool = new HttpTool();

        String strActivityId = httpTool.getOwnerName(request);
        String strFileName = httpTool.getFileName(request);
        String strFullName = strActivityId + "/" + strFileName;

        CloudSQLManager sqlManager = new CloudSQLManager();

        //TODO - There has a concern that is no verify publisher password.
        if(sqlManager.checkActivityExist(strActivityId)) {
            CloudStorageManager csManager = new CloudStorageManager();

            if(csManager.uploadActivityImage(strFullName, request.getInputStream()))
                jsonObj.addProperty("statuscode", 0);

        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "access fail, activity is not exist");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }

    private void FillFailResponseAndFlush(HttpServletResponse response, String strFailDescription) throws IOException {
        response.setContentType("application/json");
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("statuscode", 1);
        jsonObj.addProperty("status", strFailDescription);
        response.getOutputStream().print(jsonObj.toString());
        response.flushBuffer();
    }
}
