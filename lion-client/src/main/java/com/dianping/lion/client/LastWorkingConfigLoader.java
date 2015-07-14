package com.dianping.lion.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.Environment;
import com.dianping.lion.util.KeyUtils;

public class LastWorkingConfigLoader extends AbstractConfigLoader {

    private static Logger logger = LoggerFactory.getLogger(LastWorkingConfigLoader.class);
    
    private Properties lastWorkingConfigs = null;
    
    @Override
    public void init() {
        String file = null;
        try {
            file = getLastWorkingConfigFile();
            if(file != null) {
                lastWorkingConfigs = loadLastWorkingConfigs(file);
            }
        } catch (Exception e) {
            logger.error("failed to load last working configs from " + file, e);
        }
    }

    private String getLastWorkingConfigFile() throws IOException {
        File latestFile = null;
        File dir = new File(ConfigCache.PERSIST_DIR);
        if(dir.exists()) {
            File[] files = dir.listFiles();
            String prefix = getPrefix() + ".properties.";
            for(File file : files) {
                if(file.getName().startsWith(prefix)) {
                    if(latestFile == null || file.lastModified() > latestFile.lastModified())
                        latestFile = file;
                }
            }
        }
        
        return latestFile == null ? null : latestFile.getCanonicalPath();
    }

    private String getPrefix() {
        return (Environment.getAppName() == null ? "config" : Environment.getAppName());
    }
    
    private Properties loadLastWorkingConfigs(String file) throws IOException {
        Properties props = new Properties();
        Reader reader = null;
        try {
            reader = new FileReader(file);
            props.load(reader);
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        for(String key : props.stringPropertyNames()) {
            if(KeyUtils.isSecretKey(key)) {
                String value = props.getProperty(key);
                value = KeyUtils.decode(value);
                props.setProperty(key, value);
            }
        }
        return props;
    }

    @Override
    public String get(String key) throws Exception {
        String value = null;
        if(lastWorkingConfigs != null) {
            value = lastWorkingConfigs.getProperty(key);
        }
        return value;
    }

    @Override
    public int getOrder() {
        return ORDER_LAST_WORKING;
    }

}
