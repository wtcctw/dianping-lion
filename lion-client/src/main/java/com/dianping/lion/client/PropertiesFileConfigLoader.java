package com.dianping.lion.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.Environment;
import com.dianping.lion.util.PropertiesLoader;

public class PropertiesFileConfigLoader extends AbstractConfigLoader {

    private static Logger logger = LoggerFactory.getLogger(PropertiesFileConfigLoader.class);
    
    private String propertiesFile;
    private boolean includeLocalProps = false;
    private String environment;
    private Properties properties;

    public PropertiesFileConfigLoader() {
        this.propertiesFile = System.getProperty(KEY_PROPERTIES_FILE, DEFAULT_PROPERTIES_FILE);
        if(propertiesFile == null) {
            return;
        }
        String value = System.getProperty(KEY_INCLUDE_LOCAL_PROPS, DEFAULT_INCLUDE_LOCAL_PROPS);
        if(value != null && value.toLowerCase().equals("true")) {
            includeLocalProps = true;
        }
        environment = Environment.getEnv();
    }
    
    public void init() {
        if(propertiesFile == null) {
            return;
        }
        try {
            properties = PropertiesLoader.loadFromClassPath(propertiesFile);
        } catch (IOException e) {
            logger.warn("failed to load properties file", e);
            return;
        }
        if(properties == null) {
            return;
        }
        List<String> keyList = new ArrayList<String>();
        if (!environment.equalsIgnoreCase("dev") && !environment.equalsIgnoreCase("alpha") && !includeLocalProps) {
            for (Object key : properties.keySet()) {
                String value = properties.getProperty((String) key);
                if (!(value.startsWith(DEFAULT_PLACEHOLDER_PREFIX) && value.endsWith(DEFAULT_PLACEHOLDER_SUFFIX))) {
                    keyList.add((String) key);
                }
            }
        }
        for (String key : keyList) {
            properties.remove(key);
            logger.info("ignore key " + key + " in file " + propertiesFile + " in env " + environment);
        }
    }

    @Override
    public String get(String key) {
        if(properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    public int getOrder() {
        return ORDER_PROPERTIES_FILE;
    }
}
