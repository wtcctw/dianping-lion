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

import org.json.JSONObject;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.RegisterRelatedException;
import com.dianping.lion.exception.RuntimeBusinessException;


/**
 * TODO Comment of GetConfigServlet
 * @author danson.liu
 *
 */
public class GetConfigServlet extends AbstractLionServlet {

	private static final long serialVersionUID = 1512883947325409564L;
	
	private static final String PROP_DB = "db";
	private static final String PROP_ZK = "zk";

	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		PrintWriter writer = resp.getWriter();
		String projectName = getNotBlankParameter(req, PARAM_PROJECT);
		String key = getNotBlankParameter(req, PARAM_KEY);
		String configKey = checkConfigKey(projectName, key);
		String env = getNotBlankParameter(req, PARAM_ENV);
		
		Config config = configService.findConfigByKey(configKey);
		if (config == null) {
			throw new RuntimeBusinessException("config[" + configKey + "] not found.");
		}
		Environment environment = getRequiredEnv(env);
		
		JSONObject resultJson = new JSONObject();
		ConfigInstance configInst = configService.findInstance(config.getId(), environment.getId(), ConfigInstance.NO_CONTEXT);
		if (configInst != null) {
			resultJson.put(PROP_DB, configInst.getValue());
		}
		try {
			String registeredValue = configService.getConfigFromRegisterServer(environment.getId(), configKey);
			if (registeredValue != null) {
				resultJson.put(PROP_ZK, registeredValue);
			}
		} catch (RegisterRelatedException e) {
			throw new RuntimeBusinessException("get config value from zk failed.");
		}
		writer.print(SUCCESS_CODE + "|" + resultJson.toString());
	}

}
