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
package com.dianping.lion.service.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProjectService;

/**
 * @author danson.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath*:config/spring/appcontext-*.xml"	
})
public class ProjectServiceImplTest {
	
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testGetTeams() {
		//TODO 修改该test case，先构建数据再恢复现场
//		List<Team> teams = projectService.getTeams();
//		Assert.assertEquals(4, teams.size());
	}

}
