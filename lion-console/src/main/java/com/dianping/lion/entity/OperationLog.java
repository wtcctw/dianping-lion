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
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.entity;

import java.util.Date;
import com.dianping.lion.util.OperationTypeEnum;

/**
 * @author youngphy.yang
 */
public class OperationLog {
	
	private int id;
	private int opType;
	private int opUserId;
	private String opUserIp;
	private int envId;
	private Date opTime;
	private int projectId;	
	private String content;
	
	private String opName;
	private String projectName;
	private String envName;
	private String opUserName;
	
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
		opName = OperationTypeEnum.fromInt(opType).getValue();
	}
	public int getOpUserId() {
		return opUserId;
	}
	public void setOpUserId(int opUserId) {
		this.opUserId = opUserId;
	}
	public String getOpUserIp() {
		return opUserIp;
	}
	public void setOpUserIp(String opUserIp) {
		this.opUserIp = opUserIp;
	}
	public int getEnvId() {
		return envId;
	}
	public void setEnvId(int envId) {
		this.envId = envId;
	}
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
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
