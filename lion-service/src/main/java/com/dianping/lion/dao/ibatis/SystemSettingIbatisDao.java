/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-1
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

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.SystemSettingDao;
import com.dianping.lion.util.Maps;

/**
 * @author danson.liu
 *
 */
public class SystemSettingIbatisDao extends SqlMapClientDaoSupport implements SystemSettingDao {

	@Override
	public String get(String key) {
		return (String) getSqlMapClientTemplate().queryForObject("SystemSetting.getSettingValueByKey", key);
	}

	@Override
	public void update(String key, String value) {
		getSqlMapClientTemplate().update("SystemSetting.updateSetting", Maps.entry("key", key).entry("value", value).get());
	}

}
