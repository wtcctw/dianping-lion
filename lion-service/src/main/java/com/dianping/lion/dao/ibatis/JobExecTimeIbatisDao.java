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

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.JobExecTimeDao;
import com.dianping.lion.entity.JobExecTime;

public class JobExecTimeIbatisDao extends SqlMapClientDaoSupport implements
		JobExecTimeDao {

	@Override
	public void startTransaction() throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startTransaction();
		boolean autoCommit = getSqlMapClientTemplate().getSqlMapClient().getCurrentConnection().getAutoCommit();
		if (autoCommit) { 
			getSqlMapClientTemplate().getSqlMapClient().getCurrentConnection().setAutoCommit(false);
		}
	}

	@Override
	public JobExecTime getJobExecTime(String name) {
		  Object obj = getSqlMapClientTemplate().queryForObject("JobExecTime.findTime", name);
		  return (JobExecTime) obj;
	}
	
	@Override
	public JobExecTime getJobById(int jobId) {
		  Object obj = getSqlMapClientTemplate().queryForObject("JobExecTime.findJobByID", jobId);
		  return (JobExecTime) obj;
	}

	@Override
	public void updateLastJobExecTime(JobExecTime jobExecTime) {
		getSqlMapClientTemplate().update("JobExecTime.updateLastJobExecTime", jobExecTime);
	}

	@Override
	public void updateLastFetchTime(JobExecTime jobExecTime) {
		getSqlMapClientTemplate().update("JobExecTime.updateLastFetchTime", jobExecTime);
	}

	@Override
	public void commitTransaction() throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().commitTransaction();
//		session.commitTransaction();
	}

	@Override
	public void endTransaction() throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().endTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobExecTime> findAll() {
		return getSqlMapClientTemplate().queryForList("JobExecTime.findAll");
	}

	@Override
	public void addJob(JobExecTime jobExecTime){
		getSqlMapClientTemplate().insert("JobExecTime.insertJob", jobExecTime);
	}

	@Override
	public void deleteJob(int id) {
		getSqlMapClientTemplate().delete("JobExecTime.deleteJob", id);
	}

	@Override
	public void updateJob(JobExecTime jobExecTime) {
		getSqlMapClientTemplate().update("JobExecTime.updateJob", jobExecTime);
	}
}
