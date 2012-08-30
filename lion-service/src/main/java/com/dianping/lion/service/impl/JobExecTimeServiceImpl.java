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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.JobExecTimeDao;
import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.service.JobExecTimeService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.spi.Callback;

public class JobExecTimeServiceImpl implements JobExecTimeService {
	
	@Autowired
	private JobExecTimeDao jobExecTimeDao;
	
	@Autowired
	private OperationLogService operationLogService;

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

	@Override
	public void updateLastFetchTime(JobExecTime jobExecTime) {
		jobExecTimeDao.updateLastFetchTime(jobExecTime);
	}

	@Override
	public void updateLastJobExecTime(JobExecTime jobExecTime) {
		jobExecTimeDao.updateLastJobExecTime(jobExecTime);
	}

	@Override
	public List<JobExecTime> findAll() {
		return jobExecTimeDao.findAll();
	}

	@Override
	public void addJob(JobExecTime jobExecTime){
		jobExecTimeDao.addJob(jobExecTime);
		operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Job_Add, "创建Job: " + jobExecTime.getJobName() 
		        + ", 状态: " + jobExecTime.isSwitcher() + ", mail: " + jobExecTime.getFailMail()));
	}

	@Override
	public void deleteJob(int id){
	    JobExecTime job = getJobById(id);
		jobExecTimeDao.deleteJob(id);
		if (job != null) {
		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Job_Delete, "删除Job: " + job.getJobName()));
		}
	}

	@Override
	public void updateJob(JobExecTime jobExecTime) {
	    JobExecTime existJob = getJobById(jobExecTime.getId());
		jobExecTimeDao.updateJob(jobExecTime);
		if (existJob != null) {
		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Job_Edit, "编辑Job: " + jobExecTime.getJobName()
		            + ", before[状态: " + existJob.isSwitcher() + ", mail: " + existJob.getFailMail() 
		            + "], after[状态: " + jobExecTime.isSwitcher() + ", mail: " + jobExecTime.getFailMail() + "]"));
		}
	}

	@Override
	public JobExecTime getJobById(int jobId) {
		return jobExecTimeDao.getJobById(jobId);
	}
	
    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

}
