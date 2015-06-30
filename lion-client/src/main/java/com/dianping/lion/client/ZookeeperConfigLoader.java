package com.dianping.lion.client;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static final String KEY_FAIL_LIMIT = "lion.zookeeper.fail.limit";
    private static final int DEFAULT_FAIL_LIMIT = 3600;

    private static Logger logger = LoggerFactory.getLogger(ZookeeperConfigLoader.class);
    
    private String appName;
    private String swimlane;
    private String zookeeperAddress;
    private ConcurrentMap<String, ZookeeperValue> keyValueMap;

    private CuratorFramework curatorClient;
    private ZookeeperOperation zookeeperOperation;
    private ZookeeperDataWatcher zookeeperDataWatcher;
    
    private AtomicBoolean isSyncing = new AtomicBoolean(false);
    private volatile long lastSyncTime = System.currentTimeMillis();
    private volatile int syncInterval;
    private volatile int failLimit;
    private volatile int failCount = 0;
    
    public ZookeeperConfigLoader() {
        zookeeperAddress = Environment.getZKAddress();
        Assert.notNull(zookeeperAddress, "zookeeper address is null");
        appName = Environment.getAppName();
        swimlane = Environment.getSwimlane();
        keyValueMap = new ConcurrentHashMap<String, ZookeeperValue>();
    }
    
    public void init() {
        zookeeperDataWatcher = new ZookeeperDataWatcher(this);
        zookeeperOperation = new ZookeeperOperation();
        
        curatorClient = newCuratorClient();
        zookeeperOperation.setCuratorClient(curatorClient);
        
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
            
            keyValueMap.put(key, zkValue == null ? ZookeeperValue.NOT_EXIST_VALUE : zkValue);
            
            return zkValue == null ? null : zkValue.getValue();
        } catch (Exception ex) {
            logger.error("failed to get value for key: " + key, ex);
            throw ex;
        }
    }
    
    public ZookeeperValue getZookeeperValue(String key) throws Exception {
        ZookeeperValue zkValue = null;
        
        // Try to get application specific configs
        if(KeyUtils.isComponentKey(key)) {
            zkValue = getProjectSpecificValue(key);
            
            if(zkValue == null) {
                zkValue = getDefaultValue(key);
            }
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
        
        Transaction t = Cat.newTransaction("lion", "get");
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

        public ConfigSyncer() {
            try {
                String value = get(KEY_SYNC_INTERVAL);
                syncInterval = (value == null ? DEFAULT_SYNC_INTERVAL : Integer.parseInt(value));
            } catch (Exception e) {
                syncInterval = DEFAULT_SYNC_INTERVAL;
            }
            try {
                String value = get(KEY_FAIL_LIMIT);
                failLimit = (value == null ? DEFAULT_FAIL_LIMIT : Integer.parseInt(value));
            } catch (Exception e) {
                failLimit = DEFAULT_FAIL_LIMIT;
            }
        }
        
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(1000);
                    checkZookeeper();
                    trySyncConfig(false);
                } catch (InterruptedException e) {
                    logger.warn("lion config sync thread is interrupted");
                    break;
                }
            }
        }
        
        private void checkZookeeper() {
            boolean isConnected = false;
            try {
                isConnected = curatorClient.getZookeeperClient().getZooKeeper().getState().isConnected();
            } catch(Exception e) {
                if(failCount % 10 == 0)
                    logger.error("failed to check zookeeper status", e);
            }
            if(isConnected) {
                failCount = 0;
            } else {
                if(++failCount >= failLimit) {
                    try {
                        renewCuratorClient();
                        failCount = 0;
                        logger.info("renewed curator client to " + zookeeperAddress);
                        Cat.logEvent("lion", "zookeeper:renewSuccess", Message.SUCCESS, "" + failLimit);
                    } catch (Exception e) {
                        failCount = failCount / 2;
                        logger.error("failed to renew curator client", e);
                        Cat.logEvent("lion", "zookeeper:renewFailure", Message.SUCCESS, e.getMessage());
                    }
                }
            }
        }
    }
    
    private void renewCuratorClient() throws Exception {
        CuratorFramework newCuratorClient = newCuratorClient();
        CuratorFramework oldCuratorClient = this.curatorClient;
        this.curatorClient = newCuratorClient;
        zookeeperOperation.setCuratorClient(newCuratorClient);
        trySyncConfig(true);
        if(oldCuratorClient != null) {
            try {
                oldCuratorClient.close();
            } catch(Exception e) {
                logger.error("failed to close curator client: " + e.getMessage());
            }
        }
    }
    
    private CuratorFramework newCuratorClient() {
        CuratorFramework curatorClient = CuratorFrameworkFactory.newClient(
                zookeeperAddress, 60*1000, 30*1000, new RetryNTimes(3, 1000));
        
        curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("lion zookeeper state: " + newState);
                Cat.logEvent("lion", "zookeeper:" + newState);
                if (newState == ConnectionState.RECONNECTED) {
                    try {
                        trySyncConfig(true);
                    } catch (Exception e) {
                        logger.error("failed to sync lion config", e);
                    }
                }
            }
        });
        
        curatorClient.getCuratorListenable().addListener(zookeeperDataWatcher);
        
        curatorClient.start();
        
        try {
            curatorClient.getZookeeperClient().blockUntilConnectedOrTimedOut();
        } catch (Exception e) {
            logger.error("failed to connect to zookeeper: " + zookeeperAddress);
        }
        
        return curatorClient;
    }

    private void trySyncConfig(boolean force) {
        long now = System.currentTimeMillis();
        if (force || (now - lastSyncTime >= syncInterval)) {
            if (isSyncing.compareAndSet(false, true)) {
                try {
                    syncConfig();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    logger.error("failed to sync lion config", e);
                } finally {
                    lastSyncTime = System.currentTimeMillis();
                    isSyncing.set(false);
                }
            }
        }
    }

    private void syncConfig() throws Exception {
        if(curatorClient.getZookeeperClient().isConnected()) {
            Transaction t = Cat.getProducer().newTransaction("lion", "sync");
            try {
                for (Entry<String, ZookeeperValue> entry : keyValueMap.entrySet()) {
                    String key = entry.getKey();
                    ZookeeperValue currentZkValue = entry.getValue();
                    
                    ZookeeperValue fetchedZkValue = getZookeeperValue(key);
                    if(fetchedZkValue != null && !fetchedZkValue.equals(currentZkValue)) {
                        Cat.logEvent("lion", "config:changed:sync", Message.SUCCESS, 
                                key + ":" + escape(key, fetchedZkValue.getValue()));
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
