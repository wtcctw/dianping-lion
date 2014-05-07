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
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.Paginater;

/**
 * @author danson.liu
 *
 */
public interface ConfigDao {

	/**
	 * 获取配置项
	 * @param configId
	 * @return
	 */
	Config getConfig(int configId);

	/**
	 * 获取下一个配置项(按seq排序)
	 * @param configId
	 * @return
	 */
	Config getNextConfig(int configId);

	/**
	 * 获取上一个配置项(按seq排序)
	 * @param configId
	 * @return
	 */
	Config getPrevConfig(int configId);

	/**
	 * 获取存在配置实例的配置项
	 * @param projectId
	 * @param envId
	 */
	List<Integer> findHasInstanceConfigs(int projectId, int envId);

	/**
	 * 获取存在泳道配置实例的配置项
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
	 * 获取指定项目的所有配置项
	 * @param projectId
	 * @return
	 */
	List<Config> findConfigByProject(int projectId);

	/**
	 * 获取指定key列表的所有配置项
	 * @param keys
	 * @return
	 */
	List<Config> findConfigByKeys(List<String> keys);

	/**
	 * 更新配置项
	 * @param config
	 */
	int updateConfig(Config config);

    /**
     * 删除配置项
     * @param configId
     */
    int deleteConfig(int configId);

    /**
     * 创建配置项
     * @param config
     */
    int createConfig(Config config);

	/**
	 * 获取指定项目下所有配置项的最大seq值
	 * @param projectId
	 * @return
	 */
	int getMaxConfigSeq(int projectId);

	/**
	 * 获取指定配置项在指定环境下的最大实例seq值
	 * @param configId
	 * @param envId
	 * @return
	 */
	int getMaxConfigInstSeq(int configId, int envId);

	/**
	 * 获取指定项目在指定环境下的所有默认泳道配置实例
	 * @param projectId
	 * @param envId
	 * @return
	 */
	Map<Integer, ConfigInstance> findDefaultInstances(int projectId, int envId);

	/**
     * 删除指定配置项在指定环境下指定泳道的配置实例
     * @param configId
     * @param envId
     * @param group 泳道名
     */
	int deleteInstance(int configId, int envId, String group);

	/**
	 * 删除指定配置项在指定环境下的所有配置实例，envId=null表示删除所有环境下的
	 * @param configId
	 * @param envId
	 */
	int deleteInstances(int configId, Integer envId);

	/**
	 * 获取指定配置项在指定环境和泳道下的配置实例
	 * @param configId
	 * @param envId
	 * @param context 配置所在泳道
	 */
	ConfigInstance findInstance(int configId, int envId, String context);

	/**
	 * 获取指定配置项在指定环境和泳道下的配置实例
	 * @param key
	 * @param envId
	 * @param context 配置所在泳道
	 */
	ConfigInstance findInstance(String key, int envId, String context);

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
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv);

	/**
	 * @param projectId
	 * @param envId
	 * @return
	 */
	List<ConfigInstance> findInstancesByProjectAndEnv(int projectId, int envId);

	/**
	 * 获取指定配置在指定环境下至多maxPerEnv个配置实例(必定包含default config instance)
	 * @param configId
	 * @param envId
	 * @param maxPerEnv
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, int envId, Integer maxPerEnv);

	int getConfigInstCount(int configId, int envId);

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

	long getConfigCount(ConfigCriteria criteria);

	List<Config> getConfigList(ConfigCriteria criteria, Paginater<Config> paginater);

	boolean hasConfigReferencedTo(String configKey, int envId);

	List<Integer> getProjectHasReferencedConfigs(int projectId);

	List<ConfigInstance> getInstanceReferencedTo(String configKey, int envId);

    List<Config> findConfigByPrefix(String prefix);

    List<ConfigInstance> findInstancesByKeys(List<String> keys, int envId, String group);

    List<ConfigInstance> findInstancesByPrefix(String prefix, int envId, String group);

}
