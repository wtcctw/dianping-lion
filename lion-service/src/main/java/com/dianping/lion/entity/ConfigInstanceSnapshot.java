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

/**
 * TODO Comment of ConfigInstanceSnapshot
 * @author danson.liu
 *
 */
public class ConfigInstanceSnapshot extends ConfigInstance {

	private static final long serialVersionUID = -1604430605925991824L;
	
	private int snapshotSetId;
	
	/**
	 * @param instance
	 */
	public ConfigInstanceSnapshot(ConfigInstance instance) {
		this.configId = instance.getConfigId();
		this.envId = instance.envId;
		this.desc = instance.desc;
		this.value = instance.value;
		this.context = instance.context;
		this.contextmd5 = instance.contextmd5;
		this.createUserId = instance.createUserId;
		this.modifyUserId = instance.modifyUserId;
		this.createTime = instance.createTime;
		this.modifyTime = instance.modifyTime;
		this.seq = instance.seq;
	}

	/**
	 * 
	 */
	public ConfigInstanceSnapshot() {
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

}
