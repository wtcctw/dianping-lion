/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-15
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
package com.dianping.lion.entity;

import java.io.Serializable;
import java.util.Date;

import com.dianping.lion.util.EnumUtils;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigStatus implements Serializable {

	private int id;
	private int configId;
	private int envId;
	private int status;
	private ConfigStatusEnum statusEnum;
	private Date activeTime;
	
	public ConfigStatus() {
	}
	
	public ConfigStatus(int configId, int envId, ConfigStatusEnum status) {
		this.configId = configId;
		this.envId = envId;
		this.status = status.getValue();
		this.statusEnum = status;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}
	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	/**
	 * @return the envId
	 */
	public int getEnvId() {
		return envId;
	}
	/**
	 * @param envId the envId to set
	 */
	public void setEnvId(int envId) {
		this.envId = envId;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	public ConfigStatusEnum getStatusEnum() {
		if (this.statusEnum == null) {
			synchronized (this) {
				if (this.statusEnum == null) {
					this.statusEnum = EnumUtils.fromEnumProperty(ConfigStatusEnum.class, "value", this.status);
				}
			}
		}
		return this.statusEnum;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
		this.statusEnum = null;
	}
	/**
	 * @return the activeTime
	 */
	public Date getActiveTime() {
		return activeTime;
	}
	/**
	 * @param activeTime the activeTime to set
	 */
	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}
	
}
