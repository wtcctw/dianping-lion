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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationLogSearch;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.OperationTypeEnum;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class OperationLogAction extends ActionSupport {
	
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
	private List<Project> projects;
	private List<User> users;
	private Map<Integer, String> opTypes = new LinkedHashMap<Integer, String>();
	private List<Environment> envs;
	
	//data from frontend
	private String clientdata;

/*	public String execute() {
		operationLogs = operationLogService.getLogs();
		return SUCCESS;
	}*/
	
	public String getOpLogs() {
		initializePage();
		operationLogs = operationLogService.getLogs();
		return SUCCESS;
	}
	
	public String getOpLogList() {
		Gson gson = new Gson();
		OperationLogSearch operationLogSearch = gson.fromJson(clientdata,OperationLogSearch.class);
		operationLogSearch.setContent("%"+operationLogSearch.getContent()+"%");
		operationLogs = operationLogService.getLogList(operationLogSearch);
		return SUCCESS;
	}
	
	public String getClientdata() {
		return clientdata;
	}

	public void setClientdata(String clientdata) {
		this.clientdata = clientdata;
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

	protected void initializePage() {
		projects = projectService.getProjects();
		users = userService.findAll();
		envs = environmentService.findAll();
		opTypes.put(-1, "任意类型");
		for(int i = 0; i < OperationTypeEnum.values().length; i++) {
			opTypes.put(i, OperationTypeEnum.values()[i].getValue());
		}
	}
	
}
