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

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.OperationLogDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationLogSearch;

public class OperationLogIbatisDao extends SqlMapClientDaoSupport implements OperationLogDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationLog> getLogs() {
		return getSqlMapClientTemplate().queryForList("OperationLog.getOpLogs");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperationLog> getLogList(OperationLogSearch operationLogSearch) {
		return getSqlMapClientTemplate().queryForList("OperationLog.getOpLogList",operationLogSearch);
	}

	@Override
	public void insertOpLog(OperationLog opLog) {
		getSqlMapClientTemplate().insert("OperationLog.insertOpLog", opLog);
	}

}
