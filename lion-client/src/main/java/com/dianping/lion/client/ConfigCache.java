/**
 *
 */
package com.dianping.lion.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.lion.Constants;
import com.dianping.lion.Environment;
import com.dianping.lion.log.LoggerLoader;
import com.dianping.lion.util.EncodeUtils;

/**
 * <p>
 * Title: ConfigCache.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 *
 * @author saber miao
 * @version 1.0
 * @created 2010-12-30 下午06:12:36
 */
public class ConfigCache {

    static {
        LoggerLoader.init();
    }
    
	private static Logger logger = LoggerFactory.getLogger(ConfigCache.class);
	
	private static final String KEY_SYNC_INTERVAL = "lion.sync.interval";
	private static final int DEFAULT_SYNC_INTERVAL = 120000;
	
	private static final String MAGIC_VALUE = "~!@#$%^&*()_+";

	private static volatile ConfigCache instance;

	private static Map<String, String> cache = new ConcurrentHashMap<String, String>();

	private static Map<String, Long> timestampMap = new ConcurrentHashMap<String, Long>();

	private CuratorFramework curatorClient;

	private List<ConfigChange> changeList = new CopyOnWriteArrayList<ConfigChange>();

	private String address;

	private Properties localProps;
	
	private volatile boolean isConnected = false;

	public String getAppenv(String key) {
		return Environment.getProperty(key);
	}

	void reset() {
	    instance = null;
		cache.clear();
		timestampMap.clear();
		changeList.clear();
	}

	/**
	 * 如果已经使用SpringConfig配置为Spring Bean，或者用getInstance(String address)成功调用过一次，
	 * 则用此方法可以成功返回ConfigCache，否则必须使用getInstance(String address)获取ConfigCache
	 *
	 * @return
	 * @throws LionException
	 */
	public static ConfigCache getInstance() throws LionException {
		if (instance == null) {
		    instance = getInstance(Environment.getZKAddress());
		}
		return instance;
	}

	/**
	 * @param address
	 *           ip地址和端口，如127.0.0.1：2181，192.168.8.21：2181
	 * @return
	 * @throws LionException
	 */
	public static ConfigCache getInstance(String address) throws LionException {
		if (instance == null) {
			synchronized (ConfigCache.class) {
				if (instance == null) {
					try {
						ConfigCache cc = new ConfigCache();
						cc.setAddress(address);
						cc.init();
						instance = cc;
					} catch (Exception e) {
						logger.error("Failed to initialize ConfigCache", e);
						throw new LionException(e);
					}
				}
			}
		} else {
			if (!address.equals(instance.getAddress())) {
				throw new LionException("ConfigCache has been initialized with address " + address);
			}
		}
		return instance;
	}

