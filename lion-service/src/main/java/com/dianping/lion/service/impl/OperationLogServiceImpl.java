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

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.OperationLogDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ProjectPrivilegeDecider;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.util.StringUtils;
import com.dianping.lion.vo.OperationLogCriteria;
import com.dianping.lion.vo.Paginater;

public class OperationLogServiceImpl implements OperationLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);
	
	@Autowired
	private OperationLogDao operationLogDao;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ProjectPrivilegeDecider configPrivilegeDecider;

    @Override
    public Paginater<OperationLog> getLogList(OperationLogCriteria logCriteria, Paginater<OperationLog> paginater) {
        long logCount = operationLogDao.getLogCount(logCriteria, paginater);
        List<OperationLog> logList = operationLogDao.getLogList(logCriteria, paginater);
        paginater.setTotalCount(logCount);
        paginater.setResults(refactorConfig(logList));
        return paginater;
    }

    private List<OperationLog> refactorConfig(List<OperationLog> logList) {
        User currentUser = SecurityUtils.getCurrentUser();
        for (OperationLog log : logList) {
            OperationTypeEnum opType = log.getOpTypeEnum();
            if (opType == OperationTypeEnum.Config_Edit || opType == OperationTypeEnum.API_SetConfig) {
                Config config = configService.findConfigByKey(log.getKey1());
                boolean hasLookPrivilege = false;
                if (config != null) {
                    hasLookPrivilege = configPrivilegeDecider.hasReadConfigPrivilege(log.getProjectId(), log.getEnvId(), config.getId(), 
                            currentUser != null ? currentUser.getId() : null);
                } else {
                    hasLookPrivilege = currentUser != null && (currentUser.isAdmin() || currentUser.isSA());
                }
                //TODO 这里进行性能优化, 一次获取
                String oldValue = (String) ObjectUtils.defaultIfNull(getLogKey(log.getId(), "key3"), "");
                if (opType == OperationTypeEnum.Config_Edit) {
                	String newValue = (String) ObjectUtils.defaultIfNull(getLogKey(log.getId(), "key4"), "");
	                String oldCutValue = StringUtils.cutString(oldValue, 80);
	                String newCutValue = StringUtils.cutString(newValue, 80);
	                log.setContent("设置配置项: " + log.getKey1() + ", before[" + (hasLookPrivilege ? oldCutValue : "***") 
	                        + "], after[" + (hasLookPrivilege ? newCutValue : "***") + "]");
	                if (hasLookPrivilege && (oldCutValue.length() != oldValue.length() || newCutValue.length() != newValue.length())) {
	                    log.setKey5("true");
	                }
                } else if (opType == OperationTypeEnum.API_SetConfig) {
                	if (!hasLookPrivilege) {
                		log.setContent("设置配置项: " + log.getKey1() + ", value: ***");
                		log.setKey1("false");
                	} else {
                		log.appendContent(", before: " + StringUtils.cutString(oldValue, 80));
                		log.setKey1("true");
                	}
                }
            }
        }
        return logList;
    }

    @Override
    public void createOpLog(OperationLog oplog) {
        try {
            if (oplog.getOpUserId() == null) {
                oplog.setOpUserId(SecurityUtils.getCurrentUserId());
            }
            operationLogDao.insertOpLog(oplog);
        } catch (Exception e) {
            logger.warn("create operation log failed.", e);
        }
    }

    @Override
    public String getLogKey(int logid, String key) {
        return operationLogDao.getLogKey(logid, key);
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public void setConfigPrivilegeDecider(ProjectPrivilegeDecider configPrivilegeDecider) {
        this.configPrivilegeDecider = configPrivilegeDecider;
    }
    
}
