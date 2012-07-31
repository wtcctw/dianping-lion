/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-30
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
public class RegisterToZookeeperException extends RegisterRelatedException {

	private static final long serialVersionUID = -9030616832482904030L;

	public RegisterToZookeeperException(String message) {
		super(message);
	}

	public RegisterToZookeeperException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegisterToZookeeperException(Throwable cause) {
		super(cause);
	}
	
}
