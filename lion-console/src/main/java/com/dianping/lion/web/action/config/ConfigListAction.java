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

import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigListAction extends AbstractConfigAction {
	
	private Integer configId;
	
	private ConfigCriteria criteria = new ConfigCriteria();
	
	private List<Environment> environments;
	
	private List<ConfigVo> configVos;

	public String list() {
		this.environments = environmentService.findAll();
		this.project = projectService.getProject(projectId);
		if (envId == null) {
			envId = !environments.isEmpty() ? environments.get(0).getId() : null;
		}
		if (envId != null) {
			criteria.setProjectId(projectId);
			criteria.setEnvId(envId);
			configVos = configService.findConfigVos(criteria);
		}
		return SUCCESS;
	}
	
	public String add() {
		this.project = projectService.getProject(projectId);
		return SUCCESS;
	}
	
	public String clearInstance() {
		configService.clearInstance(configId, envId);
		createQueryParam2();
		return SUCCESS;
	}
	
	public String delete() {
		configService.delete(configId);
		createQueryParam2();
		return SUCCESS;
	}
	
	public String moveUp() {
		try {
			configService.moveUp(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		createQueryParam1();
		return SUCCESS;
	}
	
	public String moveDown() {
		try {
			configService.moveDown(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		createQueryParam1();
		return SUCCESS;
	}

	/**
	 * @return the configInsts
	 */
	public List<ConfigVo> getConfigVos() {
		return configVos;
	}

	/**
	 * @return the environments
	 */
	public List<Environment> getEnvironments() {
		return environments;
	}

	/**
	 * @return the configId
	 */
	public Integer getConfigId() {
		return configId;
	}

	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	/**
	 * @return the criteria
	 */
	public ConfigCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(ConfigCriteria criteria) {
		this.criteria = criteria;
	}
	
}
