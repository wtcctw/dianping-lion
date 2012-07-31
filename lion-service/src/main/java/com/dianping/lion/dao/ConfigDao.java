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
import com.dianping.lion.entity.ConfigStatusEnum;

/**
 * @author danson.liu
 *
 */
public interface ConfigDao {

	List<Config> findConfigsByProject(int projectId);

	/**
	 * 获取Config
	 * @param configId
	 * @return
	 */
	Config getConfig(int configId);

	/**
	 * 获取下一个Config(按seq排序)
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
	 * @param configId
	 * @param envId
	 * @return
	 */
	int getMaxInstSeq(int configId, int envId);
	
	/**
	 * 获取上一个Config(按seq排序)
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
	Map<Integer, ConfigInstance> findDefaultInstances(int projectId, int envId);

	/**
	 * 获取存在configInstance的Config
	 * @param projectId
	 * @param e
	 */
	List<Integer> findHasInstanceConfigs(int projectId, int envId);

	/**
	 * 获取存在context config instance的Config
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
	 * 获取指定key的配置项
	 * @param key
	 * @return
	 */
	Config findConfigByKey(String key);

	/**
	 * 删除指定配置项在指定环境下的所有配置实例，envId=null表示删除所有环境下的
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
	 * 删除指定配置项
	 * @param configId
	 */
	int delete(int configId);

	/**
	 * 创建配置项
	 * @param config
	 */
	int create(Config config);

	/**
	 * 获取指定配置项在指定环境下具有指定context值的配置实例
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

	/**
	 * 创建配置项在特定环境下的状态(存在则更新)
	 * @param configStatus
	 */
	int createStatus(ConfigStatus status);
	
	/**
	 * 更新status所指定的config和env下的配置项状态
	 * @param status
	 * @return
	 */
	int updateStatus(ConfigStatus status);
	
	/**
	 * 更新指定配置在指定环境下的状态
	 * @return
	 */
	int updateStatusStat(int configId, int envId, ConfigStatusEnum status);

	/**
	 * @param instance
	 */
	int updateInstance(ConfigInstance instance);

	/**
	 * 获取指定配置在各环境下至多maxPerEnv个配置实例
	 * @param id
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv);
	
	/**
	 * 获取指定配置在指定环境下至多maxPerEnv个配置实例
	 * @param configId
	 * @param envId
	 * @param maxPerEnv
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, int envId, Integer maxPerEnv);
	
}
