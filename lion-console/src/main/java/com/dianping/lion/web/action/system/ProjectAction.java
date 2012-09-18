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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.dianping.lion.ConsoleConstants;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.ProjectMember;
import com.dianping.lion.entity.Team;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author xiangbin.miao
 *
 */
@SuppressWarnings("serial")
public class ProjectAction extends AbstractLionAction implements ServletRequestAware{
	
	private ProjectService projectService;
	private UserService userService;
	
	private List<Project> projectList;
	private List<Team> teamList;
	private List<User> userList;
	
	private String active = ConsoleConstants.PROJECT_NAME;
	
	private int teamSelect = 0;
	private int productSelect = 0;
	
	private String message = "成功";
	
	private int projectId;
	private int productId;
	private int userId;
	private String memberType;
	private String projectName;
	
    private List<ProjectMember> owners;
    private List<ProjectMember> members;
    private List<ProjectMember> operators;

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public String execute() {
		Map param = new HashMap();
		param.put("teamId", this.teamSelect);
		param.put("productId", this.productSelect);
		this.projectList = this.projectService.getProjectsByTeamAndProduct(param);
		this.teamList = this.projectService.getTeams();
		this.active = ConsoleConstants.PROJECT_NAME;
		this.userList = this.userService.findAll();
		return SUCCESS;
	}
	
	public String projectAdd(){
		Project project = new Project();
		project.setName(this.projectName);
		project.setProductId(this.productId);
		Date date = new Date();
		project.setCreateTime(date);
		project.setModifyTime(date);
		this.projectService.addProject(project);
		return SUCCESS;
	}
	
	public String projectEdit(){
		Project project = new Project();
		project.setId(this.projectId);
		project.setName(this.projectName);
		project.setProductId(this.productId);
		Date date = new Date();
		project.setCreateTime(date);
		project.setModifyTime(date);
		this.projectService.editProject(project);
		return SUCCESS;
	}
	
	public String projectDel(){
		this.projectService.delProject(this.projectId);
		return SUCCESS;
	}
	
	public boolean hasEditMemberPrivilege(int projectId) {
		return this.privilegeDecider.hasManageProjectMemberPrivilege(projectId, SecurityUtils.getCurrentUser());
	}
	
	public String productList(){
		this.active = ConsoleConstants.PRODUCT_NAME;
		return SUCCESS;
	}
	
	public String teamList(){
		this.active = ConsoleConstants.TEAM_NAME;
		return SUCCESS;
	}
	
	public String getAllMembers() {
	    owners = projectService.getOwners(projectId);
	    members = projectService.getMembers(projectId);
	    operators = projectService.getOperators(projectId);
	    return SUCCESS;
	}
	
	public String addMember() {
		if (!hasEditMemberPrivilege(projectId)) {
			throw NoPrivilegeException.INSTANCE;
		}
	    projectService.addMember(projectId, memberType, userId);
	    createSuccessStreamResponse();
	    return SUCCESS;
	}
	
	public String deleteMember() {
		if (!hasEditMemberPrivilege(projectId)) {
			throw NoPrivilegeException.INSTANCE;
		}
	    projectService.deleteMember(projectId, memberType, userId);
	    createSuccessStreamResponse();
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

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getProductSelect() {
		return productSelect;
	}

	public void setProductSelect(int productSelect) {
		this.productSelect = productSelect;
	}

    /**
     * @return the owners
     */
    public List<ProjectMember> getOwners() {
        return owners;
    }

    /**
     * @return the members
     */
    public List<ProjectMember> getMembers() {
        return members;
    }

    /**
     * @return the operators
     */
    public List<ProjectMember> getOperators() {
        return operators;
    }

}
