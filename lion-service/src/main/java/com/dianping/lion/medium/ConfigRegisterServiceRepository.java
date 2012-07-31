/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-27
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
package com.dianping.lion.medium;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.NoAvailableRegisterServiceException;
import com.dianping.lion.service.EnvironmentService;

/**
 * @author danson.liu
 *
 */
public class ConfigRegisterServiceRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigRegisterServiceRepository.class);
	
	private static Map<Integer, ConfigRegisterService> registerServices = new ConcurrentHashMap<Integer, ConfigRegisterService>();
	
	@Autowired
	private ConfigRegisterServiceFactory registerServiceFactory;
	
	@Autowired
	private EnvironmentService environmentService;

	public ConfigRegisterService getRegisterService(int envId) {
		ConfigRegisterService registerService = registerServices.get(envId);
		if (registerService != null) {
			return registerService;
		}
		Environment environment = environmentService.findEnvByID(envId);
		if (environment == null) {
			throw new EntityNotFoundException("Environment with id[" + envId + "] not found.");
		}
		try {
			registerService = registerServiceFactory.createRegisterService(environment);
			if (registerService != null) {
				setRegisterService(envId, registerService);
				return registerService;
			}
		} catch (RuntimeException e) {
			logger.error("Create config register service failed.", e);
		}
		return new NoAvailableRegisterService(environment.getLabel());
	}
	
	public void setRegisterService(int envId, ConfigRegisterService registerService) {
		ConfigRegisterService oldRegisterService = registerServices.put(envId, registerService);
		if (oldRegisterService != null) {
			destroyRegisterService(envId, oldRegisterService);
		}
	}
	
	public void removeRegisterService(int envId) {
		ConfigRegisterService registerService = registerServices.remove(envId);
		if (registerService != null) {
			destroyRegisterService(envId, registerService);
		}
	}

	private void destroyRegisterService(int envId, ConfigRegisterService registerService) {
		try {
			registerService.destroy();
		} catch (RuntimeException e) {
			Environment environment = environmentService.findEnvByID(envId);
			logger.warn("Destroy environment[" + (environment != null ? environment.getLabel() : envId) + "]'s old" 
					+ "registerService[" + registerService + "] failed.", e);
		}
	}

	public void setEnvironmentService(EnvironmentService environmentService) {
		this.environmentService = environmentService;
	}

	public void setRegisterServiceFactory(ConfigRegisterServiceFactory registerServiceFactory) {
		this.registerServiceFactory = registerServiceFactory;
	}
	
}

class NoAvailableRegisterService implements ConfigRegisterService {
	
	private final String environment;

	public NoAvailableRegisterService(String environment) {
		this.environment = environment;
	}

	@Override
	public void registerContextValue(String key, String value) {
		throw registerServiceNotFoundError();
	}

	@Override
	public void registerAndPushContextValue(String key, String value) {
		throw registerServiceNotFoundError();
	}

	@Override
	public void registerDefaultValue(String key, String defaultVal) {
		throw registerServiceNotFoundError();
	}

	@Override
	public void registerAndPushDefaultValue(String key, String defaultVal) {
		throw registerServiceNotFoundError();
	}

	@Override
	public void unregister(String key) {
		throw registerServiceNotFoundError();
	}

	@Override
	public String get(String key) {
		throw registerServiceNotFoundError();
	}
	
	@Override
	public void destroy() {
	}

	private NoAvailableRegisterServiceException registerServiceNotFoundError() {
		return new NoAvailableRegisterServiceException(environment, "环境[" + environment + "]没有可用的配置注册系统, 请检查环境设置或注册系统状态.");
	}
	
}
