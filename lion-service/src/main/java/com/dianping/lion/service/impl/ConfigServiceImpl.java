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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

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
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.ReferencedConfigForbidDeleteException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.register.ConfigRegisterService;
import com.dianping.lion.register.ConfigRegisterServiceRepository;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.ConfigDeleteResult;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.ConfigValueResolver;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.util.DBUtils;
import com.dianping.lion.util.EnumUtils;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;
import com.dianping.lion.vo.HasValueEnum;
import com.dianping.lion.vo.Paginater;

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

	@Autowired
	private OperationLogService operationLogService;

	private TransactionTemplate transactionTemplate;

	private ConfigValueResolver valueResolver = new DefaultConfigValueResolver(this);

	private CacheClient cacheClient;

	public ConfigServiceImpl(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
		this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Override
	public List<ConfigVo> findConfigVos(ConfigCriteria criteria) {
		int projectId = criteria.getProjectId();
		int envId = criteria.getEnvId();
		HasValueEnum hasValue = EnumUtils.fromEnumProperty(HasValueEnum.class, "value", criteria.getHasValue());
		List<Config> configs = configDao.findConfigByProject(projectId);
		List<ConfigVo> configVos = new ArrayList<ConfigVo>(configs.size());
		if (!configs.isEmpty()) {
			List<Integer> hasInstanceConfigs = configDao.findHasInstanceConfigs(projectId, envId);
			List<Integer> hasContextInstConfigs = configDao.findHasContextInstConfigs(projectId, envId);
			Map<Integer, ConfigInstance> defaultInsts = configDao.findDefaultInstances(projectId, envId);
			List<Integer> hasReferencedConfigs = isSharedProject(projectId) ? configDao.getProjectHasReferencedConfigs(projectId) : Collections.<Integer>emptyList();
			for (Config config : configs) {
				//配置不会太多，使用内存过滤，无分页，如果配置太多影响到性能则考虑数据库过滤和分页
				String key = StringUtils.trim(criteria.getKey());
				String value = StringUtils.trim(criteria.getValue());
				ConfigInstance defaultInst = defaultInsts.get(config.getId());
				if ((StringUtils.isEmpty(key) || config.getKey().contains(key))
						&& (hasValue == HasValueEnum.All || (hasValue == HasValueEnum.Yes && defaultInst != null)
								|| (hasValue == HasValueEnum.No && defaultInst == null))
						&& (StringUtils.isEmpty(value) || (defaultInst != null && defaultInst.getValue().contains(value)))) {
					configVos.add(new ConfigVo(config, hasInstanceConfigs.contains(config.getId()),
							hasContextInstConfigs.contains(config.getId()), hasReferencedConfigs.contains(config.getId()), defaultInst));
				}
			}
		}
		return configVos;
	}

	public boolean isSharedProject(int projectId) {
		return projectId == ServiceConstants.PROJECT_SHARED_ID || projectId == ServiceConstants.PROJECT_DB_ID;
	}

	@Override
	public void moveDown(int projectId, int configId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		projectDao.lockProject(projectId);	//锁定指定projectId，避免同一项目下的config出现相同的seq值
		Config nextConfig = configDao.getNextConfig(configId);
		if (nextConfig != null) {
		    try {
    			int seq = config.getSeq();
    			config.setSeq(nextConfig.getSeq());
    			nextConfig.setSeq(seq);
    			configDao.updateConfig(config);
    			configDao.updateConfig(nextConfig);
		    } finally {
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getId());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getKey());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + nextConfig.getId());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + nextConfig.getKey());
		    }
		}
	}

	@Override
	public void moveUp(int projectId, Integer configId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		projectDao.lockProject(projectId);
		Config prevConfig = configDao.getPrevConfig(configId);
		if (prevConfig != null) {
		    try {
    			int seq = config.getSeq();
    			config.setSeq(prevConfig.getSeq());
    			prevConfig.setSeq(seq);
    			configDao.updateConfig(config);
    			configDao.updateConfig(prevConfig);
		    } finally {
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getId());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getKey());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + prevConfig.getId());
		    	cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + prevConfig.getKey());
		    }
		}
	}

	@Override
	public void deleteInstances(int configId, int envId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("Config[id=" + configId + "] not found.");
		}
		deleteInstances(config, envId);
	}

	public void deleteInstances(Config config, int envId) {
		if (hasConfigReferencedTo(config.getKey(), envId)) {
			throw new ReferencedConfigForbidDeleteException(config.getKey());
		}
		configDao.deleteInstances(config.getId(), envId);
		configDao.deleteStatus(config.getId(), envId);
		getRegisterService(envId).unregister(config.getKey());
	}

    public void deleteInstance(int configId, int envId, String group) {
        Config config = getConfig(configId);
        configDao.deleteInstance(configId, envId, group);
        if(configDao.getConfigInstCount(configId, envId) == 0)
            configDao.deleteStatus(configId, envId);
        getRegisterService(envId).unregister(config.getKey(), group);
    }

	private boolean hasConfigReferencedTo(String configKey, int envId) {
		return configDao.hasConfigReferencedTo(configKey, envId);
	}

	public List<ConfigInstance> getInstanceReferencedTo(String configKey, int envId) {
		return configDao.getInstanceReferencedTo(configKey, envId);
	}

	@Override
	public ConfigDeleteResult deleteConfig(int configId) {
		final ConfigDeleteResult result = new ConfigDeleteResult();
		final Config config = getConfig(configId);
		if (config != null) {
		    result.setConfig(config);
			List<Environment> environments = environmentService.findAll();
			for (final Environment environment : environments) {
				try {
					this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
						@Override
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							deleteInstances(config, environment.getId());
						}
					});
				} catch (RuntimeException e) {
					logger.error("Delete config[" + config.getKey() + "] in environment[" + environment.getLabel()
							+ "] failed.", e);
					result.addFailedEnv(environment.getLabel());
					if (e instanceof ReferencedConfigForbidDeleteException) {
						result.setHasReference(true);
					}
				}
			}
			if (result.isSucceed()) {
				configDao.deleteConfig(configId);
				cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + configId);
				cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getKey());
			}
		}
		return result;
	}

	@Override
	public int createConfig(Config config) {
		Config configFound = configDao.findConfigByKey(config.getKey());
		if (configFound != null) {
			Project project = projectDao.getProject(configFound.getProjectId());
			throw new RuntimeBusinessException("该配置项已存在(project: " + (project != null ? project.getName() : "***") + ", desc: " + configFound.getDesc() + ")!");
		}
		Integer currentUserId = SecurityUtils.getCurrentUserId();
		if(currentUserId == null) {
		    throw new RuntimeBusinessException("Invalid user id " + currentUserId);
		}
		int projectId = config.getProjectId();
		if (config.getCreateUserId() == null) {
			config.setCreateUserId(currentUserId);
		}
		if (config.getModifyUserId() == null) {
			config.setModifyUserId(currentUserId);
		}
		projectDao.lockProject(projectId);
		if (!config.isPrivatee()) {
			config.setPrivatee(isSharedProject(projectId));
		}
		config.setSeq(configDao.getMaxConfigSeq(projectId) + 1);
		return configDao.createConfig(config);
	}

	@Override
	public int createInstance(ConfigInstance instance) {
		return createInstance(instance, ConfigSetType.RegisterAndPush);
	}

	public int createInstance(ConfigInstance instance, ConfigSetType setType) {
		Integer currentUserId = SecurityUtils.getCurrentUserId();
		if (instance.getCreateUserId() == null) {
			instance.setCreateUserId(currentUserId);
		}
		if (instance.getModifyUserId() == null) {
			instance.setModifyUserId(currentUserId);
		}
		processInstanceIfReferenceType(instance);
		int retryTimes = 0;
		while (true) {
			try {
				instance.setSeq(configDao.getMaxConfigInstSeq(instance.getConfigId(), instance.getEnvId()) + 1);
				int instId = configDao.createInstance(instance);
				updateConfigModifyStatus(instance.getConfigId(), instance.getEnvId());

                Config config = getConfig(instance.getConfigId());
                List<ConfigInstance> refInstances = getInstanceReferencedTo(config.getKey(), instance.getEnvId());

				//确保注册操作是在最后一步
				if (setType == ConfigSetType.RegisterAndPush) {
					registerAndPush(instance);
				} else if (setType == ConfigSetType.Register) {
				    register(instance);
				}

                updateReferencedInstances(config, refInstances, setType);

				return instId;
			} catch (RuntimeException e) {
				if (DBUtils.isDuplicateKeyError(e)) {
					if (retryTimes++ >= 1) {
						String errorMsg = StringUtils.isNotBlank(instance.getContext()) ? "该上下文["+instance.getContext()+"]下配置值已存在!": "默认配置值已存在!";
						throw new RuntimeBusinessException(errorMsg);
					}
				} else {
					throw e;
				}
			}
		}
	}

	private void processInstanceIfReferenceType(ConfigInstance instance) {
		String configval = instance.getValue();
		if (configval != null && configval.contains(ServiceConstants.REF_CONFIG_PREFIX)) {
			Matcher matcher = ServiceConstants.REF_EXPR_PATTERN.matcher(configval);
			if (matcher.find()) {
				String refkey = matcher.group(1);
				instance.setRefkey(refkey);
				return;
			}
		}
		instance.setRefkey(null);
	}

	@Override
	public int updateInstance(ConfigInstance instance) {
		return updateInstance(instance, ConfigSetType.RegisterAndPush);
	}

	private int updateInstance(ConfigInstance instance, ConfigSetType setType) {
		instance.setModifyUserId(SecurityUtils.getCurrentUserId());
		processInstanceIfReferenceType(instance);
		int instId = configDao.updateInstance(instance);
		updateConfigModifyStatus(instance.getConfigId(), instance.getEnvId());

		Config config = getConfig(instance.getConfigId());
		List<ConfigInstance> refInstances = getInstanceReferencedTo(config.getKey(), instance.getEnvId());

		//确保注册操作是在最后一步(后续的都需要try-catch掉所有error)
		if (setType == ConfigSetType.RegisterAndPush) {
			registerAndPush(instance);
		} else if (setType == ConfigSetType.Register) {
			register(instance);
		}

		updateReferencedInstances(config, refInstances, setType);
		return instId;
	}

	private void updateReferencedInstances(Config config, List<ConfigInstance> refInstances, ConfigSetType setType) {
		try {
			for (ConfigInstance refInstance : refInstances) {
				int envId = refInstance.getEnvId();
				try {
					if (setType == ConfigSetType.RegisterAndPush) {
						registerAndPush(refInstance.getConfigId(), envId);
					} else if (setType == ConfigSetType.Register) {
						register(refInstance.getConfigId(), envId);
					}
				} catch (RuntimeException e) {
					Config refconfig = getConfig(refInstance.getConfigId());
					operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Config_Edit, config.getProjectId(), envId,
							"同步更新关联引用配置项[" + (refconfig != null ? refconfig.getKey() : refInstance.getConfigId()) + "]出错，请重新保存该配置.")
						.key(config.getKey(), ConfigInstance.NO_CONTEXT));
				}
			}
		} catch (Exception e) {
			logger.error("Update config[" + config.getKey() + "]'s referenced configs failed.", e);
		}
	}

	@Override
	public void setConfigValue(int configId, int envId, String context, String value) {
		setConfigValue(configId, envId, context, value, ConfigSetType.RegisterAndPush);
	}

	public void setConfigValue(int configId, int envId, String context, String value, ConfigSetType setType) {
		ConfigInstance found = configDao.findInstance(configId, envId, context);
		if (found == null) {
			createInstance(new ConfigInstance(configId, envId, context, value), setType);
		} else {
			found.setValue(value);
			updateInstance(found, setType);
		}
	}

	@Override
	public void setConfigValue(int projectId, int envId, String key, String desc, String context, String value, ConfigSetType setType) {
		Config config = findConfigByKey(key);
		int configId = 0;
		if (config == null) {
			config = new Config();
			config.setKey(key);
			config.setDesc(desc);
			config.setTypeEnum(ConfigTypeEnum.String);
			config.setProjectId(projectId);
			configId = createConfig(config);
		} else {
			configId = config.getId();
		}
		setConfigValue(configId, envId, context, value, setType);
	}

	public void register(int configId, int envId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new RuntimeBusinessException("config[id=" + configId + "] not found, maybe deleted.");
		}
		ConfigInstance defaultInst = findDefaultInstance(configId, envId);
		if (defaultInst == null) {
			throw new RuntimeBusinessException("该环境下配置项不存在!");
		}
		try {
			ConfigRegisterService registerService = getRegisterService(envId);
			//register context value需要放在register default value前面，重要的操作在后面，确保zk与db数据一致，大部分配置无context value
			registerService.registerContextValue(config.getKey(), getConfigContextValue(configId, envId));
			registerService.registerDefaultValue(config.getKey(), resolveConfigFinalValue(defaultInst.getValue(), envId));
		} catch (RuntimeException e) {
			Environment environment = environmentService.findEnvByID(envId);
			logger.error("Register config[" + config.getKey() + "] to env[" + (environment != null ? environment.getLabel() : envId)
					+ "] failed.", e);
			throw e;
		}
	}

	public void registerAndPush(int configId, int envId) {
		Config config = getConfig(configId);
		if (config == null) {
			throw new EntityNotFoundException("config[id=" + configId + "] not found, maybe deleted.");
		}
		ConfigInstance defaultInst = findDefaultInstance(configId, envId);
		if (defaultInst == null) {
			throw new RuntimeBusinessException("该环境下配置项不存在!");
		}
		try {
			ConfigRegisterService registerService = getRegisterService(envId);
			//register context value需要放在register default value前面，重要的操作在后面，确保zk与db数据一致，大部分配置无context value
			//第一个注册操作就需要push，第二个不需要，确保第一个操作时客户端感知到变化时就已经感知到了push事务
			registerService.registerAndPushContextValue(config.getKey(), getConfigContextValue(configId, envId));
			registerService.registerDefaultValue(config.getKey(), resolveConfigFinalValue(defaultInst.getValue(), envId));
		} catch (RuntimeException e) {
			Environment environment = environmentService.findEnvByID(envId);
			logger.error("Register and push config[" + config.getKey() + "] to env[" + (environment != null ? environment.getLabel() : envId)
					+ "] failed.", e);
			throw e;
		}
	}

	private String resolveConfigFinalValue(String configval, int envId) {
		return valueResolver.resolve(configval, envId);
	}

	private void updateConfigModifyStatus(int configId, int envId) {
		int updated = configDao.updateModifyStatus(configId, envId);
		if (updated == 0) {
			ConfigStatus status = new ConfigStatus(configId, envId);
			Integer currentUserId = SecurityUtils.getCurrentUserId();
			status.setCreateUserId(currentUserId);
			status.setModifyUserId(currentUserId);
			configDao.createStatus(status);
		}
	}

	@Override
	public Config findConfigByKey(String key) {
	    Config config = cacheClient.get(ServiceConstants.CACHE_CONFIG_PREFIX + key);
        if (config == null) {
            config = configDao.findConfigByKey(key);
            if (config != null) {
            	cacheClient.set(ServiceConstants.CACHE_CONFIG_PREFIX + key, config);
            }
        }
        return config;
	}

	public List<Config> findConfigByPrefix(String prefix) {
	    return configDao.findConfigByPrefix(prefix);
	}
	
	@Override
	public List<Config> findConfigByKeys(List<String> keys) {
		return configDao.findConfigByKeys(keys);
	}
	
	@Override
	public List<ConfigInstance> findInstancesByKeys(List<String> keys, int envId, String group) {
	    return configDao.findInstancesByKeys(keys, envId, group);
	}

    @Override
    public List<ConfigInstance> findInstancesByPrefix(String prefix, int envId, String group) {
        return configDao.findInstancesByPrefix(prefix, envId, group);
    }
    
	@Override
	public Config getConfig(int configId) {
	    Config config = cacheClient.get(ServiceConstants.CACHE_CONFIG_PREFIX + configId);
	    if (config == null) {
            config = configDao.getConfig(configId);
            if (config != null) {
            	cacheClient.set(ServiceConstants.CACHE_CONFIG_PREFIX + configId, config);
            }
        }
        return config;
	}

	public int updateConfig(Config config) {
	    try {
    	    config.setModifyUserId(SecurityUtils.getCurrentUserId());
    	    return configDao.updateConfig(config);
	    } finally {
	        cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getId());
	        cacheClient.remove(ServiceConstants.CACHE_CONFIG_PREFIX + config.getKey());
	    }
	}

	@Override
	public String getConfigFromRegisterServer(int envId, String key) {
		return getRegisterService(envId).get(key);
	}

    @Override
    public String getConfigFromRegisterServer(int envId, String key,
            String group) {
        return getRegisterService(envId).get(key, group);
    }

	@Override
	public ConfigInstance findInstance(int configId, int envId, String context) {
		return configDao.findInstance(configId, envId, context);
	}
	
	@Override
	public ConfigInstance findInstance(String key, int envId, String context) {
	    return configDao.findInstance(key, envId, context);
	}

    @Override
    public String getConfigValue(int configId, int envId, String context) {
        ConfigInstance instance = findInstance(configId, envId, context);
        if (instance != null) {
            return resolveConfigFinalValue(instance.getValue(), envId);
        }
        return null;
    }

    @Override
	public ConfigInstance findDefaultInstance(int configId, int envId) {
		return findInstance(configId, envId, ConfigInstance.NO_CONTEXT);
	}

	@Override
	public List<Config> findConfigs(int projectId) {
		return configDao.findConfigByProject(projectId);
	}

	@Override
	public List<ConfigInstance> findInstances(int projectId, int envId) {
		return configDao.findInstancesByProjectAndEnv(projectId, envId);
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
				valueObj.put("value", resolveConfigFinalValue(topInst.getValue(), envId));
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

	@Override
	public Map<Integer, Date> findModifyTime(int projectId, int envId) {
		Map<Integer, Date> modifyTimes = new HashMap<Integer, Date>();
		List<ConfigStatus> statusList = configDao.findModifyTime(projectId, envId);
		for (ConfigStatus status : statusList) {
			modifyTimes.put(status.getConfigId(), status.getModifyTime());
		}
		return modifyTimes;
	}

	@Override
	public Paginater<Config> paginateConfigs(ConfigCriteria criteria, Paginater<Config> paginater) {
		long configCount = configDao.getConfigCount(criteria);
		List<Config> logList = configDao.getConfigList(criteria, paginater);
        paginater.setTotalCount(configCount);
        paginater.setResults(logList);
		return paginater;
	}

	public ConfigRegisterService getRegisterService(int envId) {
		return registerServiceRepository.getRequiredRegisterService(envId);
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

    public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
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

	public void setValueResolver(ConfigValueResolver valueCalculator) {
		this.valueResolver = valueCalculator;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}

    @Override
    public void register(ConfigInstance configInstance) {
        Config config = null;
        try {
            config = getConfig(configInstance.getConfigId());
            ConfigRegisterService registerService = getRegisterService(configInstance.getEnvId());
            registerService.registerGroupValue(config.getKey(), configInstance.getContext(), configInstance.getValue());
        } catch (RuntimeException e) {
            Environment environment = environmentService.findEnvByID(configInstance.getEnvId());
            logger.error("Register config[" + (config != null ? config.getKey() : configInstance.getConfigId()) + "] to env[" +
                    (environment != null ? environment.getLabel() : configInstance.getEnvId()) + "] failed.", e);
            throw e;
        }
    }

    @Override
    public void registerAndPush(ConfigInstance configInstance) {
        Config config = null;
        try {
            config = getConfig(configInstance.getConfigId());
            ConfigRegisterService registerService = getRegisterService(configInstance.getEnvId());
            registerService.registerAndPushGroupValue(config.getKey(), configInstance.getContext(), configInstance.getValue());
        } catch (RuntimeException e) {
            Environment environment = environmentService.findEnvByID(configInstance.getEnvId());
            logger.error("Register config[" + (config != null ? config.getKey() : configInstance.getConfigId()) + "] to env[" +
                    (environment != null ? environment.getLabel() : configInstance.getEnvId()) + "] failed.", e);
            throw e;
        }
    }

}
