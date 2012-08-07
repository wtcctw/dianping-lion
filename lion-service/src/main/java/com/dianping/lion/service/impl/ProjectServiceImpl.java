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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.ProjectStatus;
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

	@SuppressWarnings("rawtypes")
	@Override
	public List<Project> getProjectsByTeamAndProduct(Map param) {
		return projectDao.getProjectsByTeamAndProduct(param);
	}

	@Override
	public Integer addProject(Project project) {
		return projectDao.insertProject(project);
	}

	@Override
	public Integer editProject(Project project) {
		return projectDao.updateProject(project);
	}

	@Override
	public Integer delProject(int projectId) {
		return projectDao.delProject(projectId);
	}

	@Override
	public List<Project> getProjectsByProduct(int productId) {
		return projectDao.getProjectsByProduct(productId);
	}

	@Override
	public Project findProject(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Project name cannot be blank.");
		}
		return projectDao.findProject(name);
	}

	@Override
	public void changeEffectStatus(int projectId, int envId, boolean effected) {
		int updated = projectDao.updateEffectStat(projectId, envId, effected);
		if (updated == 0) {
			projectDao.createEffectStat(new ProjectStatus(projectId, envId, effected));
		}
	}

	@Override
	public boolean getEffectStatus(int projectId, int envId) {
		return projectDao.getEffectStatus(projectId, envId);
	}

}
