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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.vo.ConfigInstanceVo;

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
	public List<ConfigInstanceVo> findInstanceVos(int projectId, int envId) {
		return configDao.findInstanceVos(projectId, envId);
	}

	@Override
	public void moveDown(int projectId, int configId) {
		Config config = configDao.getConfig(configId);
		if (config == null || config.isDeleted()) {
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
		if (config == null || config.isDeleted()) {
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

}
