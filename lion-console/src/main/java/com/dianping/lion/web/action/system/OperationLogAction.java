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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationLogSearch;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.web.action.common.AbstractLionAction;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class OperationLogAction extends AbstractLionAction implements ServletRequestAware{
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	//表格内容
	private List<OperationLog> operationLogs;
	
	//搜索条件
	private HttpServletRequest request;
	private List<Project> projects;
	private List<User> users;
	private Map<Integer, String> opTypes = new LinkedHashMap<Integer, String>();
	private List<Environment> envs;
	
	//for project search
	private int pid = -1;
	private String projectName;
	
	public String getOpLogs() {
		initializePage();
		operationLogs = operationLogService.getLogs();
		return SUCCESS;
	}
	
	public String getOpLogList() {
		OperationLogSearch operationLogSearch = new OperationLogSearch();
		operationLogSearch.setContent(request.getParameter("content"));
		operationLogSearch.setEnv(Integer.valueOf(request.getParameter("env")));
		operationLogSearch.setFrom(request.getParameter("from"));
		operationLogSearch.setTo(request.getParameter("to"));
		operationLogSearch.setOpType(Integer.valueOf(request.getParameter("opType")));
		operationLogSearch.setUser(Integer.valueOf(request.getParameter("user")));
		operationLogSearch.setProject(Integer.valueOf(request.getParameter("project")));
		operationLogSearch.setContent("%"+operationLogSearch.getContent()+"%");
		operationLogs = operationLogService.getLogList(operationLogSearch);
		return SUCCESS;
	}
	
	public String getOpLogsByProject() {
		initializePage();
		OperationLogSearch operationLogSearch = new OperationLogSearch();
		operationLogSearch.setProject(pid);
		operationLogs = operationLogService.getLogList(operationLogSearch);
		return SUCCESS;
	}
	
	public String getOpLogListByProject() {
		OperationLogSearch operationLogSearch = new OperationLogSearch();
		operationLogSearch.setContent(request.getParameter("content"));
		operationLogSearch.setEnv(Integer.valueOf(request.getParameter("env")));
		operationLogSearch.setFrom(request.getParameter("from"));
		operationLogSearch.setTo(request.getParameter("to"));
		operationLogSearch.setOpType(Integer.valueOf(request.getParameter("opType")));
		operationLogSearch.setUser(Integer.valueOf(request.getParameter("user")));
		operationLogSearch.setProject(Integer.valueOf(request.getParameter("project")));
		operationLogSearch.setContent("%"+operationLogSearch.getContent()+"%");
		operationLogs = operationLogService.getLogList(operationLogSearch);
		return SUCCESS;
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}

	public List<OperationLog> getOperationLogs() {
		return operationLogs;
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public List<User> getUsers() {
		return users;
	}

	public Map<Integer, String> getOpTypes() {
		return opTypes;
	}

	public List<Environment> getEnvs() {
		return envs;
	}

	@SuppressWarnings("unchecked")
	protected void initializePage() {
		if(pid != -1) {
			Project project = projectService.getProject(pid);
			projectName = project.getName();
		}
		projects = projectService.getProjects();
		Collections.sort(projects, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((Project)o1).getName().compareTo(((Project)o2).getName());
			}
		});
		users = userService.findAll();
		envs = environmentService.findAll();
		opTypes.put(-1, "任意类型");
		for(int i = 0; i < OperationTypeEnum.values().length; i++) {
			opTypes.put(i, OperationTypeEnum.values()[i].getValue());
		}
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request =  request;
		
	}
	
}
