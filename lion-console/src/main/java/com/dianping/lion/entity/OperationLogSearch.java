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
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.entity;

/**
 * OperationLogSearch
 * @author youngphy.yang
 *
 */
public class OperationLogSearch {
	private String content = "";
	private int project = -1;
	private int opType = -1;
	private int user = -1;
	private int env = -1;
	private String from = "";
	private String to = "";
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getProject() {
		return project;
	}
	public void setProject(int project) {
		this.project = project;
	}
	public int getOpType() {
		return opType;
	}
	public void setOpType(int opType) {
		this.opType = opType;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getEnv() {
		return env;
	}
	public void setEnv(int env) {
		this.env = env;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
}
