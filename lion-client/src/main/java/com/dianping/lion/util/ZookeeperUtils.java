package com.dianping.lion.util;

import com.dianping.lion.Constants;

public class ZookeeperUtils {

    public static String getConfigPath(String key) {
        String path = Constants.CONFIG_PATH + Constants.PATH_SEPARATOR + key;
        return path;
    }
    
    public static String getConfigPath(String key, String group) {
        String path = Constants.CONFIG_PATH + Constants.PATH_SEPARATOR + key;
        if(group != null)
            path = path + Constants.PATH_SEPARATOR + group;
        return path;
    }
    
    public static String getTimestampPath(String path) {
        return path + Constants.PATH_SEPARATOR + Constants.CONFIG_TIMESTAMP;
    }
    
}
