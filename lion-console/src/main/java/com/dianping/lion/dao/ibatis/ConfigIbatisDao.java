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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.vo.ConfigInstanceVo;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDao extends SqlMapClientDaoSupport implements ConfigDao {

	@Override
	public List<ConfigInstanceVo> findInstanceVos(int projectId, int envId) {
		List<Config> configs = findConfigsByProject(projectId, false);
		List<ConfigInstanceVo> instanceVos = new ArrayList<ConfigInstanceVo>(configs.size());
		if (!configs.isEmpty()) {
			Map<Integer, ConfigInstance> instances = findInstanceMapByProjectAndEnv(projectId, envId, false);
			for (Config config : configs) {
				instanceVos.add(new ConfigInstanceVo(config, instances.get(config.getId())));
			}
		}
		return instanceVos;
	}
	
	@SuppressWarnings("unchecked")
	private List<Config> findConfigsByProject(int projectId, boolean includeDeleted) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectId", projectId);
		params.put("includeDeleted", includeDeleted);
		return getSqlMapClientTemplate().queryForList("Config.findConfigsByProject", params);
	}
	
	private Map<Integer, ConfigInstance> findInstanceMapByProjectAndEnv(int projectId, int envId, boolean includeDeleted) {
		List<ConfigInstance> instances = findInstancesByProjectAndEnv(projectId, envId, includeDeleted);
		Map<Integer, ConfigInstance> map = new HashMap<Integer, ConfigInstance>(instances.size());
		for (ConfigInstance instance : instances) {
			map.put(instance.getConfigId(), instance);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private List<ConfigInstance> findInstancesByProjectAndEnv(int projectId, int envId, boolean includeDeleted) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectId", projectId);
		params.put("envId", envId);
		params.put("includeDeleted", includeDeleted);
		return getSqlMapClientTemplate().queryForList("Config.findInstancesByProjectAndEnv", params);
	}

}
