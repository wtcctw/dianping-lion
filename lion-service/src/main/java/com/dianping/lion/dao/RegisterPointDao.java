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
package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.RegisterPoint;

/**
 * TODO Comment of RegisterPointDao
 * @author danson.liu
 *
 */
public interface RegisterPointDao {

	/**
	 * @param registerPoint
	 */
	int create(RegisterPoint registerPoint);

	/**
	 * @param configSnapshot
	 */
	int create(ConfigSnapshot configSnapshot);

	/**
	 * @param configInstSnapshots
	 */
	void create(List<ConfigInstanceSnapshot> configInstSnapshots);

}
