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

import com.dianping.lion.dao.ConfigReleaseDao;
import com.dianping.lion.entity.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TODO Comment of The Class
 *
 * @author danson.liu
 */
public class ConfigReleaseIbatisDaoTest extends AbstractConfigRelatedDaoTestSupport {

    @Autowired
    private ConfigReleaseDao configReleaseDao;

    private static final String Feature_1 = "feature_1";
    private static final String Task_1 = "task_1";
    private static final String Task_2 = "task_2";

    @Before
    public void setUp() {
        super.setUp();

    }

    @Test
    public void testCreateSetTask() {
        int taskId = configReleaseDao.createSetTask(buildConfigSetTask());
        assertTrue(taskId > 0);
    }

    @Test
    public void testCreateConfigSnapshotSet() {
        int setId = configReleaseDao.createConfigSnapshotSet(buildConfigSnapshotSet(Project_Foo_Id, Env1_Id, Task_1));
        assertTrue(setId > 0);
    }

    @Test
    public void testCreateConfigSnapshots() {
        int setId = configReleaseDao.createConfigSnapshotSet(buildConfigSnapshotSet(Project_Foo_Id, Env1_Id, Task_1));
        List<ConfigSnapshot> snapshots = new ArrayList<ConfigSnapshot>();
        Config config = configDao.getConfig(foo1ConfigId);
        ConfigSnapshot snapshot = new ConfigSnapshot(config);
        snapshot.setSnapshotSetId(setId);
        snapshot.setValueModifyTime(new Date());
        snapshots.add(snapshot);
        config = configDao.getConfig(foo2ConfigId);
        snapshot = new ConfigSnapshot(config);
        snapshot.setSnapshotSetId(setId);
        snapshot.setValueModifyTime(new Date());
        snapshots.add(snapshot);
        try {
            configReleaseDao.createConfigSnapshots(snapshots);
        } catch (Exception e) {
            fail("create config snapshots failed.");
        }
        List<ConfigSnapshot> snapshotsFound = configReleaseDao.findConfigSnapshots(setId);
        assertNotNull(snapshotsFound);
        assertEquals(2, snapshotsFound.size());
    }

    @Test
    public void testCreateConfigInstSnapshots() {
        int setId = configReleaseDao.createConfigSnapshotSet(buildConfigSnapshotSet(Project_Foo_Id, Env1_Id, Task_1));
        List<ConfigInstanceSnapshot> instSnapshots = new ArrayList<ConfigInstanceSnapshot>();
        ConfigInstance instance = configDao.findInstance(foo1ConfigId, Env1_Id, ConfigInstance.NO_CONTEXT);
        ConfigInstanceSnapshot instSnapshot = new ConfigInstanceSnapshot(instance);
        instSnapshot.setSnapshotSetId(setId);
        instSnapshots.add(instSnapshot);
        try {
            configReleaseDao.createConfigInstSnapshots(instSnapshots);
        } catch (Exception e) {
            fail("create config instance snapshots failed.");
        }
        List<ConfigInstanceSnapshot> snapshotsFound = configReleaseDao.findConfigInstSnapshots(setId);
        assertNotNull(snapshotsFound);
        assertEquals(1, snapshotsFound.size());
    }

    @Test
    public void testGetSetTask() {
        configReleaseDao.createSetTask(buildConfigSetTask());
        List<ConfigSetTask> setTasks = configReleaseDao.getSetTask(Project_Foo_Id, Env1_Id, new String[]{Feature_1},
                new String[]{Config_Foo1_Key});
        assertNotNull(setTasks);
        assertEquals(1, setTasks.size());
        setTasks = configReleaseDao.getSetTask(Project_Foo_Id, Env1_Id, new String[]{Feature_1},
                new String[]{Config_Foo2_Key});
        assertNotNull(setTasks);
        assertEquals(1, setTasks.size());
    }

    @Test
    public void testFindSnapshotSetToRollback() {
        configReleaseDao.createConfigSnapshotSet(buildConfigSnapshotSet(Project_Foo_Id, Env1_Id, Task_1));
        configReleaseDao.createConfigSnapshotSet(buildConfigSnapshotSet(Project_Foo_Id, Env1_Id, Task_2));
        ConfigSnapshotSet snapshotSet = configReleaseDao.findSnapshotSetToRollback(Project_Foo_Id, Env1_Id, Task_1);
        assertNotNull(snapshotSet);
        assertEquals(Task_2, snapshotSet.getTask());
        snapshotSet = configReleaseDao.findSnapshotSetToRollback(Project_Foo_Id, Env2_Id, Task_1);
        assertNull(snapshotSet);
    }

    private ConfigSetTask buildConfigSetTask() {
        ConfigSetTask setTask = new ConfigSetTask();
        setTask.setProjectId(Project_Foo_Id);
        setTask.setKey(Config_Foo1_Key);
        setTask.setValue("New_Foo1_Value");
        setTask.setContext(ConfigInstance.NO_CONTEXT);
        setTask.setEnvId(Env1_Id);
        setTask.setFeature(Feature_1);
        return setTask;
    }

    private ConfigSnapshotSet buildConfigSnapshotSet(int projectId, int envId, String task) {
        ConfigSnapshotSet snapshot = new ConfigSnapshotSet();
        snapshot.setProjectId(projectId);
        snapshot.setEnvId(envId);
        snapshot.setTask(task);
        return snapshot;
    }

}
