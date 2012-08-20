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
package com.dianping.lion.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.UserDao;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.exception.IncorrectPasswdException;
import com.dianping.lion.exception.SystemUserForbidLoginException;
import com.dianping.lion.exception.UserLockedException;
import com.dianping.lion.exception.UserNotFoundException;
import com.dianping.lion.service.UserService;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

    @Override
    public User loadById(int id) {
        User user = findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User[id=" + id + "] not found.");
        }
        return user;
    }

    @Override
    public User login(String loginName, String passwd) {
        // TODO implement me!
        if ("lion".equals(loginName) && "lion".equals(passwd)) {
            User mockUser = new User();
            mockUser.setId(1);
            mockUser.setName("lion");
            mockUser.setLoginName("lion测试用户");
            mockUser.setEmail("lion@dianping.com");
            mockUser.setSystem(false);
            mockUser.setCreateTime(new Date());
            return mockUser;
        } else if ("danson.liu".equals(loginName) && "danson".equals(passwd)) {
            return loadById(12);
        } else if ("redmine".equals(loginName)) {
            throw new SystemUserForbidLoginException();
        } else if ("lucy".equals(loginName)) {
            throw new UserLockedException();
        } else if ("foo".equals(loginName)) {
            throw new UserNotFoundException(loginName);
        } else {
            throw new IncorrectPasswdException();
        }
    }

}
