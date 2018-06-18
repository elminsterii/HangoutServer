package com.fff.hos.startup;

import com.fff.hos.gcs.StorageManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class StartStopListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(StartStopListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        StorageManager sm = new StorageManager();
        sm.initialize();

        System.out.println("System has been started.");
        LOGGER.info("System has been started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("System has been stopped.");
        LOGGER.info("System has been stopped.");
    }
}
