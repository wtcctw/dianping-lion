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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.entity.ConfigStatusEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.util.DBUtils;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;

/**
 * @author danson.liu
 *
 */
public class ConfigServiceImpl implements ConfigService {
	
	@Autowired
	private ConfigDao configDao;
	
	@Autowired
	private ProjectDao projectDao;

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
				ConfigStatus status = configStatus.get(config.getId());
				if ((StringUtils.isEmpty(key) || config.getKey().contains(key)) && (criteria.getStatus() == -1 
						|| (status != null && status.getStatus() == criteria.getStatus()))) {
					configVos.add(new ConfigVo(config, status, hasInstanceConfigs.contains(config.getId()), 
							hasContextInstConfigs.contains(config.getId()), defaultInsts.get(config.getId())));
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
		int deleted = configDao.deleteInstance(configId, envId);
		configDao.deleteStatus(configId, envId);
		if (deleted > 0) {
			//TODO 从相应环境的zookeeper中移除该config值
			
		}
	}

	@Override
	public void delete(int configId) {
		int deleted = configDao.delete(configId);
		configDao.deleteInstance(configId, null);
		configDao.deleteStatus(configId, null);
		if (deleted > 0) {
			//TODO 从各个环境的zookeeper中移除该config值
			
		}
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
		changeConfigStatus(instance.getConfigId(), instance.getEnvId(), ConfigStatusEnum.Undeployed);
		int currentUserId = SecurityUtils.getCurrentUser().getId();
		instance.setCreateUserId(currentUserId);
		instance.setModifyUserId(currentUserId);
		try {
			return configDao.createInstance(instance);
		} catch (RuntimeException e) {
			if (DBUtils.isDuplicateKeyError(e)) {
				String errorMsg = StringUtils.isNotBlank(instance.getContext()) ? "该上下文[context]下配置值已存在!": "默认配置值已存在!";
				throw new RuntimeBusinessException(errorMsg);
			} else { throw e; }
		}
	}
	
	@Override
	public int updateInstance(ConfigInstance instance) {
		changeConfigStatus(instance.getConfigId(), instance.getEnvId(), ConfigStatusEnum.Undeployed);
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

}
