/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-10
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

import java.util.Arrays;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ConfigReleaseDao;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.ConfigSnapshotSet;
import com.dianping.lion.util.Maps;

/**
 * TODO Comment of ConfigReleaseIbatisDao
 * @author danson.liu
 *
 */
public class ConfigReleaseIbatisDao extends SqlMapClientDaoSupport implements ConfigReleaseDao {

	@Override
	public int createSetTask(ConfigSetTask task) {
		return (Integer) getSqlMapClientTemplate().insert("ConfigRelease.insertSetTask", task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetTask> getSetTask(int projectId, int envId, String[] features, String[] keys) {
		return getSqlMapClientTemplate().queryForList("ConfigRelease.getSetTask", Maps.entry("projectId", projectId).entry("envId", envId)
				.entry("features", Arrays.asList(features)).entry("keys", Arrays.asList(keys)).get());
	}

	@Override
	public int createConfigSnapshotSet(ConfigSnapshotSet snapshot) {
		return (Integer) getSqlMapClientTemplate().insert("ConfigRelease.insertSnapshotSet", snapshot);
	}

	@Override
	public void createConfigSnapshots(List<ConfigSnapshot> configSnapshots) {
		int batchSize = 50;
		int snapshotSize = configSnapshots.size();
		for (int fromIndex = 0; fromIndex < snapshotSize;) {
			int toIndex = fromIndex + batchSize <= snapshotSize ? fromIndex + batchSize : snapshotSize;
			List<ConfigSnapshot> batchList = configSnapshots.subList(fromIndex, toIndex);
			getSqlMapClientTemplate().insert("ConfigRelease.insertConfigSnapshots", batchList);
			fromIndex = toIndex;
		}
	}

	@Override
	public void createConfigInstSnapshots(List<ConfigInstanceSnapshot> configInstSnapshots) {
		int batchSize = 20;
		int snapshotSize = configInstSnapshots.size();
		for (int fromIndex = 0; fromIndex < snapshotSize;) {
			int toIndex = fromIndex + batchSize <= snapshotSize ? fromIndex + batchSize : snapshotSize;
			List<ConfigInstanceSnapshot> batchList = configInstSnapshots.subList(fromIndex, toIndex);
			getSqlMapClientTemplate().insert("ConfigRelease.insertConfigInstSnapshots", batchList);
			fromIndex = toIndex;
		}
	}

	@Override
	public ConfigSnapshotSet findFirstSnapshotSet(int projectId, int envId, String task) {
		return (ConfigSnapshotSet) getSqlMapClientTemplate().queryForObject("ConfigRelease.findFirstSnapshotSet", 
				Maps.entry("projectId", projectId).entry("envId", envId).entry("task", task).get());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSnapshot> findConfigSnapshots(int snapshotSetId) {
		return getSqlMapClientTemplate().queryForList("ConfigRelease.findConfigSnapshots", snapshotSetId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigInstanceSnapshot> findConfigInstSnapshots(int snapshotSetId) {
		return getSqlMapClientTemplate().queryForList("ConfigRelease.findConfigInstSnapshots", snapshotSetId);
	}

}
