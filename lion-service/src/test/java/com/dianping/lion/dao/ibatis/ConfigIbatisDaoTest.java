/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-17
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
package com.dianping.lion.dao.ibatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.support.AbstractDaoTestSupport;

/**
 * @author danson.liu
 *
 */
public class ConfigIbatisDaoTest extends AbstractDaoTestSupport {

	@Autowired
	private ConfigDao configDao;
	
	private static final int Project_Foo_Id = 200000;
	private static final int Project_Loo_Id = 200001;
	private static final int Project_Koo_Id = 200002;
	private static final int Env1_Id = 100;
	private static final int Env2_Id = 101;
	private static final String Config_Foo1_Key = "config_fortest1_key";
	private static final String Config_Foo1_Env1_Value1 = "config_fortest1_env1_value1";
	private static final String Config_Foo1_Env1_Value2 = "config_fortest1_env1_value2";
	private static final String Config_Foo1_Env2_Value = "config_fortest1_env2_value";
	private static final String Config_Foo2_Key = "config_fortest2_key";
	private static final String Config_Foo2_Env1_Value = "config_fortest2_env1_value";
	private static final String Config_Foo2_Env2_Value = "config_fortest2_env2_value";
	private static final String Config_Loo1_Key = "config_fortest3_key";
	private static final String Config_Loo2_Key = "config_fortest4_key";
	private static final String Config_Foo1_Desc = "config_fortest1_desc";
	private static final String Config_Foo2_Desc = "config_fortest2_desc";
	private static final String Config_Loo1_Desc = "config_fortest3_desc";
	private static final String Config_Loo2_Desc = "config_fortest4_desc";
	private static final int Config_Foo1_Seq = 100000;
	private static final int Config_Foo1_Env1_Inst_Seq1 = 100000;
	private static final int Config_Foo1_Env1_Inst_Seq2 = 100002;
	private static final int Config_Foo1_Env2_Inst_Seq = 100004;
	private static final int Config_Foo2_Seq = 100003;
	private static final int Config_Loo1_Seq = 100001;
	private static final int Config_Loo2_Seq = 100002;
	private static final int NotExists_Config_Id = 500000;
	private static final String Foo_Context = "Foo_Context";
	private int foo1ConfigId;
	private int foo1ConfigEnv1InstId1;
	private int foo1ConfigEnv1InstId2;
	private int foo1ConfigEnv2InstId;
	private int foo2ConfigId;
	private int loo1ConfigId;
	private int loo2ConfigId;
	
