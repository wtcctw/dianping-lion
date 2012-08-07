/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-4
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

import com.dianping.lion.dao.RegisterPointDao;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.RegisterPoint;

/**
 * TODO Comment of RegisterIbatisDao
 * @author danson.liu
 *
 */
public class RegisterPointIbatisDao extends SqlMapClientDaoSupport implements RegisterPointDao {

	@Override
	public int create(RegisterPoint registerPoint) {
		return (Integer) getSqlMapClientTemplate().insert("RegisterPoint.insertPoint", registerPoint);
	}

	@Override
	public int create(ConfigSnapshot configSnapshot) {
		return (Integer) getSqlMapClientTemplate().insert("RegisterPoint.insertConfigSnapshot", configSnapshot);
	}

	@Override
	public void create(List<ConfigInstanceSnapshot> configInstSnapshots) {
		getSqlMapClientTemplate().insert("RegisterPoint.insertConfigInstSnapshots", configInstSnapshots);
	}

}
