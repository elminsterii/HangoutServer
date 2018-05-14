package com.fff.hos.httpservice.person;

import com.fff.hos.data.Activity;
import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.fff.hos.json.HttpJsonToPerson;
import com.fff.hos.tools.StringTool;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HttpServiceUnregister", value = "/unregister"
        , initParams = { @WebInitParam(name = "gcsBucketName", value = "persons_icons")})
public class HttpServiceUnregister extends HttpServlet {

    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        HttpJsonToPerson jsonToPerson = new HttpJsonToPerson();
        Person person = jsonToPerson.parse(request);
        JsonObject jsonObj = new JsonObject();

        if (person != null) {
            CloudSQLManager sqlManager = new CloudSQLManager();

            if (sqlManager.checkPersonValid(person)) {
                Person oldPerson = sqlManager.queryPerson(person);

                if (sqlManager.unregister(person)) {
                    //delete all icons belong the user after unregister success.
                    String strBucketName = getInitParameter("gcsBucketName");
                    String[] arrFileNames = oldPerson.getIcon().split(",");
                    StringTool stringTool = new StringTool();
                    for(String strIcon : arrFileNames) {
                        if(stringTool.checkStringNotNull(strIcon))
                            gcsService.delete(new GcsFilename(strBucketName, strIcon));
                    }

                    //delete all activities belong the user after unregister success.
                    Activity activity = new Activity();
                    activity.setPublisherEmail(person.getEmail());
                    sqlManager.deleteActivity(activity);

                    jsonObj.addProperty("statuscode", 0);

                } else {
                    jsonObj.addProperty("statuscode", 1);
                    jsonObj.addProperty("status", "unregister fail, email or password wrong?");
                }
            } else {
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "unregister fail, invalid user or user is not exist");
            }

        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "unregister fail, JSON format wrong");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }
}
