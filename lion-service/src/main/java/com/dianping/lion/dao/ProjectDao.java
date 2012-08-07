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
package com.dianping.lion.dao;

import java.util.List;
import java.util.Map;

import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.ProjectStatus;
import com.dianping.lion.entity.Team;

/**
 * @author danson.liu
 *
 */
public interface ProjectDao {

	List<Team> getTeams();
	
	List<Project> getProjects();
	
	Project getProject(int projectId);
	Project findProject(String name);
	
	/**
	 * 该接口如果系统运行后发现有问题，则更换方案
	 * @param projectId
	 */
	void lockProject(int projectId);
	
	@SuppressWarnings("rawtypes")
	List<Project> getProjectsByTeamAndProduct(Map param);
	
	Integer insertProject(Project project);
	Integer updateProject(Project project);
	Integer delProject(int projectId);
	
	List<Project> getProjectsByProduct(int productId);

	/**
	 * @param projectId
	 * @param envId
	 * @param effected
	 * @return
	 */
	int updateEffectStat(int projectId, int envId, boolean effected);

	/**
	 * @param projectStatus
	 */
	int createEffectStat(ProjectStatus projectStatus);

	/**
	 * @param projectId
	 * @param envId
	 * @return
	 */
	boolean getEffectStatus(int projectId, int envId);

}
