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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.dianping.lion.Constants;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author xiangbin.miao
 *
 */
@SuppressWarnings("serial")
public class ProjectAction extends AbstractLionAction implements ServletRequestAware{
	
	private ProjectService projectService;
	
	private List<Project> projectList;
	private List<Team> teamList;
	
	private String active = Constants.PROJECT_NAME;
	private HttpServletRequest request;
	
	private int teamSelect = 0;
	private int productSelect = 0;

	public String execute() {
		Map param = new HashMap();
		param.put("teamId", this.teamSelect);
		param.put("productId", this.productSelect);
		this.projectList = this.projectService.getProjectsByTeamAndProduct(param);
		this.teamList = this.projectService.getTeams();
		this.active = Constants.PROJECT_NAME;
		return SUCCESS;
	}
	
	public String productList(){
		this.active = Constants.PRODUCT_NAME;
		return SUCCESS;
	}
	
	public String teamList(){
		this.active = Constants.TEAM_NAME;
		return SUCCESS;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public int getTeamSelect() {
		return teamSelect;
	}

	public void setTeamSelect(int teamSelect) {
		this.teamSelect = teamSelect;
	}

	public int getProductSelect() {
		return productSelect;
	}

	public void setProductSelect(int productSelect) {
		this.productSelect = productSelect;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}
