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
package com.dianping.lion.service;

import java.util.List;
import java.util.Map;

import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;

/**
 * @author danson.liu
 *
 */
public interface ProjectService {

	/**
	 * 获取所有业务组信息
	 * @return
	 */
	List<Team> getTeams();
	
	/**
	 * 获取所有的项目信息；
	 * @return
	 */
	List<Project> getProjects();
	
	/**
	 * 获取符合条件的项目信息；
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Project> getProjectsByTeamAndProduct(Map param);
	
	/**
	 * @param projectId
	 * @return
	 */
	Project getProject(int projectId);
	
	Project findProject(String name);
	
	Integer addProject(Project project);
	
	Integer editProject(Project project);
	Integer delProject(int projectId);
	
	List<Project> getProjectsByProduct(int productId);
}
