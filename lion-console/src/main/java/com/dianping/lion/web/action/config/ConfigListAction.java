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

import org.apache.commons.lang.xwork.StringUtils;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.exception.ReferencedConfigForbidDeleteException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigDeleteResult;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;
import com.dianping.lion.vo.Paginater;
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
	
	private Paginater<Config> paginater = new Paginater<Config>();
	
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
			configVos = enrichWithPrivilege(projectId, envId, SecurityUtils.getCurrentUser(), configService.findConfigVos(criteria));
		}
		return SUCCESS;
	}

    public String ajaxList() {
		criteria.setProjectId(projectId);
		criteria.setEnvId(envId);
		configVos = enrichWithPrivilege(projectId, envId, SecurityUtils.getCurrentUser(), configService.findConfigVos(criteria));
		return SUCCESS;
	}
    
    public String ajaxSimpleList() {
    	paginater.setMaxResults(13);
    	paginater = configService.paginateConfigs(criteria, paginater);
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
    	    	if (hasLockPrivilege()) {
    	    		config.setPrivatee(!configAttr.isPublic());
    	    	}
    	    	String desclog = null;
    	    	if (hasEditAttrPrivilege(config.getProjectId())) {
    	    		String oldDesc = config.getDesc();
    	    		config.setDesc(this.config.getDesc());
    	    		if (!StringUtils.equals(oldDesc, config.getDesc())) {
    	    			desclog = ", desc: " + config.getDesc();
    	    		}
    	    	}
    	        configService.updateConfig(config);
    	        String logcontent = "设置配置属性: " + config.getKey() + ", [公开: " + configAttr.isPublic() 
    	        	+ (desclog != null ? desclog : "") + "]";
				operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_EditAttr, config.getProjectId(), logcontent)
					.key(config.getKey()));
    	    }
    	    createSuccessStreamResponse();
	    } catch (RuntimeException e) {
	        createErrorStreamResponse();
	    }
	    return SUCCESS;
	}
	
	public String clearInstance() {
		try {
			Config configFound = configService.getConfig(configId);
			if (configFound != null) {
				if (!privilegeDecider.hasEditConfigPrivilege(configFound.getProjectId(), envId, configId, SecurityUtils.getCurrentUserId())) {
					throw NoPrivilegeException.INSTANCE;
				}
			}
			configService.deleteInstance(configId, envId);
			if (configFound != null) {
				operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Clear, configFound.getProjectId(), envId, 
						"清除配置项值[" + configFound.getKey() + "]").key(configFound.getKey()));
			}
			createSuccessStreamResponse();
		} catch (ReferencedConfigForbidDeleteException e) {
			createErrorStreamResponse("清除失败[存在对该配置的引用].");
		} catch (RuntimeException e) {
			createErrorStreamResponse("清除失败[" + e.getMessage() + "].");
		}
		return SUCCESS;
	}
	
	public String delete() {
		if (!checkDeletePrivilege(configId)) {
			createErrorStreamResponse("删除失败: 线上环境无权限，且已设置值.");
			return SUCCESS;
		}
		try {
			ConfigDeleteResult deleteResult = configService.delete(configId);
			if (deleteResult.isSucceed()) {
			    Config configDeleted = deleteResult.getConfig();
			    if (configDeleted != null) {
			        operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Delete, configDeleted.getProjectId(), 
			                "删除配置项[" + configDeleted.getKey() + "]").key(configDeleted.getKey()));
			    }
				createSuccessStreamResponse();
			} else {
				List<String> failedEnvs = deleteResult.getFailedEnvs();
				String failedEnvStr = "";
				for (int i = 0; i < failedEnvs.size(); i++) {
					failedEnvStr += (i > 0 ? "," : "") + failedEnvs.get(i);
				}
				createErrorStreamResponse("[" + failedEnvStr + "]配置清除失败" + (deleteResult.isHasReference() ? "[存在对该配置的引用]" : "") 
						+ "，其他环境成功.");
			}
		} catch (RuntimeException e) {
			createErrorStreamResponse("删除失败.");
		}
		return SUCCESS;
	}
	
	private boolean checkDeletePrivilege(int configId) {
		Config config = configService.getConfig(configId);
		if (config != null) {
			List<Environment> environments = environmentService.findAll();
			Integer currentUserId = SecurityUtils.getCurrentUserId();
			for (Environment environment : environments) {
				if (!privilegeDecider.hasEditConfigPrivilege(config.getProjectId(), environment.getId(), configId, currentUserId)) {
					ConfigInstance instance = configService.findInstance(configId, environment.getId(), ConfigInstance.NO_CONTEXT);
					if (instance != null) {
						return false;
					}
				}
			}
		}
		return true;
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
    
    private List<ConfigVo> enrichWithPrivilege(int projectId, int envId, User user, List<ConfigVo> configVos) {
        boolean hasLockPrivilege = user != null && privilegeDecider.hasLockConfigPrivilege(user.getId());
        Integer userId = user != null ? user.getId() : null;
        for (ConfigVo configVo : configVos) {
            Config config = configVo.getConfig();
            boolean hasReadPrivilege = privilegeDecider.hasReadConfigPrivilege(projectId, envId, config.getId(), userId);
            configVo.setHasReadPrivilege(hasReadPrivilege);
            boolean hasEditPrivilege = privilegeDecider.hasEditConfigPrivilege(projectId, envId, config.getId(), userId);
            configVo.setHasEditPrivilege(hasEditPrivilege);
            configVo.setHasLockPrivilege(hasLockPrivilege);
        }
        return configVos;
    }

	/**
	 * @return the configInsts
	 */
	public List<ConfigVo> getConfigVos() {
		return configVos;
	}

	public Paginater<Config> getPaginater() {
		return paginater;
	}

	public void setPaginater(Paginater<Config> paginater) {
		this.paginater = paginater;
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
