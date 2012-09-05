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
package com.dianping.lion.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.UserDao;
import com.dianping.lion.entity.User;
import com.dianping.lion.util.Maps;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.UserCriteria;

/**
 * @author danson.liu
 *
 */
public class UserIbatisDao extends SqlMapClientDaoSupport implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		return getSqlMapClientTemplate().queryForList("User.findAll");
	}

	@Override
	public long getUserCount(UserCriteria userCriteria, Paginater<User> paginater) {
		Long userCount = (Long) getSqlMapClientTemplate().queryForObject("User.getUserCount", Maps.entry("criteria", userCriteria)
				.entry("paginater", paginater).get());
		return userCount != null ? userCount : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList(UserCriteria userCriteria, Paginater<User> paginater) {
		return getSqlMapClientTemplate().queryForList("User.getUserList", Maps.entry("criteria", userCriteria)
				.entry("paginater", paginater).get());
	}
	
	public User findById(int id) {
		return (User)getSqlMapClientTemplate().queryForObject("User.findById", id);
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findByNameOrLoginNameLike(String name, boolean includeSystem) {
        if (StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }
        return getSqlMapClientTemplate().queryForList("User.findByNameOrLoginNameLike", Maps.entry("name", name)
        		.entry("includeSystem", includeSystem).get());
    }

	@Override
	public User findByName(String userName) {
		return (User)getSqlMapClientTemplate().queryForObject("User.findByName", userName);
	}

	@Override
	public void insertUser(User user) {
		getSqlMapClientTemplate().insert("User.insertUser", user);
	}
	
	@Override
	public void updatePassword(User user) {
		getSqlMapClientTemplate().update("User.updateMD5Password", user);
	}

	@Override
	public int update(User user) {
		return getSqlMapClientTemplate().update("User.updateUser", user);
	}
}
