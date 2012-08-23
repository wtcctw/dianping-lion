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
package com.dianping.lion.dao.ibatis;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.OperationLogDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationLogSearch;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.support.AbstractDaoTestSupport;

public class OpeationLogIbatisDaoTest extends AbstractDaoTestSupport {
	
	@Autowired
	private OperationLogDao operationLogDao;

	@Test
	@Ignore
	public void testGetLogs() {
		List<OperationLog> opeationLogs = operationLogDao.getLogs();
		Assert.assertNotNull(opeationLogs);
	}
	
	@Test
	@Ignore
	public void testGetLogList() {
		OperationLogSearch operationLogSearch = new OperationLogSearch();
		List<OperationLog> opeationLogs = operationLogDao.getLogList(operationLogSearch);
		Assert.assertNotNull(opeationLogs);

		operationLogSearch.setContent("%xxxxttt%");
		operationLogSearch.setProject(234235);
		opeationLogs = operationLogDao.getLogList(operationLogSearch);
		Assert.assertTrue(opeationLogs.size() == 0);
		
		operationLogSearch.setContent("");
		operationLogSearch.setProject(-1);
		operationLogSearch.setFrom("2012-07-16");
		operationLogSearch.setTo("2012-07-18");
		opeationLogs = operationLogDao.getLogList(operationLogSearch);
		Assert.assertTrue(opeationLogs.size() != 0);
	}
	
	@Test
	public void testInsertOpLog() {
		List<OperationLog> preAllOpLogs = operationLogDao.getLogs();
		int preSize = preAllOpLogs==null?0:preAllOpLogs.size();
		OperationLog opLog = new OperationLog();
		opLog.setContent("Junit test insert oplog");
		opLog.setEnvId(2);
		opLog.setOpUserId(1);
		opLog.setOpUserIp("192.168.7.34");
		opLog.setOpTime(new Date(System.currentTimeMillis()));
		opLog.setProjectId(1);
		opLog.setOpType(OperationTypeEnum.ROLLBACK.ordinal());
		operationLogDao.insertOpLog(opLog);
		List<OperationLog> postAllOpLogs = operationLogDao.getLogs();
		int postSize = postAllOpLogs==null?0:postAllOpLogs.size();
		Assert.assertTrue(postSize > preSize);
	}
	

}
