/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-10
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.dianping.lion.dao.ConfigReleaseDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.ConfigSnapshotSet;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigRelaseService;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.util.SecurityUtils;

/**
 * TODO Comment of ConfigReleaseServiceImpl
 * @author danson.liu
 *
 */
public class ConfigReleaseServiceImpl implements ConfigRelaseService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigReleaseServiceImpl.class);
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ConfigReleaseDao configReleaseDao;
	
	private TransactionTemplate transactionTemplate;
	
	private ConfigReleaseServiceImpl(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Override
	public int createSetTask(ConfigSetTask task) {
		return configReleaseDao.createSetTask(task);
	}

	@Override
	public void executeSetTask(int projectId, int envId, String[] features, String[] keys, final boolean push2App) {
		List<ConfigSetTask> configSetTasks = configReleaseDao.getSetTask(projectId, envId, features, keys);
		for (final ConfigSetTask configSetTask : configSetTasks) {
			try {
				this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						if (configSetTask.isDelete()) {
							Config config = configService.findConfigByKey(configSetTask.getKey());
							if (config != null) {
								//后续如果有需求再实现删除配置功能(基于context的删除 or 删除全部instance?)
							}
						} else {
							configService.setConfigValue(configSetTask.getProjectId(), configSetTask.getEnvId(), configSetTask.getKey(), "", 
									configSetTask.getContext(), configSetTask.getValue(), push2App ? ConfigSetType.RegisterAndPush : ConfigSetType.Register);
						}
					}
				});
			} catch (RuntimeException e) {
				logger.error("Failed to execute configSet tasks while set config[" + configSetTask.getKey() + "], interrupt this execution.", e);
				throw new RuntimeBusinessException("Error happend when set config[" + configSetTask.getKey() + "], retry or contact lion " 
						+ "administrator.");
			}
		}
	}

	@Override
	public int createSnapshotSet(int projectId, int envId, String task) {
		ConfigSnapshotSet snapshotSet = new ConfigSnapshotSet(projectId, envId, task);
		snapshotSet.setCreateUserId(SecurityUtils.getCurrentUserId());
		int setSnapshotId = configReleaseDao.createConfigSnapshotSet(snapshotSet);
		List<Config> configs = configService.findConfigs(projectId);
		if (!configs.isEmpty()) {
			List<ConfigSnapshot> configSnapshots = new ArrayList<ConfigSnapshot>(configs.size());
			Map<Integer, Date> modifyTimes = configService.findModifyTime(projectId, envId);
			for (Config config : configs) {
				ConfigSnapshot configSnapshot = new ConfigSnapshot(config);
				configSnapshot.setSnapshotSetId(setSnapshotId);
				configSnapshot.setValueModifyTime(modifyTimes.get(config.getId()));
				configSnapshots.add(configSnapshot);
			}
			configReleaseDao.createConfigSnapshots(configSnapshots);
			List<ConfigInstance> instances = configService.findInstances(projectId, envId);
			if (!instances.isEmpty()) {
				List<ConfigInstanceSnapshot> configInstSnapshots = new ArrayList<ConfigInstanceSnapshot>();
				for (ConfigInstance instance : instances) {
					ConfigInstanceSnapshot instSnapshot = new ConfigInstanceSnapshot(instance);
					instSnapshot.setSnapshotSetId(setSnapshotId);
					configInstSnapshots.add(instSnapshot);
				}
				configReleaseDao.createConfigInstSnapshots(configInstSnapshots);
			}
		}
		return setSnapshotId;
	}

	@Override
	public ConfigSnapshotSet findFirstSnapshotSet(int projectId, int envId, String task) {
		return configReleaseDao.findFirstSnapshotSet(projectId, envId, task);
	}

	@Override
	public void rollbackSnapshotSet(ConfigSnapshotSet snapshotSet) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param configReleaseDao the configReleaseDao to set
	 */
	public void setConfigReleaseDao(ConfigReleaseDao configReleaseDao) {
		this.configReleaseDao = configReleaseDao;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

}
