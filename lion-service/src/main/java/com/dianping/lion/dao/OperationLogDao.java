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
package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.vo.OperationLogCriteria;
import com.dianping.lion.vo.Paginater;

public interface OperationLogDao {

	int insertOpLog(OperationLog oplog);
	
    long getLogCount(OperationLogCriteria logCriteria, Paginater<OperationLog> paginater);

    List<OperationLog> getLogList(OperationLogCriteria logCriteria, Paginater<OperationLog> paginater);
    
    /**
     * 获取指定oplog的指定key("key1", "key2"...)的内容
     * @param logId
     * @param key
     * @return
     */
    String getLogKey(int logId, String key);
    
}
