package com.fff.hos.tools;

import java.io.IOException;
import java.util.logging.Logger;

public class SysRunTool {

    private static final Logger LOGGER = Logger.getLogger(SysRunTool.class.getName());

    public void sysRunMySQL() {
        final String EXE_SQLD = "mysqld.exe";
        String strDBPath = System.getProperty("aqlLocalDBFolder");
        strDBPath += EXE_SQLD;

        try {
            Runtime.getRuntime().exec(strDBPath);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
