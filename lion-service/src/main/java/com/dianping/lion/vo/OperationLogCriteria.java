/**
 * Project: com.dianping.lion.lion-console-0.0.1-new
 * 
 * File Created at 2012-7-13
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.vo;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * OperationLogSearch
 * 
 * @author youngphy.yang
 * 
 */
public class OperationLogCriteria {
    private String opType;
    private Integer opTypeStart;
    private Integer opTypeEnd;
    private boolean projectRelated;
    private Integer projectId;
    private Integer envId;
    private String userLoginName;
    private Integer userId;
    private String content;
    private Date from;
    private Date to;
    
    public OperationLogCriteria() {
        this.from = DateUtils.addDays(Calendar.getInstance().getTime(), -7);
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer project) {
        this.projectId = project;
    }

    public Integer getOpTypeStart() {
        return opTypeStart;
    }

    public void setOpTypeStart(Integer opTypeStart) {
        this.opTypeStart = opTypeStart;
    }

    public Integer getOpTypeEnd() {
        return opTypeEnd;
    }

    public void setOpTypeEnd(Integer opTypeEnd) {
        this.opTypeEnd = opTypeEnd;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer env) {
        this.envId = env;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpType() {
        return opType;
    }

    public boolean isProjectRelated() {
        return projectRelated;
    }

    public void setProjectRelated(boolean projectRelated) {
        this.projectRelated = projectRelated;
    }

    public void setOpType(String opType) {
        this.opType = opType;
        String[] tokens = StringUtils.splitPreserveAllTokens(opType, '|');
        if (StringUtils.isNotBlank(tokens[0])) {
            opTypeStart = Integer.parseInt(tokens[0]);
        }
        if (StringUtils.isNotBlank(tokens[1])) {
            opTypeEnd = Integer.parseInt(tokens[1]);
        }
        projectRelated = Boolean.parseBoolean(tokens[2]);
    }

}
