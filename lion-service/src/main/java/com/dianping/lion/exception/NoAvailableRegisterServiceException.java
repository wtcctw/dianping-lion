/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-28
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
package com.dianping.lion.exception;

/**
 * @author danson.liu
 *
 */
public class NoAvailableRegisterServiceException extends RegisterRelatedException {

	private static final long serialVersionUID = -6518374335535564384L;
	
	private String environment;

	public NoAvailableRegisterServiceException(String environment, String message) {
		super(message);
		this.environment = environment;
	}

	public NoAvailableRegisterServiceException(String environment, String message, Throwable cause) {
		super(message, cause);
		this.environment = environment;
	}

	public NoAvailableRegisterServiceException(String environment, Throwable cause) {
		super(cause);
		this.environment = environment;
	}

	public String getEnvironment() {
		return environment;
	}
	
}
