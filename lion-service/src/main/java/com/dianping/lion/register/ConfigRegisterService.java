/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-27
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
package com.dianping.lion.register;

/**
 * @author danson.liu
 *
 */
public interface ConfigRegisterService {

	void registerContextValue(String key, String value);
	
	/**
	 * 兼容老版本的lion, 仅有配置默认值, 全部升级后该接口可移除
	 * @param key
	 * @param defaultVal
	 */
	void registerDefaultValue(String key, String defaultVal);
	
	void registerAndPushContextValue(String key, String value);
	
	/**
	 * 兼容老版本的lion, 仅有配置默认值, 全部升级后该接口可移除
	 * @param key
	 * @param defaultVal
	 */
	void registerAndPushDefaultValue(String key, String defaultVal);
	
	void unregister(String key);
	
	String get(String key);
	
	String getAddresses();
	
	void destroy();
	
}
