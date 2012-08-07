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
package com.dianping.lion.api.db;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.impl.ConfigServiceImpl;
import com.dianping.lion.service.impl.EnvironmentServiceImpl;
import com.dianping.lion.util.JsonParser;

/**
 * JsonParserTest
 * @author youngphy.yang
 *
 */
public class JsonParserTest {
	private static String content = null;
	@BeforeClass
	public static void setup() throws Exception {
		URL url = JsonParserTest.class.getClassLoader().getResource("dbjson.txt");
		File f = new File(url.getPath());
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine()) != null)
			sb.append(line);
		content = sb.toString();
	}
	
	@Test
	public void testGetDBAlias() throws Exception {
		JsonParser jp = new JsonParser();
		Map<String,Boolean> configs = jp.getDBAlias(content);
		assertEquals(3, configs.size());
//		Assert.assertEquals(configs.contains("timestamp"), false);
	}
	
	@Test
	public void testGetConfigInstances() throws Exception {
		JsonParser jp = new JsonParser() {
			public EnvironmentService getEnvService() {
				final Map<String, Integer> envMap = new HashMap<String, Integer>();
				envMap.put("dev", 1);
				envMap.put("alpha", 2);
				envMap.put("beta", 3);
				envMap.put("product", 4);
				return new EnvironmentServiceImpl() {
					public Environment findEnvByName(String envName) {
						Environment env = new Environment();
						env.setId(envMap.get(envName));
						return env;
					}
				};
			}
			public ConfigService getConfigService() {
				return new ConfigServiceImpl(null) {
					public Config getConfigByName(String name){
						Config config = new Config();
						config.setId(3);
						return config;
					}
				};
			}
		};
		Map<ConfigInstance, Boolean> cis = jp.getConfigInstances(content);
		assertEquals(3, cis.size());
	}
	
}
