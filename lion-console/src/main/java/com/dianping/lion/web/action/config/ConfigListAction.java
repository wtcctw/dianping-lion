/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-6
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
package com.dianping.lion.web.action.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.vo.ConfigInstanceVo;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigListAction extends AbstractLionAction {
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	private Integer envId;
	
	private int projectId;
	
	private List<Environment> environments;
	
	private List<ConfigInstanceVo> configInsts;

	public String execute() {
		this.environments = environmentService.findAll();
		if (envId == null) {
			envId = !environments.isEmpty() ? environments.get(0).getId() : null;
		}
		if (envId != null) {
			configInsts = configService.findInstanceVos(projectId, envId);
		}
		return SUCCESS;
	}

	/**
	 * @return the envId
	 */
	public Integer getEnvId() {
		return envId;
	}

	/**
	 * @param envId the envId to set
	 */
	public void setEnvId(Integer envId) {
		this.envId = envId;
	}

	/**
	 * @return the pid
	 */
	public int getPid() {
		return projectId;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(int pid) {
		this.projectId = pid;
	}

	/**
	 * @return the configInsts
	 */
	public List<ConfigInstanceVo> getConfigInsts() {
		return configInsts;
	}

	/**
	 * @return the environments
	 */
	public List<Environment> getEnvironments() {
		return environments;
	}
	
}
