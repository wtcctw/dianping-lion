/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-17
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
package com.dianping.lion.dao.ibatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.support.AbstractDaoTestSupport;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDaoTest extends AbstractDaoTestSupport {

	@Autowired
	private ConfigDao configDao;
	
	@Test
	public void testGetMaxSeq() {
		int maxSeq = configDao.getMaxSeq(-1000);
		assertEquals(0, maxSeq);
	}
	
	@Test
	public void testCreate() {
		Config config = new Config();
		config.setKey("test.key");
		config.setDesc("desc");
		config.setCreateUserId(0);
		config.setModifyUserId(0);
		config.setType(ConfigTypeEnum.String.getValue());
		config.setProjectId(1);
		int configId = configDao.create(config);
		assertTrue(configId > 0);
	}
	
}
