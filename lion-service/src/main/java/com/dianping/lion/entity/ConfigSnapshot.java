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
 * TODO Comment of ConfigSnapshot
 * @author danson.liu
 *
 */
public class ConfigSnapshot extends Config {

	private static final long serialVersionUID = -8836861178321278697L;
	
	private int registPointId;
	
	private boolean delete;

	/**
	 * @param config
	 */
	public ConfigSnapshot(Config config) {
		this.key = config.getKey();
		this.desc = config.getDesc();
		this.type = config.getType();
		this.projectId = config.getProjectId();
		this.createUserId = config.getCreateUserId();
		this.modifyUserId = config.getModifyUserId();
		this.createTime = config.getCreateTime();
		this.modifyTime = config.getModifyTime();
		this.seq = config.getSeq();
	}

	/**
	 * 
	 */
	public ConfigSnapshot() {
	}

	/**
	 * @return the registPointId
	 */
	public int getRegistPointId() {
		return registPointId;
	}

	/**
	 * @param registPointId the registPointId to set
	 */
	public void setRegistPointId(int registPointId) {
		this.registPointId = registPointId;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}
