/**
 * Project: com.dianping.lion.lion-service-0.0.1
 *
 * File Created at 2012-7-28
 * $Id$
 *
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.register.zookeeper;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.Constants;
import com.dianping.lion.exception.ReadFromZookeeperException;
import com.dianping.lion.exception.RegisterToZookeeperException;
import com.dianping.lion.exception.UnregisterFromZookeeperException;
import com.dianping.lion.register.ConfigRegisterService;
import com.dianping.lion.util.EncodeUtils;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class ConfigZookeeperService implements ConfigRegisterService {

	private static final Logger logger = LoggerFactory.getLogger(ConfigZookeeperService.class);

	private int sessionTimeout = 60000;

	private final String serverIps;

	private String parentPath = "/DP/CONFIG";

	private String contextNode = "CONTEXTVALUE";

	private String timestampNode = "TIMESTAMP";

	private String charset = "UTF-8";

	private CuratorFramework curatorClient;

	public ConfigZookeeperService(String serverIps) {
		this.serverIps = serverIps;
	}

	@Override
	public void registerContextValue(final String key, final String value) {
		try {
			set(parentPath + "/" + key + "/" + contextNode, value);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Register config[" + key + "]'s context value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerAndPushContextValue(final String key, final String value) {
		try {
			//更新timestamp的操作应该在前面，如果设置值的操作在前面成功，而更新timestamp在后失败而认定注册配置失败的话，对上层判断会造成混淆
			//并且对于lion-client的客户端感知也很重要，key node变更后，如果判断timestamp的逻辑发生在更新之前就有问题，所以也需要先更新timestamp
			set(parentPath + "/" + key + "/" + timestampNode, System.currentTimeMillis());
			set(parentPath + "/" + key + "/" + contextNode, value);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Push config[" + key + "]'s context value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerDefaultValue(final String key, final String defaultVal) {
		try {
			set(parentPath + "/" + key, defaultVal);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Register config[" + key + "]'s default value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerAndPushDefaultValue(final String key, final String defaultVal) {
		try {
			//更新timestamp的操作应该在前面，如果设置值的操作在前面成功，而更新timestamp在后失败而认定注册配置失败的话，对上层判断会造成混淆
			set(parentPath + "/" + key + "/" + timestampNode, System.currentTimeMillis());
			set(parentPath + "/" + key, defaultVal);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Push config[" + key + "]'s default value to zookeeper failed.", e);
		}
	}

	@Override
	public void unregister(String key) {
		unregister(key, null);
	}

	@Override
	public String get(String key) {
		try {
			return new String(getData(parentPath + "/" + key), charset);
		} catch (NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new ReadFromZookeeperException("Read config[" + key + "] from zookeeper failed.", e);
		}
	}

	@Override
	public void destroy() {
		this.curatorClient.close();
	}

	public void set(String path, String value) throws Exception {
		if (value != null) {
		    value = SecurityUtils.tryDecode(value);
			set(path, value.getBytes(charset));
		} else {
			logger.warn("Set null config value to zk[" + StringUtils.substringBefore(this.serverIps, ",") + "] with path[" + path + "].");
		}
	}

	private void set(String path, long value) throws Exception {
		set(path, EncodeUtils.getLongBytes(value));
	}

	private void set(String path, byte[] bytes) throws Exception {
		if (exists(path)) {
			curatorClient.setData().forPath(path, bytes);
		} else {
		    curatorClient.create().creatingParentsIfNeeded().forPath(path, bytes);
		}
	}

	public void init() throws IOException {
	    curatorClient = CuratorFrameworkFactory.newClient(serverIps, sessionTimeout, 30*1000, 
                new RetryNTimes(Integer.MAX_VALUE, 1000));
        
        curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("lion zookeeper state changed to {}", newState);
            }
        });
        
        curatorClient.start();
	}

	/**
	 * @param sessionTimeout the sessionTimeout to set
	 */
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * @param parentPath the parentPath to set
	 */
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath.trim();
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @param contextNode the valueNode to set
	 */
	public void setContextNode(String contextNode) {
		this.contextNode = contextNode;
	}

	/**
	 * @param timestampNode the timestampNode to set
	 */
	public void setTimestampNode(String timestampNode) {
		this.timestampNode = timestampNode;
	}

	@Override
	public String getAddresses() {
		return this.serverIps;
	}

    @Override
    public void registerGroupValue(final String key, final String group, final String value) {
        try {
            String path = getPath(key, group);
            set(path, value);
        } catch (Exception e) {
            throw new RegisterToZookeeperException("Register config[" + key
                    + "]'s context value to zookeeper failed.", e);
        }
    }

    @Override
    public void registerAndPushGroupValue(final String key, final String group, final String value) {
        try {
            String path = getPath(key, group);
            String tsPath = getTimestampPath(path);
            //更新timestamp的操作应该在前面，如果设置值的操作在前面成功，而更新timestamp在后失败而认定注册配置失败的话，对上层判断会造成混淆
            //并且对于lion-client的客户端感知也很重要，key node变更后，如果判断timestamp的逻辑发生在更新之前就有问题，所以也需要先更新timestamp
            set(tsPath, System.currentTimeMillis());
            set(path, value);
        } catch (Exception e) {
            throw new RegisterToZookeeperException("Push config[" + key + "/" + group + "]'s context value to zookeeper failed.", e);
        }
    }

    @Override
    public void unregister(String key, String group) {
        try {
            String path = getPath(key, group);
            if (exists(path)) {
                curatorClient.delete().deletingChildrenIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            throw new UnregisterFromZookeeperException("Unregister config[" + key + "] from zookeeper failed.", e);
        }
    }

    @Override
    public String get(String key, String group) {
        try {
            String path = getPath(key, group);
            return new String(getData(path), charset);
        } catch (NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new ReadFromZookeeperException("Read config[" + key + "/" + group + "] from zookeeper failed.", e);
        }
    }

    private boolean exists(String path) throws Exception {
        return curatorClient.checkExists().forPath(path) != null;
    }
    
    private byte[] getData(String path) throws Exception {
        return curatorClient.getData().forPath(path);
    }
    
    private String getPath(String key, String group) {
        String path = Constants.CONFIG_PATH + Constants.PATH_SEPARATOR + key;
        if(StringUtils.isNotBlank(group)) {
            path = path + Constants.PATH_SEPARATOR + group;
        }
        return path;
    }

    private String getTimestampPath(String path) {
        return path + Constants.PATH_SEPARATOR + Constants.CONFIG_TIMESTAMP;
    }
}
