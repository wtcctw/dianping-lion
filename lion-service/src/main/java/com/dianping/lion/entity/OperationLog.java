/**
 * Project: lion-console
 * 
 * File Created at 2012-7-11
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
package com.dianping.lion.entity;

import java.util.Date;

/**
 * @author youngphy.yang
 * @author danson.liu
 */
public class OperationLog {

    private int id;
    private int opType;
    private OperationTypeEnum opTypeEnum;
    private Integer opUserId;
    private String opUserIp;
    private Date opTime;
    private Integer projectId;
    private Integer envId;
    private String content;
    
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;

    private String projectName;
    private String envName;
    private String opUserName;

    public OperationLog() {
    }
    
    public OperationLog(OperationTypeEnum opType, String content) {
        this(opType, null, content);
    }
    
    public OperationLog(OperationTypeEnum opType, Integer projectId, String content) {
        this(opType, projectId, (Integer) null, content);
    }
    
    public OperationLog(OperationTypeEnum opType, Integer projectId) {
        this(opType, projectId, (Integer) null, null);
    }
    
    public OperationLog(OperationTypeEnum opType, Integer projectId, Integer envId, String content) {
        this(opType, projectId, envId, content, null, null, null, null, null);
    }
    
    public OperationLog(OperationTypeEnum opType, Integer projectId, Integer envId) {
        this(opType, projectId, envId, null);
    }
    
    public OperationLog(OperationTypeEnum opType, Integer projectId, Integer envId, String content, String key1, String key2, String key3, 
        String key4, String key5) {
        
        this.setOpType(opType);
        this.projectId = projectId;
        this.envId = envId;
        this.content = content;
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
        this.key5 = key5;
    }
    
    public OperationLog key(String key1, String key2, String key3, String key4, String key5) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
        this.key5 = key5;
        return this;
    }
    
    public OperationLog key(String key1, String key2, String key3, String key4) {
        return key(key1, key2, key3, key4, null);
    }
    
    public OperationLog key(String key1, String key2, String key3) {
        return key(key1, key2, key3, null);
    }
    
    public OperationLog key(String key1, String key2) {
        return key(key1, key2, null);
    }
    
    public OperationLog key(String key1) {
        return key(key1, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
        this.opTypeEnum = OperationTypeEnum.fromValue(opType);
    }
    
    public void setOpType(OperationTypeEnum opTypeEnum) {
        this.opType = opTypeEnum.getValue();
        this.opTypeEnum = opTypeEnum;
    }

    public OperationTypeEnum getOpTypeEnum() {
        return opTypeEnum;
    }

    public Integer getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(Integer opUserId) {
        this.opUserId = opUserId;
    }

    public String getOpUserIp() {
        return opUserIp;
    }

    public void setOpUserIp(String opUserIp) {
        this.opUserIp = opUserIp;
    }

    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

    public String getKey5() {
        return key5;
    }

    public void setKey5(String key5) {
        this.key5 = key5;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getOpUserName() {
        return opUserName;
    }

    public void setOpUserName(String opUserName) {
        this.opUserName = opUserName;
    }
}
