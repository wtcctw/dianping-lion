/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-9-3
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
package com.dianping.lion.web.action.system;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class CacheManageAction extends AbstractLionAction implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private Map<String, CacheClient> caches;
	
	private String cache;
	
	@Override
	protected void checkModulePrivilege() {
		if (!privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_CACHE, SecurityUtils.getCurrentUserId())) {
			throw NoPrivilegeException.INSTANCE;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		caches = applicationContext.getBeansOfType(CacheClient.class);
		return SUCCESS;
	}
	
	public String clearCache() {
		CacheClient cacheClient = (CacheClient) applicationContext.getBean(cache);
		if (cacheClient != null) {
			cacheClient.removeAll();
		}
		createSuccessStreamResponse();
		return SUCCESS;
	}
	
	public Map<String, CacheClient> getCaches() {
		return caches;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
		this.applicationContext = applicationContext;
	}

}
