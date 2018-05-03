package com.fff.hos.tools;

public class DBTool {
    public static boolean checkStringNotNull(String str) {
        boolean bRes = false;
        if (str != null && !str.isEmpty())
            bRes = true;
        return bRes;
    }

    public static String addStatusCode(String strJson, int iCode) {
        if (strJson == null)
            return "";

        StringBuffer strBuffer = new StringBuffer(strJson);
        if (!strJson.isEmpty())
            strBuffer.insert(1, "\"statuscode\":" + iCode + ",");
        else
            strBuffer.insert(0, "\"statuscode\":" + iCode);

        strBuffer.trimToSize();

        return strBuffer.toString();
    }
}
