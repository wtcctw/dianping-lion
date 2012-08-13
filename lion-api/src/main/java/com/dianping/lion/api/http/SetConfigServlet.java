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

import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;

/**
 * 根据项目名和环境，设置配置项的http接口
 * @author danson.liu
 *
 */
public class SetConfigServlet extends AbstractLionServlet {

	private static final long serialVersionUID 	= 1420817678507296960L;
	
	private static final String EFFECT_SOON 	= "1";
	
	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName = getNotBlankParameter(req, PARAM_PROJECT);
		String env = getNotBlankParameter(req, PARAM_ENV);
		String key = getNotBlankParameter(req, PARAM_KEY);
		String value = getRequiredParameter(req, PARAM_VALUE);
		String effect = getNotBlankParameter(req, PARAM_EFFECT);	//是否立即生效
		String configKey = checkConfigKey(projectName, key);
		
		Project project = getRequiredProject(projectName);
		Environment environment = getRequiredEnv(env);
		
		if (EFFECT_SOON.equals(effect)) {
			configService.setConfigValue(project.getId(), environment.getId(), configKey, "", ConfigInstance.NO_CONTEXT, value, 
					ConfigSetType.RegisterAndPush);
		} else {
			String feature = getNotBlankParameter(req, PARAM_FEATURE);
			ConfigSetTask configSetTask = new ConfigSetTask();
			configSetTask.setProjectId(project.getId());
			configSetTask.setEnvId(environment.getId());
			configSetTask.setFeature(feature);
			configSetTask.setKey(configKey);
			configSetTask.setValue(value);
			configSetTask.setContext(ConfigInstance.NO_CONTEXT);
			configSetTask.setDelete(false);
			configReleaseService.createSetTask(configSetTask);
		}
		resp.getWriter().print(SUCCESS_CODE);
	}

}
