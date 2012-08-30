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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.JobExecTimeDao;
import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.service.JobExecTimeService;
import com.dianping.lion.service.spi.Callback;

/**
 * SyncJob 保证HA多实例中，只有一个实例运行
 * @author youngphy.yang
 *
 */
public abstract class SyncJob {
	private static final Logger logger = LoggerFactory.getLogger(SyncJob.class);
	@Autowired
	protected JobExecTimeService jobExecTimeService;
	
	@Autowired
	protected JobExecTimeDao jobExecTimeDao;
	
	protected String jobName;
	
	private double jobDownTime;
	
	/**
	 * 先抢坑位，后做事务。
	 * @throws Exception
	 */
	public void work() throws Exception {
		if(jobExecTimeDao.tryUpdateLastJobExecTime(jobName, jobDownTime) > 0) {
			jobExecTimeService.execTransaction(new Callback(){
				@Override
				public void execute() throws Exception {
					JobExecTime jobExecTime = jobExecTimeDao.getJobExecTime(jobName);
					if(jobExecTime.isSwitcher()) {
						Calendar can = Calendar.getInstance();
						can.setTime(jobExecTime.getLastJobExecTime());
						doBusiness();
						jobExecTime.setLastJobExecTime(new Date(System.currentTimeMillis()));
					}
				}});
		} else {
			logger.info("Job discarded as the task been done by the same job at this time.");
		}
	}
	
	abstract protected void  doBusiness() throws Exception;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public double getJobDownTime() {
		return jobDownTime;
	}

	public void setJobDownTime(double jobDownTime) {
		this.jobDownTime = jobDownTime;
	}
	
}
