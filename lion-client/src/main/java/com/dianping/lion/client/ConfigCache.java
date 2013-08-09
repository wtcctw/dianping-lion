/**
 * 
 */
package com.dianping.lion.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import com.dianping.lion.Constants;
import com.dianping.lion.Utils;

/**
 * <p>
 * Title: ConfigPuller.java
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
			this.zk = new ZooKeeperWrapper(this.address, this.timeout, new ConfigWatcher());
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

		try {
			env = new Properties();
			FileInputStream fis = new FileInputStream("/data/webapps/appenv");
			env.load(fis);
		} catch (Exception e) {
			logger.warn("no appenv in this machine", e);
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
			String path = Constants.CONFIG_PATH + "/" + key;
			String timestampPath = path + "/" + Constants.CONFIG_TIMESTAMP;
			try {
				if (this.zk.exists(path, false) != null) {
					Watcher watcher = new ConfigDataWatcher(path, timestampPath, key);
					value = new StringValue(new String(this.zk.getData(path, watcher, null), Constants.CHARSET));
					cache.put(key, value);
					logger.info(">>>>>>>>>>>>getProperty key:" + key + "  value:" + value.getValue());
					if (this.zk.exists(timestampPath, false) != null) {
						Long timestamp = Utils.getLong(this.zk.getData(timestampPath, false, null));
						timestampMap.put(path, timestamp);
					}
				} else {
					cache.put(key, new StringValue(null));
					logger.info(">>>>>>>>>>>getProperty key:" + key + "   value is null*******");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new LionException(e);
			}
		}
		if (value != null) {
			return value.getValue();
		}
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

	class ConfigWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			if (event.getState() == KeeperState.Expired) {
				logger.info("Session Expried init,Invoke ZooKeeperWrapper method!");
				try {
					zk.sessionExpiredReConnect();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				return;
			}
		}
	}

	private class ConfigDataWatcher implements Watcher {

		private String path;

		private String timestampPath;

		private String key;

		public ConfigDataWatcher(String path, String timestampPath, String key) {
			this.path = path;
			this.timestampPath = timestampPath;
			this.key = key;
		}

		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == EventType.NodeCreated || event.getType() == EventType.NodeDataChanged) {
				try {
					zk.removeWatcher(this.path);
					if (zk.exists(this.timestampPath, false) != null) {
						Long timestamp = Utils.getLong(zk.getData(this.timestampPath, false, null));
						Long timestamp_ = timestampMap.get(this.path);
						if (timestamp_ == null || timestamp > timestamp_) {
							timestampMap.put(this.path, timestamp);
							byte[] data = zk.getData(this.path, this, null);
							if (data != null) {
								String value = new String(data, Constants.CHARSET);
								logger.info(">>>>>>>>>>>>pushProperty key:" + key + "  value:" + value);
								StringValue sv = cache.get(this.key);
								if (sv == null) {
									sv = new StringValue(value);
									cache.put(this.key, sv);
								}
								synchronized (sv) {
									sv.value = value;
									if (ConfigCache.this.changeList != null) {
										for (ConfigChange change : ConfigCache.this.changeList) {
											change.onChange(this.key, value);
										}
									}
								}
							}
						} else {
							zk.getData(this.path, this, null);
						}
					} else {
						zk.getData(this.path, this, null);
					}
				} catch (KeeperException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			} else if (event.getType() == EventType.NodeDeleted) {
				cache.remove(this.key);
			}
		}

	}

	private class CheckConfig implements Runnable {

		private long lastTime = System.currentTimeMillis();

		@Override
		public void run() {
			int k = 0;
			while (true) {
				try {
					long now = System.currentTimeMillis();
					if (now - this.lastTime > 20000) {
						for (Entry<String, StringValue> entry : cache.entrySet()) {
							synchronized (entry.getValue()) {

								String path = Constants.CONFIG_PATH + "/" + entry.getKey();
								String timestampPath = path + "/" + Constants.CONFIG_TIMESTAMP;
								if (zk.exists(timestampPath, false) != null) {
									Long timestamp = Utils.getLong(zk.getData(timestampPath, false, null));
									Long timestamp_ = timestampMap.get(path);
									if (timestamp_ == null || timestamp > timestamp_) {
										timestampMap.put(path, timestamp);
										if (zk.exists(path, false) != null) {
											String value = new String(zk.getData(path, false, null), Constants.CHARSET);

											if (!value.equals(entry.getValue().value)) {
												logger.info(">>>>>>>>>>>>syncProperty key:" + entry.getKey() + "  value:" + value);
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
