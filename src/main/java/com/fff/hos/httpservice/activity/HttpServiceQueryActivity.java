package com.fff.hos.httpservice.activity;

import com.fff.hos.data.Activity;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToJsonObj;
import com.fff.hos.tools.StringTool;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceQueryActivity", value = "/queryactivity")
public class HttpServiceQueryActivity extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceQueryActivity.class.getName());
    private static final String TAG_QUERY = "ids";
    private static final String SYMBOL_SPLIT = ",";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JsonObject jsonObj = HttpJsonToJsonObj.parse(request);
        JsonObject resJsonObj = new JsonObject();
        JsonArray resJsonArray = null;

        if (jsonObj != null) {
            JsonElement jsElement = jsonObj.get(TAG_QUERY);

            if(jsElement != null) {
                String strIDs = jsElement.getAsString();
                String[] arrIDs = StringTool.splitStringBySymbol(strIDs, SYMBOL_SPLIT);

                if(arrIDs != null && arrIDs.length > 0) {
                    ArrayList<Activity> lsActicities = new ArrayList<>();

                    for(String strId : arrIDs) {
                        Activity resActivity = CloudSQLManager.getInstance().queryActivity(strId);

                        if (resActivity != null)
                            lsActicities.add(resActivity);
                    }

                    if(lsActicities.size() <= 0) {
                        resJsonObj.addProperty("statuscode", 1);
                        resJsonObj.addProperty("status", "query fail, no any activities found");
                    } else {
                        Gson gson = new GsonBuilder().setLenient().create();
                        Type listType = new TypeToken<ArrayList<Activity>>() {}.getType();
                        String strRes = gson.toJson(lsActicities, listType);

                        resJsonArray = new JsonArray();
                        resJsonObj.addProperty("statuscode", 0);
                        resJsonArray.add(resJsonObj);
                        resJsonArray.addAll(new JsonParser().parse(strRes).getAsJsonArray());
                    }
                }
            } else {
                resJsonObj.addProperty("statuscode", 1);
                resJsonObj.addProperty("status", "query fail, Json property wrong");
            }
        } else {
            resJsonObj.addProperty("statuscode", 1);
            resJsonObj.addProperty("status", "query fail, JSON format wrong");
        }

        if(resJsonArray != null)
            response.getWriter().print(resJsonArray.toString());
        else
            response.getWriter().print(resJsonObj.toString());

        response.flushBuffer();
    }
}
