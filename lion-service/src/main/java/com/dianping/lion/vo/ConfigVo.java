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
 * 针对某一特定环境的配置信息
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigVo implements Serializable {
	
	private Config config;
	
	private boolean hasInstance;	//有设置配置实例
	
	private boolean hasContextInst;	//有context based配置实例
	
	private ConfigInstance defaultInstance;	//默认配置实例
	
	private boolean hasReadPrivilege;
	private boolean hasEditPrivilege;
	private boolean hasLockPrivilege;

	private boolean hasReference;

	/**
	 * @param config
	 * @param instance
	 */
	public ConfigVo(Config config, boolean hasInstance, boolean hasContextInst, boolean hasReference, ConfigInstance instance) {
		this.config = config;
		this.hasInstance = hasInstance;
		this.hasContextInst = hasContextInst;
		this.hasReference = hasReference;
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

    public boolean isHasReadPrivilege() {
        return hasReadPrivilege;
    }

    public void setHasReadPrivilege(boolean hasReadPrivilege) {
        this.hasReadPrivilege = hasReadPrivilege;
        if (!hasReadPrivilege && defaultInstance != null) {
        	defaultInstance.setValue(null);
        }
    }

    public boolean isHasEditPrivilege() {
        return hasEditPrivilege;
    }

    public void setHasEditPrivilege(boolean hasEditPrivilege) {
        this.hasEditPrivilege = hasEditPrivilege;
    }

    public boolean isHasLockPrivilege() {
        return hasLockPrivilege;
    }

    public void setHasLockPrivilege(boolean hasLockPrivilege) {
        this.hasLockPrivilege = hasLockPrivilege;
    }

	public boolean isHasReference() {
		return hasReference;
	}

	public void setHasReference(boolean hasReference) {
		this.hasReference = hasReference;
	}
    
	public boolean isJdbcUrl() {
	    if(config != null) {
	        return config.getKey().endsWith(".jdbc.url");
	    }
	    return false;
	}
}
