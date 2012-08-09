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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;


/**
 * TODO Comment of RegisterConfigsServlet
 * url: takeeffect?project=xxx&key=xxx&key=xxx&env=xxx("key" params not present if no config is updated)
 * 这里的key是包含了projectName前缀的完整key
 * @author danson.liu
 *
 */
public class TakeEffectConfigsServlet extends AbstractLionServlet {

	private static final long serialVersionUID = 4596830426101363885L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName = getRequiredParameter(req, PARAM_PROJECT);
		String[] keys = req.getParameterValues(PARAM_KEY);
		String env = getRequiredParameter(req, PARAM_ENV);
		Project project = projectService.findProject(projectName);
		if (project == null) {
			throw new RuntimeBusinessException("project[" + projectName + "] not found.");
		}
		Environment environment = environmentService.findEnvByName(env);
		if (environment == null) {
			throw new RuntimeBusinessException("environment[" + env + "] not found.");
		}
		
		boolean checkExistsPass = true;
		List<Config> configsToRegister = new ArrayList<Config>();
		List<String> configsNotFound = new ArrayList<String>();
		List<String> keyList = keys != null ? Arrays.asList(keys) : Collections.EMPTY_LIST;
		Map<String, Config> configs = getNeedRegisteredConfigs(keyList);
		if (keys != null && keys.length > 0) {
			for (String key : keys) {
				Config config = configs.get(key);
				if (config == null) {
					checkExistsPass = false;
					configsNotFound.add(key);
				} else {
					ConfigInstance configInst = configService.findDefaultInstance(config.getId(), environment.getId());
					if (configInst == null) {
						checkExistsPass = false;
						configsNotFound.add(key);
					} else {
						configsToRegister.add(config);
					}
				}
			}
		}
		if (!checkExistsPass) {
			throw new RuntimeBusinessException("config[" + StringUtils.join(configsNotFound, ",") + "] not found or not set.");
		}
		List<Config> ineffectConfigs = configService.findForeffectiveConfig(project.getId(), environment.getId());
		for (Config ineffectConfig : ineffectConfigs) {
			if (!keyList.contains(ineffectConfig.getKey())) {
				configsToRegister.add(ineffectConfig);
			}
		}
		if (!configsToRegister.isEmpty()) {
			configService.autoRegister(project, environment.getId(), configsToRegister);
		}
		resp.getWriter().write("ok");
	}

	/**
	 * @param keys
	 * @return
	 */
	private Map<String, Config> getNeedRegisteredConfigs(List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Config> configMap = new HashMap<String, Config>();
		List<Config> configList = configService.findConfigByKeys(keys);
		for (Config config : configList) {
			configMap.put(config.getKey(), config);
		}
		return configMap;
	}

}
