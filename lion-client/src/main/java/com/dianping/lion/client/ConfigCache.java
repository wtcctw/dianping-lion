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

import com.dianping.lion.Constants;
import com.dianping.lion.Environment;
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

	private static Logger logger = LoggerFactory.getLogger(ConfigCache.class);

	private static volatile ConfigCache instance;

	private static Map<String, String> cache = new ConcurrentHashMap<String, String>();

	private static Map<String, Long> timestampMap = new ConcurrentHashMap<String, Long>();

	private CuratorFramework curatorClient;

	private List<ConfigChange> changeList = new CopyOnWriteArrayList<ConfigChange>();

	private String address;

	private Properties localProps;

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
                if (newState == ConnectionState.RECONNECTED) {
                    try {
                        watchAll();
                    } catch(Exception e) {
                        logger.error("failed to watch all lion key", e);
                    }
                }
            }
        });
        
	    curatorClient.getCuratorListenable().addListener(new ConfigDataWatcher());
        
	    curatorClient.start();
		
		startCheckConfigThread();
	}
	
	private void watchAll() throws Exception {
	    for(String key : cache.keySet()) {
	        String path = getConfigPath(key);
	        watch(path);
	    }
	}
	
	private void watch(String path) throws Exception {
	    curatorClient.checkExists().watched().forPath(path);
	}
	
	private void startCheckConfigThread() {
	    Thread t = new Thread(new CheckConfig(), "lion-config-check");
        t.setDaemon(true);
        t.start();
	}
	
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
	
	// FIXME if key is null, should throw NullPointerException
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
	        logger.warn("{} does not exist", path);
	        return null;
	    }
	}
	
	private byte[] getDataWatched(String path) throws Exception {
	    try {
    	    byte[] data = curatorClient.getData().watched().forPath(path);
            return data;
    	} catch(NoNodeException e) {
            logger.warn("{} does not exist", path);
            return null;
        }
	}
	
	private String getGroup() {
	    String group = getAppenv(Constants.KEY_SWIMLANE);
	    return trimToNull(group);
	}

	private String getConfigPath(String key) throws LionException {
	    return getConfigPath(key, null);
	}
	
	private String getConfigPath(String key, String group) throws LionException {
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

	//TODO: test the getDate returned value if it's a empty path
	private String getValue(String key, String group) throws Exception {
	    String path = getConfigPath(key, group);
        String timestampPath = getTimestampPath(path);

        if( existsWatched(path) ) {
            String value = new String(getData(path), Constants.CHARSET);
            // Cache key <-> value
            cache.put(key, value);
            // Update path <-> timestamp
            if( exists(timestampPath) ) {
                Long timestamp = EncodeUtils.getLong(getData(timestampPath));
                timestampMap.put(path, timestamp);
            }
            return value;
        }

        logger.info("path {} does not exist", path);
        return null;
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
                process(event.getWatchedEvent());
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
					
					if( exists(tsPath) ) {
						Long timestamp = EncodeUtils.getLong(getData(tsPath));
						Long timestamp_ = timestampMap.get(path);
						if (timestamp_ == null || timestamp > timestamp_) {
							timestampMap.put(path, timestamp);
							byte[] data = getDataWatched(path);
							if (data != null) {
								String value = new String(data, Constants.CHARSET);
								cache.put(key, value);
								synchronized (value) {
									logger.info(">>>>>>>>>>>>config changed, key: {}, value: {}", key, value);
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
				}
			} else if (event.getType() == EventType.NodeDeleted) {
                try {
                    String key = getKey(event.getPath());
                    cache.remove(key);
                    watch(event.getPath());
                } catch (Exception ex) {
                    logger.error("", ex);
                }
			}
		}

		private String getKey(String path) throws Exception {
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

		private String getGroup(String path) throws Exception {
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

	private class CheckConfig implements Runnable {

		private long lastTime;
		private String group;
		
		public CheckConfig() throws LionException {
			lastTime = System.currentTimeMillis();
			group = getGroup();
		}
		
		@Override
		public void run() {
			logger.debug("CheckConfig thread started, swimlane is " + group);
            
			int k = 0;
			while (true) {
				try {
					long now = System.currentTimeMillis();
					if (now - this.lastTime > 60000) {
						for (Entry<String, String> entry : cache.entrySet()) {
							synchronized (entry.getValue()) {
							    String path = getConfigPath(entry.getKey());
							    if(group != null) {
							        String pp = getConfigPath(entry.getKey(), group);
							        if(exists(pp))
							            path = pp;
							    }
								String timestampPath = getTimestampPath(path);
								if (exists(timestampPath)) {
									Long timestamp = EncodeUtils.getLong(getData(timestampPath));
									Long timestamp_ = timestampMap.get(path);
									if (timestamp_ == null || timestamp > timestamp_) {
										timestampMap.put(path, timestamp);
										if (exists(path)) {
											String value = new String(getData(path), Constants.CHARSET);

											if (!value.equals(entry.getValue())) {
												entry.setValue(value);
												logger.info(">>>>>>>>>>>>config changed, key: {}, value: {}", entry.getKey(), value);
												if (changeList != null) {
													for (ConfigChange change : changeList) {
														change.onChange(entry.getKey(), value);
													}
												}
											}
										}
									}
								}
							}
						}
						this.lastTime = now;
					} else {
						Thread.sleep(2000);
					}
					k = 0;
				} catch (Exception e) {
					k++;
					if (k > 3) {
						try {
							Thread.sleep(2000);
							k = 0;
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					logger.error("", e);
				}
			}
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
