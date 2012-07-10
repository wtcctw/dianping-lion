/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-10
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
package com.dianping.lion.web.action.system;

import java.util.List;

import com.dianping.lion.entity.Project;
import com.dianping.lion.service.ProjectService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author xiangbin.miao
 *
 */
@SuppressWarnings("serial")
public class ProjectAction extends ActionSupport {
	
	private ProjectService projectService;
	
	private List<Project> projectList;
	
//	private String active = Constants.PROJECT_NAME;

	public String execute() {
		this.projectService.getProjects();
//		this.active = Constants.PROJECT_NAME;
		return SUCCESS;
	}
	
	public String productList(){
//		this.active = Constants.PRODUCT_NAME;
		return SUCCESS;
	}
	
	public String teamList(){
//		this.active = Constants.TEAM_NAME;
		return SUCCESS;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
}
