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

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class EnvironmentAction extends AbstractLionAction {
	
	@Autowired
	private EnvironmentService environmentService;
	
	//表格内容
	private List<Environment> environmentList;
	
	private Environment environment;
	private int envId;
	private String envName;
	private String envLabel;
	private String envIps;
	private boolean online;
	private int seq;
	
	@Override
	public void checkModulePrivilege() {
		if (!privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_ENV, SecurityUtils.getCurrentUserId())) {
			throw NoPrivilegeException.INSTANCE;
		}
	}
	
	public String getEnvLists() {
		environmentList = environmentService.findAll();
		return SUCCESS;
	}
	
	public String addEnv() {
		return SUCCESS;
	}
	
	public String addEnvSubmit() {
		Environment environment = new Environment();
		environment.setId(envId);
		environment.setIps(envIps);
		environment.setLabel(envLabel);
		environment.setName(envName);
		environment.setOnline(online);
		environment.setSeq(seq);
		environmentService.create(environment);
		environmentList = environmentService.findAll();
		return SUCCESS;
	}
	
	public String editEnv() {
		environment = environmentService.findEnvByID(envId);
		return SUCCESS;
	}
	
	public String editEnvSubmit() {
		Environment environment = new Environment();
		environment.setId(envId);
		environment.setIps(envIps);
		environment.setLabel(envLabel);
		environment.setName(envName);
		environment.setOnline(online);
		environment.setSeq(seq);
		environmentService.update(environment);
		environmentList = environmentService.findAll();
		return SUCCESS;
	}
	
	public String deleteEnvAjax() {
		environmentService.delete(envId);
		environmentList = environmentService.findAll();
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

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public int getEnvId() {
		return envId;
	}

	public void setEnvId(int envId) {
		this.envId = envId;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getEnvLabel() {
		return envLabel;
	}

	public void setEnvLabel(String envLabel) {
		this.envLabel = envLabel;
	}

	public String getEnvIps() {
		return envIps;
	}

	public void setEnvIps(String envIps) {
		this.envIps = envIps;
	}

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
