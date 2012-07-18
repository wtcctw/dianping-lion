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

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.ProjectService;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigEditAction extends AbstractConfigAction {
	
	private Config config;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ConfigService configService;
	
	public String createConfig() {
		project = projectService.getProject(projectId);
		config.setProjectId(projectId);
		try {
			config = configService.create(config);
			return SUCCESS;
		} catch (RuntimeBusinessException e) {
			errorMessage = e.getMessage();
			return INPUT;
		}
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Config config) {
		this.config = config;
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
	
}
