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
	
	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName = getRequiredParameter(req, PARAM_PROJECT);
		String[] features = getRequiredParameters(req, PARAM_FEATURE);
		String env = getRequiredParameter(req, PARAM_ENV);
		String key = getRequiredParameter(req, PARAM_KEY);
		String value = getRequiredParameter(req, PARAM_VALUE);
		String effect = getRequiredParameter(req, PARAM_EFFECT);	//是否立即生效
		String configKey = key.startsWith(projectName) ? key : projectName + "." + key;
		PrintWriter writer = resp.getWriter();
		Project project = projectService.findProject(projectName);
		if (project == null) {
			throw new RuntimeBusinessException("project[" + projectName + "] not found.");
		}
		Environment environment = environmentService.findEnvByName(env);
		if (environment == null) {
			throw new RuntimeBusinessException("environment[" + env + "] not found.");
		}
		if ("1".equals(effect)) {
			setAndEffectConfig(project, environment, configKey, value);
		}
		writer.print(SUCCESS_CODE);
	}

	private void setAndEffectConfig(Project project, Environment environment, String key, String value) {
		Config config = configService.findConfigByKey(key);
		int configId = 0;
		if (config == null) {
			config = new Config();
			config.setKey(key);
			config.setDesc("");
			config.setTypeEnum(ConfigTypeEnum.String);
			config.setProjectId(project.getId());
			configId = configService.create(config);
		} else {
			configId = config.getId();
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
		configService.
	}

}
