/**
 * Project: com.dianping.lion.lion-api-0.0.1-new
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
package com.dianping.lion.job;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.JobExecTimeDao;
import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.service.JobExecTimeService;
import com.dianping.lion.service.spi.Callback;

/**
 * SyncJob
 * @author youngphy.yang
 *
 */
public abstract class SyncJob {
	@Autowired
	protected JobExecTimeService jobExecTimeService;
	
	@Autowired
	protected JobExecTimeDao jobExecTimeDao;
	
	protected String jobName;
	
	private long jobDownTime;
	
	public void work() throws Exception {
		jobExecTimeService.execTransaction(new Callback(){
			@Override
			public void execute() throws Exception {
				JobExecTime jobExecTime = jobExecTimeDao.getJobExecTime(jobName);
				Calendar can = Calendar.getInstance();
				can.setTime(jobExecTime.getLastJobExecTime());
				if(System.currentTimeMillis() - can.getTimeInMillis() > jobDownTime) {
					doBusiness();
					jobExecTime.setLastJobExecTime(new Date(System.currentTimeMillis()));
					jobExecTimeDao.updateLastJobExecTime(jobExecTime);
				}
			}});
	}
	
	abstract protected void  doBusiness() throws Exception;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getJobDownTime() {
		return jobDownTime;
	}

	public void setJobDownTime(long jobDownTime) {
		this.jobDownTime = jobDownTime;
	}
	
}
