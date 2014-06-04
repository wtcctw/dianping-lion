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

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.TeamDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.TeamService;

public class TeamServiceImpl implements TeamService {
	
	@Autowired
	private TeamDao teamDao;
	
	@Autowired
	private OperationLogService operationLogService;
	
	private CacheClient cacheClient;
	private CacheClient projectCacheClient;

	@Override
	public List<Team> findAll() {
		return teamDao.findAll();
	}

	@Override
	public Team findTeam(String name) {
	    return teamDao.findTeam(name);
	}
	
	@Override
	public void delete(int id) {
	    try {
    	    Team team = findTeamByID(id);
    		teamDao.delete(id);
    		if (team != null) {
    		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Team_Delete, "删除业务团队: " + team.getName()));
    		}
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public Team findTeamByID(int id) {
		return teamDao.findTeamByID(id);
	}

	@Override
	public int create(Team team) {
	    try {
    		teamDao.create(team);
    		operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Team_Add, "创建业务团队: " + team.getName()));
    		return team.getId();
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public void update(Team team) {
	    try {
    	    Team existTeam = findTeamByID(team.getId());
    		teamDao.update(team);
    		if (existTeam != null) {
    		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Team_Edit, "编辑业务团队, before[名称: " 
	            	+ existTeam.getName() + "], after[名称: " + team.getName() + "]"));
    		}
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        	projectCacheClient.removeAll();
	    }
	}

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * @param teamDao the teamDao to set
     */
    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

	public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}

	public void setProjectCacheClient(CacheClient projectCacheClient) {
		this.projectCacheClient = projectCacheClient;
	}

}
