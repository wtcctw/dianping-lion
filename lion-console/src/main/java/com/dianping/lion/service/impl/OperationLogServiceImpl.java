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
package com.dianping.lion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.OperationLogDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationLogSearch;
import com.dianping.lion.service.OperationLogService;

public class OperationLogServiceImpl implements OperationLogService {
	
	@Autowired
	private OperationLogDao operationLogDao;

	@Override
	public List<OperationLog> getLogs() {
		return operationLogDao.getLogs();
	}

	@Override
	public List<OperationLog> getLogList(OperationLogSearch operationLogSearch) {
		return operationLogDao.getLogList(operationLogSearch);
	}
}
