/**
 * Project: com.dianping.lion.lion-api-0.0.1
 * 
 * File Created at 2012-8-1
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
package com.dianping.lion.api.http;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;

/**
 * 根据项目名和环境，设置配置项的http接口
 * @author danson.liu
 *
 */
public class SetConfigServlet extends AbstractLionServlet {

	private static final long serialVersionUID 	= 1420817678507296960L;
	
	private static final String PARAM_VALUE 	= "value";		//配置值
	
	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName = getRequiredParameter(req, PARAM_PROJECT);
		String env = getRequiredParameter(req, PARAM_ENV);
		String key = getRequiredParameter(req, PARAM_KEY);
		String value = getRequiredParameter(req, PARAM_VALUE);
		String configKey = key.startsWith(projectName) ? key : projectName + "." + key;
		PrintWriter writer = resp.getWriter();
		Project project = projectService.findProject(projectName);
		if (project == null) {
			throw new RuntimeBusinessException("project[" + projectName + "] not found.");
		}
		Config config = configService.findConfigByKey(configKey);
		int configId = 0;
		if (config == null) {
			config = new Config();
			config.setKey(configKey);
			config.setDesc("");
			config.setTypeEnum(ConfigTypeEnum.String);
			config.setProjectId(project.getId());
			configId = configService.create(config);
		} else {
			configId = config.getId();
		}
		Environment environment = environmentService.findEnvByName(env);
		if (environment == null) {
			throw new RuntimeBusinessException("environment[" + env + "] not found.");
		}
		//可能以后会支持context config的设置
		ConfigInstance configInst = configService.findInstance(configId, environment.getId(), ConfigInstance.NO_CONTEXT);
		if (configInst == null) {
			configInst = new ConfigInstance(configId, environment.getId(), ConfigInstance.NO_CONTEXT, value);
			configService.createInstance(configInst);
		} else {
			configInst.setValue(value);
			configService.updateInstance(configInst);
		}
		projectService.changeEffectStatus(project.getId(), environment.getId(), false);
		writer.print("ok");
	}

}
