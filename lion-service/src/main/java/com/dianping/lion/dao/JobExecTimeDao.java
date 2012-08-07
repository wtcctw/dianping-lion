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

import java.sql.SQLException;

import com.dianping.lion.entity.JobExecTime;

public interface JobExecTimeDao {
	void startTransaction()throws SQLException;
	JobExecTime getJobExecTime(String name);
	void updateLastJobExecTime(JobExecTime jobExecTime);
	void updateLastFetchTime(JobExecTime jobExecTime);
	void commitTransaction()throws SQLException;
	void endTransaction() throws SQLException;
}
