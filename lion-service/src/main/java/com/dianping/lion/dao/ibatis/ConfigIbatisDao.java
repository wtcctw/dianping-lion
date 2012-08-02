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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.entity.ConfigStatusEnum;
import com.dianping.lion.util.Maps;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDao extends SqlMapClientDaoSupport implements ConfigDao {
	
	@SuppressWarnings("unchecked")
	public List<Config> findConfigsByProject(int projectId) {
		return getSqlMapClientTemplate().queryForList("Config.findConfigsByProject", projectId);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<ConfigInstance> findInstancesByProjectAndEnv(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findInstancesByProjectAndEnv", 
				Maps.entry("projectId", projectId).entry("envId", envId).get());
	}

	public Config getConfig(int configId) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.getConfig", configId);
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
	public Map<Integer, ConfigInstance> findDefaultInstances(int projectId, int envId) {
		List<ConfigInstance> instances = getSqlMapClientTemplate().queryForList("Config.findDefaultInstances", 
				Maps.entry("projectId", projectId).entry("envId", envId).get());
		Map<Integer, ConfigInstance> instanceMap = new HashMap<Integer, ConfigInstance>(instances.size());
		for (ConfigInstance instance : instances) {
			instanceMap.put(instance.getConfigId(), instance);
		}
		return instanceMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findHasInstanceConfigs(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findHasInstanceConfigs", 
				Maps.entry("projectId", projectId).entry("envId", envId).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findHasContextInstConfigs(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findHasContextInstConfigs", 
				Maps.entry("projectId", projectId).entry("envId", envId).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ConfigStatus> findConfigStatus(int projectId, int envId) {
		List<ConfigStatus> statusList = getSqlMapClientTemplate().queryForList("Config.findConfigStatuses", 
				Maps.entry("projectId", projectId).entry("envId", envId).get());
		Map<Integer, ConfigStatus> statusMap = new HashMap<Integer, ConfigStatus>(statusList.size());
		for (ConfigStatus status : statusList) {
			statusMap.put(status.getConfigId(), status);
		}
		return statusMap;
	}
	
	@Override
	public int deleteInstance(int configId, Integer envId) {
		return getSqlMapClientTemplate().delete("Config.deleteInstance", Maps.entry("configId", configId)
				.entry("envId", envId).get());
	}
	
	@Override
	public int deleteStatus(int configId, Integer envId) {
		return getSqlMapClientTemplate().delete("Config.deleteStatus", Maps.entry("configId", configId)
				.entry("envId", envId).get());
	}
	
	@Override
	public int delete(int configId) {
		return getSqlMapClientTemplate().delete("Config.deleteConfig", configId);
	}
	
	@Override
	public int create(Config config) {
		return (Integer) getSqlMapClientTemplate().insert("Config.insertConfig", config);
	}
	
	@Override
	public int getMaxSeq(int projectId) {
		Object maxSeq = getSqlMapClientTemplate().queryForObject("Config.getMaxSeq", projectId);
		return maxSeq != null ? (Integer) maxSeq : 0;
	}

	@Override
	public int getMaxInstSeq(int configId, int envId) {
		Object maxSeq = getSqlMapClientTemplate().queryForObject("Config.getMaxInstSeq", Maps.entry("configId", configId)
				.entry("envId", envId).get());
		return maxSeq != null ? (Integer) maxSeq : 0;
	}

	@Override
	public Config findConfigByKey(String key) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.findConfigByKey", key);
	}

	@Override
	public ConfigInstance findInstance(int configId, int envId, String context) {
		return (ConfigInstance) getSqlMapClientTemplate().queryForObject("Config.findInstance", Maps.entry("configId", configId)
				.entry("envId", envId).entry("contextmd5", DigestUtils.md5Hex(context)).get());
	}

	@Override
	public int createInstance(ConfigInstance instance) {
		instance.setContextmd5(DigestUtils.md5Hex(instance.getContext()));
		return (Integer) getSqlMapClientTemplate().insert("Config.insertInstance", instance);
	}

	@Override
	public int createStatus(ConfigStatus status) {
		return (Integer) getSqlMapClientTemplate().insert("Config.insertStatus", status);
	}

	@Override
	public int updateStatus(ConfigStatus status) {
		return getSqlMapClientTemplate().update("Config.updateStatus", status);
	}

	@Override
	public int updateStatusStat(int configId, int envId, ConfigStatusEnum status) {
		return getSqlMapClientTemplate().update("Config.updateStatusStat", Maps.entry("configId", configId)
				.entry("envId", envId).entry("status", status.getValue()).get());
	}

	@Override
	public int updateInstance(ConfigInstance instance) {
		if (instance.getContextmd5() == null) {
			instance.setContextmd5(DigestUtils.md5Hex(instance.getContext()));
		}
		return getSqlMapClientTemplate().update("Config.updateInstance", instance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv) {
		if (maxPerEnv == null) {
			return getSqlMapClientTemplate().queryForList("Config.findInstance", Maps.entry("configId", configId).get());
		}
		return getSqlMapClientTemplate().queryForList("Config.findMaxInstsBySeq", Maps.entry("configId", configId).entry("max", maxPerEnv).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigInstance> findInstancesByConfig(int configId, int envId, Integer maxPerEnv) {
		if (maxPerEnv == null) {
			return getSqlMapClientTemplate().queryForList("Config.findInstance", Maps.entry("configId", configId).entry("envId", envId).get());
		}
		return getSqlMapClientTemplate().queryForList("Config.findMaxInstsBySeq", Maps.entry("configId", configId).entry("envId", envId).entry("max", maxPerEnv).get());
	}
	@Override
	public Config getConfigByName(String configName) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.getConfigByName", configName);
	}

}
