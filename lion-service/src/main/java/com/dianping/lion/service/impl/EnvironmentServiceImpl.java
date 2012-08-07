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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.medium.ConfigRegisterServiceRepository;
import com.dianping.lion.service.EnvironmentService;

/**
 * @author danson.liu
 *
 */
public class EnvironmentServiceImpl implements EnvironmentService {
	
	@Autowired
	private EnvironmentDao environmentDao;
	
	@Autowired
	private ConfigRegisterServiceRepository mediumServiceRepository;

	@Override
	public List<Environment> findAll() {
		return environmentDao.findAll();
	}

	@Override
	public Map<Integer, Environment> findEnvMap() {
		List<Environment> envList = findAll();
		Map<Integer, Environment> envMap = new HashMap<Integer, Environment>(envList.size());
		for (Environment environment : envList) {
			envMap.put(environment.getId(), environment);
		}
		return envMap;
	}

	public void delete(int id) {
		environmentDao.delete(id);
		mediumServiceRepository.removeRegisterService(id);
	}

	@Override
	public int save(Environment env) {
		return environmentDao.save(env);
	}

	@Override
	public void update(Environment env) {
		Environment oldEnv = findEnvByID(env.getId());
		environmentDao.update(env);
		if (!StringUtils.equals(oldEnv.getIps(), env.getIps())) {
			mediumServiceRepository.removeRegisterService(env.getId());
		}
	}
	
	@Override
	public Environment findEnvByID(int id) {
		return environmentDao.findEnvByID(id);
	}

	@Override
	public Environment findEnvByName(String name) {
		return environmentDao.findEnvByName(name);
	}

	@Override
	public Environment findPrevEnv(int envId) {
		return environmentDao.findPrevEnv(envId);
	}

	public void setMediumServiceRepository(ConfigRegisterServiceRepository mediumServiceRepository) {
		this.mediumServiceRepository = mediumServiceRepository;
	}

}
