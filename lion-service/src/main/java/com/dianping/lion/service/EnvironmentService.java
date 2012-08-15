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
package com.dianping.lion.service;

import java.util.List;
import java.util.Map;

import com.dianping.lion.entity.Environment;

/**
 * @author danson.liu
 *
 */
public interface EnvironmentService {

	List<Environment> findAll();
	
	Map<Integer, Environment> findEnvMap();

	Environment findEnvByID(int id);
	
	/**
	 * 获取当前环境的上一个环境
	 * @param envId
	 * @return
	 */
	Environment findPrevEnv(int envId);
	
	Environment findEnvByName(String name);
	
	int create(Environment env);
	
	void update(Environment env);
	
	void delete(int id);
	
}
