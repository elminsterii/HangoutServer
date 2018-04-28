package com.fff.hos.tools;

import java.io.IOException;
import java.util.logging.Logger;

public class SysRunTool {

    private static final Logger LOGGER = Logger.getLogger(SysRunTool.class.getName());

    public static void sysRunMySQL() {
        final String EXE_SQLD = "mysqld.exe";
        String strDBPath = System.getProperty("sysDBPath");
        strDBPath += EXE_SQLD;

        String[] arrParameter = {"-d"};

        try {
            Runtime.getRuntime().exec(strDBPath, arrParameter);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static void sysLoginMySQLWithRoot() {
        final String EXE_SQLD = "mysql.exe";
        String strDBPath = System.getProperty("sysDBPath");
        strDBPath += EXE_SQLD;

        String[] arrParameter = {"-u", System.getProperty("sqlUserName")
                , "-p", System.getProperty("sqlUserPassword")};
        try {
            Runtime.getRuntime().exec(strDBPath, arrParameter);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
