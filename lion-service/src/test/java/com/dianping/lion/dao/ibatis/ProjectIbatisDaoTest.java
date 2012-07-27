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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;
import com.dianping.lion.support.AbstractDaoTestSupport;

/**
 * @author danson.liu
 *
 */
public class ProjectIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private ProjectDao projectDao;

	@Test
	public void testGetTeams() {
		//TODO 修改该test case
		List<Team> teams = projectDao.getTeams();
		Assert.assertEquals(4, teams.size());
		Team team = teams.get(0);
		Assert.assertEquals("主站", team.getName());
		List<Product> products = team.getProducts();
		Assert.assertEquals(4, products.size());
		Product product = products.get(0);
		Assert.assertEquals("商户", product.getName());
		List<Project> projects = product.getProjects();
		Assert.assertEquals(7, projects.size());
		Project project = projects.get(0);
		Assert.assertEquals("shop-web", project.getName());
		project = projects.get(1);
		Assert.assertEquals("shop-service", project.getName());
	}

}
