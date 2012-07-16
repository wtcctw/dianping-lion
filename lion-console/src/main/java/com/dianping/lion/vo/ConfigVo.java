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
import com.dianping.lion.entity.ConfigStatus;

/**
 * 针对某一特定环境的配置信息
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigVo implements Serializable {
	
	private Config config;
	
	private ConfigStatus status;
	
	private boolean hasInstance;	//有设置配置实例
	
	private boolean hasContextInst;	//有context based配置实例
	
	private ConfigInstance defaultInstance;	//默认配置实例

	/**
	 * @param config
	 * @param instance
	 */
	public ConfigVo(Config config, ConfigStatus status, boolean hasInstance, boolean hasContextInst, ConfigInstance instance) {
		this.config = config;
		this.status = status;
		this.hasInstance = hasInstance;
		this.hasContextInst = hasContextInst;
		this.defaultInstance = instance;
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
	public ConfigInstance getDefaultInstance() {
		return defaultInstance;
	}

	/**
	 * @return the status
	 */
	public ConfigStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ConfigStatus status) {
		this.status = status;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setDefaultInstance(ConfigInstance instance) {
		this.defaultInstance = instance;
	}

	/**
	 * @return the hasInstance
	 */
	public boolean isHasInstance() {
		return hasInstance;
	}

	/**
	 * @param hasInstance the hasInstance to set
	 */
	public void setHasInstance(boolean hasInstance) {
		this.hasInstance = hasInstance;
	}

	/**
	 * @return the hasContextInst
	 */
	public boolean isHasContextInst() {
		return hasContextInst;
	}

	/**
	 * @param hasContextInst the hasContextInst to set
	 */
	public void setHasContextInst(boolean hasContextInst) {
		this.hasContextInst = hasContextInst;
	}

}
