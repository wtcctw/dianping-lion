/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-9
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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.support.AbstractDaoTestSupport;

public class EnvironmentIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private EnvironmentDao environmentDao;
//	static private int id;
	
/*	@Test
	public void testInsert() {
		int oriSize = environmentDao.findAll().size();
		Environment environment = new Environment();
		environment.setIps("192.168.7.45");
		environment.setLabel("其它");
		environment.setName("other");
		environment.setSeq(1);
		id = environmentDao.save(environment);
		int nowSize = environmentDao.findAll().size();
		assertTrue(nowSize > oriSize);
	}
	
	@Test
	public void testUpdate() {
		Environment environment = new Environment();
		environment.setIps("dev.lion.dp:2182");
		environment.setLabel("开发");
		environment.setName("dev");
		environment.setSeq(1);
		environment.setId(1);
		environmentDao.update(environment);
	}*/
	
	@Test
	public void testDelete() {
		environmentDao.delete(5);
	}
	
/*	@Test
	public void testFindAll() {
		List<Environment> envs = environmentDao.findAll();
		assertTrue(envs.size() > 0);
	}*/

}
