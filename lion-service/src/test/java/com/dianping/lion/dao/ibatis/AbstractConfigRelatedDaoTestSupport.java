/**
 * File Created at 12-9-27
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

import com.dianping.lion.dao.ConfigDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigTypeEnum;
import com.dianping.lion.support.AbstractDaoTestSupport;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO Comment of The Class
 *
 * @author danson.liu
 */
public abstract class AbstractConfigRelatedDaoTestSupport extends AbstractDaoTestSupport {

    @Autowired
    protected ConfigDao configDao;

    protected static final int Project_Foo_Id = 200000;
    protected static final int Project_Loo_Id = 200001;
    protected static final int Project_Koo_Id = 200002;
    protected static final int Env1_Id = 100;
    protected static final int Env2_Id = 101;
    protected static final String Config_Foo1_Key = "config_fortest1_key";
    protected static final String Config_Foo1_Env1_Value1 = "config_fortest1_env1_value1";
    protected static final String Config_Foo1_Env1_Value2 = "config_fortest1_env1_value2";
    protected static final String Config_Foo1_Env2_Value = "config_fortest1_env2_value";
    protected static final String Config_Foo2_Key = "config_fortest2_key";
    protected static final String Config_Loo1_Key = "config_fortest3_key";
    protected static final String Config_Loo2_Key = "config_fortest4_key";
    protected static final String Config_Foo1_Desc = "config_fortest1_desc";
    protected static final String Config_Foo2_Desc = "config_fortest2_desc";
    protected static final String Config_Loo1_Desc = "config_fortest3_desc";
    protected static final String Config_Loo2_Desc = "config_fortest4_desc";
    protected static final int Config_Foo1_Seq = 100000;
    protected static final int Config_Foo1_Env1_Inst_Seq1 = 100000;
    protected static final int Config_Foo1_Env1_Inst_Seq2 = 100002;
    protected static final int Config_Foo1_Env2_Inst_Seq = 100004;
    protected static final int Config_Foo2_Seq = 100003;
    protected static final int Config_Loo1_Seq = 100001;
    protected static final int Config_Loo2_Seq = 100002;
    protected static final int NotExists_Config_Id = 500000;
    protected static final String Foo_Context = "Foo_Context";
    protected int foo1ConfigId;
    protected int foo1ConfigEnv1InstId1;
    protected int foo1ConfigEnv1InstId2;
    protected int foo1ConfigEnv2InstId;
    protected int foo2ConfigId;
    protected int loo1ConfigId;
    protected int loo2ConfigId;

    @Before
    public void setUp() {
        foo1ConfigId = configDao.createConfig(buildConfig(Project_Foo_Id, Config_Foo1_Key, ConfigTypeEnum.String, Config_Foo1_Desc, false, Config_Foo1_Seq));
        foo1ConfigEnv1InstId1 = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT, null, Config_Foo1_Env1_Value1,
                Config_Foo1_Env1_Inst_Seq1));
        foo1ConfigEnv1InstId2 = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env1_Id, Foo_Context, null, Config_Foo1_Env1_Value2,
                Config_Foo1_Env1_Inst_Seq2));
        foo1ConfigEnv2InstId = configDao.createInstance(buildConfigInstance(foo1ConfigId, Env2_Id, ConfigInstance.NO_CONTEXT, null, Config_Foo1_Env2_Value,
                Config_Foo1_Env2_Inst_Seq));
        foo2ConfigId = configDao.createConfig(buildConfig(Project_Foo_Id, Config_Foo2_Key, ConfigTypeEnum.String, Config_Foo2_Desc, false, Config_Foo2_Seq));
        loo1ConfigId = configDao.createConfig(buildConfig(Project_Loo_Id, Config_Loo1_Key, ConfigTypeEnum.String, Config_Loo1_Desc, false, Config_Loo1_Seq));
        loo2ConfigId = configDao.createConfig(buildConfig(Project_Loo_Id, Config_Loo2_Key, ConfigTypeEnum.String, Config_Loo2_Desc, false, Config_Loo2_Seq));
        configDao.createInstance(buildConfigInstance(loo2ConfigId, Env2_Id, ConfigInstance.NO_CONTEXT, Config_Foo1_Key, "foo_val", 1));
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

}
