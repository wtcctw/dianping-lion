/**
 * Project: com.dianping.lion.lion-console-0.0.1
 *
 * File Created at 2012-7-9
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

import com.dianping.lion.dao.EnvironmentDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.support.AbstractDaoTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;


public class EnvironmentIbatisDaoTest extends AbstractDaoTestSupport {

    @Autowired
    private EnvironmentDao environmentDao;
    private static final String Env1_Name = "env1_name";
    private static final String Env2_Name = "env2_name";
    private static final String Env1_Label = "env1_label";
    private static final String Env2_Label = "env2_label";
    private static final String Env1_Ips = "env1_ips";
    private static final String Env2_Ips = "env2_ips";
    private static final boolean Env1_IsOneline = false;
    private static final boolean Env2_IsOneline = true;
    private static final int Env1_Seq = -2;
    private static final int Env2_Seq = -1;
    private int env1Id;
    private int env2Id;

    @Before
    public void before() {
        env1Id = environmentDao.create(createEnvironment(Env1_Name, Env1_Label, Env1_Ips, Env1_IsOneline, Env1_Seq));
        env2Id = environmentDao.create(createEnvironment(Env2_Name, Env2_Label, Env2_Ips, Env2_IsOneline, Env2_Seq));
    }

    @Test
    public void testFindAll() {
        List<Environment> environments = environmentDao.findAll();
        assertNotNull(environments);
        assertTrue(environments.size() > 0);
    }

    @Test
    public void testFindEnvByID() {
        Environment environment = environmentDao.findEnvByID(env1Id);
        assertNotNull(environment);
        assertEquals(Env1_Name, environment.getName());
        environment = environmentDao.findEnvByID(-1000);
        assertNull(environment);
    }

    @Test
    public void testFindEnvByName() {
        Environment environment = environmentDao.findEnvByName(Env1_Name);
        assertNotNull(environment);
        environment = environmentDao.findEnvByName("no_exists_name");
        assertNull(environment);
    }

    @Test
    public void testUpdate() {
        Environment environment = environmentDao.findEnvByID(env1Id);
        String newEnvName = "env_new_name";
        String newEnvLabel = "env_new_label";
        String newEnvIps = "env_new_ips";
        environment.setName(newEnvName);
        environment.setLabel(newEnvLabel);
        environment.setIps(newEnvIps);
        environmentDao.update(environment);
        environment = environmentDao.findEnvByID(env1Id);
        assertNotNull(environment);
        assertEquals(newEnvName, environment.getName());
        assertEquals(newEnvLabel, environment.getLabel());
        assertEquals(newEnvIps, environment.getIps());
    }

    @Test
    public void testDelete() {
        Environment environment = environmentDao.findEnvByID(env1Id);
        assertNotNull(environment);
        environmentDao.delete(env1Id);
        environment = environmentDao.findEnvByID(env1Id);
        assertNull(environment);
    }

    @Test
    public void testFindPrevEnv() {
        Environment environment = environmentDao.findPrevEnv(env1Id);
        assertNull(environment);
        environment = environmentDao.findPrevEnv(env2Id);
        assertNotNull(environment);
        assertEquals(Env1_Name, environment.getName());
    }

    private Environment createEnvironment(String name, String label, String ips, boolean online, int seq) {
        Environment environment = new Environment();
        environment.setName(name);
        environment.setLabel(label);
        environment.setIps(ips);
        environment.setOnline(online);
        environment.setSeq(seq);
        return environment;
    }

}
