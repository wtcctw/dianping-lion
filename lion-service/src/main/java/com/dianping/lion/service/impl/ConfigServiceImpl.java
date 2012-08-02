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
package com.dianping.lion.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.entity.ConfigStatusEnum;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.medium.ConfigRegisterService;
import com.dianping.lion.medium.ConfigRegisterServiceRepository;
import com.dianping.lion.service.ConfigDeleteResult;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.util.DBUtils;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;

/**
 * @author danson.liu
 *
 */
public class ConfigServiceImpl implements ConfigService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
	
	@Autowired
	private ConfigDao configDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private ConfigRegisterServiceRepository registerServiceRepository;
	
	private TransactionTemplate transactionTemplate;
	
	public ConfigServiceImpl(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Override
	public List<ConfigVo> findConfigVos(ConfigCriteria criteria) {
		int projectId = criteria.getProjectId();
		int envId = criteria.getEnvId();
		List<Config> configs = configDao.findConfigsByProject(projectId);
		List<ConfigVo> configVos = new ArrayList<ConfigVo>(configs.size());
		if (!configs.isEmpty()) {
			List<Integer> hasInstanceConfigs = configDao.findHasInstanceConfigs(projectId, envId);
			List<Integer> hasContextInstConfigs = configDao.findHasContextInstConfigs(projectId, envId);
			Map<Integer, ConfigInstance> defaultInsts = configDao.findDefaultInstances(projectId, envId);
			Map<Integer, ConfigStatus> configStatus = configDao.findConfigStatus(projectId, envId);
			for (Config config : configs) {
				//配置不会太多，使用内存过滤，无分页，如果配置太多影响到性能则考虑数据库过滤和分页
				String key = StringUtils.trim(criteria.getKey());
				String value = StringUtils.trim(criteria.getValue());
				ConfigStatus status = configStatus.get(config.getId());
				ConfigInstance defaultInst = defaultInsts.get(config.getId());
				if ((StringUtils.isEmpty(key) || config.getKey().contains(key)) 
						&& (criteria.getStatus() == -1 || (status != null && status.getStatus() == criteria.getStatus())
								|| (criteria.getStatus() == ConfigStatusEnum.Noset.getValue() && defaultInst == null))
						&& (StringUtils.isEmpty(value) || (defaultInst != null && defaultInst.getValue().contains(value)))) {
					configVos.add(new ConfigVo(config, status, hasInstanceConfigs.contains(config.getId()), 
							hasContextInstConfigs.contains(config.getId()), defaultInst));
				}
			}
		}
		return configVos;
	}

	@Override
	public void moveDown(int projectId, int configId) {
		Config config = configDao.getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		projectDao.lockProject(projectId);	//锁定指定projectId，避免同一项目下的config出现相同的seq值
		Config nextConfig = configDao.getNextConfig(configId);
		if (nextConfig != null) {
			int seq = config.getSeq();
			config.setSeq(nextConfig.getSeq());
			nextConfig.setSeq(seq);
			configDao.update(config);
			configDao.update(nextConfig);
		}
	}

	@Override
	public void moveUp(int projectId, Integer configId) {
		Config config = configDao.getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		projectDao.lockProject(projectId);
		Config prevConfig = configDao.getPrevConfig(configId);
		if (prevConfig != null) {
			int seq = config.getSeq();
			config.setSeq(prevConfig.getSeq());
			prevConfig.setSeq(seq);
			configDao.update(config);
			configDao.update(prevConfig);
		}
	}

	@Override
	public void clearInstance(int configId, int envId) {
		Config config = configDao.getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		deleteInstance(config, envId);
	}
	
	private void deleteInstance(Config config, int envId) {
		int configId = config.getId();
		int deleted = configDao.deleteInstance(configId, envId);
		configDao.deleteStatus(configId, envId);
		if (deleted > 0) {
			ConfigRegisterService registerService = getRegisterService(envId);
			registerService.unregister(config.getKey());
		}
	}

	@Override
	public ConfigDeleteResult delete(int configId) {
		final ConfigDeleteResult result = new ConfigDeleteResult();
		final Config config = configDao.getConfig(configId);
		if (config != null) {
			List<Environment> environments = environmentService.findAll();
			for (final Environment environment : environments) {
				this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try {
							deleteInstance(config, environment.getId());
						} catch (Exception e) {
							logger.error("Unregister config[" + config.getKey() + "] from environment[" 
									+ environment.getLabel() + "] failed.", e);
							result.addFailedEnv(environment);
							status.setRollbackOnly();
						}
					}
				});
			}
			if (result.isSucceed()) {
				configDao.delete(configId);
			}
		}
		return result;
	}

	@Override
	public int create(Config config) {
		Config configFound = configDao.findConfigByKey(config.getKey());
		if (configFound != null) {
			Project project = projectDao.getProject(configFound.getProjectId());
			throw new RuntimeBusinessException("该配置项已存在(project: " + (project != null ? project.getName() : "***") + ", desc: " + configFound.getDesc() + ")!");
		}
		int currentUserId = SecurityUtils.getCurrentUser().getId();
		int projectId = config.getProjectId();
		config.setCreateUserId(currentUserId);
		config.setModifyUserId(currentUserId);
		projectDao.lockProject(projectId);
		config.setSeq(configDao.getMaxSeq(projectId) + 1);
		return configDao.create(config);
	}

	@Override
	public int createInstance(ConfigInstance instance) {
		changeConfigStatus(instance.getConfigId(), instance.getEnvId(), ConfigStatusEnum.Ineffective);
		int currentUserId = SecurityUtils.getCurrentUser().getId();
		instance.setCreateUserId(currentUserId);
		instance.setModifyUserId(currentUserId);
		int retryTimes = 0;
		while (true) {
			try {
				instance.setSeq(configDao.getMaxInstSeq(instance.getConfigId(), instance.getEnvId()) + 1);
				return configDao.createInstance(instance);
			} catch (RuntimeException e) {
				if (DBUtils.isDuplicateKeyError(e)) {
					if (retryTimes++ >= 1) {
						String errorMsg = StringUtils.isNotBlank(instance.getContext()) ? "该上下文[context]下配置值已存在!": "默认配置值已存在!";
						throw new RuntimeBusinessException(errorMsg);
					}
				} else {
					throw e;
				}
			}
		}
	}
	
	@Override
	public int updateInstance(ConfigInstance instance) {
		changeConfigStatus(instance.getConfigId(), instance.getEnvId(), ConfigStatusEnum.Ineffective);
		return configDao.updateInstance(instance);
	}
	
	@Override
	public void setConfigValue(int configId, int envId, String context, String value) {
		ConfigInstance found = configDao.findInstance(configId, envId, context);
		if (found == null) {
			createInstance(new ConfigInstance(configId, envId, context, value));
		} else {
			found.setValue(value);
			updateInstance(found);
		}
	}

	@Override
	public void changeConfigStatus(int configId, int envId, ConfigStatusEnum status) {
		int updated = configDao.updateStatusStat(configId, envId, status);
		if (updated == 0) {
			configDao.createStatus(new ConfigStatus(configId, envId, status));
		}
	}

	@Override
	public Config findConfigByKey(String key) {
		return configDao.findConfigByKey(key);
	}

	@Override
	public Config getConfig(int configId) {
		return configDao.getConfig(configId);
	}

	@Override
	public ConfigInstance findInstance(int configId, int envId, String context) {
		return configDao.findInstance(configId, envId, context);
	}

	@Override
	public ConfigInstance findDefaultInstance(int configId, int envId) {
		return findInstance(configId, envId, ConfigInstance.NO_CONTEXT);
	}

	@Override
	public List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv) {
		Config config = getConfig(configId);
		if (config == null) {
			return Collections.emptyList();
		}
		return configDao.findInstancesByConfig(config.getId(), maxPerEnv);
	}

	@Override
	public void registerToMedium(int configId, int envId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config not found, maybe deleted.");
		}
		ConfigInstance defaultInst = findDefaultInstance(configId, envId);
		if (defaultInst == null) {
			throw new RuntimeBusinessException("该环境下配置项不存在!");
		}
		try {
			changeConfigStatus(configId, envId, ConfigStatusEnum.Effective);
			ConfigRegisterService registerService = getRegisterService(envId);
			registerService.registerDefaultValue(config.getKey(), defaultInst.getValue());
			registerService.registerContextValue(config.getKey(), getConfigContextValue(configId, envId));
		} catch (RuntimeException e) {
			logger.error("Register config[" + config.getKey() + "] failed.", e);
			throw e;
		}
	}

	@Override
	public void registerAndPushToMedium(int configId, int envId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config not found, maybe deleted.");
		}
		ConfigInstance defaultInst = findDefaultInstance(configId, envId);
		if (defaultInst == null) {
			throw new RuntimeBusinessException("该环境下配置项不存在!");
		}
		try {
			changeConfigStatus(configId, envId, ConfigStatusEnum.Effective);
			ConfigRegisterService registerService = getRegisterService(envId);
			registerService.registerAndPushDefaultValue(config.getKey(), defaultInst.getValue());
			registerService.registerContextValue(config.getKey(), getConfigContextValue(configId, envId));
		} catch (RuntimeException e) {
			logger.error("Register and push config[" + config.getKey() + "] failed.", e);
			throw e;
		}
	}

	@Override
	public String getConfigContextValue(int configId, int envId) {
		try {
			List<ConfigInstance> topInsts = configDao.findInstancesByConfig(configId, envId, ServiceConstants.MAX_AVAIL_CONFIG_INST);
			if (!topInsts.isEmpty()) {
				Iterator<ConfigInstance> iterator = topInsts.iterator();
				while (iterator.hasNext()) {
					ConfigInstance inst = iterator.next();
					if (inst.isDefault()) {
						iterator.remove();
						break;
					}
				}
			}
			JSONArray valueArray = new JSONArray();
			for (ConfigInstance topInst : topInsts) {
				JSONObject valueObj = new JSONObject();
				valueObj.put("value", topInst.getValue());
				valueObj.put("context", topInst.getContext());
				valueArray.put(valueObj);
			}
			return valueArray.toString();
		} catch (JSONException e) {
			Config config = getConfig(configId);
			throw new RuntimeBusinessException("Cannot create config[key=" + config.getKey() + "]'s register value, " 
					+ "plz check its instances' value.", e);
		}
	}

	public ConfigRegisterService getRegisterService(int envId) {
		return registerServiceRepository.getRegisterService(envId);
	}

	/**
	 * @param configDao the configDao to set
	 */
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	/**
	 * @param projectDao the projectDao to set
	 */
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	/**
	 * @param registerServiceRepository the mediumServiceRepository to set
	 */
	public void setRegisterServiceRepository(ConfigRegisterServiceRepository registerServiceRepository) {
		this.registerServiceRepository = registerServiceRepository;
	}

	/**
	 * @param environmentService the environmentService to set
	 */
	public void setEnvironmentService(EnvironmentService environmentService) {
		this.environmentService = environmentService;
	}

	@Override
	public Config getConfigByName(String configName) {
		return configDao.getConfigByName(configName);
	}

}
