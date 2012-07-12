/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
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
package com.dianping.lion.vo;

import java.io.Serializable;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigInstanceVo implements Serializable {
	
	private Config config;
	
	private ConfigInstance instance;

	/**
	 * @param config
	 * @param instance
	 */
	public ConfigInstanceVo(Config config, ConfigInstance instance) {
		this.config = config;
		this.instance = instance;
	}

	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * @return the instance
	 */
	public ConfigInstance getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(ConfigInstance instance) {
		this.instance = instance;
	}
	
	public boolean existsInstance() {
		return instance != null;
	}

}
