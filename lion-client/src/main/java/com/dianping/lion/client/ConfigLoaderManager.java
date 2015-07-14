package com.dianping.lion.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.util.KeyUtils;

//TODO: config persistent to disk, load failover configs from disk
public class ConfigLoaderManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoaderManager.class);
    
    private List<ConfigLoader> configLoaders;
    
    ConfigLoaderManager() {
        init();
    }
    
    public void init() {
        ServiceLoader<ConfigLoader> serviceLoader = ServiceLoader.load(ConfigLoader.class);
        configLoaders = new ArrayList<ConfigLoader>();
        for(ConfigLoader configLoader : serviceLoader) {
            configLoader.init();
            configLoaders.add(configLoader);
        }
        
        Collections.sort(configLoaders, new Comparator<ConfigLoader>() {
            @Override
            public int compare(ConfigLoader o1, ConfigLoader o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }
    
    public String get(String key) throws Exception {
        String value = null;
        for(ConfigLoader configLoader : configLoaders) {
            value = configLoader.get(key);
            if(value == null) {
                continue;
            }
            
            if(isReferenceValue(value)) {
                key = getReferencedKey(value);
            } else {
                break;
            }
        }
        if(logger.isDebugEnabled()) {
            logger.debug("loaded config: " + key + " => " + KeyUtils.escape(key, value));
        }
        return value;
    }
    
    private boolean isReferenceValue(String value) {
        return (value != null && 
                value.startsWith(ConfigLoader.DEFAULT_PLACEHOLDER_PREFIX) && 
                value.endsWith(ConfigLoader.DEFAULT_PLACEHOLDER_SUFFIX));
    }
    
    private String getReferencedKey(String value) {
        return value.substring(2, value.length() - 1);
    }
    
    public void addConfigListener(ConfigListener configListener)  {
        for(ConfigLoader configLoader : configLoaders) {
            configLoader.addConfigListener(configListener);
        }
    }
    
}
