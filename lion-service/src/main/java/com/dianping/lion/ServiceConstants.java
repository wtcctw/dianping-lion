/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-29
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
package com.dianping.lion;

/**
 * @author danson.liu
 *
 */
public interface ServiceConstants {

	/**最大有效的配置实例个数，按照ConfigInstance的Seq字段倒序，默认配置实例必须额外包含，若不在当中的话*/
	int MAX_AVAIL_CONFIG_INST = 30;
	
	int TEAM_PUBLIC_ID = 100;
	int PRODUCT_PUBLIC_ID = 100;
	int PROJECT_DB_ID = 100;
	int PROJECT_PUBLIC_ID = 101;
	
	int USER_REDMINE_ID = 100;
	int USER_SA_ID = 101;
	int USER_DBA_ID = 102;
	
	String CACHE_KEY_ENVLIST = "cache_env_list";
	
}
