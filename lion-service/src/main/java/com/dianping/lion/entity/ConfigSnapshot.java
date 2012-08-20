/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-3
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

import java.util.Date;


/**
 * TODO Comment of ConfigSnapshot
 * @author danson.liu
 *
 */
public class ConfigSnapshot extends Config {

	private static final long serialVersionUID = -8836861178321278697L;
	
	private int snapshotSetId;
	
	private int configId;
	
	//该Config快照时的相应环境值的最后修改时间
	private Date valueModifyTime;

	/**
	 * @param config
	 */
	public ConfigSnapshot(Config config) {
		this.configId = config.getId();
		this.key = config.getKey();
		this.desc = config.getDesc();
		this.type = config.getType();
		this.projectId = config.getProjectId();
		this.createUserId = config.getCreateUserId();
		this.modifyUserId = config.getModifyUserId();
		this.createTime = config.getCreateTime();
		this.modifyTime = config.getModifyTime();
		this.privatee = config.isPrivatee();
		this.seq = config.getSeq();
	}

	/**
	 * 
	 */
	public ConfigSnapshot() {
	}
	
	public Config toConfig() {
		Config config = new Config();
		config.setKey(key);
		config.setDesc(desc);
		config.setType(type);
		config.setProjectId(projectId);
		config.setCreateUserId(createUserId);
		config.setCreateTime(createTime);
		config.setModifyUserId(modifyUserId);
		config.setModifyTime(modifyTime);
		return config;
	}

	/**
	 * @return the snapshotSetId
	 */
	public int getSnapshotSetId() {
		return snapshotSetId;
	}

	/**
	 * @param snapshotSetId the snapshotSetId to set
	 */
	public void setSnapshotSetId(int snapshotSetId) {
		this.snapshotSetId = snapshotSetId;
	}

	/**
	 * @return the valueModifyTime
	 */
	public Date getValueModifyTime() {
		return valueModifyTime;
	}

	/**
	 * @param valueModifyTime the valueModifyTime to set
	 */
	public void setValueModifyTime(Date valueModifyTime) {
		this.valueModifyTime = valueModifyTime;
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

}
