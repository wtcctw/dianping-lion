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

import java.io.Serializable;
import java.util.Date;

/**
 * @author danson.liu
 *
 */
public class RegisterPoint implements Serializable {

	private static final long serialVersionUID = -7327989454910065746L;
	
	private int id;
	
	private int projectId;
	
	private int envId;
	
	private Date executeTime;
	
	private int executorId;
	
	/**
	 * 是否有效(当该point被rollback掉则无效)
	 */
	private boolean valid = true;
	
	/**
	 * 是否是人工执行注册，否则是api接口触发(auto)
	 */
	private boolean manual;
	
	public RegisterPoint(int projectId, int envId) {
		this.projectId = projectId;
		this.envId = envId;
	}
	
	public RegisterPoint() {
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
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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
	 * @return the executeTime
	 */
	public Date getExecuteTime() {
		return executeTime;
	}

	/**
	 * @param executeTime the executeTime to set
	 */
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	/**
	 * @return the executorId
	 */
	public int getExecutorId() {
		return executorId;
	}

	/**
	 * @param executorId the executorId to set
	 */
	public void setExecutorId(int executorId) {
		this.executorId = executorId;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the manual
	 */
	public boolean isManual() {
		return manual;
	}

	/**
	 * @param manual the manual to set
	 */
	public void setManual(boolean manual) {
		this.manual = manual;
	}

}
