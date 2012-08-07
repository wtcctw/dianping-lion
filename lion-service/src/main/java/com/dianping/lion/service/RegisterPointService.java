/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-4
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

import com.dianping.lion.entity.Config;

/**
 * TODO Comment of RegisterPointService
 * @author danson.liu
 *
 */
public interface RegisterPointService {

	/**
	 * @param projectId
	 * @param envId
	 * @param auto 
	 * @param configs	<Config, isDelete>配置和是否是删除的Map信息
	 */
	int create(int projectId, int envId, boolean manual, Map<Config, Boolean> configs);
	
	int create(int projectId, int envId, boolean manual, List<Config> configs);

}
