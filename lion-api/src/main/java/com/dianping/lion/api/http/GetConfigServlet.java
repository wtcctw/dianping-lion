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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.RuntimeBusinessException;


/**
 * TODO Comment of GetConfigServlet
 * @author danson.liu
 *
 */
public class GetConfigServlet extends AbstractLionServlet {

	private static final long serialVersionUID = 1512883947325409564L;

	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		JSONObject resultJson = new JSONObject();
		PrintWriter writer = resp.getWriter();
		try {
			String projectName = getRequiredParameter(req, PARAM_PROJECT);
			String key = getRequiredParameter(req, PARAM_KEY);
			String configKey = key.startsWith(projectName) ? key : projectName + "." + key;
			String env = req.getParameter(PARAM_ENV);
			Config config = configService.findConfigByKey(configKey);
			if (config == null) {
				throw new RuntimeBusinessException("config[" + configKey + "] not found.");
			}
			List<Environment> environments = new ArrayList<Environment>();
			if (StringUtils.isNotBlank(env)) {
				Environment environment = environmentService.findEnvByName(env);
				if (environment == null) {
					throw new RuntimeBusinessException("environment[" + env + "] not found.");
				}
				environments.add(environment);
			} else {
				environments.addAll(environmentService.findAll());
			}
			for (Environment environment : environments) {
				ConfigInstance configInst = configService.findInstance(config.getId(), environment.getId(), ConfigInstance.NO_CONTEXT);
				if (configInst != null) {
					resultJson.put(environment.getName(), configInst.getValue());
				}
			}
			writer.print(resultJson.toString());
		} catch (Exception e) {
			try {
				resultJson.put("error", e.getMessage());
				writer.print(resultJson.toString());
			} catch (JSONException e1) {
				logger.error("create json result object failed.", e1);
				throw e1;
			}
		}
	}

}
