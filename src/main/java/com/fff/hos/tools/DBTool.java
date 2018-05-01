package com.fff.hos.tools;

public class DBTool {
    public static boolean checkStringNotNull(String str) {
        boolean bRes = false;
        if (str != null && !str.isEmpty())
            bRes = true;
        return bRes;
    }
}
