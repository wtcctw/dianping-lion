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
package com.dianping.lion.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;
import com.dianping.lion.vo.Paginater;

/**
 * @author danson.liu
 *
 */
public interface ConfigService {

	/**
	 * @param criteria
	 * @param env
	 * @return
	 */
	List<ConfigVo> findConfigVos(ConfigCriteria criteria);

	/**
	 * @param projectId
	 * @param configId
	 */
	void moveDown(int projectId, int configId);

	/**
	 * @param projectId
	 * @param configId
	 */
	void moveUp(int projectId, Integer configId);

	/**
	 * 清除指定配置项在指定环境下的所有值
	 * @param configId
	 * @param envId
	 */
	void deleteInstances(int configId, int envId);

	/**
	 * 清除指定配置项在指定环境下的所有值
	 * @param config
	 * @param envId
	 */
	void deleteInstances(Config config, int envId);

	/**
     * 清除指定配置项在指定环境中指定泳道下的值
     * @param config 配置项
     * @param envId  环境
     * @param group  泳道
     */
	void deleteInstance(int configId, int envId, String group);

	/**
	 * 删除指定配置项
	 * @param configId
	 */
	ConfigDeleteResult deleteConfig(int configId);

	/**
	 * 创建配置项
	 * @param config
	 */
	int createConfig(Config config);

	/**
	 * 创建配置项实例
	 * @param instance
	 */
	int createInstance(ConfigInstance instance);

	/**
	 * @param instance
	 * @param setType
	 * @return
	 */
	int createInstance(ConfigInstance instance, ConfigSetType setType);

	/**
	 * 获取指定key配置的所有环境下的配置值, 若指定maxPerEnv则每个环境下至多获取maxPerEnv条(按照seq倒序)
	 * @param configKey
	 * @param maxPerEnv
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv);

	/**
	 * 获取指定key列表在指定环境下的配置值
	 * @param keyList
	 * @param envId
	 * @param group
	 * @return
	 */
	List<ConfigInstance> findInstancesByKeys(List<String> keyList, int envId, String group);

	/**
	 * @param key
	 */
	Config findConfigByKey(String key);

	List<Config> findConfigByKeys(List<String> keys);

	List<Config> findConfigs(int projectId);
	
	List<Config> findConfigByPrefix(String prefix);

	/**
	 * @param configId
	 * @return
	 */
	Config getConfig(int configId);

	int updateConfig(Config config);

	String getConfigFromRegisterServer(int envId, String key);

	String getConfigFromRegisterServer(int envId, String key, String group);

	/**
	 * 指定配置项在指定环境下的注册值(包含所有context-based config value的json结构)
	 * @param configId
	 * @param envId
	 * @return
	 */
	String getConfigContextValue(int configId, int envId);

	/**
	 * @param configId
	 * @param envId
	 * @param context
	 * @return
	 */
	ConfigInstance findInstance(int configId, int envId, String context);

    /**
     *
     * @param configId
     * @param envId
     * @param context
     * @return
     */
    String getConfigValue(int configId, int envId, String context);

	/**
	 * 查询配置项默认实例
	 * @param configId
	 * @param envId
	 * @return
	 */
	ConfigInstance findDefaultInstance(int configId, int envId);

	List<ConfigInstance> findInstances(int projectId, int envId);

	int updateInstance(ConfigInstance instance);

	/**
	 * 设置指定配置在指定环境和context下的值
	 * @param configId
	 * @param envId
	 * @param context
	 * @param value
	 */
	void setConfigValue(int configId, int envId, String context, String value);

	/**
	 * @param configId
	 * @param envId
	 * @param context
	 * @param value
	 * @param setType
	 */
	void setConfigValue(int configId, int envId, String context, String value, ConfigSetType setType);

	/**
	 * @param id
	 * @param id2
	 * @param configKey
	 * @param noContext
	 * @param value
	 */
	void setConfigValue(int projectId, int envId, String key, String desc, String context, String value, ConfigSetType setType);

	/**
	 * @param projectId
	 * @param envId
	 */
	Map<Integer, Date> findModifyTime(int projectId, int envId);

	/**
	 * @param configId
	 * @param envId
	 */
	void register(int configId, int envId);

	/**
	 * @param configId
	 * @param envId
	 */
	void registerAndPush(int configId, int envId);

	/**
     * @param configInstance
     */
    void register(ConfigInstance configInstance);

    /**
     * @param configInstance
     */
    void registerAndPush(ConfigInstance configInstance);

	Paginater<Config> paginateConfigs(ConfigCriteria criteria, Paginater<Config> paginater);

	List<ConfigInstance> getInstanceReferencedTo(String configKey, int envId);

    ConfigInstance findInstance(String key, int envId, String group);

    List<ConfigInstance> findInstancesByPrefix(String prefix, int envId, String group);

    List<ConfigInstance> findInstancesByProject(int projectId, int envId, String group);

}
