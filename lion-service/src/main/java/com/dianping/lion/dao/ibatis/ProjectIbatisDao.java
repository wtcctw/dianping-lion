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
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.ProjectMember;
import com.dianping.lion.entity.Team;
import com.dianping.lion.util.Maps;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class ProjectIbatisDao extends SqlMapClientDaoSupport implements ProjectDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeams() {
		return getSqlMapClientTemplate().queryForList("Project.getTeams");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjects() {
		return getSqlMapClientTemplate().queryForList("Project.getProjects");
	}

	@Override
	public Project getProject(int projectId) {
		return (Project) getSqlMapClientTemplate().queryForObject("Project.getProject", projectId);
	}

	@Override
	public Project findProject(String name) {
		return (Project) getSqlMapClientTemplate().queryForObject("Project.findProjectByName", name);
	}

	@Override
	public void lockProject(int projectId) {
		getSqlMapClientTemplate().update("Project.lockProject", projectId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Project> getProjectsByTeamAndProduct(Map param) {
		return getSqlMapClientTemplate().queryForList("Project.getProjectsByTeamAndProduct",param);
	}

	@Override
	public Integer insertProject(Project project) {
		return (Integer)getSqlMapClientTemplate().insert("Project.insertProject", project);
	}

	@Override
	public Integer updateProject(Project project) {
		return (Integer)getSqlMapClientTemplate().update("Project.updateProject", project);
	}

	@Override
	public Integer delProject(int projectId) {
		return (Integer)getSqlMapClientTemplate().delete("Project.delProject",projectId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsByProduct(int productId) {		
		List<Project> productProjects = getSqlMapClientTemplate().queryForList("Project.selectProjectsByProduct", productId);
		return productProjects;
	}

    @Override
    public boolean isMember(int projectId, int userId) {
        return  getSqlMapClientTemplate().queryForObject("Project.findMember", Maps.entry("projectId", projectId)
                .entry("userId", userId).get()) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMember> getMembers(int projectId) {
        return getSqlMapClientTemplate().queryForList("Project.findMemebers", projectId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMember> getOwners(int projectId) {
        return getSqlMapClientTemplate().queryForList("Project.findOwners", projectId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectMember> getOperators(int projectId) {
        return getSqlMapClientTemplate().queryForList("Project.findOperators", projectId);
    }

    @Override
    public void addOwner(int projectId, int userId) {
        getSqlMapClientTemplate().insert("Project.addOwner", Maps.entry("projectId", projectId).entry("userId", userId)
                .entry("createUserId", SecurityUtils.getCurrentUserId()).get());
    }

    @Override
    public void addMember(int projectId, int userId) {
        getSqlMapClientTemplate().insert("Project.addMember", Maps.entry("projectId", projectId).entry("userId", userId)
                .entry("createUserId", SecurityUtils.getCurrentUserId()).get());
    }

    @Override
    public void addOperator(int projectId, int userId) {
        getSqlMapClientTemplate().insert("Project.addOperator", Maps.entry("projectId", projectId).entry("userId", userId)
                .entry("createUserId", SecurityUtils.getCurrentUserId()).get());
    }

    @Override
    public void deleteOwner(int projectId, int userId) {
        getSqlMapClientTemplate().delete("Project.deleteOwner", Maps.entry("projectId", projectId).entry("userId", userId).get());
    }

    @Override
    public void deleteMember(int projectId, int userId) {
        getSqlMapClientTemplate().delete("Project.deleteMember", Maps.entry("projectId", projectId).entry("userId", userId).get());
    }

    @Override
    public void deleteOperator(int projectId, int userId) {
        getSqlMapClientTemplate().delete("Project.deleteOperator", Maps.entry("projectId", projectId).entry("userId", userId).get());
    }

	@Override
	 public Project getProjectExtra(int projectId) {
		  try {
			   return (Project) getSqlMapClientTemplate().queryForObject("Project.getProjectsExtra", projectId);
		  } catch (Exception e) {
			   e.printStackTrace();
		  }
		 return null;
	 }
}
