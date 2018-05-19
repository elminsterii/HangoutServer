package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.json.HttpJsonToJsonObj;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

@WebServlet(name = "HttpServiceQueryPerson", value = "/queryperson")
public class HttpServiceQueryPerson extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToJsonObj jsonToJsonObj = new HttpJsonToJsonObj();
        JsonObject jsonObj = jsonToJsonObj.parse(request);

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.queryPerson(jsonObj);
        String strResponse = errHandler.handleError(serverResp.getStatus());

        if(serverResp.getStatus() == ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
            Gson gson = new GsonBuilder().setLenient().create();
            Type listType = new TypeToken<ArrayList<Person>>() {}.getType();
            String strRes = gson.toJson(serverResp.getContent(), listType);

            JsonArray resJsonArray = new JsonArray();
            resJsonArray.add(new JsonParser().parse(strResponse));
            resJsonArray.addAll(new JsonParser().parse(strRes).getAsJsonArray());

            strResponse = resJsonArray.toString();
        }

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
