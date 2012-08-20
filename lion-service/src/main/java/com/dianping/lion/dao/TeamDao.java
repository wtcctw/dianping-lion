package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.Team;

public interface TeamDao {
	List<Team> findAll();
	
	Team findTeamByID(int id);
	
	int create(Team team);
	
	void update(Team team);
	
	void delete(int id);
}
