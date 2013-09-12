/**
 *
 */
package com.dianping.lion.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.dianping.lion.Constants;
import com.dianping.lion.Utils;

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

	private static Logger logger = Logger.getLogger(ConfigCache.class);

	private static ConfigCache configCache;

	private static Map<String, StringValue> cache = new ConcurrentHashMap<String, StringValue>();

	private static Map<String, Long> timestampMap = new ConcurrentHashMap<String, Long>();

	private ZooKeeperWrapper zk;

	private ConfigDataWatcher configDataWatcher = new ConfigDataWatcher();

	private List<ConfigChange> changeList = new ArrayList<ConfigChange>(); // CopyOnWriteArrayList

	private boolean isInit = false;

	private int timeout = 60000;

	private String address;

	private String avatarBizPrefix = "avatar-biz.";

	private Properties pts;

	private Properties env;

	public String getAppenv(String key) {
		if (env != null) {
			return env.getProperty(key);
		} else {
			return null;
		}
	}

	private static class StringValue {
		String value;

		StringValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	void reset() {
	    configCache = null;
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
		if (configCache == null) {
			throw new LionException(
			      "you must config SpringConfig or use getInstance(String address) before this operation");
		}
		return configCache;
	}

	static ConfigCache getMockInstance() {
		configCache = new ConfigCache();
		return configCache;
	}

	/**
	 * @param address
	 *           ip地址和端口，如127.0.0.1：2181，192.168.8.21：2181
	 * @return
	 * @throws LionException
	 */
	public static ConfigCache getInstance(String address) throws LionException {
		if (configCache == null) {
			synchronized (cache) {
				if (configCache == null) {
					try {
						configCache = new ConfigCache();
						configCache.setAddress(address);
						configCache.init();
					} catch (IOException e) {
						throw new LionException(e);
					}
				}
			}
		} else {
			if (!address.equals(configCache.getAddress())) {
				throw new LionException("error address:" + address);
			}
		}
		return configCache;
	}

	private void init() throws IOException {
		if (!this.isInit) {
			this.zk = new ZooKeeperWrapper(this.address, this.timeout, new ZookeeperSessionWatcher());
			try {
				if (this.zk.exists(Constants.DP_PATH, false) == null) {
					this.zk.create(Constants.DP_PATH, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				if (this.zk.exists(Constants.CONFIG_PATH, false) == null) {
					this.zk.create(Constants.CONFIG_PATH, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				Thread t = new Thread(new CheckConfig());
				t.setDaemon(true);
				t.start();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			this.isInit = true;
		}

		FileInputStream fis = null;
		try {
			env = new Properties();
			fis = new FileInputStream(Constants.ENVIRONMENT_FILE);
			env.load(fis);
		} catch (Exception e) {
			logger.warn("No environment file at location " + Constants.ENVIRONMENT_FILE, e);
		} finally {
		    if(fis != null) {
		        try {
		            fis.close();
		        } catch(Exception ex) {
		            logger.error("Failed to close file " + Constants.ENVIRONMENT_FILE);
		        }
		    }
		}
	}

	// FIXME if key is null, should throw NullPointerException
	public String getProperty(String key) throws LionException {
		if (key != null) {
			key = key.trim();
		} else {
			return null;
		}
		if (Constants.avatarBizKeySet.contains(key)) {
			key = this.avatarBizPrefix + key;
		}
		String v = null;
		if (this.pts != null) {
			v = this.pts.getProperty(key);
			if (v != null && !v.startsWith("${") && !v.endsWith("}")) {
				return v;
			}
			if (v != null && v.startsWith("${") && v.endsWith("}")) {
				v = v.substring(2);
				v = v.substring(0, v.length() - 1);
				return getZKValue(v);
			}
		}
		if (v == null) {
			v = getZKValue(key);
		}
		return v;
	}

	private String getZKValue(String key) throws LionException {
		StringValue value = cache.get(key);

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
		        logger.error("Failed to get value for key: " + key, ex);
		        throw new LionException(ex);
		    }
		}

		return value != null ? value.getValue() : null;
	}

	private boolean exists(String path) throws Exception {
        Stat stat = zk.exists(path, false);
        return stat != null;
    }

	private boolean exists(String path, Watcher watcher) throws Exception {
	    Stat stat = zk.exists(path, watcher);
	    return stat != null;
	}

	private String getGroup() throws LionException {
	    String group = ConfigCache.getInstance().getAppenv(Constants.KEY_SWIMLANE);
	    if(group == null || group.trim().length() == 0)
            return null;
	    return group.trim();
	}

	private String getConfigPath(String key) throws LionException {
	    return getConfigPath(key, null);
	}

	private String getTimestampPath(String path) {
	    return path + Constants.PATH_SEPARATOR + Constants.CONFIG_TIMESTAMP;
	}
	private String getConfigPath(String key, String group) throws LionException {
	    String path = Constants.CONFIG_PATH + Constants.PATH_SEPARATOR + key;
	    if(group != null)
	        path = path + Constants.PATH_SEPARATOR + group;
	    return path;
	}

	private StringValue getValue(String key) throws Exception {
	    return getValue(key, null);
	}

	private StringValue getValue(String key, String group) throws Exception {
	    String path = getConfigPath(key, group);
        String timestampPath = getTimestampPath(path);

        if( exists(path, configDataWatcher) ) {
            StringValue value = new StringValue(new String(this.zk.getData(path, configDataWatcher, null), Constants.CHARSET));
            // Cache key <-> value
            cache.put(key, value);
            // Update path <-> timestamp
            if( exists(timestampPath) ) {
                Long timestamp = Utils.getLong(this.zk.getData(timestampPath, false, null));
                timestampMap.put(path, timestamp);
            }
            return value;
        }

        logger.warn("Path does not exist: " + path);
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

	class ZookeeperSessionWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			if (event.getState() == KeeperState.Expired) {
				logger.info("Zookeeper session expried, reconnecting...");
				try {
					zk.reconnectSession();
					logger.info("Zookeeper session reconnected");
				} catch (Exception e) {
					logger.error("Zookeeper session reconnect failed", e);
				}
				return;
			}
		}
	}

	private class ConfigDataWatcher implements Watcher {

		public ConfigDataWatcher() {
		}

		@Override
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
					zk.removeWatcher(path);
					if( exists(tsPath) ) {
						Long timestamp = Utils.getLong(zk.getData(tsPath, false, null));
						Long timestamp_ = timestampMap.get(path);
						if (timestamp_ == null || timestamp > timestamp_) {
							timestampMap.put(path, timestamp);
							byte[] data = zk.getData(path, this, null);
							if (data != null) {
								String value = new String(data, Constants.CHARSET);
								StringValue sv = cache.get(key);
								if (sv == null) {
									sv = new StringValue(value);
									cache.put(key, sv);
								}
								synchronized (sv) {
									sv.value = value;
									logger.info(">>>>>>>>>>>>Key changed " + key + " group is " + group);
									if(ConfigCache.this.changeList != null) {
										for (ConfigChange change : ConfigCache.this.changeList) {
											change.onChange(key, value);
										}
									}
								}
							}
						} else {
							zk.getData(path, this, null);
						}
					} else {
						zk.getData(path, this, null);
					}
				} catch (Exception ex) {
					logger.error("", ex);
				}
			} else if (event.getType() == EventType.NodeDeleted) {
                try {
                    String key = getKey(event.getPath());
                    cache.remove(key);
                } catch (Exception ex) {
                    logger.error("", ex);
                }
			}
		}

		private String getKey(String path) throws Exception {
		    int idx = path.lastIndexOf(Constants.PATH_SEPARATOR);
		    if(idx == -1)
		        return null;
		    String key = path.substring(idx + 1);
		    if(key.equals(ConfigCache.this.getGroup())) {
		        int idx2 = path.lastIndexOf(Constants.PATH_SEPARATOR, idx-1);
		        if(idx2 != -1)
		            key = path.substring(idx2+1, idx);
		    }
		    return key;
		}

		private String getGroup(String path) throws Exception {
		    String group = ConfigCache.this.getGroup();
		    if(path.endsWith(Constants.PATH_SEPARATOR + group))
		        return group;
		    return null;
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

		private long lastTime = System.currentTimeMillis();

		@Override
		public void run() {
			String group = null;
			try {
                group = getGroup();
            } catch (LionException ex) {
                logger.error("Failed to get group", ex);
            }

            int k = 0;
			while (true) {
				try {
					long now = System.currentTimeMillis();
					if (now - this.lastTime > 20000) {
						for (Entry<String, StringValue> entry : cache.entrySet()) {
							synchronized (entry.getValue()) {
							    String path = getConfigPath(entry.getKey());
							    if(group != null) {
							        String pp = getConfigPath(entry.getKey(), group);
							        if(exists(pp))
							            path = pp;
							    }
								String timestampPath = getTimestampPath(path);
								if (exists(timestampPath)) {
									Long timestamp = Utils.getLong(zk.getData(timestampPath, false, null));
									Long timestamp_ = timestampMap.get(path);
									if (timestamp_ == null || timestamp > timestamp_) {
										timestampMap.put(path, timestamp);
										if (exists(path)) {
											String value = new String(zk.getData(path, false, null), Constants.CHARSET);

											if (!value.equals(entry.getValue().value)) {
												entry.getValue().value = value;
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
					logger.error(e.getMessage(), e);
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
		this.pts = pts;
	}

	public Properties getPts() {
		return pts;
	}

	/**
	 * @return the zk
	 */
	public ZooKeeperWrapper getZk() {
		return zk;
	}

	public void setZk(ZooKeeperWrapper zk) {
		this.zk = zk;
	}
}
