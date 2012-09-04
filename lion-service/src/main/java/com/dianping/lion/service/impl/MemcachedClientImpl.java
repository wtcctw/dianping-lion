/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-3
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

/**
 * TODO Comment of MemcachedClientImpl
 * @author danson.liu
 *
 */
public class MemcachedClientImpl extends CacheClientSupport {

	@Override
	protected <T> T doGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doSet(String key, Serializable value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSet(String key, Serializable value, int expiration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalRemove(String key) {
		// TODO Auto-generated method stub
		
	}
	
//	private MemcachedClientIF memcachedClient;
//	
//	private static final long DEFAULT_GET_TIMEOUT = 30;
//
//	private static final int DEFAULT_EXPERATION = 60 * 60;
//	
//	private long timeout = DEFAULT_GET_TIMEOUT;
//	
//	private String addressList;
//
//	public MemcachedClientImpl(String addresses) {
//		this.addressList = addresses;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected <T> T doGet(String key) {
//		Future<Object> future = memcachedClient.asyncGet(reformKey(key));
//        try {
//            return (T) future.get(timeout, TimeUnit.MILLISECONDS);
//        } catch (Exception e) {
//            return null;
//        }
//	}
//
//	@Override
//	protected void doSet(String key, Serializable value) {
//		memcachedClient.set(reformKey(key), DEFAULT_EXPERATION, value);
//	}
//
//	@Override
//	protected void doSet(String key, Serializable value, int expiration) {
//		memcachedClient.set(reformKey(key), expiration, value);
//	}
//
//	@Override
//	public void internalRemove(String key) {
//		memcachedClient.delete(reformKey(key));
//	}
//
//	private String reformKey(String key) {
//		return key != null ? key.replace(" ", "@+~") : key;
//	}
//
//	public void setTimeout(long timeout) {
//		this.timeout = timeout;
//	}
//	
//	public void init() {
//		ExtendedConnectionFactory connectionFactory = new ExtendedKetamaConnectionFactory();
//		
//		
//	}

}
