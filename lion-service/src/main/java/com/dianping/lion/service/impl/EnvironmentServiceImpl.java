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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.register.ConfigRegisterService;
import com.dianping.lion.register.ConfigRegisterServiceRepository;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class EnvironmentServiceImpl implements EnvironmentService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EnvironmentDao environmentDao;
	
	@Autowired
	private ConfigRegisterServiceRepository registerServiceRepository;
	
	private Ehcache ehcache;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> findAll() {
		Element element = ehcache.get(ServiceConstants.CACHE_KEY_ENVLIST);
		if (element == null) {
			synchronized (this) {
				element = ehcache.get(ServiceConstants.CACHE_KEY_ENVLIST);
				if (element == null) {
					List<Environment> allEnvs = environmentDao.findAll();
					element = new Element(ServiceConstants.CACHE_KEY_ENVLIST, allEnvs, true, null, null);
					ehcache.put(element);
					refreshRegisterServiceRepository(allEnvs);
				}
			}
		}
		return (List<Environment>) element.getObjectValue();
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
		ehcache.remove(ServiceConstants.CACHE_KEY_ENVLIST);
	}

	@Override
	public int create(Environment env) {
		Integer currentUserId = SecurityUtils.getCurrentUserId();
		env.setCreateUserId(currentUserId);
		env.setModifyUserId(currentUserId);
		int envId = environmentDao.create(env);
		ehcache.remove(ServiceConstants.CACHE_KEY_ENVLIST);
		return envId;
	}

	@Override
	public void update(Environment env) {
		env.setModifyUserId(SecurityUtils.getCurrentUserId());
		environmentDao.update(env);
		ehcache.remove(ServiceConstants.CACHE_KEY_ENVLIST);
	}
	
	@Override
	public Environment findEnvByID(int id) {
		List<Environment> envList = findAll();
		for (Environment env : envList) {
			if (env.getId() == id) {
				return env;
			}
		}
		return null;
	}

    @Override
    public Environment loadEnvByID(int id) {
        Environment environment = findEnvByID(id);
        if (environment == null) {
            throw new EntityNotFoundException("Environment[id=" + id + "] not found.");
        }
        return environment;
    }

	@Override
	public Environment findEnvByName(String name) {
		List<Environment> envList = findAll();
		for (Environment env : envList) {
			if (StringUtils.equals(env.getName(), name)) {
				return env;
			}
		}
		return null;
	}

	@Override
	public Environment findPrevEnv(int envId) {
		Environment prevEnv = null;
		List<Environment> envList = findAll();
		for (Environment env : envList) {
			if (env.getId() == envId) {
				return prevEnv;
			}
			prevEnv = env;
		}
		return null;
	}

	/**
	 * @param allEnvs
	 */
	@SuppressWarnings("unchecked")
	private void refreshRegisterServiceRepository(List<Environment> allEnvs) {
		Set<Integer> allEnvIds = new HashSet<Integer>(allEnvs.size());
		for (Environment env : allEnvs) {
			allEnvIds.add(env.getId());
			ConfigRegisterService registerService = registerServiceRepository.getRegisterService(env.getId());
			if (registerService != null && !StringUtils.equals(registerService.getAddresses(), env.getIps())) {
				registerServiceRepository.removeRegisterService(env.getId());
			}
		}
		Set<Integer> registeredEnvironments = registerServiceRepository.getRegisteredEnvironments();
		Collection<Integer> needRemovedEnvIds = CollectionUtils.subtract(registeredEnvironments, allEnvIds);
		for (Integer needRemovedEnvId : needRemovedEnvIds) {
			registerServiceRepository.removeRegisterService(needRemovedEnvId);
		}
	}
	
	public void init() {
		final List<Environment> environments = findAll();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Environment environment : environments) {
					try {
						registerServiceRepository.getRequiredRegisterService(environment.getId());
					} catch (RuntimeException e) {
						logger.warn("Build config register service[env=" + environment.getLabel() 
								+ "] failed while environment initialize.", e);
					}
				}
			}
		}).start();
	}

	public void setRegisterServiceRepository(ConfigRegisterServiceRepository registerServiceRepository) {
		this.registerServiceRepository = registerServiceRepository;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

}
