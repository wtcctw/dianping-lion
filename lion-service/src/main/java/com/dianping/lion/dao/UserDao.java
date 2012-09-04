/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
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
package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.User;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.UserCriteria;

public interface UserDao {

	List<User> findAll();
	
	User findById(int id);

    List<User> findByNameOrLoginNameLike(String name);
	
	User findByName(String userName);
	
	void insertUser(User user);
	
	void updatePassword(User user);

	long getUserCount(UserCriteria userCriteria, Paginater<User> paginater);

	List<User> getUserList(UserCriteria userCriteria, Paginater<User> paginater);

	int update(User user);
	
}
