package com.fff.hos.tools;

import java.util.List;

public class StringTool {

    public String strTagsToRegExp(String strTags) {
        if(strTags == null || strTags.isEmpty())
            return "";

        StringBuilder strBuilder = new StringBuilder();
        String[] arrString = strTags.split(",");

        if(arrString.length <= 0)
            return "";

        for(String str : arrString) {
            strBuilder.append("[[:<:]]").append(str).append("[[:>:]]");
            strBuilder.append("|");
        }

        if(strBuilder.length() > 0)
            strBuilder.deleteCharAt(strBuilder.length()-1);

        return strBuilder.toString();
    }

    public boolean checkStringNotNull(String str) {
        boolean bRes = false;
        if (str != null && !str.isEmpty())
            bRes = true;
        return bRes;
    }

    public String strListToJsonString(List<String> lsString) {
        if(lsString == null || lsString.isEmpty())
            return "";

        StringBuilder strBuilder = new StringBuilder();
        for(String str : lsString)
            strBuilder.append(str).append(',');

        if(strBuilder.length() > 0)
            strBuilder.deleteCharAt(strBuilder.length()-1);

        return strBuilder.toString();
    }

    public String addStatusCode(String strJson, int iCode) {
        if (strJson == null)
            return "";

        StringBuilder strBuilder = new StringBuilder(strJson);
        if (!strJson.isEmpty())
            strBuilder.insert(1, "\"statuscode\":" + iCode + ",");
        else
            strBuilder.insert(0, "\"statuscode\":" + iCode);

        strBuilder.trimToSize();

        return strBuilder.toString();
    }
}
