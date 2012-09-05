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

/**
 * @author danson.liu
 *
 */
public interface SystemSettingService {
	
	String getSetting(String key);
	
	boolean getBool(String key, boolean defaultIfNull);
	
	void update(String key, String value);

}
