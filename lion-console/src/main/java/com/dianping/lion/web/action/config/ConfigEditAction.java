/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-17
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigEditAction extends AbstractConfigAction {
	
	private Config config;
	
	private boolean trim;
	
	private String value;
	
	private List<Integer> environments;
	
	public String create() {
		int configType = config.getType();
		boolean isStringType = configType == ConfigTypeEnum.String.getValue();
		if ((isStringType && trim) || !isStringType) {
			value = value.trim();
		}
		int configId;
		try {
			configId = configService.create(config);
		} catch (RuntimeBusinessException e) {
			createErrorStreamResponse(e.getMessage());
			return SUCCESS;
		}
		List<String> failedEnvs = new ArrayList<String>();
		Map<Integer, Environment> envMap = environmentService.findEnvMap();
		for (Integer environment : environments) {
			ConfigInstance instance = new ConfigInstance();
			instance.setConfigId(configId);
			instance.setEnvId(environment);
			instance.setValue(value);
			try {
				configService.createInstance(instance);
			} catch (Exception e) {
				failedEnvs.add(envMap.get(environment).getLabel());
			}
		}
		if (failedEnvs.isEmpty()) {
			createSuccessStreamResponse();
		} else {
			createWarnStreamResponse("保存配置项值到DB失败，在[" + StringUtils.join(failedEnvs, ',') + "]环境上.");
		}
		return SUCCESS;
	}
	
	public String createAndDeploy() {
		
		return null;
	}
	
//	public String createConfig() {
//		project = projectService.getProject(projectId);
//		config.setProjectId(projectId);
//		try {
//			config = configService.create(config);
//			return SUCCESS;
//		} catch (RuntimeBusinessException e) {
//			errorMessage = e.getMessage();
//			return INPUT;
//		}
//	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * @return the projectId
	 */
	public int getPid() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setPid(int projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the trim
	 */
	public boolean isTrim() {
		return trim;
	}

	/**
	 * @param trim the trim to set
	 */
	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	/**
	 * @return the environments
	 */
	public List<Integer> getEnvironments() {
		return environments;
	}

	/**
	 * @param environments the environments to set
	 */
	public void setEnvironments(List<Integer> environments) {
		this.environments = environments;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
