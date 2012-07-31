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
import org.json.JSONObject;

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
	
	private int configId;
	
	private boolean trim;
	
	private String value;
	
	private List<Integer> envIds;
	
	private boolean ifDeploy;
	private boolean ifPush;
	
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
		for (Integer envId : envIds) {
			ConfigInstance instance = new ConfigInstance();
			instance.setConfigId(configId);
			instance.setEnvId(envId);
			instance.setValue(value);
			try {
				configService.createInstance(instance);
				if (ifPush) {
					configService.registerAndPushToMedium(configId, envId);
				} else if (ifDeploy) {
					configService.registerToMedium(configId, envId);
				}
			} catch (RuntimeException e) {
				logger.error("保存/推送配置失败.", e);
				failedEnvs.add(envMap.get(envId).getLabel());
			}
		}
		if (failedEnvs.isEmpty()) {
			createSuccessStreamResponse();
		} else {
			createWarnStreamResponse("保存/推送[" + StringUtils.join(failedEnvs, ',') + "]环境上的配置项值失败,请通过列表检查.");
		}
		return SUCCESS;
	}
	
	public String saveDefaultValue() {
		Config configFound = configService.getConfig(configId);
		if (configFound == null) {
			createErrorStreamResponse("该配置已不存在!");
			return SUCCESS;
		}
		List<String> failedEnvs = new ArrayList<String>();
		Map<Integer, Environment> envMap = environmentService.findEnvMap();
		for (Integer envId : envIds) {
			ConfigInstance instance = new ConfigInstance();
			instance.setConfigId(configId);
			instance.setEnvId(envId);
			instance.setValue(value);
			try {
				configService.setConfigValue(configId, envId, ConfigInstance.NO_CONTEXT, value);
				if (ifPush) {
					configService.registerAndPushToMedium(configId, envId);
				} else if (ifDeploy) {
					configService.registerToMedium(configId, envId);
				}
			} catch (Exception e) {
				logger.error("保存/推送配置失败.", e);
				failedEnvs.add(envMap.get(envId).getLabel());
			}
		}
		if (failedEnvs.isEmpty()) {
			createSuccessStreamResponse();
		} else {
			createWarnStreamResponse("保存/推送[" + StringUtils.join(failedEnvs, ',') + "]环境上的配置项值失败,请通过列表检查.");
		}
		return SUCCESS;
	}
	
	public String loadDefaultValue() {
		Config configFound = configService.getConfig(configId);
		if (configFound == null) {
			createErrorStreamResponse("该配置已不存在!");
			return SUCCESS;
		}
		ConfigInstance instanceFound = configService.findDefaultInstance(configId, envId);
		String message = null;
		if (instanceFound == null) {
			Environment prevEnv = environmentService.findPrevEnv(envId);
			if (prevEnv != null) {
				instanceFound = configService.findDefaultInstance(configId, prevEnv.getId());
				if (instanceFound != null) {
					message = "Value预填[" + prevEnv.getLabel() + "]环境的值.";
				}
			}
		}
		createStreamResponse("{\"code\":0, \"value\":" + JSONObject.quote(instanceFound != null ? instanceFound.getValue() : "") + ", " 
				+ "\"msg\":\"" + (message != null ? message : "") + "\"}");
		return SUCCESS;
	}
	
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
	public List<Integer> getEnvIds() {
		return envIds;
	}

	/**
	 * @param environments the environments to set
	 */
	public void setEnvIds(List<Integer> envIds) {
		this.envIds = envIds;
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

	/**
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}

	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(int configId) {
		this.configId = configId;
	}

	/**
	 * @param ifDeploy the ifDeploy to set
	 */
	public void setIfDeploy(boolean ifDeploy) {
		this.ifDeploy = ifDeploy;
	}

	/**
	 * @param ifPush the ifPush to set
	 */
	public void setIfPush(boolean ifPush) {
		this.ifPush = ifPush;
	}
	
}
