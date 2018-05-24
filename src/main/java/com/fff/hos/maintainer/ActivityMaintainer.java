package com.fff.hos.maintainer;

import com.fff.hos.data.Activity;
import com.fff.hos.tools.StringTool;

import java.sql.Timestamp;

import static com.fff.hos.data.Activity.ACTIVITY_STATUS.*;

public class ActivityMaintainer {

    private static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    public boolean maintain(Activity activity) {
        boolean bRes;

        if(activity == null)
            return false;

        bRes = maintainStatus(activity);

        return bRes;
    }

    private boolean maintainStatus(Activity activity) {
        boolean bRes;

        String strDateBegin = activity.getDateBegin();
        String strDateEnd = activity.getDateEnd();

        StringTool stringTool = new StringTool();

        if (!stringTool.checkStringNotNull(strDateBegin)
                || !stringTool.checkStringNotNull(strDateEnd))
            return false;

        try {
            Timestamp tsNow = new Timestamp(System.currentTimeMillis());
            Timestamp tsBegin = Timestamp.valueOf(strDateBegin);
            Timestamp tsEnd = Timestamp.valueOf(strDateEnd);

            if(tsNow.after(tsEnd)) {
                activity.setStatus(ST_ACTIVITY_DONE.ordinal());
                activity.setEarlyBird(0);
            } else if(tsNow.after(tsBegin) && tsNow.before(tsEnd)) {
                activity.setStatus(ST_ACTIVITY_STARTING.ordinal());
                activity.setEarlyBird(0);
            } else if((tsBegin.getTime() - tsNow.getTime()) < ONE_DAY_MS) {
                activity.setStatus(ST_ACTIVITY_READY_START.ordinal());
                activity.setEarlyBird(0);
            } else {
                activity.setStatus(ST_ACTIVITY_NOT_START.ordinal());
                activity.setEarlyBird(1);
            }
            bRes = true;
        } catch (IllegalArgumentException e) {
            bRes = false;
        }

        return bRes;
    }
}
