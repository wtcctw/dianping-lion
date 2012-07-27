/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-15
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
package com.dianping.lion.util;

import com.dianping.lion.entity.User;

/**
 * @author danson.liu
 *
 */
public class SecurityUtils {
	
	public static User getCurrentUser() {
		//TODO remove mock, implement me!
		User user = new User();
		user.setId(0);
		return user;
	}

}
