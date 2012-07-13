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
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.UrlUtils;
import com.dianping.lion.vo.ConfigInstanceVo;
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
	
	private List<Environment> environments;
	
	private List<ConfigInstanceVo> configInsts;

	private HttpServletRequest request;

	public String list() {
		this.environments = environmentService.findAll();
		this.project = projectService.getProject(projectId);
		if (envId == null) {
			envId = !environments.isEmpty() ? environments.get(0).getId() : null;
		}
		if (envId != null) {
			configInsts = configService.findInstanceVos(projectId, envId);
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String moveUp() {
		//TODO 处理EntityNotFoundException异常
		configService.moveUp(projectId, configId);
		query = UrlUtils.resolveUrl(request.getParameterMap());
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String moveDown() {
		//TODO 处理EntityNotFoundException异常
		configService.moveDown(projectId, configId);
		query = UrlUtils.resolveUrl(request.getParameterMap());
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
	
}
