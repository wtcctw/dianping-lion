/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-14
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
package com.dianping.lion.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * TODO Comment of EhcacheTest1
 * @author danson.liu
 *
 */
public class EhcacheTest1 {

	public static void main(String[] args) throws InterruptedException {
		CacheManager cacheManager = new CacheManager(EhcacheTest1.class.getResource("/config/ehcache1.xml"));
//		Ehcache cache = cacheManager.addCacheIfAbsent("Default_Cache");
		Cache cache = cacheManager.getCache("templateCache");
		while (true) {
			Element element = cache.get("Nice");
			System.out.println(element != null ? element.getObjectValue() : null);
			Thread.sleep(4000);
		}
	}
	
}
