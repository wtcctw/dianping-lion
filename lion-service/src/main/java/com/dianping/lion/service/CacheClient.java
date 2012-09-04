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
package com.dianping.lion.service;

import java.io.Serializable;

/**
 * @author danson.liu
 *
 */
public interface CacheClient {

	<T> T get(String key);
	
	void set(String key, Serializable value);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param expiration in seconds from now on, zero indicate no limited
	 */
	void set(String key, Serializable value, int expiration);
	
	void remove(String key);

	void removeAll();
	
}
