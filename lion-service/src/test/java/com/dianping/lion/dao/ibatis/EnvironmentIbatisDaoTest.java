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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.support.AbstractDaoTestSupport;

public class EnvironmentIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private EnvironmentDao environmentDao;
	static private int id;
	
	@Test
	/**
	 * process: select(not exist)->create->select->update->select->delete->select
	 */
	public void testAllDBOperations() {
//		insert();
		update();
		delete();
	}
	
/*	public void insert() {
		Environment environment = new Environment();
		environment.setIps("192.168.7.45");
		environment.setLabel("其它");
		environment.setName("other");
		environment.setSeq(1);
		id = environmentDao.create(environment);
		Environment envQueried = environmentDao.findEnvByID(id);
		assertTrue("other".equals(envQueried.getName()));
	}*/
	
	private void update() {
		Environment environment = new Environment();
		environment.setIps("dev.lion.dp:2182");
		environment.setLabel("开发");
		environment.setName("dev");
		environment.setSeq(1);
		environment.setId(id);
		environmentDao.update(environment);
		Environment envQueried = environmentDao.findEnvByName("dev");
		assertNotNull(envQueried);
		assertEquals("dev.lion.dp:2182", envQueried.getIps());
	}
	
	private void delete() {
		environmentDao.delete(id);
		List<Environment> envsQueried = environmentDao.findAll();
		assertEquals(0, envsQueried.size());
	}

}
