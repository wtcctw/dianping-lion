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
import com.dianping.lion.util.Maps;
import com.dianping.lion.vo.OperationLogCriteria;
import com.dianping.lion.vo.Paginater;

public class OperationLogIbatisDao extends SqlMapClientDaoSupport implements OperationLogDao {
    
    @Override
    public long getLogCount(OperationLogCriteria logCriteria, Paginater<OperationLog> paginater) {
        Long logCount = (Long) getSqlMapClientTemplate().queryForObject("OperationLog.getOpLogCount", Maps.entry("criteria", logCriteria)
                .entry("paginater", paginater).get());
        return logCount != null ? logCount : 0L;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OperationLog> getLogList(OperationLogCriteria logCriteria, Paginater<OperationLog> paginater) {
        return getSqlMapClientTemplate().queryForList("OperationLog.getOpLogList", Maps.entry("criteria", logCriteria)
                .entry("paginater", paginater).get());
    }

	@Override
	public void insertOpLog(OperationLog oplog) {
		getSqlMapClientTemplate().insert("OperationLog.insertOpLog", oplog);
	}

    @Override
    public String getLogKey(int logId, String key) {
        String selectClause = null;
        if ("key1".equals(key)) {
            selectClause = "OperationLog.getOpLogKey1";
        } else if ("key2".equals(key)) {
            selectClause = "OperationLog.getOpLogKey2";
        } else if ("key3".equals(key)) {
            selectClause = "OperationLog.getOpLogKey3";
        } else if ("key4".equals(key)) {
            selectClause = "OperationLog.getOpLogKey4";
        } else if ("key5".equals(key)) {
            selectClause = "OperationLog.getOpLogKey5";
        } else if ("key6".equals(key)) {
            selectClause = "OperationLog.getOpLogKey6";
        } else {
            return null;
        }
        return (String) getSqlMapClientTemplate().queryForObject(selectClause, Maps.entry("id", logId).get());
    }

}
