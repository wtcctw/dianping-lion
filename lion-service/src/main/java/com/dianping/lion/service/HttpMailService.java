/**
 * Project: lion-service
 * 
 * File Created at 2012-8-14
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
package com.dianping.lion.service;

import java.io.UnsupportedEncodingException;

/**
 * HttpMailService
 * @author youngphy.yang
 *
 */
public interface HttpMailService {
	public boolean sendMail(int mailType, String mail, String title, String body) throws UnsupportedEncodingException;
}
