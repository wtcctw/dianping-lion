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

	List<Config> findConfigByProject(int projectId);

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
	Map<Integer, ConfigInstance> findDefaultInstance(int projectId, int envId);

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
	 * 获取指定key的配置项
	 * @param key
	 * @return
	 */
	Config findConfigByKey(String key);

	/**
	 * @param keys
	 * @return
	 */
	List<Config> findConfigByKeys(List<String> keys);
	
	/**
	 * 删除指定配置项在指定环境下的所有配置实例，envId=null表示删除所有环境下的
	 * @param configId
	 * @param envId
	 */
	int deleteInstance(int configId, Integer envId);
	
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
	 * @param instance
	 */
	int updateInstance(ConfigInstance instance);

	/**
	 * 获取指定配置在各环境下至多maxPerEnv个配置实例
	 * @param id
	 * @return
	 */
	List<ConfigInstance> findInstanceByConfig(int configId, Integer maxPerEnv);

	/**
	 * @param projectId
	 * @param envId
	 * @return
	 */
	List<ConfigInstance> findInstanceByProjectAndEnv(int projectId, int envId);

	/**
	 * 获取指定配置在指定环境下至多maxPerEnv个配置实例(必定包含default config instance)
	 * @param configId
	 * @param envId
	 * @param maxPerEnv
	 * @return
	 */
	List<ConfigInstance> findInstanceByConfig(int configId, int envId, Integer maxPerEnv);
	
	int updateModifyStatus(int configId, int envId);
	
	int createStatus(ConfigStatus status);
	
	int deleteStatus(int configId, int envId);

	/**
	 * @param projectId
	 * @param envId
	 */
	List<ConfigStatus> findStatus(int projectId, int envId);

	/**
	 * @param projectId
	 * @param envId
	 * @return
	 */
	List<ConfigStatus> findModifyTime(int projectId, int envId);

}
