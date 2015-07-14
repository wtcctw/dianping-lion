package com.dianping.lion.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.TeamDao;
import com.dianping.lion.entity.Team;

public class TeamIbatisDao extends SqlMapClientDaoSupport implements TeamDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findAll() {
		return getSqlMapClientTemplate().queryForList("Team.findAll");
	}
	
	@Override
	public Team findTeam(String name) {
	    return (Team)getSqlMapClientTemplate().queryForObject("Team.find", name);
	}
	
	@Override
	public void delete(int id) {
		getSqlMapClientTemplate().delete("Team.deleteTeam", id);
	}

	@Override
	public Team findTeamByID(int id) {
		return (Team)getSqlMapClientTemplate().queryForObject("Team.findByID", id);
	}

	@Override
	public int create(Team team) {
		getSqlMapClientTemplate().insert("Team.insertTeam", team);
		return team.getId();
	}

	@Override
	public void update(Team team) {
		getSqlMapClientTemplate().update("Team.updateTeam", team);
	}
}
