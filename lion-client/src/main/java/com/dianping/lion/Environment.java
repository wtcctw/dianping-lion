package com.dianping.lion;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.util.PropertiesLoader;

public class Environment {

    private static Logger logger = LoggerFactory.getLogger(Environment.class);
    
    private static Properties props = null;
    private static String appName = null;
    private static String deployenv = null;
    private static String swimlane = null;
    private static String zkserver = null;
    
    static {
        try {
            props = PropertiesLoader.load("classpath:META-INF/app.properties");
        } catch (FileNotFoundException e) {
            logger.warn("app.properties doesn't exist");
        } catch (Throwable e) {
            logger.error("failed to load app.properties", e);
        }
        if(props != null) {
            appName = props.getProperty("app.name");
            appName = StringUtils.trimToNull(appName);
        } else {
            props = new Properties();
        }
        props.putAll(loadAppEnv());
    }
    
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public static String getAppName() {
        return appName;
    }
    
    public static String getEnv() {
        return deployenv;
    }
    
    public static String getSwimlane() {
        return swimlane;
    }
    
    public static String getZKAddress() {
        return zkserver;
    }
    
    public static Properties loadAppEnv() {
        Properties props = null;
        
        URL url = Environment.class.getProtectionDomain().getCodeSource().getLocation();
//        URL url = Bar.class.getResource(Bar.class.getSimpleName() + ".class");
        String path = url.getPath();
        int idx = path.indexOf("WEB-INF");
        if(idx != -1) {
            // load from WAR_ROOT
            String p = path.substring(0, idx) + "appenv";
            try {
                props = PropertiesLoader.load(p);
            } catch (FileNotFoundException e) {
                logger.warn(p + " doesn't exist");
            } catch (Throwable e) {
                logger.error("failed to load " + p, e);
            }
        } else {
            // load from CLASSPATH
            try {
                props = PropertiesLoader.load("classpath:appenv");
            } catch (FileNotFoundException e) {
                logger.warn("appenv doesn't exist in classpath");
            } catch (Throwable e) {
                logger.error("failed to load appenv from classpath", e);
            }
        }
        
        // load from /data/webapps/appenv
        if(props == null) {
            try {
                props = PropertiesLoader.load("/data/webapps/appenv");
            } catch (FileNotFoundException e) {
                logger.warn("/data/webapps/appenv doesn't exist");
            } catch (Throwable e) {
                logger.error("failed to load /data/webapps/appenv", e);
            }
        }
        
        // using default values
        if(props == null) {
            props = getDefaultAppEnv();
        }
        
        checkAppEnv(props);
        logger.info("loaded appenv, env [%s], swimlane [%s], zkserver [%s]",
                new String[] {deployenv, swimlane, zkserver});
        return props;
    }
    
    private static void checkAppEnv(Properties props) {
        deployenv = props.getProperty(Constants.KEY_DEPLOYENV);
        deployenv = StringUtils.trimToNull(deployenv);
        checkNotNull(deployenv, Constants.KEY_DEPLOYENV + " is null");
        zkserver = props.getProperty(Constants.KEY_ZKSERVER);
        zkserver = StringUtils.trimToNull(zkserver);
        checkNotNull(zkserver, Constants.KEY_ZKSERVER + " is null");
        swimlane = props.getProperty(Constants.KEY_SWIMLANE);
        swimlane = StringUtils.trimToNull(swimlane);
    }

    private static Properties getDefaultAppEnv() {
        Properties props = new Properties();
        props.put(Constants.KEY_DEPLOYENV, Constants.DEFAULT_DEPLOYENV);
        props.put(Constants.KEY_ZKSERVER, Constants.DEFAULT_ZKSERVER);
        return props;
    }
    
    public static void main(String[] args) {
        System.out.println(deployenv);
        System.out.println(zkserver);
        System.out.println(swimlane);        
    }
}
