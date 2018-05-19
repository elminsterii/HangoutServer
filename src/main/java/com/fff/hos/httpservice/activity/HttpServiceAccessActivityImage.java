package com.fff.hos.httpservice.activity;

import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;
import com.fff.hos.tools.HttpTool;
import com.fff.hos.tools.StringTool;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceAccessActivityImage", urlPatterns = {"/accessactivityimage/*"})
public class HttpServiceAccessActivityImage extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpg");

        HttpTool httpTool = new HttpTool();
        String strActivityId = httpTool.getOwnerName(request);
        String strFileName = httpTool.getFileName(request);

        StringTool stringTool = new StringTool();
        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        String strResponse;

        if(stringTool.checkStringNotNull(strFileName)) {
            //download image...
            response.setContentType("image/jpg");
            ServerResponse serverResp = serverMgr.downloadActivityImage(strActivityId, strFileName, response.getOutputStream());

            if(serverResp.getStatus() != ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
                strResponse = errHandler.handleError(serverResp.getStatus());
                response.getOutputStream().print(strResponse);
                response.flushBuffer();
            }
        } else {
            //take image list...
            response.setContentType("application/json");

            ServerResponse serverResp = serverMgr.listActivityImage(strActivityId);
            strResponse = errHandler.handleError(serverResp.getStatus());

            if(serverResp.getStatus() == ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
                final String TAG_IMAGES = "images";
                String strImages = (String)serverResp.getContent();

                JsonObject jsonImages = new JsonObject();
                jsonImages.addProperty(TAG_IMAGES, strImages);

                JsonArray resJsonArray = new JsonArray();
                resJsonArray.add(new JsonParser().parse(strResponse));
                resJsonArray.add(jsonImages);

                strResponse = resJsonArray.toString();
            }

            response.getOutputStream().print(strResponse);
            response.flushBuffer();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpTool httpTool = new HttpTool();

        String strActivityId = httpTool.getOwnerName(request);
        String strFileName = httpTool.getFileName(request);
        String strFullName = strActivityId + "/" + strFileName;

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.uploadActivityImage(strActivityId, strFullName, request.getInputStream());
        String strResponse = errHandler.handleError(serverResp.getStatus());

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
