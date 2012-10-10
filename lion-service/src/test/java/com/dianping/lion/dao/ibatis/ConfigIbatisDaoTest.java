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

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigStatus;
import com.dianping.lion.vo.ConfigCriteria;
import com.dianping.lion.vo.Paginater;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author danson.liu
 */
public class ConfigIbatisDaoTest extends AbstractConfigRelatedDaoTestSupport {

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
        Map<Integer, ConfigInstance> defaultInstances = configDao.findDefaultInstance(Project_Foo_Id, Env1_Id);
        assertNotNull(defaultInstances);
        assertEquals(1, defaultInstances.size());
        assertTrue(defaultInstances.containsKey(foo1ConfigId));
        ConfigInstance configInstance = defaultInstances.get(foo1ConfigId);
        assertEquals(foo1ConfigEnv1InstId1, configInstance.getId());
    }

    @Test
    public void testFindHasInstanceConfigs() {
        List<Integer> hasInstanceConfigs = configDao.findHasInstanceConfigs(Project_Foo_Id, Env1_Id);
        assertNotNull(hasInstanceConfigs);
        assertEquals(1, hasInstanceConfigs.size());
        Integer configId = hasInstanceConfigs.get(0);
        assertEquals(new Integer(foo1ConfigId), configId);
        hasInstanceConfigs = configDao.findHasInstanceConfigs(Project_Loo_Id, Env1_Id);
        assertNotNull(hasInstanceConfigs);
        assertEquals(0, hasInstanceConfigs.size());
    }

    @Test
    public void testFindHasContextInstConfigs() {
        List<Integer> hasContextInstConfigs = configDao.findHasContextInstConfigs(Project_Foo_Id, Env1_Id);
        assertNotNull(hasContextInstConfigs);
        assertEquals(1, hasContextInstConfigs.size());
        Integer configId = hasContextInstConfigs.get(0);
        assertEquals(new Integer(foo1ConfigId), configId);
        hasContextInstConfigs = configDao.findHasContextInstConfigs(Project_Foo_Id, Env2_Id);
        assertNotNull(hasContextInstConfigs);
        assertEquals(0, hasContextInstConfigs.size());
    }

    @Test
    public void testFindConfigByKey() {
        Config config = configDao.findConfigByKey(Config_Foo1_Key);
        assertNotNull(config);
        assertEquals(foo1ConfigId, config.getId());
        config = configDao.findConfigByKey("not_exists_key");
        assertNull(config);
    }

    @Test
    public void testFindConfigByKeys() {
        List<Config> configs = configDao.findConfigByKeys(Arrays.asList(Config_Foo1_Key, Config_Foo2_Key, "not_exists_key"));
        assertNotNull(configs);
        assertEquals(2, configs.size());
        configs = configDao.findConfigByKeys(Collections.<String>emptyList());
        assertNotNull(configs);
        assertEquals(0, configs.size());
    }

    @Test
    public void testDeleteInstance() {
        int deleteCount = configDao.deleteInstance(foo1ConfigId, Env1_Id);
        assertEquals(2, deleteCount);
        ConfigInstance instance = configDao.findInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT);
        assertNull(instance);
        instance = configDao.findInstance(foo1ConfigId, Env1_Id, Foo_Context);
        assertNull(instance);
        deleteCount = configDao.deleteInstance(foo1ConfigId, Env2_Id);
        assertEquals(1, deleteCount);
        deleteCount = configDao.deleteInstance(foo1ConfigId, -1090022);
        assertEquals(0, deleteCount);
    }

    @Test
    public void testDelete() {
        int deleteCount = configDao.delete(foo1ConfigId);
        assertEquals(1, deleteCount);
        Config config = configDao.getConfig(foo1ConfigId);
        assertNull(config);
        deleteCount = configDao.delete(-12222);
        assertEquals(0, deleteCount);
    }

    @Test
    public void testFindInstance() {
        ConfigInstance instance = configDao.findInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT);
        assertNotNull(instance);
        assertEquals(foo1ConfigEnv1InstId1, instance.getId());
    }

    @Test
    public void testUpdateInstance() {
        ConfigInstance instance = configDao.findInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT);
        String newValue = "new value";
        String newRefkey = "new refkey";
        instance.setValue(newValue);
        instance.setRefkey(newRefkey);
        int updateCount = configDao.updateInstance(instance);
        assertEquals(1, updateCount);
        instance = configDao.findInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT);
        assertNotNull(instance);
        assertEquals(newValue, instance.getValue());
        assertEquals(newRefkey, instance.getRefkey());
    }

    @Test
    public void testFindInstanceByConfig() {
        List<ConfigInstance> instances = configDao.findInstanceByConfig(foo1ConfigId, 10);
        assertNotNull(instances);
        assertEquals(3, instances.size());
    }

    @Test
    public void testFindInstanceByProjectAndEnv() {
        List<ConfigInstance> instances = configDao.findInstanceByProjectAndEnv(Project_Foo_Id, Env1_Id);
        assertNotNull(instances);
        assertEquals(2, instances.size());
    }

    @Test
    public void testFindInstanceByConfigAndEnv() {
        List<ConfigInstance> instances = configDao.findInstanceByConfig(foo1ConfigId, Env1_Id, 10);
        assertNotNull(instances);
        assertEquals(2, instances.size());
    }

    @Test
    public void testModifyStatus() {
        ConfigStatus status = new ConfigStatus(foo1ConfigId, Env1_Id);
        Date modifyTime = new Date();
        status.setModifyTime(modifyTime);
        configDao.createStatus(status);
        List<ConfigStatus> configStatuses = configDao.findStatus(Project_Foo_Id, Env1_Id);
        assertNotNull(configStatuses);
        assertEquals(1, configStatuses.size());
        ConfigStatus statusFound = configStatuses.get(0);
        Date modifyTime1 = statusFound.getModifyTime();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}
        int updateCount = configDao.updateModifyStatus(foo1ConfigId, Env1_Id);
        assertEquals(1, updateCount);

        configStatuses = configDao.findStatus(Project_Foo_Id, Env1_Id);
        assertNotNull(configStatuses);
        assertEquals(1, configStatuses.size());
        statusFound = configStatuses.get(0);
        assertTrue(statusFound.getModifyTime().after(modifyTime1));

        List<ConfigStatus> statusList = configDao.findModifyTime(Project_Foo_Id, Env1_Id);
        assertNotNull(statusList);
        assertEquals(1, statusList.size());

        int deleteCount = configDao.deleteStatus(foo1ConfigId, Env1_Id);
        assertEquals(1, deleteCount);
        configStatuses = configDao.findStatus(Project_Foo_Id, Env1_Id);
        assertNotNull(configStatuses);
        assertEquals(0, configStatuses.size());
    }

    @Test
    public void testGetConfigCount() {
        ConfigCriteria criteria = new ConfigCriteria();
        criteria.setProjectId(Project_Foo_Id);
        long configCount = configDao.getConfigCount(criteria);
        assertEquals(2, configCount);
        criteria.setKey(Config_Foo1_Key);
        configCount = configDao.getConfigCount(criteria);
        assertEquals(1, configCount);
        criteria.setKey("not_exist_key");
        configCount = configDao.getConfigCount(criteria);
        assertEquals(0, configCount);
    }

    @Test
    public void testGetConfigList() {
        ConfigCriteria criteria = new ConfigCriteria();
        criteria.setProjectId(Project_Foo_Id);
        Paginater<Config> paginater = new Paginater<Config>();
        List<Config> configList = configDao.getConfigList(criteria, paginater);
        assertNotNull(configList);
        assertEquals(2, configList.size());
        criteria.setKey(Config_Foo1_Key);
        configList = configDao.getConfigList(criteria, paginater);
        assertNotNull(configList);
        assertEquals(1, configList.size());
        criteria.setKey("not_exist_key");
        configList = configDao.getConfigList(criteria, paginater);
        assertNotNull(configList);
        assertEquals(0, configList.size());
        paginater.setPageNumber(2);
        configList = configDao.getConfigList(criteria, paginater);
        assertNotNull(configList);
        assertEquals(0, configList.size());
    }

    @Test
    public void testHasConfigReferencedTo() {
        boolean hasReferencedTo = configDao.hasConfigReferencedTo(Config_Foo1_Key, Env1_Id);
        assertFalse(hasReferencedTo);
        hasReferencedTo = configDao.hasConfigReferencedTo(Config_Foo1_Key, Env2_Id);
        assertTrue(hasReferencedTo);
    }

    @Test
    public void testGetProjectHasReferencedConfigs() {
        List<Integer> hasReferencedConfigs = configDao.getProjectHasReferencedConfigs(Project_Foo_Id);
        assertNotNull(hasReferencedConfigs);
        assertEquals(1, hasReferencedConfigs.size());
        hasReferencedConfigs = configDao.getProjectHasReferencedConfigs(Project_Loo_Id);
        assertNotNull(hasReferencedConfigs);
        assertEquals(0, hasReferencedConfigs.size());
    }

    @Test
    public void testGetInstanceReferencedTo() {
        List<ConfigInstance> instances = configDao.getInstanceReferencedTo(Config_Foo1_Key, Env2_Id);
        assertNotNull(instances);
        assertEquals(1, instances.size());
        instances = configDao.getInstanceReferencedTo(Config_Foo1_Key, Env1_Id);
        assertNotNull(instances);
        assertEquals(0, instances.size());
    }

}
