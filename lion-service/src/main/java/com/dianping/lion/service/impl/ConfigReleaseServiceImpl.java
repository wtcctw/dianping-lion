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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.dao.ConfigReleaseDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.ConfigSnapshotSet;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigRelaseService;
import com.dianping.lion.service.ConfigRollbackResult;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class ConfigReleaseServiceImpl implements ConfigRelaseService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigReleaseServiceImpl.class);
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ConfigReleaseDao configReleaseDao;
	
	@Autowired
	private ConfigDao configDao;
	
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

	@SuppressWarnings("unchecked")
	@Override
	public ConfigRollbackResult rollbackSnapshotSet(ConfigSnapshotSet snapshotSet) {
		ConfigRollbackResult rollbackResult = new ConfigRollbackResult();
		final int projectId = snapshotSet.getProjectId();
		final int envId = snapshotSet.getEnvId();
		try {
			List<ConfigSnapshot> configSnapshots = configReleaseDao.findConfigSnapshots(snapshotSet.getId());
			List<ConfigInstanceSnapshot> configInstSnapshots = configReleaseDao.findConfigInstSnapshots(snapshotSet.getId());
			List<Config> currentConfigs = configService.findConfigs(projectId);
			List<ConfigInstance> currentConfigInsts = configService.findInstances(projectId, envId);
			Map<Integer, Date> modifyTimes = configService.findModifyTime(projectId, envId);
			
			Map<String, ConfigSnapshot> configSnapshotMap = new HashMap<String, ConfigSnapshot>(configSnapshots.size());
			for (ConfigSnapshot configSnapshot : configSnapshots) {
				configSnapshotMap.put(configSnapshot.getKey(), configSnapshot);
			}
			Map<Integer, List<ConfigInstanceSnapshot>> configInstSnapshotMap = new HashMap<Integer, List<ConfigInstanceSnapshot>>(
				configSnapshots.size()
			);
			for (ConfigInstanceSnapshot configInstSnapshot : configInstSnapshots) {
				List<ConfigInstanceSnapshot> instList = configInstSnapshotMap.get(configInstSnapshot.getConfigId());
				if (instList == null) {
					instList = new ArrayList<ConfigInstanceSnapshot>();
					configInstSnapshotMap.put(configInstSnapshot.getConfigId(), instList);
				}
				instList.add(configInstSnapshot);
			}
			Set<String> configSnapshotKeys = configSnapshotMap.keySet();
			
			final Map<String, Config> currentConfigMap = new HashMap<String, Config>(currentConfigs.size());
			for (Config currentConfig : currentConfigs) {
				currentConfigMap.put(currentConfig.getKey(), currentConfig);
			}
			Map<Integer, List<ConfigInstance>> currentConfigInstMap = new HashMap<Integer, List<ConfigInstance>>(currentConfigInsts.size());
			for (ConfigInstance currentConfigInst : currentConfigInsts) {
				List<ConfigInstance> instList = currentConfigInstMap.get(currentConfigInst.getConfigId());
				if (instList == null) {
					instList = new ArrayList<ConfigInstance>();
					currentConfigInstMap.put(currentConfigInst.getConfigId(), instList);
				}
				instList.add(currentConfigInst);
			}
			Set<String> currentConfigKeys = currentConfigMap.keySet();
			
			Set<String> notRemovedKeys = new HashSet<String>();
			//1. 清除当前存在，但未存在于配置镜像中的配置
			/* Notice: 
			 * 回滚时配置不删除，因为删除是即时反应到线上的系统的，并且回滚配置是在回滚程序之前执行，可能会造成线上系统因缺少配置
			 * 而导致异常
			 */
			Collection<String> configNeedRemoveKeys = CollectionUtils.subtract(currentConfigKeys, configSnapshotKeys);
			notRemovedKeys.addAll(configNeedRemoveKeys);
	//		for (final String configNeedRemoveKey : configNeedRemoveKeys) {
	//			this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
	//				@Override
	//				protected void doInTransactionWithoutResult(TransactionStatus status) {
	//					Config configNeedRemove = currentConfigMap.get(configNeedRemoveKey);
	//					configService.deleteInstance(configNeedRemove.getId(), envId);
	//				}
	//			});
	//		}
			
			//2. 恢复当前存在，且镜像中也存在，但被变更过的配置
			Collection<String> intersectionKeys = CollectionUtils.intersection(currentConfigKeys, configSnapshotKeys);
			for (String intersectionKey : intersectionKeys) {
				ConfigSnapshot configSnapshot = configSnapshotMap.get(intersectionKey);
				final Config currentConfig = currentConfigMap.get(intersectionKey);
				final List<ConfigInstanceSnapshot> snapshotInstList = configInstSnapshotMap.get(configSnapshot.getConfigId());
				final List<ConfigInstance> currentInstList = currentConfigInstMap.get(currentConfig.getId());
				boolean snapshotNoInst = snapshotInstList == null || snapshotInstList.isEmpty();
				boolean currentNoInst = currentInstList == null || currentInstList.isEmpty();
				if (snapshotNoInst == true && currentNoInst == false) {
					notRemovedKeys.add(intersectionKey);
				} else if (snapshotNoInst == false && currentNoInst == true) {
					//恢复该config的所有instance，并部署到register server上
					this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							restoreConfigInstSnapshots(currentConfig.getId(), envId, snapshotInstList);
						}
					});
				} else if (snapshotNoInst == false && currentNoInst == false) {
					Date modifyTime = modifyTimes.get(currentConfig.getId());
					Date recordModifyTime = configSnapshot.getValueModifyTime();
					if (modifyTime == null || recordModifyTime == null || !modifyTime.equals(recordModifyTime)) {
						boolean onlyOneSnapshotInst = snapshotInstList.size() == 1;
						boolean onlyOneCurrentInst = currentInstList.size() == 1;
						if (onlyOneSnapshotInst && onlyOneCurrentInst) {
							ConfigInstanceSnapshot snapshotInst = snapshotInstList.get(0);
							ConfigInstance currentInst = currentInstList.get(0);
							if (snapshotInst.getContext().equals(currentInst.getContext())
									&& snapshotInst.getValue().equals(currentInst.getValue())) {
								continue;
							}
						}
						this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
							@Override
							protected void doInTransactionWithoutResult(TransactionStatus status) {
								configDao.deleteInstance(currentConfig.getId(), envId);
								restoreConfigInstSnapshots(currentConfig.getId(), envId, snapshotInstList);
							}
						});
					}
				}
			}
			
			//3. 添加当前不存在，但镜像中存在的
			Collection<String> configNeedAddKeys = CollectionUtils.subtract(configSnapshotKeys, currentConfigKeys);
			for (final String configNeedAddKey : configNeedAddKeys) {
				final ConfigSnapshot configSnapshot = configSnapshotMap.get(configNeedAddKey);
				final List<ConfigInstanceSnapshot> snapshotInstList = configInstSnapshotMap.get(configSnapshot.getConfigId());
				if (snapshotInstList != null && !snapshotInstList.isEmpty()) {
					this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							int configId = configService.create(configSnapshot.toConfig());
							restoreConfigInstSnapshots(configId, envId, snapshotInstList);
						}
					});
				}
			}
			rollbackResult.setNotRemovedKeys(notRemovedKeys);
			return rollbackResult;
		} catch (RuntimeException e) {
			Project project = projectService.getProject(projectId);
			logger.error("rollback configs failed with[project=" + project.getName() + ", task=" 
					+ snapshotSet.getTask() + "].", e);
			throw e;
		}
	}
	
	/**
	 * @param envId
	 * @param snapshotInsts
	 * @param currentConfig
	 */
	private void restoreConfigInstSnapshots(int configId, int envId, List<ConfigInstanceSnapshot> snapshotInsts) {
		for (ConfigInstanceSnapshot snapshotInst : snapshotInsts) {
			ConfigInstance instance = new ConfigInstance(configId, envId, snapshotInst.getContext(), 
					snapshotInst.getValue());
			instance.setCreateUserId(snapshotInst.getCreateUserId());
			instance.setCreateTime(snapshotInst.getCreateTime());
			instance.setModifyUserId(snapshotInst.getModifyUserId());
			instance.setModifyTime(snapshotInst.getModifyTime());
			configService.createInstance(instance, ConfigSetType.SaveOnly);
		}
		configService.register(configId, envId);
	}

	/**
	 * @param configReleaseDao the configReleaseDao to set
	 */
	public void setConfigReleaseDao(ConfigReleaseDao configReleaseDao) {
		this.configReleaseDao = configReleaseDao;
	}

	/**
	 * @param configDao the configDao to set
	 */
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

}
