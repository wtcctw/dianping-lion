/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
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

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.JobExecTimeDao;
import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.service.JobExecTimeService;
import com.dianping.lion.service.spi.Callback;

public class JobExecTimeServiceImpl implements JobExecTimeService {
	
	@Autowired
	private JobExecTimeDao jobExecTimeDao;

	@Override
	public void startTransaction() {
		try {
			jobExecTimeDao.startTransaction();
		} catch (SQLException e) {
			//TODO
		}
	}
	@Override
	public JobExecTime getJobExecTime(String name) {
		return jobExecTimeDao.getJobExecTime(name);
	}

	@Override
	public void updateJobExecTime(JobExecTime jobExecTime) {
		jobExecTimeDao.updateJobExecTime(jobExecTime);
	}

	@Override
	public void commitTransaction() {
		try {
			jobExecTimeDao.commitTransaction();
		} catch (SQLException e) {
		}
	}

	@Override
	public void endTransaction() {
		try {
			jobExecTimeDao.endTransaction();
		} catch (SQLException e) {
		}
	}

	@Override
	public void execTransaction(Callback callback) {
		try {
			callback.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
