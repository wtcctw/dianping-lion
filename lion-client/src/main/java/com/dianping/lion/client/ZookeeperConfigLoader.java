package com.dianping.lion.client;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.lion.Constants;
import com.dianping.lion.Environment;
import com.dianping.lion.util.EncodeUtils;
import com.dianping.lion.util.KeyUtils;
import com.dianping.lion.util.ZookeeperOperation;
import com.dianping.lion.util.ZookeeperUtils;

public class ZookeeperConfigLoader extends AbstractConfigLoader {

    private static final String KEY_SYNC_INTERVAL = "lion.sync.interval";
    private static final int DEFAULT_SYNC_INTERVAL = 120000;

    private static Logger logger = LoggerFactory.getLogger(ZookeeperConfigLoader.class);
    
    private String appName;
    private String swimlane;
    private String zookeeperAddress;
    private CuratorFramework curatorClient;
    private ZookeeperOperation zookeeperOperation;
    private ConcurrentMap<String, ZookeeperValue> keyValueMap;
    
    private volatile boolean isConnected;
    
    public ZookeeperConfigLoader() {
        zookeeperAddress = Environment.getZKAddress();
        Assert.notNull(zookeeperAddress, "zookeeper address is null");
        appName = Environment.getAppName();
        swimlane = Environment.getSwimlane();
        keyValueMap = new ConcurrentHashMap<String, ZookeeperValue>();
    }
    
