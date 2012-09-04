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
package com.dianping.lion.aop;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.impl.CacheClientSupport;

/**
 * @author danson.liu
 *
 */
public class CacheInterceptor implements MethodInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(CacheInterceptor.class);
	
	private ThreadLocal<Boolean> interceptorEntry = new InheritableThreadLocal<Boolean>();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Boolean entry = interceptorEntry.get();
		if (entry == null || entry == true) {
			interceptorEntry.set(false);
			clearCacheRemovedKeys();
			try {
				return invocation.proceed();
			} finally {
				removeItemsFromCache();
				interceptorEntry.set(true);
			}
		} else {
			return invocation.proceed();
		}
	}

	private void clearCacheRemovedKeys() {
		try {
			CacheClientSupport.getCacheRemoved().clear();
		} catch (Exception e) {
			logger.warn("Clear cache removed keys failed.", e);
		}
	}

	private void removeItemsFromCache() {
		try {
			Map<CacheClient, List<String>> cacheRemoved = CacheClientSupport.getCacheRemoved();
			if (!cacheRemoved.isEmpty()) {
				for (Entry<CacheClient, List<String>> removedEntry : cacheRemoved.entrySet()) {
					CacheClient cacheClient = removedEntry.getKey();
					List<String> removedKeys = removedEntry.getValue();
					if (removedKeys != null) {
						for (String removedKey : removedKeys) {
							try {
								if (CacheClientSupport.ALL_CACHE.equals(removedKey)) {
									((CacheClientSupport) cacheClient).internalRemoveAll();
								} else {
									((CacheClientSupport) cacheClient).internalRemove(removedKey);
								}
							} catch (Exception e) {
								logger.warn("Remove item[" + removedKey + "] from cache failed.", e);
							}
						}
					}
				}
				cacheRemoved.clear();
			}
		} catch (Exception e) {
			logger.warn("Remove items from cache failed.", e);
		}
	}

}
