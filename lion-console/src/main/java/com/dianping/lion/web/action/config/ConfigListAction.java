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

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigListAction extends ActionSupport {
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private EnvironmentService environmentService;
	
	private Integer env;
	
//	private int 
	
	private List<Environment> environments;
	
	private List<ConfigInstance> configInsts;

	public String execute() {
		this.environments = environmentService.findAll();
		if (env == null) {
			env = !environments.isEmpty() ? environments.get(0).getId() : null;
		}
		if (env != null) {
//			configInsts = configService.findInstances();
		}
		return SUCCESS;
	}

	/**
	 * @return the env
	 */
	public Integer getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(Integer env) {
		this.env = env;
	}
	
}
