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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.UrlUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigAction extends AbstractLionAction implements ServletRequestAware {
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	private Integer envId;
	
	private int projectId;
	
	private Project project;
	
	private Integer configId;
	
	private String query;
	
	private ConfigCriteria criteria = new ConfigCriteria();
	
	private List<Environment> environments;
	
	private List<ConfigVo> configVos;

	private HttpServletRequest request;

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
	
	@SuppressWarnings("unchecked")
	public String clearInstance() {
		configService.clearInstance(configId, envId);
		query = UrlUtils.resolveUrl(request.getParameterMap(), "menu", "pid", "envId", "criteria.key", "criteria.status");
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String moveUp() {
		try {
			configService.moveUp(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		query = UrlUtils.resolveUrl(request.getParameterMap(), "menu", "pid", "envId");
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String moveDown() {
		try {
			configService.moveDown(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		query = UrlUtils.resolveUrl(request.getParameterMap(), "menu", "pid", "envId");
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
	 * @return the project
	 */
	public Project getProject() {
		return project;
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
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
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
