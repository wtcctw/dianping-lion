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
import com.dianping.lion.entity.Team;

/**
 * @author danson.liu
 *
 */
public interface ProjectDao {

	List<Team> getTeams();
	
	List<Project> getProjects();
	
	Project getProject(int projectId);
	
	/**
	 * 该接口如果系统运行后发现有问题，则更换方案
	 * @param projectId
	 */
	void lockProject(int projectId);
	
	@SuppressWarnings("rawtypes")
	List<Project> getProjectsByTeamAndProduct(Map param);
	
}
