/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
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
package com.dianping.lion.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDao extends SqlMapClientDaoSupport implements ConfigDao {
	
	@SuppressWarnings("unchecked")
	public List<Config> findConfigsByProject(int projectId, boolean includeDeleted) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectId", projectId);
		params.put("includeDeleted", includeDeleted);
		return getSqlMapClientTemplate().queryForList("Config.findConfigsByProject", params);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<ConfigInstance> findInstancesByProjectAndEnv(int projectId, int envId, boolean includeDeleted) {
		return getSqlMapClientTemplate().queryForList("Config.findInstancesByProjectAndEnv", parameters(projectId, envId, includeDeleted));
	}

	@Override
	public Config getConfig(int configId) {
		return getConfig(configId, false);
	}
	
	private Config getConfig(int configId, boolean includeDeleted) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("configId", configId);
		params.put("includeDeleted", includeDeleted);
		return (Config) getSqlMapClientTemplate().queryForObject("Config.getConfig", params);
	}

	@Override
	public Config getNextConfig(int configId) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.getNextConfig", configId);
	}

	@Override
	public Config getPrevConfig(int configId) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.getPrevConfig", configId);
	}

	@Override
	public void update(Config config) {
		getSqlMapClientTemplate().update("Config.updateConfig", config);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ConfigInstance> findDefaultInstances(int projectId, int envId, boolean includeDeleted) {
		List<ConfigInstance> instances = getSqlMapClientTemplate().queryForList("Config.findDefaultInstances", 
				parameters(projectId, envId, includeDeleted));
		Map<Integer, ConfigInstance> instanceMap = new HashMap<Integer, ConfigInstance>(instances.size());
		for (ConfigInstance instance : instances) {
			instanceMap.put(instance.getConfigId(), instance);
		}
		return instanceMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findHasInstanceConfigs(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findHasInstanceConfigs", parameters(projectId, envId, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findHasContextInstConfigs(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findHasContextInstConfigs", parameters(projectId, envId, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ConfigStatus> findConfigStatus(int projectId, int envId) {
		List<ConfigStatus> statusList = getSqlMapClientTemplate().queryForList("Config.findConfigStatuses", 
				parameters(projectId, envId, null));
		Map<Integer, ConfigStatus> statusMap = new HashMap<Integer, ConfigStatus>(statusList.size());
		for (ConfigStatus status : statusList) {
			statusMap.put(status.getConfigId(), status);
		}
		return statusMap;
	}
	
	@Override
	public void deleteInstance(int configId, int envId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("configId", configId);
		params.put("envId", envId);
		params.put("currentUser", SecurityUtils.getCurrentUser().getId());
		getSqlMapClientTemplate().update("Config.deleteInstance", params);
	}
	
	@Override
	public void deleteStatus(int configId, int envId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("configId", configId);
		params.put("envId", envId);
		getSqlMapClientTemplate().delete("Config.deleteStatus", params);
	}
	
	private Map<String, Object> parameters(Integer projectId, Integer envId, Boolean includeDeleted) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("projectId", projectId);
		parameters.put("envId", envId);
		parameters.put("includeDeleted", includeDeleted);
		return parameters;
	}

}
