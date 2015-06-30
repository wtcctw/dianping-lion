package com.dianping.lion.client;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.lion.Constants;
import com.dianping.lion.util.KeyUtils;
import com.dianping.lion.util.KeyUtils.ConfigKey;

public class ZookeeperDataWatcher implements CuratorListener {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperDataWatcher.class);
    
    private ZookeeperConfigLoader configLoader;
    
    public ZookeeperDataWatcher(ZookeeperConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        if(event.getType() == CuratorEventType.WATCHED) {
            WatchedEvent watchedEvent = event.getWatchedEvent();
            if(watchedEvent.getPath() != null) {
                logger.info("zookeeper event received, path: {}, event {}", watchedEvent.getPath(), watchedEvent.getType());
                try {
                    process(watchedEvent);
                } catch(Exception e) {
                    logger.error("failed to process zookeeper event, path: " + watchedEvent.getPath(), e);
                }
            }
        }
    }
    
    public void process(WatchedEvent event) throws Exception {
        ConfigKey configKey = KeyUtils.parseConfigKey(event.getPath());
        
        if(configKey == null) {
            logger.info("failed to parse config event, path: " + event.getPath());
            return;
        }
        
        if (event.getType() == EventType.NodeCreated || event.getType() == EventType.NodeDataChanged) {
            processConfigChanged(configKey);
        } else if (event.getType() == EventType.NodeDeleted) {
            processConfigDeleted(configKey);
        }
    }
    
    private void processConfigChanged(ConfigKey configKey) throws Exception {
        if(isInterestedEvent(configKey)) {
            ZookeeperValue zkValue = null;
            switch(configKey.getChannel()) {
            case Constants.CHANNEL_DEFAULT:
                zkValue = configLoader.getDefaultValue(configKey.getKey());
                break;
            case Constants.CHANNEL_SWIMLANE:
                zkValue = configLoader.getSwimlaneValue(configKey.getKey());
                break;
            case Constants.CHANNEL_PROJECT_SPECIFIC:
                zkValue = configLoader.getProjectSpecificValue(configKey.getKey());
                break;
            }
            
            if(zkValue == null) {
                logger.error("failed to get value for key: " + configKey);
                return;
            }
            
            if(acceptChange(configKey.getKey(), zkValue)) {
                Cat.logEvent("lion", "config:changed", Message.SUCCESS, configKey.toString());
                configLoader.configChanged(configKey.getKey(), zkValue);
            } else {
                logger.info("ignored config event, key: " + configKey + ", value: " + zkValue);
            }
        } else {
            logger.info("ignored config event, key: " + configKey);
        }
    }
    
    private void processConfigDeleted(ConfigKey configKey) throws Exception {
        ZookeeperValue zkValue = configLoader.getZookeeperValue(configKey.getKey());
        if(zkValue == null) {
            Cat.logEvent("lion", "config:deleted", Message.SUCCESS, configKey.toString());
            configLoader.configDeleted(configKey.getKey());
        } else {
            Cat.logEvent("lion", "config:changed", Message.SUCCESS, configKey.toString());
            configLoader.configChanged(configKey.getKey(), zkValue);
        }
    }

    private boolean isInterestedEvent(ConfigKey configKey) {
        ZookeeperValue currentValue = configLoader.getKeyValueMap().get(configKey.getKey());
        return (currentValue == null || configKey.getChannel() >= currentValue.getChannel());
    }
    
    private boolean acceptChange(String key, ZookeeperValue currentValue) {
        if(StringUtils.isEmpty(currentValue.getValue()))
            return false;
        ZookeeperValue existValue = configLoader.getKeyValueMap().get(key);
        return (existValue == null || 
                currentValue.getTimestamp() > existValue.getTimestamp());
    }
    
}