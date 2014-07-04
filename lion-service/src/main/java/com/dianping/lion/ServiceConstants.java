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

import java.util.regex.Pattern;

/**
 * @author danson.liu
 *
 */
public interface ServiceConstants {

	/**最大有效的配置实例个数，按照ConfigInstance的Seq字段倒序，默认配置实例必须额外包含，若不在当中的话*/
	int MAX_AVAIL_CONFIG_INST = 30;
	
	int TEAM_PUBLIC_ID = 1;
	int PRODUCT_PUBLIC_ID = 1;
	int PROJECT_DB_ID = 100;
	int PROJECT_SHARED_ID = 101;
	
	String PROJECT_DB_NAME = "data-source";
	String PROJECT_SHARED_NAME = "shared-config";
	
	int USER_LION_ID = 1;
	int USER_REDMINE_ID = 2;
	int USER_SA_ID = 3;
	int USER_DBA_ID = 4;
	
	int ROLE_SCM_ID = 1;
	int ROLE_SA_ID = 2;
	int ROLE_DBA_ID = 3;
	int ROLE_QA_ID = 4;
	int ROLE_DEV_ID = 5;
	
	String REF_CONFIG_PREFIX = "${";
	String REF_CONFIG_SUFFIX = "}";
	Pattern REF_EXPR_PATTERN = Pattern.compile("\\$ref\\{([^\\?]+?)(\\?(.*?))?\\}");
	
	String PROJECT_OWNER = "owner";
	String PROJECT_MEMBER = "member";
	String PROJECT_OPERATOR = "operator";
	
	String CACHE_KEY_ENVLIST = "cache_env_list";
	String CACHE_KEY_TEAMS = "cache_team_list";
	String CACHE_KEY_PROJECTS = "cache_project_list";
	String CACHE_KEY_PROJECT_PREFIX = "cache_project_";
	
	String CACHE_CONFIG_PREFIX = "cache_config_";
	
	String CACHE_PROJECT_MEMBER_PREFIX = "cache_project_member_";
	String CACHE_PROJECT_OWNER_PREFIX = "cache_project_owner_";
	String CACHE_PROJECT_OPERATOR_PREFIX = "cache_project_operator_";
	
	String CACHE_USER_PREFIX = "cache_user_";
	
	String CACHE_SETTING_PREFIX = "cache_setting_";
	
	String CACHE_PRIV_URL_PREFIX = "cache_priv_url_";
	String CACHE_USER_RES_PREFIX = "cache_user_resource_";
	
	String SETTING_CACHE_ENABLED = "cache_enabled";
	String SETTING_GETCONFIG_WHITELIST = "http-getconfig-whitelist";

	
	String RES_CODE_CACHE = "res_cache";
	String RES_CODE_USER = "res_user";
	String RES_CODE_SETTING = "res_setting";
	String RES_CODE_JOB = "res_job";
	String RES_CODE_SECURITY = "res_security";
	
	String RES_CODE_ENVLIST = "res_env_list";
	String RES_CODE_ENVADD = "res_env_add";
	String RES_CODE_ENVEDIT = "res_env_edit";
	String RES_CODE_ENVDEL = "res_env_delete";
	
	String RES_CODE_PROJADD = "res_project_add";
	String RES_CODE_PROJEDIT = "res_project_edit";
	String RES_CODE_PROJDEL = "res_project_delete";
	
	String RES_CODE_PRODADD = "res_product_add";
	String RES_CODE_PRODEDIT = "res_product_edit";
	String RES_CODE_PRODDEL = "res_product_delete";
	
	String RES_CODE_TEAMADD = "res_team_add";
	String RES_CODE_TEAMEDIT = "res_team_edit";
	String RES_CODE_TEAMDEL = "res_team_delete";
	
	String RES_CODE_OPLOGVIEW = "res_oplog_view";
	
}
