/**
 * Project: lion-service
 * 
 * File Created at 2012-8-3
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

import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.service.spi.Callback;

/**
 * JobExecTimeService
 * @author youngphy.yang
 *
 */
public interface JobExecTimeService {
	void startTransaction();
	void execTransaction(Callback callback);
	List<JobExecTime> findAll();
	JobExecTime getJobExecTime(String name);
	JobExecTime getJobById(int jobId);
	void updateLastJobExecTime(JobExecTime jobExecTime);
	void updateLastFetchTime(JobExecTime jobExecTime);
	void addJob(JobExecTime jobExecTime);
	void updateJob(JobExecTime jobExecTime);
	void deleteJob(int id);
	void commitTransaction();
	void endTransaction();
}
