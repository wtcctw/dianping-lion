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

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class EnvironmentAction extends AbstractLionAction{
	
	@Autowired
	private EnvironmentService environmentService;
	
	//表格内容
	private List<Environment> environmentList;
	
	public String getEnvLists() {
		environmentList = environmentService.findAll();
		return SUCCESS;
	}
	
	public String addEnv() {
		return SUCCESS;
	}

	public EnvironmentService getEnvironmentService() {
		return environmentService;
	}

	public void setEnvironmentService(EnvironmentService environmentService) {
		this.environmentService = environmentService;
	}

	public List<Environment> getEnvironmentList() {
		return environmentList;
	}

	public void setEnvironmentList(List<Environment> environmentList) {
		this.environmentList = environmentList;
	}
	
}
