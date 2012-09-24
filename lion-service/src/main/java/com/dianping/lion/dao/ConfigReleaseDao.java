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
package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.ConfigSnapshotSet;

/**
 * @author danson.liu
 *
 */
public interface ConfigReleaseDao {
	
	int createSetTask(ConfigSetTask task);

	/**
	 * @param snapshot
	 */
	int createConfigSnapshotSet(ConfigSnapshotSet snapshot);

	/**
	 * @param configSnapshots
	 */
	void createConfigSnapshots(List<ConfigSnapshot> configSnapshots);

	/**
	 * @param configInstSnapshots
	 */
	void createConfigInstSnapshots(List<ConfigInstanceSnapshot> configInstSnapshots);

	/**
	 * @param projectId
	 * @param envId
	 * @param features
	 * @param keys
	 * @return 
	 */
	List<ConfigSetTask> getSetTask(int projectId, int envId, String[] features, String[] keys);

	/**
	 * @param projectId
	 * @param envId
	 * @param task
	 * @return
	 */
	ConfigSnapshotSet findSnapshotSetToRollback(int projectId, int envId, String task);

	/**
	 * @param id
	 * @return
	 */
	List<ConfigSnapshot> findConfigSnapshots(int snapshotSetId);

	/**
	 * @param id
	 * @return
	 */
	List<ConfigInstanceSnapshot> findConfigInstSnapshots(int snapshotSetId);

}
