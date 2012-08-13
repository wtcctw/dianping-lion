/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-10
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

/**
 * TODO Comment of ConfigStatus
 * @author danson.liu
 *
 */
public class ConfigStatus implements Serializable {
	
	private static final long serialVersionUID = 5381899901569472487L;
	
	private int id;
	
	private int configId;
	
	private int envId;
	
	private Date createTime;
	
	private int createUserId;
	
	private Date modifyTime;
	
	private int modifyUserId;

	public ConfigStatus(int configId, int envId) {
		this.configId = configId;
		this.envId = envId;
	}

	public ConfigStatus() {
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
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the modifyUserId
	 */
	public int getModifyUserId() {
		return modifyUserId;
	}

	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

}
