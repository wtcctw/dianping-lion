/**
 * Project: com.dianping.lion.lion-api-0.0.1-new
 * 
 * File Created at 2012-7-31
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
package com.dianping.lion.db;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.util.JsonParser;

/**
 * Storager
 * @author youngphy.yang
 *
 */
public class Storager {
	@Autowired
	private JsonParser jsonParser;
	@Autowired
	private ConfigService configService;
	@Autowired
	private EnvironmentService envService;
	
	public void init() {
		jsonParser.setConfigService(configService);
		jsonParser.setEnvService(envService);
	}
	
	public void store(String dsContent) throws Exception {
		checkAndSaveDBConfig(dsContent);
		checkAndUpdateDBConfigInstance(dsContent);
		//no instance correlated, consider to remove the config
		checkAndUpdateDBConfigAgain(dsContent);
	}
	
	protected void checkAndSaveDBConfig(String dsContent) throws Exception {
		Map<String,Boolean> dbAlias = jsonParser.getDBAlias(dsContent);
		for(Entry<String,Boolean> entry : dbAlias.entrySet()) {
			if(entry.getValue()) {
				//remove the config!!
			} else {
				Config config = configService.findConfigByKey(entry.getKey());
				//not exist, then insert
				if(config == null) {
					config = new Config();
					config.setDesc("");
					config.setKey(entry.getKey());
					config.setType(ConfigTypeEnum.String.getValue());
					config.setProjectId(ServiceConstants.PROJECT_DB_ID);
					configService.create(config);
				}
			}
		}
	}
	
	protected void checkAndUpdateDBConfigInstance(String dsContent) throws Exception {
		Map<ConfigInstance, Boolean> cis = jsonParser.getConfigInstances(dsContent);
		for (Entry<ConfigInstance, Boolean> entry : cis.entrySet()) {
			ConfigInstance ci = entry.getKey();
			if(entry.getValue()) {
				//delete the instance
				configService.deleteInstance(ci.getConfigId(), ci.getEnvId());
			} else {
				ConfigInstance result = configService.findInstance(ci.getConfigId(), ci.getEnvId(), ConfigInstance.NO_CONTEXT);
				if(result ==  null) {
					configService.createInstance(ci);
				} else {
					ci.setConfigId(result.getConfigId());
					configService.updateInstance(ci);
				}
			}
		}
	}
	
	protected void checkAndUpdateDBConfigAgain(String dsContent) throws Exception {
		Map<String,Boolean> dbAlias = jsonParser.getDBAlias(dsContent);
		for(Entry<String,Boolean> entry : dbAlias.entrySet()) {
			Config config = configService.findConfigByKey(entry.getKey());
			if(config !=  null) {
				int configId = config.getId();
				List<ConfigInstance> configs = configService.findInstancesByConfig(configId, null);
				if(configs == null || configs.size() == 0) {
					//no instance related, considering to the remove the config
					configService.delete(configId);
					continue;
				}
			}
		}
		
	}

	public JsonParser getJsonParser() {
		return jsonParser;
	}

	public void setJsonParser(JsonParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public EnvironmentService getEnvService() {
		return envService;
	}

	public void setEnvService(EnvironmentService envService) {
		this.envService = envService;
	}
	
}
