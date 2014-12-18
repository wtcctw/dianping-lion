package com.dianping.lion;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvZooKeeperConfig {
    
    private static Logger logger = LoggerFactory.getLogger(EnvZooKeeperConfig.class);
    
    private static String FILENAME = "appenv";
    private static String DEFAULT_LOCATION = "/data/webapps/appenv";
    
    private static String KEY_ENV = "deployenv";
    private static String KEY_ZKSERVER = "zkserver";
    private static String KEY_SWIMLANE = "swimlane";
    private static String DEFAULT_ENV = "dev";
    private static String DEFAULT_ZKSERVER = "dev.lion.dp:2181";
    private static String DEFAULT_SWIMLANE = "";
    
    private static String PATH_JAR = "jar:file:";
    private static String PATH_WAR = "WEB-INF/lib";
    private static String JARPATHSUF = "/com/dianping/lion/EnvZooKeeperConfig.class";

    private static String env;
    private static String zkAddress;
    private static String swimlane;

    static {
        File envFile = null;
        FileReader fileReader = null;
        Properties props = null;
        try {
            envFile = getEnvFile();
            if(envFile == null) {
                logger.warn("failed to find appenv file at " + DEFAULT_LOCATION + ", use default env dev");
                env = DEFAULT_ENV;
                zkAddress = DEFAULT_ZKSERVER;
                swimlane = DEFAULT_SWIMLANE;
            } else {
                fileReader = new FileReader(envFile);
                props = new Properties();
                props.load(fileReader);
                
                env = props.getProperty(KEY_ENV);
                zkAddress = props.getProperty(KEY_ZKSERVER);
                swimlane = props.getProperty(KEY_SWIMLANE);
                
                if(env == null || zkAddress == null) {
                    throw new RuntimeException("Invalid env file: " + envFile);
                }
                
                env = env.trim();
                zkAddress = zkAddress.trim();
                if(swimlane == null) {
                    swimlane = DEFAULT_SWIMLANE;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    private static File getEnvFile() {
        String path = EnvZooKeeperConfig.class.getClassLoader()
                .getResource("com/dianping/lion/EnvZooKeeperConfig.class").toString();
        
        int indexOfJar = path.indexOf(PATH_JAR);
        int indexOfWar = path.indexOf(PATH_WAR);
        
        if (indexOfJar == -1) {
            throw new RuntimeException("Failed to find EnvZooKeeperConfig.class");
        }
        
        if (indexOfWar == -1) {
            indexOfWar = path.indexOf(JARPATHSUF);
            path = path.substring(PATH_JAR.length(), indexOfWar);
            int indexOfLast = path.lastIndexOf('/');
            path = path.substring(0, indexOfLast + 1);
        } else {
            path = path.substring(PATH_JAR.length(), indexOfWar);
        }
        
        File file = new File(path + FILENAME);
        if(!file.exists())
            file = new File(DEFAULT_LOCATION);
        
        if(!file.exists())
            return null;
        
        return file;
    }
    
    public static String getEnv() {
        return env;
    }

    public static String getZKAddress() {
        return zkAddress;
    }
    
    public static String getSwimlane() {
        return swimlane;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(KEY_ENV + "\t: " + EnvZooKeeperConfig.getEnv());
        System.out.println(KEY_ZKSERVER + "\t: " + EnvZooKeeperConfig.getZKAddress());
        System.out.println(KEY_SWIMLANE + "\t: " + EnvZooKeeperConfig.getSwimlane());
    }

}
