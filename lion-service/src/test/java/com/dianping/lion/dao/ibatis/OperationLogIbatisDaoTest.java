/**
 * File Created at 12-9-27
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

import com.dianping.lion.dao.OperationLogDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.support.AbstractDaoTestSupport;
import com.dianping.lion.vo.OperationLogCriteria;
import com.dianping.lion.vo.Paginater;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO Comment of The Class
 *
 * @author danson.liu
 */
public class OperationLogIbatisDaoTest extends AbstractDaoTestSupport {

    @Autowired
    private OperationLogDao operationLogDao;
    private static final int Project_Foo_Id = 100000000;
    private static final int Env1_Id = 100000000;
    private static final String Log1_Content = "log1_content";
    private static final String Log1_Key1 = "log1_key1";
    private static final String Log1_Key2 = "log1_key2";
    private static final String Log1_Key3 = "log1_key3";
    private static final String Log1_Key4 = "log1_key4";
    private static final String Log1_Key5 = "log1_key5";
    private static final String Log1_Key6 = "log1_key6";
    private static final String Log2_Content = "log2_content";
    private static final String Log2_Key1 = "log2_key1";
    private static final String Log2_Key2 = "log2_key2";
    private static final String Log2_Key3 = "log2_key3";
    private static final String Log2_Key4 = "log2_key4";
    private static final String Log2_Key5 = "log2_key5";
    private static final String Log2_Key6 = "log2_key6";
    private int log1Id;
    private int log2Id;

    @Before
    public void setUp() throws Exception {
        OperationLog operationLog = new OperationLog(OperationTypeEnum.Config_Add, Project_Foo_Id, Env1_Id,
                Log1_Content, Log1_Key1, Log1_Key2, Log1_Key3, Log1_Key4, Log1_Key5, Log1_Key6);
        log1Id = operationLogDao.insertOpLog(operationLog);
        operationLog = new OperationLog(OperationTypeEnum.Config_Add, Project_Foo_Id, Env1_Id,
                Log2_Content, Log2_Key1, Log2_Key2, Log2_Key3, Log2_Key4, Log2_Key5, Log2_Key6);
        log2Id = operationLogDao.insertOpLog(operationLog);
    }

    @Test
    public void testGetLogCount() throws Exception {
        OperationLogCriteria criteria = new OperationLogCriteria();
        criteria.setProjectId(Project_Foo_Id);
        criteria.setEnvId(Env1_Id);
        String opType = OperationTypeEnum.Config_All.getBegin() + "|" + OperationTypeEnum.Config_All.getEnd() + "|"
                + OperationTypeEnum.Config_All.isProjectRelated();
        criteria.setOpType(opType);
        Paginater<OperationLog> paginater = new Paginater<OperationLog>();
        long logCount = operationLogDao.getLogCount(criteria, paginater);
        assertEquals(2, logCount);
    }

    @Test
    public void testGetLogList() throws Exception {
        OperationLogCriteria criteria = new OperationLogCriteria();
        criteria.setProjectId(Project_Foo_Id);
        criteria.setEnvId(Env1_Id);
        String opType = OperationTypeEnum.Config_All.getBegin() + "|" + OperationTypeEnum.Config_All.getEnd() + "|"
                + OperationTypeEnum.Config_All.isProjectRelated();
        criteria.setOpType(opType);
        Paginater<OperationLog> paginater = new Paginater<OperationLog>();
        List<OperationLog> logList = operationLogDao.getLogList(criteria, paginater);
        assertEquals(2, logList.size());
    }

    @Test
    public void testGetLogKey() throws Exception {
        String logKey = operationLogDao.getLogKey(log1Id, "key1");
        assertEquals(Log1_Key1, logKey);
        logKey = operationLogDao.getLogKey(log1Id, "key6");
        assertEquals(Log1_Key6, logKey);
        logKey = operationLogDao.getLogKey(log2Id, "key1");
        assertEquals(Log2_Key1, logKey);
        logKey = operationLogDao.getLogKey(-1000, "key1");
        assertNull(logKey);
    }

}
