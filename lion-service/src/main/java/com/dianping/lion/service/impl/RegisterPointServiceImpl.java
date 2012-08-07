/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-4
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
package com.dianping.lion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.dao.RegisterPointDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.RegisterPoint;
import com.dianping.lion.service.RegisterPointService;
import com.dianping.lion.util.SecurityUtils;

/**
 * TODO Comment of RegisterPointServiceImpl
 * @author danson.liu
 *
 */
public class RegisterPointServiceImpl implements RegisterPointService {
	
	@Autowired
	private RegisterPointDao registerPointDao;
	
	@Autowired
	private ConfigDao configDao;

	@Override
	public int create(int projectId, int envId, boolean manual, Map<Config, Boolean> configs) {
		RegisterPoint registerPoint = new RegisterPoint(projectId, envId);
		registerPoint.setExecutorId(SecurityUtils.getCurrentUser().getId());
		registerPoint.setManual(manual);
		int pointId = registerPointDao.create(registerPoint);
		for (Entry<Config, Boolean> config : configs.entrySet()) {
			createConfigSnapshot(config.getKey(), config.getValue(), envId, pointId);
		}
		return pointId;
	}
	
	public int create(int projectId, int envId, boolean manual, List<Config> configs) {
		Map<Config, Boolean> configMap = new HashMap<Config, Boolean>(configs.size());
		for (Config config : configs) {
			configMap.put(config, false);
		}
		return create(projectId, envId, manual, configMap);
	}
	
	private void createConfigSnapshot(Config config, boolean isDelete, int envId, int registerPointId) {
		ConfigSnapshot configSnapshot = new ConfigSnapshot(config);
		configSnapshot.setRegistPointId(registerPointId);
		configSnapshot.setDelete(isDelete);
		int configSnapshotId = registerPointDao.create(configSnapshot);
		List<ConfigInstance> instances = configDao.findInstancesByConfig(config.getId(), envId, ServiceConstants.MAX_AVAIL_CONFIG_INST);
		if (!instances.isEmpty()) {
			List<ConfigInstanceSnapshot> configInstSnapshots = new ArrayList<ConfigInstanceSnapshot>();
			for (ConfigInstance instance : instances) {
				ConfigInstanceSnapshot instSnapshot = new ConfigInstanceSnapshot(instance);
				instSnapshot.setConfigId(configSnapshotId);
				configInstSnapshots.add(instSnapshot);
			}
			registerPointDao.create(configInstSnapshots);
		}
	}

	/**
	 * @param registerPointDao the registerPointDao to set
	 */
	public void setRegisterPointDao(RegisterPointDao registerPointDao) {
		this.registerPointDao = registerPointDao;
	}

	/**
	 * @param configDao the configDao to set
	 */
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

}
