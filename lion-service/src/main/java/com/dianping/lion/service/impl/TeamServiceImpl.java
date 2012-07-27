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

import com.dianping.lion.dao.TeamDao;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.TeamService;

public class TeamServiceImpl implements TeamService {
	
	@Autowired
	private TeamDao teamDao;

	@Override
	public List<Team> findAll() {
		return teamDao.findAll();
	}

	@Override
	public void delete(int id) {
		teamDao.delete(id);
	}

	@Override
	public Team findTeamByID(int id) {
		return teamDao.findTeamByID(id);
	}

	@Override
	public int save(Team team) {
		teamDao.save(team);
		return team.getId();
	}

	@Override
	public void update(Team team) {
		teamDao.update(team);
	}

}
