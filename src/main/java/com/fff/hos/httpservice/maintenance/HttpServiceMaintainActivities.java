package com.fff.hos.httpservice.maintenance;

import com.fff.hos.data.Activity;
import com.fff.hos.database.DatabaseManager;
import com.fff.hos.maintainer.ActivityMaintainer;
import com.fff.hos.server.ErrorHandler;
import com.fff.hos.server.ServerManager;
import com.fff.hos.server.ServerResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "HttpServiceMaintainActivities", value = "/maintainactivities")
public class HttpServiceMaintainActivities extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HttpServiceMaintainActivities.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");

        ServerManager serverMgr = new ServerManager();
        ErrorHandler errHandler = new ErrorHandler();

        ServerResponse serverResp = serverMgr.queryAll();
        String strResponse = errHandler.handleError(serverResp.getStatus());

        if(serverResp.getStatus() == ServerResponse.STATUS_CODE.ST_CODE_SUCCESS) {
            List<Activity> lsActivities = (List<Activity>)serverResp.getContent();

            ActivityMaintainer am = new ActivityMaintainer();
            DatabaseManager dbMgr = new DatabaseManager();

            for(Activity activity : lsActivities) {
                try {
                    Activity oriActivity = (Activity)activity.clone();
                    am.maintain(activity);

                    if(!oriActivity.equals(activity)) {
                        dbMgr.updateActivity(activity);
                    }

                } catch (CloneNotSupportedException e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        }

        response.getWriter().print(strResponse);
        response.flushBuffer();
    }
}
