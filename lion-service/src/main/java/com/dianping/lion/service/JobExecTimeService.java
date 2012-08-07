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
	JobExecTime getJobExecTime(String name);
	void updateJobExecTime(JobExecTime jobExecTime);
	void commitTransaction();
	void endTransaction();
}
