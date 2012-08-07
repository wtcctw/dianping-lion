/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-7
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

/**
 * TODO Comment of ProjectStatus
 * @author danson.liu
 *
 */
public class ProjectStatus implements Serializable {

	private static final long serialVersionUID = 896444519715775622L;
	
	private int id;
	
	private int projectId;
	
	private int envId;
	
	private boolean configEffected;

	/**
	 * @param projectId
	 * @param envId
	 * @param configEffected
	 */
	public ProjectStatus(int projectId, int envId, boolean configEffected) {
		this.projectId = projectId;
		this.envId = envId;
		this.configEffected = configEffected;
	}

	/**
	 * 
	 */
	public ProjectStatus() {
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
	 * @return the configEffected
	 */
	public boolean isConfigEffected() {
		return configEffected;
	}

	/**
	 * @param configEffected the configEffected to set
	 */
	public void setConfigEffected(boolean configEffected) {
		this.configEffected = configEffected;
	}

}
