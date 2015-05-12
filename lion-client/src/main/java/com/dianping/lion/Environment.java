package com.dianping.lion;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.Properties;

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
        props = PropertiesLoader.load("classpath:META-INF/app.properties");
        if(props != null) {
            appName = props.getProperty("app.name");
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
            props = PropertiesLoader.load(p);
        } else {
            // load from CLASSPATH
            props = PropertiesLoader.load("classpath:appenv");
        }
        
        // load from /data/webapps/appenv
        if(props == null) {
            props = PropertiesLoader.load("/data/webapps/appenv");
        }
        
        // using default values
        if(props == null) {
            logger.error(String.format("failed to find appenv file, default to env [%s], swimlane [%s], zkserver [%s]", 
                    Constants.DEFAULT_DEPLOYENV, Constants.DEFAULT_SWIMLANE, Constants.DEFAULT_ZKSERVER));
            props = getDefaultAppEnv();
        }
        
        checkAppEnv(props);
        logger.info(String.format("loaded appenv, env [%s], swimlane [%s], zkserver [%s]",
                deployenv, swimlane, zkserver));
        
        return props;
    }
    
    private static void checkAppEnv(Properties props) {
        deployenv = props.getProperty(Constants.KEY_DEPLOYENV);
        deployenv = trimToNull(deployenv);
        checkNotNull(deployenv, Constants.KEY_DEPLOYENV + " is null");
        zkserver = props.getProperty(Constants.KEY_ZKSERVER);
        zkserver = trimToNull(zkserver);
        checkNotNull(zkserver, Constants.KEY_ZKSERVER + " is null");
        swimlane = props.getProperty(Constants.KEY_SWIMLANE);
        swimlane = trimToNull(swimlane);
    }

    private static Properties getDefaultAppEnv() {
        Properties props = new Properties();
        props.put(Constants.KEY_DEPLOYENV, Constants.DEFAULT_DEPLOYENV);
        props.put(Constants.KEY_SWIMLANE, Constants.DEFAULT_SWIMLANE);
        props.put(Constants.KEY_ZKSERVER, Constants.DEFAULT_ZKSERVER);
        return props;
    }
    
    private static String trimToNull(String key) {
        if(key == null)
            return null;
        key = key.trim();
        return key.length() == 0 ? null : key;
    }
    
    public static void main(String[] args) {
        System.out.println(deployenv);
        System.out.println(zkserver);
        System.out.println(swimlane);        
    }
}
