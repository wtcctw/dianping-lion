package com.dianping.lion.client;

public class Lion {

    private static ConfigCache configCache = ConfigCache.getInstance();
    
    /*
     * Synonym for getStringValue()
     */
    public static String get(String key) {
        return configCache.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : value;
    }
    
    public static String getStringValue(String key) {
        return configCache.getProperty(key);
    }

    public static String getStringValue(String key, String defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : value;
    }
    
    public static byte getByteValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Byte.parseByte(value);
    }
    
    public static byte getByteValue(String key, byte defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Byte.parseByte(value);
    }
    
    public static short getShortValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Short.parseShort(value);
    }

    public static short getShortValue(String key, short defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Short.parseShort(value);
    }

    public static int getIntValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Integer.parseInt(value);
    }
    
    public static int getIntValue(String key, int defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public static long getLongValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Long.parseLong(value);
    }

    public static long getLongValue(String key, long defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    public static float getFloatValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Float.parseFloat(value);
    }

    public static float getFloatValue(String key, float defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Float.parseFloat(value);
    }
    
    public static double getDoubleValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Double.parseDouble(value);
    }
    
    public static double getDoubleValue(String key, double defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Double.parseDouble(value);
    }

    public static boolean getBooleanValue(String key) {
        String value = configCache.getProperty(key);
        if(value == null) {
            throw new LionException("config [" + key + "] does not exist");
        }
        return Boolean.parseBoolean(value);
    }

    public static boolean getBooleanValue(String key, boolean defaultValue) {
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public static void addConfigChangeListener(ConfigChange configChange) {
        configCache.addChange(configChange);
    }
    
    public static void removeConfigChangeListener(ConfigChange configChange) {
        configCache.removeChange(configChange);
    }
    
}
