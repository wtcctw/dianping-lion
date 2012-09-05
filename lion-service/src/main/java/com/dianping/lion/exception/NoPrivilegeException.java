/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-4
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
 * 无权限异常
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class NoPrivilegeException extends RuntimeBusinessException {
	
	public static NoPrivilegeException INSTANCE = new NoPrivilegeException();

	public NoPrivilegeException() {
		super("Has no privilege.");
	}
	
	public NoPrivilegeException(String message) {
		super(message);
	}

	public NoPrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPrivilegeException(Throwable cause) {
		super(cause);
	}
	
}
