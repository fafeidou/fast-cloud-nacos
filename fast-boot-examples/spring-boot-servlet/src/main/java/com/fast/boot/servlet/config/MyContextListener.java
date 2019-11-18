package com.fast.boot.servlet.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;

@WebListener
public class MyContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("app startup at " + LocalDateTime.now().toString());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

}