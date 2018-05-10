package com.fff.hos.tools;

import java.util.logging.Logger;

public class DBTool {

    private static final Logger LOGGER = Logger.getLogger(DBTool.class.getName());

    public static boolean checkStringNotNull(String str) {
        boolean bRes = false;
        if (str != null && !str.isEmpty())
            bRes = true;
        return bRes;
    }
}