    public void init() {
        curatorClient = CuratorFrameworkFactory.newClient(zookeeperAddress, 60*1000, 30*1000, 
                new RetryNTimes(Integer.MAX_VALUE, 1000));
        
        zookeeperOperation = new ZookeeperOperation(curatorClient);
        
        curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.warn("lion zookeeper state changed to {}", newState);
                if (newState == ConnectionState.CONNECTED) {
                    isConnected = true;
                } else if (newState == ConnectionState.RECONNECTED) {
                    isConnected = true;
                    Cat.logEvent("lion", "zookeeper:reconnected", Message.SUCCESS, zookeeperAddress);
                    try {
                        syncConfig();
                    } catch (Exception e) {
                        logger.error("failed to sync config with zookeeper", e);
                    }
                } else {
                    isConnected = false;
                }
            }
        });
        
        curatorClient.getCuratorListenable().addListener(new ZookeeperDataWatcher(this));
        
        curatorClient.start();
        
        try {
            curatorClient.getZookeeperClient().blockUntilConnectedOrTimedOut();
        } catch (Exception e) {
            logger.error("failed to connect to zookeeper: " + zookeeperAddress);
        }
        
        startConfigSyncThread();
    }

    private void startConfigSyncThread() {
        Thread t = new Thread(new ConfigSyncer(), "lion-config-sync");
        t.setDaemon(true);
        t.start();
    }
    
    @Override
    public String get(String key) throws Exception {
        try {
            ZookeeperValue zkValue = getZookeeperValue(key);
            
            if(zkValue != null) {
                keyValueMap.put(key, zkValue);
            } else {
                keyValueMap.put(key, ZookeeperValue.NOT_EXIST_VALUE);
            }
            
            return zkValue == null ? null : zkValue.getValue();
        } catch (Exception ex) {
            logger.error("failed to get value for key: {}" + key, ex);
            throw ex;
        }
    }
    
    public ZookeeperValue getZookeeperValue(String key) throws Exception {
        if(!isConnected) {
            throw new RuntimeException("lion zookeeper is not connected: " + zookeeperAddress);
        }
        
        ZookeeperValue zkValue = null;
        
        // Try to get application specific configs
        if(KeyUtils.isComponentKey(key)) {
            zkValue = getProjectSpecificValue(key);
        } else {
            if(swimlane != null) {
                // Try to get config for swimlane, if no value found, fall back to default
                zkValue = getSwimlaneValue(key);
            }

            // Swimlane is null or no config for swimlane, fall back to default
            if(zkValue == null) {
                zkValue = getDefaultValue(key);
            }
        }
        
        return zkValue;
    }
    
    public ZookeeperValue getProjectSpecificValue(String key) throws Exception {
        String appKey = appName + "." + key;
        ZookeeperValue zkValue = getValue(appKey, null);
        if(zkValue != null) {
            zkValue.setChannel(Constants.CHANNEL_PROJECT_SPECIFIC);
        }
        return zkValue;
    }
    
    public ZookeeperValue getSwimlaneValue(String key) throws Exception {
        if(swimlane == null) {
            return null;
        }
        
        ZookeeperValue zkValue = getValue(key, swimlane);
        if(zkValue != null) {
            zkValue.setChannel(Constants.CHANNEL_SWIMLANE);
        }
        return zkValue;
    }
    
    public ZookeeperValue getDefaultValue(String key) throws Exception {
        ZookeeperValue zkValue = getValue(key, null);
        if(zkValue != null) {
            zkValue.setChannel(Constants.CHANNEL_DEFAULT);
        }
        return zkValue;
    }
    
    private ZookeeperValue getValue(String key, String group) throws Exception {
        String path = ZookeeperUtils.getConfigPath(key, group);
        String timestampPath = ZookeeperUtils.getTimestampPath(path);
        ZookeeperValue zkValue = null;
        
        Transaction t = Cat.getProducer().newTransaction("lion", "get");
        try {
            byte[] data = zookeeperOperation.getDataWatched(path);
            
            if(data != null) {
                zkValue = new ZookeeperValue();
                zkValue.setValue(new String(data, Constants.CHARSET));
                data = zookeeperOperation.getData(timestampPath);
                if(data != null) {
                    Long timestamp = EncodeUtils.getLong(data);
                    zkValue.setTimestamp(timestamp);
                }
            }
            t.setStatus(Message.SUCCESS);
            return zkValue;
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
    }
    
    private class ConfigSyncer implements Runnable {

        private long lastSyncTime;
        private int syncInterval;
        
        public ConfigSyncer() {
            lastSyncTime = System.currentTimeMillis();
            try {
                String value = get(KEY_SYNC_INTERVAL);
                syncInterval = (value == null ? DEFAULT_SYNC_INTERVAL : Integer.parseInt(value));
            } catch (Exception e) {
                syncInterval = DEFAULT_SYNC_INTERVAL;
            }
        }
        
        @Override
        public void run() {
            int k = 0;
            while (true) {
                try {
                    long now = System.currentTimeMillis();
                    if (isConnected && (now - lastSyncTime > syncInterval)) {
                        syncConfig();
                        lastSyncTime = now;
                    } else {
                        Thread.sleep(1000);
                    }
                    k = 0;
                } catch (Exception e) {
                    k++;
                    if (k > 3) {
                        try {
                            Thread.sleep(5000);
                            k = 0;
                        } catch (InterruptedException ie) {
                            break;
                        }
                    }
                    logger.error("", e);
                }
            }
        }

    }

//    private void syncConfig() throws Exception {
//        Transaction t = Cat.getProducer().newTransaction("lion", "sync");
//        try {
//            for (Entry<String, String> entry : keyValueMap.entrySet()) {
//                String path = getRealConfigPath(entry.getKey(), swimlane);
//                String timestampPath = ZookeeperUtils.getTimestampPath(path);
//                
//                byte[] data = zookeeperOperation.getData(timestampPath);
//                if (data != null) {
//                    Long timestamp = EncodeUtils.getLong(data);
//                    Long timestamp_ = timestampMap.get(path);
//                    if (timestamp_ == null || timestamp > timestamp_) {
//                        timestampMap.put(path, timestamp);
//                        data = zookeeperOperation.getDataWatched(path);
//                        if (data != null) {
//                            String value = new String(data, Constants.CHARSET);
//                            Cat.logEvent("lion", "sync:changed", Message.SUCCESS, entry.getKey());
//                            fireConfigChanged(new ConfigEvent(entry.getKey(), value));
//                        }
//                    } else {
//                        zookeeperOperation.watch(path);
//                    }
//                }
//            }
//            t.setStatus(Message.SUCCESS);
//        } catch (Exception e) {
//            t.setStatus(e);
//            throw e;
//        } finally {
//            t.complete();
//        }
//    }
    
    private void syncConfig() throws Exception {
        Transaction t = Cat.getProducer().newTransaction("lion", "sync");
        try {
            for (Entry<String, ZookeeperValue> entry : keyValueMap.entrySet()) {
                String key = entry.getKey();
                ZookeeperValue currentZkValue = entry.getValue();
                
                ZookeeperValue fetchedZkValue = getZookeeperValue(key);
                if(fetchedZkValue != null && !fetchedZkValue.equals(currentZkValue)) {
                    Cat.logEvent("lion", "config:changed:sync", Message.SUCCESS, key + ":" + fetchedZkValue.getValue());
                    configChanged(key, fetchedZkValue);
                }
            }
            t.setStatus(Message.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
    }
    
    public ConcurrentMap<String, ZookeeperValue> getKeyValueMap() {
        return keyValueMap;
    }
    
    public void configChanged(String key, ZookeeperValue zkValue) {
        keyValueMap.put(key, zkValue);
        logger.info(">>>>>>config changed, key: " + key + ", value: " + escape(key, zkValue.getValue()));
        fireConfigChanged(new ConfigEvent(key, zkValue.getValue()));
    }
    
    public void configDeleted(String key) {
        ZookeeperValue previousValue = keyValueMap.put(key, ZookeeperValue.NOT_EXIST_VALUE);
        logger.info(">>>>>>config deleted, key: " + key + ", previous value: " + previousValue);
    }
    
    private void fireConfigChanged(ConfigEvent configEvent) {
        if(configListener != null) {
            configListener.configChanged(configEvent);
        }
    }
    
    private String escape(String key, String value) {
        if(key == null)
            return limitLength(value);
        if(key.toLowerCase().contains("password")) 
            return "********";
        return limitLength(value);
    }

    private String limitLength(String value) {
        if(value == null || value.length() <= 100)
            return value;
        return value.substring(0, 100);
    }
    
    public int getOrder() {
        return ORDER_ZOOKEEPER;
    }

}
