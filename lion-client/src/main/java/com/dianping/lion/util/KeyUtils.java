package com.dianping.lion.util;

import com.dianping.lion.Constants;
import com.dianping.lion.Environment;
import com.dianping.lion.client.Lion;

public class KeyUtils {

    private static final String appName = Environment.getAppName();
    private static final String KEY_ARCH_COMPONENTS = "lion.arch.components";
    private static final String DEFAULT_ARCH_COMPONENTS = "pigeon,avatar-cache";
    private static String[] COMPONENTS = null;

    public static String[] getComponents() {
        if(COMPONENTS == null) {
            synchronized(KeyUtils.class) {
                if(COMPONENTS == null) {
                    String value = Lion.get(KEY_ARCH_COMPONENTS, DEFAULT_ARCH_COMPONENTS);
                    COMPONENTS = org.apache.commons.lang.StringUtils.split(value, ',');
                }
            }
        }
        return COMPONENTS;
    }

    public String fixKey(String key) {
        if (Constants.avatarBizKeySet.contains(key)) {
            key = Constants.avatarBizPrefix + key;
            return key;
        }

        // For keys like "{appName}.{archComponent}.xxx.xxx", regulate key to
        // "{archComponent}.xxx.xxx",
        // ConfigLoader will load application specific configurations
        if (isProjectSpecificComponentKey(key)) {
            key = key.substring(appName.length() + 1);
        }

        return key;
    }

    public static boolean isComponentKey(String key) {
        for (String component : COMPONENTS) {
            if (key.startsWith(component + ".")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProjectSpecificComponentKey(String key) {
        if (appName == null)
            return false;
        for (String component : COMPONENTS) {
            if (key.startsWith(appName + "." + component + ".")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProjectKey(String key) {
        if (appName == null)
            return false;
        return key.startsWith(appName + ".");
    }

    public static ConfigKey parseConfigKey(String path) {
        if (path == null || !path.startsWith(Constants.CONFIG_PATH)) {
            return null;
        }
        
        ConfigKey configKey = new ConfigKey();
        String key = path.substring(Constants.CONFIG_PATH.length() + 1);
        String swimlane = null;
        int channel = Constants.CHANNEL_DEFAULT;
        
        int idx = key.indexOf(Constants.PATH_SEPARATOR);
        if (idx != -1) {
            swimlane = key.substring(idx + 1);
            key = key.substring(0, idx);
            channel = Constants.CHANNEL_SWIMLANE;
        } else if(isProjectSpecificComponentKey(key)) {
            key = key.substring(appName.length() + 1);
            channel = Constants.CHANNEL_PROJECT_SPECIFIC;
        }
        
        configKey.setKey(key);
        configKey.setSwimlane(swimlane);
        configKey.setChannel(channel);
        return configKey;
    }

    public static class ConfigKey {
        private String key;
        private String swimlane;
        private int channel;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSwimlane() {
            return swimlane;
        }

        public void setSwimlane(String swimlane) {
            this.swimlane = swimlane;
        }

        public int getChannel() {
            return channel;
        }

        public void setChannel(int channel) {
            this.channel = channel;
        }

        public String toString() {
            return String.format("ConfigKey[key=%s, swimlane=%s, channel=%s]", key, swimlane, channel);
        }
    }

}
