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
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.json.JSONObject;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
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
	
	public String create() {
		int configType = config.getType();
		boolean isStringType = configType == ConfigTypeEnum.String.getValue();
		if ((isStringType && trim) || !isStringType) {
			value = value.trim();
		}
		int configId;
		try {
			configId = configService.create(config);
			operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Add, config.getProjectId(), 
			        "创建配置项[" + config.getKey() + "]").key(config.getKey()));
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
				operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Edit, config.getProjectId(), envId, 
				        "设置配置项[" + config.getKey() + "]").key(config.getKey(), ConfigInstance.NO_CONTEXT, null, instance.getValue()));
			} catch (RuntimeException e) {
				String env = envMap.get(envId).getLabel();
				logger.error("创建配置[key=" + config.getKey() + ", env=" + env + "]失败.", e);
				failedEnvs.add(env);
			}
		}
		if (failedEnvs.isEmpty()) {
			createSuccessStreamResponse();
		} else {
			createWarnStreamResponse("保存[" + StringUtils.join(failedEnvs, ',') + "]环境下的配置项值失败.");
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
			try {
			    ConfigInstance existInstance = configService.findInstance(configId, envId, ConfigInstance.NO_CONTEXT);
				configService.setConfigValue(configId, envId, ConfigInstance.NO_CONTEXT, value);
				operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Edit, configFound.getProjectId(), envId,
				        "设置配置项: " + configFound.getKey())
				        .key(configFound.getKey(), ConfigInstance.NO_CONTEXT, existInstance != null ? existInstance.getValue() : null, value));
			} catch (Exception e) {
				String env = envMap.get(envId).getLabel();
				logger.error("保存配置[key=" + configFound.getKey() + ", env=" + env + "]失败.", e);
				failedEnvs.add(env);
			}
		}
		if (failedEnvs.isEmpty()) {
			createSuccessStreamResponse();
		} else {
			createWarnStreamResponse("保存[" + StringUtils.join(failedEnvs, ',') + "]环境下的配置项值失败.");
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
		try {
            createStreamResponse("{\"code\":0, \"value\":" + JSONObject.quote(instanceFound != null ? instanceFound.getValue() : "") 
                    + ", \"privilege\":" + JSONUtil.serialize(getEditPrivileges(configFound.getProjectId(), configId)) + ", \"msg\":\"" + (message != null ? message : "") + "\"}");
        } catch (JSONException e) {
            logger.error("get config edit privilege failed.", e);
            createErrorStreamResponse("get config edit privilege failed.");
        }
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
	
}
