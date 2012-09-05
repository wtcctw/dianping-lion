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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * @author danson.liu
 *
 */
public class EhcacheClientImpl extends CacheClientSupport {
	
	private Ehcache ehcache;

	public EhcacheClientImpl(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T doGet(String key) {
		Element element = ehcache.get(key);
		return element != null ? (T) element.getObjectValue() : null;
	}

	@Override
	protected void doSet(String key, Serializable value) {
		ehcache.put(new Element(key, value));
	}

	@Override
	protected void doSet(String key, Serializable value, int expiration) {
		ehcache.put(new Element(key, value, Boolean.FALSE, Integer.valueOf(0), Integer.valueOf(expiration)));
	}
	
	@Override
	public void remove(String key) {
		internalRemove(key);
		addKey2RemovedMap(key);
	}

	@Override
	public void internalRemove(String key) {
		ehcache.remove(key);
	}
	
	@Override
	public void removeAll() {
		internalRemoveAll();
		addKey2RemovedMap(ALL_CACHE);
	}
	
	@Override
	public void internalRemoveAll() {
		ehcache.removeAll();
	}

}
