/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-1
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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.SystemSettingDao;
import com.dianping.lion.service.SystemSettingService;

/**
 * @author danson.liu
 *
 */
public class SystemSettingServiceImpl implements SystemSettingService {
	
	@Autowired
	private SystemSettingDao systemSettingDao;
	
	private Ehcache ehcache;
	
	@Override
	public String getSetting(String key) {
		Element element = ehcache.get(ServiceConstants.CACHE_SETTING_PREFIX + key);
		if (element == null) {
			String settingVal = systemSettingDao.get(key);
			if (settingVal != null) {
				element = new Element(ServiceConstants.CACHE_SETTING_PREFIX + key, settingVal);
				ehcache.put(element);
			}
		}
		return element != null ? (String) element.getObjectValue() : null;
	}

	@Override
	public void update(String key, String value) {
		systemSettingDao.update(key, value);
		ehcache.remove(ServiceConstants.CACHE_SETTING_PREFIX + key);
	}

	@Override
	public boolean getBool(String key, boolean defaultIfNull) {
		String settingVal = getSetting(key);
		return settingVal != null ? Boolean.parseBoolean(settingVal): defaultIfNull;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

}
