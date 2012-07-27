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

import java.util.List;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatusEnum;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.ConfigVo;

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
	 * 清除指定配置项在指定环境下的值
	 * @param configId
	 * @param envId
	 */
	void clearInstance(int configId, int envId);

	/**
	 * 删除指定配置项
	 * @param configId
	 */
	void delete(int configId);

	/**
	 * 创建配置项
	 * @param config
	 */
	int create(Config config);

	/**
	 * 创建配置项实例
	 * @param instance
	 */
	int createInstance(ConfigInstance instance);
	
	/**
	 * 获取指定key配置的所有环境下的配置值, 若指定maxPerEnv则每个环境下至多获取maxPerEnv条(按照seq倒序)
	 * @param configKey
	 * @param maxPerEnv
	 * @return
	 */
	List<ConfigInstance> findInstancesByConfig(int configId, Integer maxPerEnv);

	/**
	 * @param key
	 */
	Config findConfigByKey(String key);

	/**
	 * @param configId
	 * @return
	 */
	Config getConfig(int configId);

	/**
	 * @param configId
	 * @param envId
	 * @param object
	 * @return
	 */
	ConfigInstance findInstance(int configId, int envId, String context);
	
	/**
	 * 查询配置项默认实例
	 * @param configId
	 * @param envId
	 * @return
	 */
	ConfigInstance findDefaultInstance(int configId, int envId);

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
	 * 保存配置项状态，如果存在就更新
	 * @param status
	 */
	void changeConfigStatus(int configId, int envId, ConfigStatusEnum status);

}
