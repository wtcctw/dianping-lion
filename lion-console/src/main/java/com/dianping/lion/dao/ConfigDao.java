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
import java.util.Map;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;

/**
 * @author danson.liu
 *
 */
public interface ConfigDao {

	List<Config> findConfigsByProject(int projectId, boolean includeDeleted);

	/**
	 * 获取有效的Config(未置为已删除状态的)
	 * @param configId
	 * @return
	 */
	Config getConfig(int configId);

	/**
	 * 获取下一个有效的Config(按seq排序)
	 * @param configId
	 * @return
	 */
	Config getNextConfig(int configId);
	
	/**
	 * 获取指定项目下所有配置的最大seq值
	 * @param projectId
	 * @return
	 */
	int getMaxSeq(int projectId);
	
	/**
	 * 获取上一个有效的Config(按seq排序)
	 * @param configId
	 * @return
	 */
	Config getPrevConfig(int configId);

	/**
	 * @param config
	 */
	void update(Config config);

	/**
	 * @param projectId
	 * @param envId
	 * @param b
	 * @return
	 */
	Map<Integer, ConfigInstance> findDefaultInstances(int projectId, int envId, boolean includeDeleted);

	/**
	 * 获取存在有效configInstance的有效Config
	 * @param projectId
	 * @param e
	 */
	List<Integer> findHasInstanceConfigs(int projectId, int envId);

	/**
	 * 获取存在有效context config instance的有效Config
	 * @param projectId
	 * @param envId
	 * @return
	 */
	List<Integer> findHasContextInstConfigs(int projectId, int envId);

	/**
	 * 获取指定项目下所有Config的status
	 * @param projectId
	 * @param envId 
	 */
	Map<Integer, ConfigStatus> findConfigStatus(int projectId, int envId);
	
	/**
	 * 获取指定key的有效配置项(未软删除)
	 * @param key
	 * @return
	 */
	Config findConfigByKey(String key);

	/**
	 * 删除指定配置项在指定环境下的所有配置实例(软删除)，envId=null表示删除所有环境下的
	 * @param configId
	 * @param envId
	 */
	int deleteInstance(int configId, Integer envId);

	/**
	 * 删除指定配置项在指定环境下的状态记录，envId=null表示删除所有环境下的
	 * @param configId
	 * @param envId
	 */
	int deleteStatus(int configId, Integer envId);
	
	/**
	 * 删除指定配置项(软删除)
	 * @param configId
	 */
	int delete(int configId);

	/**
	 * 创建配置项
	 * @param config
	 */
	int create(Config config);

	/**
	 * 获取指定配置项在指定环境下具有context值的配置实例(有效的[非软删除])
	 * @param configId
	 * @param envId
	 * @param context
	 */
	ConfigInstance findInstance(int configId, int envId, String context);

	/**
	 * 创建配置实例
	 * @param instance
	 * @return
	 */
	int createInstance(ConfigInstance instance);

}
