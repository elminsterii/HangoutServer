package com.fff.hos.tools;

import java.util.List;
import java.util.logging.Logger;

public class StringTool {

    private static final Logger LOGGER = Logger.getLogger(StringTool.class.getName());

    public static String strTagsToRegExp(String strTags) {
        if(strTags == null || strTags.isEmpty())
            return "";

        StringBuilder strBuffer = new StringBuilder();
        String[] arrString = strTags.split(",");

        if(arrString.length <= 0)
            return "";

        for(String str : arrString) {
            strBuffer.append("[[:<:]]").append(str).append("[[:>:]]");
            strBuffer.append("|");
        }

        strBuffer.deleteCharAt(strBuffer.length()-1);
        return strBuffer.toString();
    }

    public static boolean checkStringNotNull(String str) {
        boolean bRes = false;
        if (str != null && !str.isEmpty())
            bRes = true;
        return bRes;
    }

    public static String strListToJsonString(List<String> lsString) {
        if(lsString == null || lsString.isEmpty())
            return "";

        StringBuffer strBuffer = new StringBuffer();
        for(String str : lsString)
            strBuffer.append(str).append(',');
        strBuffer.deleteCharAt(strBuffer.length()-1);

        return strBuffer.toString();
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