	@Before
	public void before() {
		foo1ConfigId = configDao.create(buildConfig(Project_Foo_Id, Config_Foo1_Key, ConfigTypeEnum.String, Config_Foo1_Desc, false, Config_Foo1_Seq));
		foo1ConfigEnv1InstId1 = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT, null, Config_Foo1_Env1_Value1, 
				Config_Foo1_Env1_Inst_Seq1));
		foo1ConfigEnv1InstId2 = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env1_Id, Foo_Context, null, Config_Foo1_Env1_Value2, 
				Config_Foo1_Env1_Inst_Seq2));
		foo1ConfigEnv2InstId = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env2_Id, ConfigInstance.NO_CONTEXT, null, Config_Foo1_Env2_Value, 
				Config_Foo1_Env2_Inst_Seq));
		foo2ConfigId = configDao.create(buildConfig(Project_Foo_Id, Config_Foo2_Key, ConfigTypeEnum.String, Config_Foo2_Desc, false, Config_Foo2_Seq));
		loo1ConfigId = configDao.create(buildConfig(Project_Loo_Id, Config_Loo1_Key, ConfigTypeEnum.String, Config_Loo1_Desc, false, Config_Loo1_Seq));
		loo2ConfigId = configDao.create(buildConfig(Project_Loo_Id, Config_Loo2_Key, ConfigTypeEnum.String, Config_Loo2_Desc, false, Config_Loo2_Seq));
	}
	
	@Test
	public void testFindConfigByProject() {
		List<Config> configs = configDao.findConfigByProject(Project_Foo_Id);
		assertEquals(2, configs.size());
		Config configFound = configs.get(0);
		assertEquals(Config_Foo1_Key, configFound.getKey());
		configFound = configs.get(1);
		assertEquals(Config_Foo2_Key, configFound.getKey());
	}
	
	@Test
	public void testGetConfig() {
		Config configFound = configDao.getConfig(foo1ConfigId);
		assertNotNull(configFound);
		assertEquals(Config_Foo1_Key, configFound.getKey());
		configFound = configDao.getConfig(NotExists_Config_Id);
		assertNull(configFound);
	}
	
	@Test
	public void testGetNextConfig() {
		Config configFound = configDao.getNextConfig(foo1ConfigId);
		assertNotNull(configFound);
		assertEquals(foo2ConfigId, configFound.getId());
		configFound = configDao.getNextConfig(foo2ConfigId);
		assertNull(configFound);
	}
	
	@Test
	public void testGetPrevConfig() {
		Config configFound = configDao.getPrevConfig(foo1ConfigId);
		assertNull(configFound);
		configFound = configDao.getPrevConfig(foo2ConfigId);
		assertNotNull(configFound);
		assertEquals(foo1ConfigId, configFound.getId());
	}
	
	@Test
	public void testGetMaxSeq() {
		int maxSeq = configDao.getMaxSeq(Project_Foo_Id);
		assertEquals(Config_Foo2_Seq, maxSeq);
		maxSeq = configDao.getMaxSeq(Project_Koo_Id);
		assertEquals(0, maxSeq);
	}
	
	@Test
	public void testGetMaxInstSeq() {
		int maxSeq = configDao.getMaxInstSeq(foo1ConfigId, Env1_Id);
		assertEquals(Config_Foo1_Env1_Inst_Seq2, maxSeq);
		maxSeq = configDao.getMaxInstSeq(NotExists_Config_Id, Env1_Id);
		assertEquals(0, maxSeq);
	}
	
	@Test
	public void testUpdate() {
		Config config = configDao.getConfig(foo1ConfigId);
		String newDesc = "new desc";
		int newModifyUserId = 3;
		config.setPrivatee(true);
		config.setDesc(newDesc);
		config.setModifyUserId(newModifyUserId);
		int updateCount = configDao.update(config);
		assertEquals(1, updateCount);
		Config configFound = configDao.getConfig(foo1ConfigId);
		assertNotNull(configFound);
		assertEquals(newDesc, configFound.getDesc());
		assertEquals(newModifyUserId, (int) configFound.getModifyUserId());
		assertEquals(true, configFound.isPrivatee());
	}
	
	@Test
	public void testFindDefaultInstance() {
		
	}
	
	private Config buildConfig(int projectId, String key, ConfigTypeEnum typeEnum, String desc, boolean privatee, int seq) {
		Config config = new Config();
		config.setKey(key);
		config.setDesc(desc);
		config.setPrivatee(privatee);
		config.setProjectId(projectId);
		config.setSeq(seq);
		config.setTypeEnum(typeEnum);
		return config;
	}
	
	private ConfigInstance buildConfigInstance(int configId, int envId, String context, String refkey, String value, int seq) {
		ConfigInstance instance = new ConfigInstance(configId, envId, context, value);
		instance.setRefkey(refkey);
		instance.setSeq(seq);
		return instance;
	}

//	@Test
	public void testReadConfigValue() throws JSONException {
//		List<ConfigInstance> instances = configDao.findInstanceByConfig(1, 1, 30);
//		JSONArray array = new JSONArray();
//		for (ConfigInstance instance : instances) {
//			JSONObject json = new JSONObject();
//			json.put("value", instance.getValue());
//			json.put("context", instance.getContext());
//			array.put(json);
//		}
//		String nodeValue = array.toString();
//		JSONArray array2 = new JSONArray(nodeValue);
//		JSONObject jsonObj = (JSONObject) array2.get(1);
//		String configValue = (String) jsonObj.get("value");
//		JSONObject configJson = new JSONObject("{\"main-db.passwd\":[]}");
//		System.out.println(configJson.get("main-db.passwd"));
	}
	
}
