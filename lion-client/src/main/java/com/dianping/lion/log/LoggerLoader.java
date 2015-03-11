/**
 * 
 */
package com.dianping.lion.log;

public class LoggerLoader {

    private LoggerLoader() {
    }

    private static volatile boolean customLog4j = false;
    private static volatile boolean initialed = false;

    static {
        try {
            Class.forName("org.apache.log4j.Hierarchy");
            customLog4j = true;
        } catch (ClassNotFoundException e) {
            customLog4j = false;
        }
    }

    public synchronized static void init() {
        if (!initialed && customLog4j) {
            CustomLog4jFactory.init();
            initialed = true;
        }
    }

}
