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

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.EnvironmentService;

/**
 * @author danson.liu
 *
 */
public class EnvironmentServiceImpl implements EnvironmentService {
	
	@Autowired
	private EnvironmentDao environmentDao;

	@Override
	public List<Environment> findAll() {
		return environmentDao.findAll();
	}

}
