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
	
	int USER_LION_ID = 1;
	int USER_REDMINE_ID = 2;
	int USER_SA_ID = 3;
	int USER_DBA_ID = 4;
	
	int ROLE_SCM_ID = 1;
	int ROLE_SA_ID = 2;
	
	String CACHE_KEY_ENVLIST = "cache_env_list";
	String CACHE_KEY_TEAMS = "cache_team_list";
	String CACHE_KEY_PROJECTS = "cache_project_list";
	String CACHE_KEY_PROJECT_PREFIX = "cache_project_";
	
}
