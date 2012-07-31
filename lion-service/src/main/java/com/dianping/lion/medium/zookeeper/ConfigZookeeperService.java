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
package com.dianping.lion.medium.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.client.zookeeper.SessionRecoverableZookeeper;
import com.dianping.lion.client.zookeeper.ZookeeperConstants;
import com.dianping.lion.exception.ReadFromZookeeperException;
import com.dianping.lion.exception.RegisterToZookeeperException;
import com.dianping.lion.exception.UnregisterFromZookeeperException;
import com.dianping.lion.medium.ConfigRegisterService;
import com.dianping.lion.util.EncodeUtils;

/**
 * TODO Comment of ConfigZookeeperService
 * @author danson.liu
 *
 */
public class ConfigZookeeperService implements ConfigRegisterService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigZookeeperService.class);
	
	private final String serverIps;
	
	private int sessionTimeout = ZookeeperConstants.DEFAULT_SESSION_TIMEOUT;
	
	private String parentPath = ZookeeperConstants.PATH_CONFIG;
	
	private String contextNode = ZookeeperConstants.NODE_CONTEXTVAL;
	
	private String timestampNode = ZookeeperConstants.NODE_TIMESTAMP;
	
	private String charset = ZookeeperConstants.CONFIG_CHARSET;
	
	private SessionRecoverableZookeeper zookeeper;
	
	private boolean parentPathExistsEnsured;
	private Set<String> existsEnsuredPaths = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	public ConfigZookeeperService(String serverIps) {
		this.serverIps = serverIps;
	}

	@Override
	public void registerContextValue(String key, String value) {
		try {
			ensureParentPathExists();
			ensurePathExists(parentPath + "/" + key);
			set(parentPath + "/" + key + "/" + contextNode, value);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Register config[" + key + "]'s context value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerAndPushContextValue(String key, String value) {
		try {
			ensureParentPathExists();
			ensurePathExists(parentPath + "/" + key);
			set(parentPath + "/" + key + "/" + timestampNode, System.currentTimeMillis());
			set(parentPath + "/" + key + "/" + contextNode, value);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Push config[" + key + "]'s context value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerDefaultValue(String key, String defaultVal) {
		try {
			ensureParentPathExists();
			set(parentPath + "/" + key, defaultVal);
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Register config[" + key + "]'s default value to zookeeper failed.", e);
		}
	}

	@Override
	public void registerAndPushDefaultValue(String key, String defaultVal) {
		try {
			ensureParentPathExists();
			set(parentPath + "/" + key, defaultVal);
			set(parentPath + "/" + key + "/" + timestampNode, System.currentTimeMillis());
		} catch (Exception e) {
			throw new RegisterToZookeeperException("Push config[" + key + "]'s default value to zookeeper failed.", e);
		}
	}

	@Override
	public void unregister(String key) {
		try {
			String path = parentPath + "/" + key;
			if (zookeeper.exists(path, false) != null) {
				List<String> children = zookeeper.getChildren(path, false);
				if (children != null && !children.isEmpty()) {
					for (String child : children) {
						try {
							zookeeper.delete(path + "/" + child, -1);
						} catch (NoNodeException e) {
							//do nothing
						}
					}
				}
				try {
					zookeeper.delete(path, -1);
				} catch (NoNodeException e) {
					//do nothing
				}
			}
		} catch (Exception e) {
			throw new UnregisterFromZookeeperException("Unregister config[" + key + "] from zookeeper failed.", e);
		}
	}

	@Override
	public String get(String key) {
		try {
			return new String(zookeeper.getData(parentPath + "/" + key, false, null), charset);
		} catch (NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new ReadFromZookeeperException("Read config[" + key + "] from zookeeper failed.", e);
		}
	}

	@Override
	public void destroy() {
		try {
			this.zookeeper.close();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void ensurePathExists(String path) throws KeeperException, InterruptedException, IOException {
		if (!existsEnsuredPaths.contains(path)) {
			if (zookeeper.exists(path, false) == null) {
				zookeeper.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			existsEnsuredPaths.add(path);
		}
	}
	
	private void set(String path, String value) throws UnsupportedEncodingException, KeeperException, InterruptedException, IOException {
		set(path, value.getBytes(charset));
	}
	
	private void set(String path, long value) throws KeeperException, InterruptedException, IOException {
		set(path, EncodeUtils.getLongBytes(value));
	}
	
	private void set(String path, byte[] bytes) throws KeeperException, InterruptedException, IOException {
		if (zookeeper.exists(path, false) == null) {
			zookeeper.create(path, bytes, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			zookeeper.setData(path, bytes, -1);
		}
	}

	public void init() throws IOException {
		this.zookeeper = new SessionRecoverableZookeeper(serverIps, this.sessionTimeout, null);
		ensureParentPathExists();
	}

	private void ensureParentPathExists() {
		try {
			if (!parentPathExistsEnsured) {
				int fromIndex = 1;
				int indexOfSlash = -1;
				while ((indexOfSlash = parentPath.indexOf('/', fromIndex)) != -1) {
					ensurePathExists(parentPath.substring(0, indexOfSlash));
					fromIndex = indexOfSlash + 1;
				}
				ensurePathExists(parentPath);
				parentPathExistsEnsured = true;
			}
		} catch (Exception e) {
			logger.warn("Ensure zookeeper's config initial path is failed, pay attention.", e);
		}
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

}
