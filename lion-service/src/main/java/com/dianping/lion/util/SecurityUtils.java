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
	
	private static ThreadLocal<User> currentUser = new InheritableThreadLocal<User>();
	
	public static User getCurrentUser() {
		User user = currentUser.get();
		if (user != null) {
			return user;
		}
		user = new User();
		user.setId(0);
		return user;
	}
	
	public static int getCurrentUserId() {
		return getCurrentUser().getId();
	}
	
	public static void setCurrentUser(User user) {
		clearCurrentUser();
		currentUser.set(user);
	}
	
	public static void clearCurrentUser() {
		currentUser.remove();
	}

}
