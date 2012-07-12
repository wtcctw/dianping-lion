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

import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class OperationLogAction extends AbstractLionAction {
	
	@Autowired
	private OperationLogService operationLogService;
	
	private List<OperationLog> operationLogs;

/*	public String execute() {
		operationLogs = operationLogService.getLogs();
		return SUCCESS;
	}*/
	
	public String getOpLogs() {
		operationLogs = operationLogService.getLogs();
		return SUCCESS;
	}

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}

	public List<OperationLog> getOperationLogs() {
		return operationLogs;
	}
	
}
