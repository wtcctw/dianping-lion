/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-17
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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.util.UrlUtils;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class AbstractConfigAction extends AbstractLionAction {

	protected Integer envId;
	
	protected int projectId;
	
	protected Project project;
	
	protected Environment environment;
	
	protected String query;
	
	@Autowired
	protected ConfigService configService;
	
	@Autowired
	protected ProjectService projectService;
	
	@Autowired
	protected EnvironmentService environmentService;

	@SuppressWarnings("unchecked")
	protected void createQueryParam1() {
		query = UrlUtils.resolveUrl(request.getParameterMap(), "menu", "pid", "envId");
	}

	@SuppressWarnings("unchecked")
	protected void createQueryParam2() {
		query = UrlUtils.resolveUrl(request.getParameterMap(), "menu", "pid", "envId", "criteria.key", "criteria.status");
	}
	
	public boolean hasAddPrivilege(int projectId, int envId) {
	    return privilegeDecider.hasAddConfigPrivilege(projectId, envId, SecurityUtils.getCurrentUserId());
	}
	
	public boolean hasEditPrivilege(int projectId, int envId, int configId) {
	    return privilegeDecider.hasEditConfigPrivilege(projectId, envId, configId, SecurityUtils.getCurrentUserId());
	}
	
	public Map<Integer, Boolean> getEditPrivileges(int projectId, int configId) {
        Map<Integer, Boolean> editPrivilegeMap = new HashMap<Integer, Boolean>();
    	    for (Environment env : environmentService.findAll()) {
	        editPrivilegeMap.put(env.getId(), hasEditPrivilege(projectId, env.getId(), configId));
    	    }
        return editPrivilegeMap;  
	}
	
	public boolean hasLockPrivilege() {
        return privilegeDecider.hasLockConfigPrivilege(SecurityUtils.getCurrentUserId());
	}
	
	public boolean hasEditAttrPrivilege(int projectId) {
		return privilegeDecider.hasEditAttrPrivilege(projectId, SecurityUtils.getCurrentUserId());
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
	 * @return the projectId
	 */
	public int getPid() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setPid(int projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
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

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }
	
}
