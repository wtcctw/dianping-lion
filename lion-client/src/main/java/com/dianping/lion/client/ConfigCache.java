/**
 *
 */
package com.dianping.lion.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.Constants;
import com.dianping.lion.Environment;
import com.dianping.lion.log.LoggerLoader;
import com.dianping.lion.util.KeyUtils;
import com.dianping.lion.util.StringUtils;

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
public class ConfigCache implements ConfigListener {

    static {
        LoggerLoader.init();
    }
    
	private static Logger logger = LoggerFactory.getLogger(ConfigCache.class);
	
	private static volatile ConfigCache instance;

	private static ConcurrentMap<String, String> cachedConfig = new ConcurrentHashMap<String, String>();

	private List<ConfigChange> changeList = new CopyOnWriteArrayList<ConfigChange>();

	private String address;

	private Properties localProps;
	
    private String propertiesPath = Constants.DEFAULT_PROPERTIES_PATH;
    private boolean includeLocalProps = Constants.DEFAULT_INCLUDE_LOCAL_PROPS; // 是否使用本地的propertiesPath指定的配置文件中的配置

	private ConfigLoaderManager configLoader;
	
	private String appName = Environment.getAppName();
	
	public String getAppenv(String key) {
		return Environment.getProperty(key);
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
	    System.setProperty(ConfigLoader.KEY_PROPERTIES_FILE, propertiesPath);
	    System.setProperty(ConfigLoader.KEY_INCLUDE_LOCAL_PROPS, "" + includeLocalProps);
	    configLoader = ConfigLoaderManager.getInstance();
	    configLoader.addConfigListener(this);
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
		key = StringUtils.trimToNull(key);
		if(key == null)
		    throw new LionException("key is null");
		
		key = fixKey(key);
		
		String value = cachedConfig.get(key);
		if(value == null) {
		    try {
                value = configLoader.get(key);
                if(value != null) 
                    cachedConfig.put(key, value);
            } catch (Exception e) {
                throw new LionException(e);
            }
		}

        return value;
	}

	private String fixKey(String key) {
	    if (Constants.avatarBizKeySet.contains(key)) {
            key = Constants.avatarBizPrefix + key;
            return key;
        }
	    
	    if(appName == null) {
	        return key;
	    }
	    
	    // For keys like "{appName}.{archComponent}.xxx.xxx", regulate key to "{archComponent}.xxx.xxx",
	    // ConfigLoader will load application specific configurations
	    for(String archComponent : KeyUtils.getComponents()) {
    	    if(key.startsWith(appName + "." + archComponent + ".")) {
    	        return key.substring(appName.length() + 1);
    	    }
	    }
	    
	    return key;
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

    
	/**
	 * @param change
	 *           the change to set
	 */
	public void addChange(ConfigChange change) {
		this.changeList.add(change);
	}
	
	public void removeChange(ConfigChange change) {
	    this.changeList.remove(change);
	}

	/**
	 * @return the zookeeper address
	 */
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPts(Properties pts) {
		this.localProps = pts;
	}

	public Properties getPts() {
		return localProps;
	}
	
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }
    
    public String getPropertiesPath() {
        return propertiesPath;
    }

    public void setIncludeLocalProps(boolean includeLocalProps) {
        this.includeLocalProps = includeLocalProps;
    }
    
    public boolean getIncludeLocalProps() {
        return includeLocalProps;
    }

    @Override
    public void configChanged(ConfigEvent configEvent) {
        for(ConfigChange configChange : changeList) {
            configChange.onChange(configEvent.getKey(), configEvent.getValue());
        }
    }

}
