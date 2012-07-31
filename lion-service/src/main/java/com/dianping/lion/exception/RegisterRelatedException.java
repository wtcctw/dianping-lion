/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-27
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
 * TODO Comment of MediumRelatedException
 * @author danson.liu
 *
 */
public class RegisterRelatedException extends RuntimeBusinessException {

	private static final long serialVersionUID = -5395984247726266861L;

	public RegisterRelatedException(String message) {
		super(message);
	}

	public RegisterRelatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegisterRelatedException(Throwable cause) {
		super(cause);
	}
	
}
