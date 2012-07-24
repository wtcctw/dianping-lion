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

import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ProductDao;
import com.dianping.lion.entity.Product;
import com.dianping.lion.support.AbstractDaoTestSupport;

public class ProductIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private ProductDao productDao;
	static private int id;
	
	@Test
	public void testInsert() {
		int oriSize = productDao.findAll().size();
		Product product = new Product();
		product.setName("teamx");
		product.setProductLeaderId(2);
		product.setTeamId(1);
		product.setCreateTime(new Date(System.currentTimeMillis()));
		product.setModifyTime(new Date(System.currentTimeMillis()));
		id = productDao.save(product);
		int nowSize = productDao.findAll().size();
		assertTrue(nowSize > oriSize);
	}
	
	@Test
	public void testUpdate() {
		Product product = new Product();
		product.setId(id);
		product.setName("teamx");
		product.setProductLeaderId(2);
		product.setTeamId(1);
		productDao.update(product);
	}
	
	@Test
	public void testDelete() {
		productDao.delete(id);
	}
	
/*	@Test
	public void testFindAll() {
		List<Environment> envs = environmentDao.findAll();
		assertTrue(envs.size() > 0);
	}*/

}
