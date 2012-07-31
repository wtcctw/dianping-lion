/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-31
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
 * TODO Comment of UnregisterFromZookeeperException
 * @author danson.liu
 *
 */
public class UnregisterFromZookeeperException extends RegisterRelatedException {

	private static final long serialVersionUID = 7053934836879590552L;

	public UnregisterFromZookeeperException(String message) {
		super(message);
	}

	public UnregisterFromZookeeperException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnregisterFromZookeeperException(Throwable cause) {
		super(cause);
	}
	
}
