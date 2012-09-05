/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-1
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
package com.dianping.lion.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.SystemSettingService;

/**
 * @author danson.liu
 *
 */
public abstract class CacheClientSupport implements CacheClient {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String ALL_CACHE = "all";
	
	@Autowired
	private SystemSettingService settingService;
	
	private String cacheEntities;
	
	private static ThreadLocal<Map<CacheClient, List<String>>> cacheRemoved = new InheritableThreadLocal<Map<CacheClient,List<String>>>() {
		protected Map<CacheClient, List<String>> initialValue() {
			return new HashMap<CacheClient, List<String>>();
		};
	};
	
	public static Map<CacheClient, List<String>> getCacheRemoved() {
		return cacheRemoved.get();
	}

	@Override
	public <T> T get(String key) {
		if (!isCacheEnabled()) {
			return null;
		}
		return doGet(key);
	}

	protected abstract <T> T doGet(String key);

	@Override
	public void set(String key, Serializable value) {
		if (isCacheEnabled()) {
			doSet(key, value);
		}
	}

	protected abstract void doSet(String key, Serializable value);

	@Override
	public void set(String key, Serializable value, int expiration) {
		if (isCacheEnabled()) {
			doSet(key, value, expiration);
		}
	}

	protected abstract void doSet(String key, Serializable value, int expiration);

	@Override
	public void remove(String key) {
		//禁用与重新开启cache之间需有时间间隔(所有已有的缓存项失效), 否则有cache中的脏数据
		if (isCacheEnabled()) {
			internalRemove(key);
			addKey2RemovedMap(key);
		}
	}

	protected void addKey2RemovedMap(String key) {
		try {
			Map<CacheClient, List<String>> removeMap = getCacheRemoved();
			List<String> removeKeys = removeMap.get(this);
			if (removeKeys == null) {
				removeKeys = new ArrayList<String>();
				removeMap.put(this, removeKeys);
			}
			removeKeys.add(key);
		} catch (Exception e) {
			logger.warn("Add key to removed key map failed.", e);
		}
	}
	
	public abstract void internalRemove(String key);
	
	public void internalRemoveAll() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeAll() {
		if (isCacheEnabled()) {
			internalRemoveAll();
			addKey2RemovedMap(ALL_CACHE);
		}
	}

	protected boolean isCacheEnabled() {
		return settingService != null ? settingService.getBool(ServiceConstants.SETTING_CACHE_ENABLED, true) : true;
	}

	public String getCacheEntities() {
		return cacheEntities;
	}

	public void setCacheEntities(String cacheEntities) {
		this.cacheEntities = cacheEntities;
	}

}
