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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.util.Maps;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.Paginater;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDao extends SqlMapClientDaoSupport implements ConfigDao {

	@SuppressWarnings("unchecked")
	public List<Config> findConfigByProject(int projectId) {
		return getSqlMapClientTemplate().queryForList("Config.findConfigsByProject", projectId);
	}

	@SuppressWarnings({ "unchecked"})
	public List<ConfigInstance> findInstancesByProjectAndEnv(int projectId, int envId) {
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
	public int updateConfig(Config config) {
		return getSqlMapClientTemplate().update("Config.updateConfig", config);
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

	@Override
	public int deleteInstances(int configId, Integer envId) {
		return getSqlMapClientTemplate().delete("Config.deleteInstances", Maps.entry("configId", configId)
				.entry("envId", envId).get());
	}

    public int deleteInstance(int configId, int envId, String group) {
        return getSqlMapClientTemplate().delete("Config.deleteInstance", Maps.entry("configId", configId).entry("envId", envId).entry("context", group).get());
    }

	@Override
	public int deleteConfig(int configId) {
		return getSqlMapClientTemplate().delete("Config.deleteConfig", configId);
	}

	@Override
	public int createConfig(Config config) {
		return (Integer) getSqlMapClientTemplate().insert("Config.insertConfig", config);
	}

	@Override
	public int getMaxConfigSeq(int projectId) {
		Object maxSeq = getSqlMapClientTemplate().queryForObject("Config.getMaxSeq", projectId);
		return maxSeq != null ? (Integer) maxSeq : 0;
	}

	@Override
	public int getMaxConfigInstSeq(int configId, int envId) {
		Object maxSeq = getSqlMapClientTemplate().queryForObject("Config.getMaxInstSeq", Maps.entry("configId", configId)
				.entry("envId", envId).get());
		return maxSeq != null ? (Integer) maxSeq : 0;
	}

	@Override
	public Config findConfigByKey(String key) {
		return (Config) getSqlMapClientTemplate().queryForObject("Config.findConfigByKey", key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Config> findConfigByKeys(List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return Collections.emptyList();
		}
		return getSqlMapClientTemplate().queryForList("Config.findConfigByKeys", keys);
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
		List<ConfigInstance> instances = getSqlMapClientTemplate().queryForList("Config.findMaxInstsBySeq", Maps.entry("configId", configId).entry("envId", envId).entry("max", maxPerEnv).get());
		boolean hasDefaultInst = false;
		for (int i = instances.size() - 1; i >= 0; i--) {
			ConfigInstance instance = instances.get(i);
			if (ConfigInstance.NO_CONTEXT.equals(instance.getContext())) {
				hasDefaultInst = true;
				break;
			}
		}
		if (!hasDefaultInst) {
			instances.add(findInstance(configId, envId, ConfigInstance.NO_CONTEXT));
		}
		return instances;
	}

	@Override
	public int getConfigInstCount(int configId, int envId) {
	    return (Integer) getSqlMapClientTemplate().queryForObject("Config.getConfigInstCount", Maps.entry("configId", configId).entry("envId", envId).get());
	}

	@Override
	public int updateModifyStatus(int configId, int envId) {
		return getSqlMapClientTemplate().update("Config.updateModifyStatus", Maps.entry("configId", configId).entry("envId", envId)
				.entry("modifyUserId", SecurityUtils.getCurrentUserId()).get());
	}

	public int createStatus(ConfigStatus status) {
		return (Integer) getSqlMapClientTemplate().insert("Config.insertStatus", status);
	}

	public int deleteStatus(int configId, int envId) {
		return getSqlMapClientTemplate().delete("Config.deleteStatus", Maps.entry("configId", configId).entry("envId", envId).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigStatus> findStatus(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findStatus", Maps.entry("projectId", projectId).entry("envId", envId).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigStatus> findModifyTime(int projectId, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.findModifyTime", Maps.entry("projectId", projectId).entry("envId", envId).get());
	}

	@Override
	public long getConfigCount(ConfigCriteria criteria) {
		return (Long) getSqlMapClientTemplate().queryForObject("Config.getConfigCount", Maps.entry("criteria", criteria).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Config> getConfigList(ConfigCriteria criteria, Paginater paginater) {
		return getSqlMapClientTemplate().queryForList("Config.getConfigList", Maps.entry("criteria", criteria).entry("paginater", paginater).get());
	}

	@Override
	public boolean hasConfigReferencedTo(String configKey, int envId) {
		return getSqlMapClientTemplate().queryForObject("Config.hasConfigReferencedTo", Maps.entry("configKey", configKey).entry("envId", envId).get()) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigInstance> getInstanceReferencedTo(String configKey, int envId) {
		return getSqlMapClientTemplate().queryForList("Config.getInstanceReferencedTo", Maps.entry("configKey", "${"+configKey+"}").entry("envId", envId).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getProjectHasReferencedConfigs(int projectId) {
		return getSqlMapClientTemplate().queryForList("Config.getProjectHasReferencedConfigs", Maps.entry("projectId", projectId).get());
	}

    @Override
    public List<Config> findConfigByPrefix(String prefix) {
        return getSqlMapClientTemplate().queryForList("Config.findConfigsByPrefix", prefix+"%");
    }

    @Override
    public ConfigInstance findInstance(String key, int envId, String context) {
        return (ConfigInstance) getSqlMapClientTemplate().queryForObject("Config.findInstanceByKey", Maps.entry("key", key)
                .entry("envId", envId).entry("contextmd5", DigestUtils.md5Hex(context)).get());
    }

    @Override
    public List<ConfigInstance> findInstancesByKeys(List<String> keyList, int envId, String group) {
        if (keyList == null || keyList.isEmpty()) {
            return Collections.emptyList();
        }
        return getSqlMapClientTemplate().queryForList("Config.findInstancesByKeys", Maps.entry("keyList", keyList)
                .entry("envId", envId).entry("contextmd5", DigestUtils.md5Hex(group)).get());
    }

    @Override
    public List<ConfigInstance> findInstancesByPrefix(String prefix, int envId, String group) {
        return getSqlMapClientTemplate().queryForList("Config.findInstancesByPrefix", Maps.entry("prefix", prefix+"%")
                .entry("envId", envId).entry("contextmd5", DigestUtils.md5Hex(group)).get());
    }
    
    @Override
    public List<ConfigInstance> findInstancesByProject(int projectId, int envId, String group) {
        return getSqlMapClientTemplate().queryForList("Config.findInstancesByProject", Maps.entry("projectId", projectId)
                .entry("envId", envId).entry("contextmd5", DigestUtils.md5Hex(group)).get());
    }

}
