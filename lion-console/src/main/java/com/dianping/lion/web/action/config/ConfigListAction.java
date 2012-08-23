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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigDeleteResult;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;
import com.dianping.lion.web.vo.ConfigAttribute;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigListAction extends AbstractConfigAction {
	
	private Integer configId;
	
	private ConfigCriteria criteria = new ConfigCriteria();
	
	private List<Environment> environments;
	
	private List<ConfigVo> configVos;
	
	private Config config;
	
	private ConfigAttribute configAttr;
	
	private Map<Integer, List<ConfigInstance>> instanceMap;

	public String list() {
		this.environments = environmentService.findAll();
		this.project = projectService.getProject(projectId);
		if (envId == null) {
			envId = !environments.isEmpty() ? environments.get(0).getId() : null;
		}
		if (envId != null) {
			environment = environmentService.findEnvByID(envId);
			if (environment == null) {
				throw new RuntimeBusinessException("该环境已不存在!");
			}
			criteria.setProjectId(projectId);
			criteria.setEnvId(envId);
			configVos = configService.findConfigVos(criteria);
		}
		return SUCCESS;
	}
	
	public String ajaxList() {
		criteria.setProjectId(projectId);
		criteria.setEnvId(envId);
		configVos = configService.findConfigVos(criteria);
		return SUCCESS;
	}
	
	public String ajaxLoad() {
	    Config config = configService.getConfig(configId);
	    createSuccessStreamResponse(config);
	    return SUCCESS;
	}
	
	public String editConfigAttr() {
	    try {
        	    Config config = configService.getConfig(configId);
        	    if (config != null) {
        	        config.setPrivatee(!configAttr.isPublic());
        	        configService.updateConfig(config);
        	    }
        	    createSuccessStreamResponse();
	    } catch (RuntimeException e) {
	        createErrorStreamResponse();
	    }
	    return SUCCESS;
	}
	
	public String clearInstance() {
		try {
			configService.deleteInstance(configId, envId);
			createSuccessStreamResponse();
		} catch (RuntimeException e) {
			createErrorStreamResponse("清除失败[" + e.getMessage() + "].");
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			ConfigDeleteResult deleteResult = configService.delete(configId);
			if (deleteResult.isSucceed()) {
				createSuccessStreamResponse();
			} else {
				List<Environment> failedEnvs = deleteResult.getFailedEnvs();
				String failedEnvStr = "";
				for (int i = 0; i < failedEnvs.size(); i++) {
					failedEnvStr += (i > 0 ? "," : "") + failedEnvs.get(i).getLabel();
				}
				createErrorStreamResponse("[" + failedEnvStr + "]配置清除失败，其他环境成功.");
			}
		} catch (RuntimeException e) {
			createErrorStreamResponse("删除失败.");
		}
		return SUCCESS;
	}
	
	public String moveUp() {
		try {
			configService.moveUp(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		createSuccessStreamResponse();
		return SUCCESS;
	}
	
	public String moveDown() {
		try {
			configService.moveDown(projectId, configId);
		} catch (EntityNotFoundException e) {
		}
		createSuccessStreamResponse();
		return SUCCESS;
	}
	
	public String editMore() {
		this.project = projectService.getProject(projectId);
		this.environments = environmentService.findAll();
		this.environment = environmentService.findEnvByID(envId);
		this.config = configService.getConfig(configId);
		List<ConfigInstance> instances = configService.findInstancesByConfig(configId, ServiceConstants.MAX_AVAIL_CONFIG_INST);
		this.instanceMap = new HashMap<Integer, List<ConfigInstance>>();
		for (ConfigInstance instance : instances) {
			int envId = instance.getEnvId();
			if (!instanceMap.containsKey(envId)) {
				instanceMap.put(envId, new ArrayList<ConfigInstance>());
			}
			instanceMap.get(envId).add(instance);
		}
		return SUCCESS;
	}

	/**
	 * @return the configInsts
	 */
	public List<ConfigVo> getConfigVos() {
		return configVos;
	}

	/**
	 * @return the environments
	 */
	public List<Environment> getEnvironments() {
		return environments;
	}

	/**
	 * @return the configId
	 */
	public Integer getConfigId() {
		return configId;
	}

	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	/**
	 * @return the criteria
	 */
	public ConfigCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(ConfigCriteria criteria) {
		this.criteria = criteria;
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
	 * @return the instanceMap
	 */
	public Map<Integer, List<ConfigInstance>> getInstanceMap() {
		return instanceMap;
	}

	/**
	 * @param instanceMap the instanceMap to set
	 */
	public void setInstanceMap(Map<Integer, List<ConfigInstance>> instanceMap) {
		this.instanceMap = instanceMap;
	}

    /**
     * @return the configAttrs
     */
    public ConfigAttribute getConfigAttr() {
        return configAttr;
    }

    /**
     * @param configAttr the configAttrs to set
     */
    public void setConfigAttr(ConfigAttribute configAttr) {
        this.configAttr = configAttr;
    }
	
}
