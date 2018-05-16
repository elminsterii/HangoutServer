package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
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
import java.util.List;

@WebServlet(name = "HttpServiceQueryPerson", value = "/queryperson")
public class HttpServiceQueryPerson extends HttpServlet {

    private static final String TAG_EMAILS = "emails";

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
        JsonObject resJsonObj = new JsonObject();
        JsonArray resJsonArray = null;

        if (jsonObj != null) {
            JsonElement jsElement = jsonObj.get(TAG_EMAILS);

            if (jsElement != null) {
                String strEmails = jsElement.getAsString();
                StringTool stringTool = new StringTool();

                if(stringTool.checkStringNotNull(strEmails)) {
                    String[] arrEmails = strEmails.split(",");

                    CloudSQLManager sqlManager = new CloudSQLManager();
                    List<Person> lsPerson = new ArrayList<>();

                    for(String strEmail : arrEmails) {
                        Person person = sqlManager.queryPerson(strEmail);

                        if(person != null)
                            lsPerson.add(person);
                    }

                    Gson gson = new GsonBuilder().setLenient().create();
                    Type listType = new TypeToken<ArrayList<Person>>() {}.getType();
                    String strRes = gson.toJson(lsPerson, listType);

                    resJsonArray = new JsonArray();
                    resJsonObj.addProperty("statuscode", 0);
                    resJsonArray.add(resJsonObj);
                    resJsonArray.addAll(new JsonParser().parse(strRes).getAsJsonArray());

                } else {
                    resJsonObj.addProperty("statuscode", 1);
                    resJsonObj.addProperty("status", "query fail, emails is empty");
                }
            } else {
                resJsonObj.addProperty("statuscode", 1);
                resJsonObj.addProperty("status", "query fail, JSON property wrong");
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
