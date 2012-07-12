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

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;

/**
 * @author danson.liu
 *
 */
public class EnvironmentIbatisDao extends SqlMapClientDaoSupport implements EnvironmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> findAll() {
		return getSqlMapClientTemplate().queryForList("Environment.findAll");
	}

}
