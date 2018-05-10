package com.fff.hos.tools;

import java.util.logging.Logger;

public class StringTool {

    private static final Logger LOGGER = Logger.getLogger(StringTool.class.getName());

    public static String[] splitStringBySymbol(String strData, String strSymbol) {
        return strData.split(strSymbol);
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
