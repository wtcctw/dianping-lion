/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-10
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
package com.dianping.lion.web.action.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.JobExecTime;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.JobExecTimeService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class JobManagementAction extends AbstractLionAction{
	
	@Autowired
	private JobExecTimeService jobExecTimeService;
	
	//表格内容
	private List<JobExecTime> jobExecTimeList;
	
	private JobExecTime jobExecTime;
	
	private int jobId;
	private String jobName;
	private boolean switcher;
	private String failMail;
	
	@Override
	protected void checkModulePrivilege() {
		if (!privilegeService.isUserHasResourcePrivilege(SecurityUtils.getCurrentUserId(), ServiceConstants.RES_CODE_JOB)) {
			throw NoPrivilegeException.INSTANCE;
		}
	}
	
	public String getJobLists() {
		jobExecTimeList = jobExecTimeService.findAll();
		return SUCCESS;
	}
	
	public String addJob() {
		return SUCCESS;
	}
	
	public String addJobSubmit() {
		JobExecTime jobExecTime = new JobExecTime();
		jobExecTime.setFailMail(failMail);
		jobExecTime.setSwitcher(switcher);
		jobExecTime.setJobName(jobName);
		jobExecTimeService.addJob(jobExecTime);
		jobExecTimeList = jobExecTimeService.findAll();
		return SUCCESS;
	}
	
	public String editJob() {
		jobExecTime = jobExecTimeService.getJobById(jobId);
		return SUCCESS;
	}
	
	public String editJobSubmit() {
		JobExecTime jobExecTime = new JobExecTime();
		jobExecTime.setId(jobId);
		jobExecTime.setFailMail(failMail);
		jobExecTime.setSwitcher(switcher);
		jobExecTime.setJobName(jobName);
		jobExecTimeService.updateJob(jobExecTime);
		jobExecTimeList = jobExecTimeService.findAll();
		return SUCCESS;
	}
	
	public String deleteJobAjax() {
		jobExecTimeService.deleteJob(jobId);
		jobExecTimeList = jobExecTimeService.findAll();
		return SUCCESS;
	}

	public List<JobExecTime> getJobExecTimeList() {
		return jobExecTimeList;
	}

	public void setJobExecTimeList(List<JobExecTime> jobExecTimeList) {
		this.jobExecTimeList = jobExecTimeList;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public JobExecTime getJobExecTime() {
		return jobExecTime;
	}

	public void setJobExecTime(JobExecTime jobExecTime) {
		this.jobExecTime = jobExecTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public boolean isSwitcher() {
		return switcher;
	}

	public void setSwitcher(boolean switcher) {
		this.switcher = switcher;
	}

	public String getFailMail() {
		return failMail;
	}

	public void setFailMail(String failMail) {
		this.failMail = failMail;
	}
	
}
