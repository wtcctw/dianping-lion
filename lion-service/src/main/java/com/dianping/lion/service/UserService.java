/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
<<<<<<< HEAD
 * File Created at 2012-7-12
=======
 * File Created at 2012-7-15
>>>>>>> 4daf1a6fbbf0169391b620875b779b7834b64489
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

import java.util.List;

import com.dianping.lion.entity.User;

public interface UserService {

	List<User> findAll();
	
	User findById(int id);
	
	User loadById(int id);

    User login(String loginName, String passwd);

    List<User> findByNameOrLoginNameLike(String name);

}