	private void init() throws Exception {
	    curatorClient = CuratorFrameworkFactory.newClient(address, 60*1000, 30*1000, 
                new RetryNTimes(Integer.MAX_VALUE, 1000));
        
	    curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("lion zookeeper state changed to {}", newState);
                if (newState == ConnectionState.CONNECTED) {
                    isConnected = true;
                } else if (newState == ConnectionState.RECONNECTED) {
                    isConnected = true;
                    try {
                        Cat.logEvent("lion", "zookeeper:reconnected", Message.SUCCESS, address);
                        syncConfig();
                    } catch(Exception e) {
                        logger.error("failed to watch all lion key", e);
                    }
                } else {
                    isConnected = false;
                }
            }
        });
        
	    curatorClient.getCuratorListenable().addListener(new ConfigDataWatcher());
        
	    curatorClient.start();
		
		startConfigSyncThread();
	}
	
	private void watch(String path) throws Exception {
	    curatorClient.checkExists().watched().forPath(path);
	}
	
	private void startConfigSyncThread() {
	    Thread t = new Thread(new ConfigSyncer(), "lion-config-sync");
        t.setDaemon(true);
        t.start();
	}
	
	@Deprecated
	public void loadProperties(String propertiesFile) throws IOException {
	    localProps = new Properties();
	    InputStream in = null;
	    try {
	        in = new FileInputStream(propertiesFile);
	        localProps.load(in);
	    } catch(IOException e) {
	        logger.error("failed to load properties file " + propertiesFile);
	        throw e;
	    } finally {
	        if(in != null) {
	            try {
                    in.close();
                } catch (IOException e) {
                }
	        }
	    }
	}
	
	public String getProperty(String key) throws LionException {
		key = trimToNull(key);
		Assert.notNull(key, "invalid key, key is empty");
		
		if (Constants.avatarBizKeySet.contains(key)) {
			key = Constants.avatarBizPrefix + key;
		}
		
		String v = null;
		if (this.localProps != null) {
			v = this.localProps.getProperty(key);
			if (v != null && !v.startsWith("${") && !v.endsWith("}")) {
				return v;
			}
			if (v != null && v.startsWith("${") && v.endsWith("}")) {
				v = v.substring(2);
				v = v.substring(0, v.length() - 1);
				return getZkValue(v);
			}
		}
		
		if (v == null) {
			v = getZkValue(key);
		}
		
		return v;
	}

	private static String trimToNull(String key) {
	    if(key == null)
	        return null;
	    key = key.trim();
	    return key.length() == 0 ? null : key;
	}
	
	private String getZkValue(String key) throws LionException {
		String value = cache.get(key);

		if (value == null) {
		    try {
    		    String group = getGroup();
    		    if(group != null) {
    		        // Try to get config for the group, if no value found, fall back to default
                    value = getValue(key, group);
    		    }

    		    // Group is null or no config for the group, fall back to default
    	        if(value == null) {
                    value = getValue(key);
    	        }
		    } catch (Exception ex) {
		        logger.error("failed to get value for key: " + key, ex);
		        throw new LionException(ex);
		    }
		}

		if(MAGIC_VALUE.equals(value)) {
		    return null;
		}
		return value;
	}

	private boolean exists(String path) throws Exception {
        Stat stat = curatorClient.checkExists().forPath(path);
        return stat != null;
    }

	private boolean existsWatched(String path) throws Exception {
	    Stat stat = curatorClient.checkExists().watched().forPath(path);
	    return stat != null;
	}

	private byte[] getData(String path) throws Exception {
	    try {
	        byte[] data = curatorClient.getData().forPath(path);
	        return data;
	    } catch(NoNodeException e) {
	        // logger.warn("{} does not exist", path);
	        return null;
	    }
	}
	
	private byte[] getDataWatched(String path) throws Exception {
	    try {
    	    byte[] data = curatorClient.getData().watched().forPath(path);
            return data;
    	} catch(NoNodeException e) {
            // logger.warn("{} does not exist", path);
            curatorClient.checkExists().watched().forPath(path);
            return null;
        }
	}
	
	private String getGroup() {
	    String group = getAppenv(Constants.KEY_SWIMLANE);
	    return trimToNull(group);
	}

	private String getConfigPath(String key) {
	    return getConfigPath(key, null);
	}
	
	private String getConfigPath(String key, String group) {
	    String path = Constants.CONFIG_PATH + Constants.PATH_SEPARATOR + key;
	    if(group != null)
	        path = path + Constants.PATH_SEPARATOR + group;
	    return path;
	}
	
	private String getTimestampPath(String path) {
		return path + Constants.PATH_SEPARATOR + Constants.CONFIG_TIMESTAMP;
	}
	

	private String getValue(String key) throws Exception {
	    return getValue(key, null);
	}

	private String getValue(String key, String group) throws Exception {
	    String path = getConfigPath(key, group);
        String timestampPath = getTimestampPath(path);
        String value = null;
        
        Transaction t = Cat.getProducer().newTransaction("lion", "get");
        try {
            byte[] data = getDataWatched(path);
            
            if(data != null) {
                value = new String(data, Constants.CHARSET);
                // Cache key <-> value
                cache.put(key, value);
                // Cache path <-> timestamp
                data = getData(timestampPath);
                if(data != null) {
                    Long timestamp = EncodeUtils.getLong(data);
                    timestampMap.put(path, timestamp);
                }
            } else {
                cache.put(key, MAGIC_VALUE);
            }
            t.setStatus(Message.SUCCESS);
            return value;
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
	}

	public Long getLongProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Long.parseLong(value);
	}

	public Integer getIntProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Integer.parseInt(value);
	}

	public Short getShortProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Short.parseShort(value);
	}

	public Byte getByteProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Byte.parseByte(value);
	}

	public Float getFloatProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Float.parseFloat(value);
	}

	public Double getDoubleProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Double.parseDouble(value);
	}

	public Boolean getBooleanProperty(String key) throws LionException {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		return Boolean.parseBoolean(value);
	}

	private class ConfigDataWatcher implements CuratorListener {

		public ConfigDataWatcher() {
		}

        @Override
        public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
            if(event.getType() == CuratorEventType.WATCHED) {
                WatchedEvent we = event.getWatchedEvent();
                if(we.getPath() != null) {
                    logger.info("zookeeper event received, path: {}, event {}", event.getPath(), event.getType());
                    process(event.getWatchedEvent());
                }
            }
        }
        
		public void process(WatchedEvent event) {
		    if (event.getType() == EventType.NodeCreated || event.getType() == EventType.NodeDataChanged) {
			    try {
    		        String key = getKey(event.getPath());
    		        String group = getGroup(event.getPath());
    
    			    if(isFiltered(key, group)) {
    			        return;
    			    }
    			    
    			    String path = event.getPath();
    			    String tsPath = getTimestampPath(path);
				
    			    byte[] data = getData(tsPath);
    			    if(data != null) {
    			        Long timestamp = EncodeUtils.getLong(data);
                        Long timestamp_ = timestampMap.get(path);
                        if (timestamp_ == null || timestamp > timestamp_) {
                            timestampMap.put(path, timestamp);
                            data = getDataWatched(path);
                            if (data != null) {
                                String value = new String(data, Constants.CHARSET);
                                cache.put(key, value);
                                synchronized (value) {
                                    logger.info(">>>>>>config changed, key: {}, value: {}", key, escape(key, value));
                                    Cat.logEvent("lion", "zookeeper:changed", Message.SUCCESS, key);
                                    if(ConfigCache.this.changeList != null) {
                                        for (ConfigChange change : ConfigCache.this.changeList) {
                                            change.onChange(key, value);
                                        }
                                    }
                                }
                            }
                        } else {
                            watch(path);
                        }
    			    } else {
    			        watch(path);
    			    }
				} catch (Exception ex) {
				    logger.error("", ex);
				    try {
                        watch(event.getPath());
                    } catch (Exception e) {
                        logger.error("", e);
                    }
				}
			} else if (event.getType() == EventType.NodeDeleted) {
                try {
                    String key = getKey(event.getPath());
                    cache.remove(key);
                    timestampMap.remove(event.getPath());
                    watch(event.getPath());
                    // if swimlane node is deleted, watch both the default node
                    String swimlane = getGroup(event.getPath());
                    if(swimlane != null) {
                        watch(getConfigPath(key));
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
			}
		}
		
		private String getKey(String path) {
		    if(path == null || !path.startsWith(Constants.CONFIG_PATH)) {
		        return null;
		    }
		    String key = path.substring(Constants.CONFIG_PATH.length() + 1);
		    int idx = key.indexOf(Constants.PATH_SEPARATOR);
		    if(idx != -1) {
		        key = key.substring(0, idx);
		    }
		    return key;
		}

		private String getGroup(String path) {
		    String group = ConfigCache.this.getGroup();
		    if(group == null) {
		        return null;
		    }
		    return path.endsWith(Constants.PATH_SEPARATOR + group) ? group : null;
		}

		private boolean isFiltered(String key, String group) throws Exception {
		    String swimlane = ConfigCache.this.getGroup();

		    if(swimlane==null && group==null)
		        return false;
		    if(swimlane==null && group!=null)
		        return true;
		    if(swimlane!=null && group==null) {
		        String pp = getConfigPath(key, swimlane);
		        return exists(pp);
		    }
		    if(swimlane!=null && group!=null)
		        return false;
		    return true;
		}

	}

    private String escape(String key, String value) {
        if(key == null)
            return value;
        if(key.toLowerCase().contains("password")) 
            return "********";
        return value;
    }
    
	private class ConfigSyncer implements Runnable {

		private long lastSyncTime;
	    private int syncInterval;
	    
	    public ConfigSyncer() {
	        lastSyncTime = System.currentTimeMillis();
	        try {
                String value = getValue(KEY_SYNC_INTERVAL);
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

	private String getRealConfigPath(String key, String group) throws Exception {
	    String path = getConfigPath(key);
        if(group != null) {
            String path_ = getConfigPath(key, group);
            if(exists(path_))
                path = path_;
        }
        return path;
	}
	
    private void syncConfig() throws Exception {
        Transaction t = Cat.getProducer().newTransaction("lion", "sync");
        try {
            String group = getGroup();
            for (Entry<String, String> entry : cache.entrySet()) {
                synchronized (entry.getValue()) {
                    String path = getRealConfigPath(entry.getKey(), group);
                    String timestampPath = getTimestampPath(path);
                    
                    byte[] data = getData(timestampPath);
                    if (data != null) {
                        Long timestamp = EncodeUtils.getLong(data);
                        Long timestamp_ = timestampMap.get(path);
                        if (timestamp_ == null || timestamp > timestamp_) {
                            timestampMap.put(path, timestamp);
                            data = getDataWatched(path);
                            if (data != null) {
                                String value = new String(data, Constants.CHARSET);
    
                                if (!value.equals(entry.getValue())) {
                                    entry.setValue(value);
                                    logger.info(">>>>>>config changed, key: {}, value: {}", entry.getKey(), escape(entry.getKey(), value));
                                    Cat.logEvent("lion", "sync:changed", Message.SUCCESS, entry.getKey());
                                    if (changeList != null) {
                                        for (ConfigChange change : changeList) {
                                            change.onChange(entry.getKey(), value);
                                        }
                                    }
                                }
                            }
                        } else {
                            watch(path);
                        }
                    }
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
    
	/**
	 * @param change
	 *           the change to set
	 */
	// FIXME return this is better
	public void addChange(ConfigChange change) {
		this.changeList.add(change);
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *           the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param pts
	 *           the pts to set
	 */
	public void setPts(Properties pts) {
		this.localProps = pts;
	}

	public Properties getPts() {
		return localProps;
	}

}
