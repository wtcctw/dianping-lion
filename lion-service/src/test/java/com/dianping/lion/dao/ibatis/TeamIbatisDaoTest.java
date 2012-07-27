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

import com.dianping.lion.dao.TeamDao;
import com.dianping.lion.entity.Team;
import com.dianping.lion.support.AbstractDaoTestSupport;

public class TeamIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private TeamDao teamDao;
	static private int id;
	
	@Test
	public void testInsert() {
		int oriSize = teamDao.findAll().size();
		Team team = new Team();
		team.setName("teamx");
		team.setCreateTime(new Date(System.currentTimeMillis()));
		team.setModifyTime(new Date(System.currentTimeMillis()));
		id = teamDao.save(team);
		int nowSize = teamDao.findAll().size();
		assertTrue(nowSize > oriSize);
	}
	
	@Test
	public void testUpdate() {
		Team team = new Team();
		team.setName("teamx");
		team.setCreateTime(new Date(System.currentTimeMillis()));
		team.setModifyTime(new Date(System.currentTimeMillis()));
		teamDao.update(team);
	}
	
	@Test
	public void testDelete() {
		teamDao.delete(id);
	}
	
/*	@Test
	public void testFindAll() {
		List<Environment> envs = environmentDao.findAll();
		assertTrue(envs.size() > 0);
	}*/

}
