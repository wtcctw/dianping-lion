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
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.vo.ConfigInstanceVo;

/**
 * @author danson.liu
 *
 */
public class ConfigServiceImpl implements ConfigService {
	
	@Autowired
	private ConfigDao configDao;

	@Override
	public List<ConfigInstanceVo> findInstanceVos(int projectId, int envId) {
		return configDao.findInstanceVos(projectId, envId);
	}

}
