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

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProjectService;

/**
 * @author danson.liu
 *
 */
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;

	@Override
	public List<Team> getTeams() {
		return projectDao.getTeams();
	}

	@Override
	public List<Project> getProjects() {
		return this.projectDao.getProjects();
	}

	@Override
	public Project getProject(int projectId) {
		return projectDao.getProject(projectId);
	}

}
