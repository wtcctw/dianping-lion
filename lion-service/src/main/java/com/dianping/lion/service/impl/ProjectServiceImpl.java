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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.service.ProjectService;

/**
 * @author danson.liu
 *
 */
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
	private Ehcache ehcache;
	private Ehcache projectEhcache;

	@SuppressWarnings("unchecked")
    @Override
	public List<Team> getTeams() {
	    Element element = ehcache.get(ServiceConstants.CACHE_KEY_TEAMS);
	    if (element == null) {
	        List<Team> teams = projectDao.getTeams();
	        element = new Element(ServiceConstants.CACHE_KEY_TEAMS, teams);
	        ehcache.put(element);
	    }
        return (List<Team>) element.getObjectValue();
	}

	@SuppressWarnings("unchecked")
    @Override
	public List<Project> getProjects() {
	    Element element = ehcache.get(ServiceConstants.CACHE_KEY_PROJECTS);
	    if (element == null) {
	        List<Project> projects = this.projectDao.getProjects();
	        element = new Element(ServiceConstants.CACHE_KEY_PROJECTS, projects);
	        ehcache.put(element);
	    }
		return (List<Project>) element.getObjectValue();
	}
	
	@Override
	public Project getProject(int projectId) {
	    Element element = projectEhcache.get(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
	    if (element == null) {
	        Project project = projectDao.getProject(projectId);
	        if (project != null) {
	            element = new Element(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId, project);
	            projectEhcache.put(element);
	        }
	    }
	    return element != null ? (Project) element.getObjectValue(): null;
	}

	@Override
	public Project loadProject(int projectId) {
	    Project project = getProject(projectId);
	    if (project == null) {
            throw new EntityNotFoundException("Project[id=" + projectId + "] not found.");
        }
		return project;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Project> getProjectsByTeamAndProduct(Map param) {
		return projectDao.getProjectsByTeamAndProduct(param);
	}

	@Override
	public Integer addProject(Project project) {
		Integer projectId = projectDao.insertProject(project);
		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
        return projectId;
	}

	@Override
	public Integer editProject(Project project) {
		Integer updated = projectDao.updateProject(project);
		projectEhcache.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + project.getId());
		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
        return updated;
	}

	@Override
	public Integer delProject(int projectId) {
		Integer deleted = projectDao.delProject(projectId);
		projectEhcache.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
        return deleted;
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
    public boolean isMember(int projectId, int userId) {
        return projectDao.isMember(projectId, userId);
    }

    /**
     * @param ehcache the ehcache to set
     */
    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    /**
     * @param projectEhcache the projectEhcache to set
     */
    public void setProjectEhcache(Ehcache projectEhcache) {
        this.projectEhcache = projectEhcache;
    }

}
