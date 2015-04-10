package com.dianping.lion.util;

public class StringUtils {
    
    public static String trimToNull(String key) {
        if(key == null)
            return null;
        key = key.trim();
        return key.length() == 0 ? null : key;
    }
    
}
