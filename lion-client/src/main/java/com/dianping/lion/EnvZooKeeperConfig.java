package com.dianping.lion;

public class EnvZooKeeperConfig {
    
    public static String getEnv() {
        return Environment.getEnv();
    }

    public static String getZKAddress() {
        return Environment.getZKAddress();
    }
    
    public static String getSwimlane() {
        return Environment.getSwimlane();
    }

}
