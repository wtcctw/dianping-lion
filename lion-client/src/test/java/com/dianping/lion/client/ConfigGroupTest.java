package com.dianping.lion.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.dianping.lion.Constants;
import com.dianping.lion.console.ZKClient;
import org.apache.curator.test.TestingServer;

public class ConfigGroupTest {

    public static final String ENV_FILE = "/data/webapps/appenv";
    public static final String GROUP = "group";
    public static final String KEY = "project.key";
    public static final String VALUE = "value";
    public static final String NEW_VALUE = "new_value";
    public static final String GROUP_KEY = KEY + Constants.PATH_SEPARATOR + GROUP;
    public static final String GROUP_VALUE = "group_value";
    public static final String NEW_GROUP_VALUE = "new_group_value";
    public static final String NON_EXIST_KEY = "project.non.exist.key";

    public void createEnvFile(String group) throws IOException {
        File file = new File(ENV_FILE);
        FileOutputStream out = new FileOutputStream(file);
        out.write((Constants.KEY_SWIMLANE + " = " + group).getBytes());
        out.flush();
        out.close();
        out = null;
    }

    public void removeEnvFile() {
        File file = new File(ENV_FILE);
        if(!file.exists()) {
            System.out.println("File does not exist " + ENV_FILE);
            return;
        }
        if(file.delete()) {
            System.out.println("Deleted file " + ENV_FILE);
        } else {
            System.out.println("Can not delete file " + ENV_FILE);
        }
    }

    public TestingServer startZkServer(boolean createGroupNode) throws Exception {
        TestingServer server = new TestingServer();
        ZKClient client = ZKClient.getInstance(server.getConnectString(), Constants.CONFIG_PATH);
        client.create(KEY, VALUE);
        if(createGroupNode)
            client.create(GROUP_KEY, GROUP_VALUE);
        return server;
    }

    public void stopZkServer(TestingServer server) throws Exception {
        server.stop();
    }


    @Test
    public void getConfigWithoutGroup() throws Exception {
        removeEnvFile();
        TestingServer zkServer = startZkServer(true);
        ConfigCache configMgr = ConfigCache.getInstance(zkServer.getConnectString());
        String value = configMgr.getProperty(KEY);
        assertEquals(VALUE, value);
        value = configMgr.getProperty(NON_EXIST_KEY);
        assertNull(value);
        configMgr.reset();
        zkServer.stop();
    }

    @Test
    public void getConfigWithGroup() throws Exception {
        createEnvFile(GROUP);
        TestingServer zkServer = startZkServer(true);
        ConfigCache configMgr = ConfigCache.getInstance(zkServer.getConnectString());
        String value = configMgr.getProperty(KEY);
        assertEquals(GROUP_VALUE, value);
        value = configMgr.getProperty(NON_EXIST_KEY);
        assertNull(value);
        configMgr.reset();
        zkServer.stop();
        removeEnvFile();
    }

    @Test
    public void getConfigWithGroupFallback() throws Exception {
        createEnvFile(GROUP);
        TestingServer zkServer = startZkServer(false);
        ConfigCache configMgr = ConfigCache.getInstance(zkServer.getConnectString());
        String value = configMgr.getProperty(KEY);
        assertEquals(VALUE, value);
        value = configMgr.getProperty(NON_EXIST_KEY);
        assertNull(value);
        configMgr.reset();
        zkServer.stop();
        removeEnvFile();
    }

    @Test
    public void notifyConfigChangeWithoutGroup() throws Exception {
        removeEnvFile();
        TestingServer zkServer = startZkServer(true);
        ConfigCache configMgr = ConfigCache.getInstance(zkServer.getConnectString());
        ConfigMonitor configMonitor = new ConfigMonitor();
        configMgr.addChange(configMonitor);

        String value = configMgr.getProperty(KEY);
        assertEquals(VALUE, value);

        ZKClient client = ZKClient.getInstance(zkServer.getConnectString(), Constants.CONFIG_PATH);
        client.setAndPush(KEY, NEW_VALUE);
        Thread.sleep(1000);
        assertEquals(KEY, configMonitor.getChangedKey());
        assertEquals(NEW_VALUE, configMonitor.getChangedValue());
        assertEquals(1, configMonitor.getChangeCount());

        client.setAndPush(GROUP_KEY, NEW_GROUP_VALUE);
        Thread.sleep(1000);
        assertEquals(KEY, configMonitor.getChangedKey());
        assertEquals(NEW_VALUE, configMonitor.getChangedValue());
        assertEquals(1, configMonitor.getChangeCount());

        configMgr.reset();
        zkServer.stop();
    }

    @Test
    public void notifyConfigChangeWithGroup() throws Exception {
        createEnvFile(GROUP);
        TestingServer zkServer = startZkServer(false);
        ConfigCache configMgr = ConfigCache.getInstance(zkServer.getConnectString());
        ConfigMonitor configMonitor = new ConfigMonitor();
        configMgr.addChange(configMonitor);

        String value = configMgr.getProperty(KEY);
        assertEquals(VALUE, value);

        ZKClient client = ZKClient.getInstance(zkServer.getConnectString(), Constants.CONFIG_PATH);
        client.setAndPush(KEY, NEW_VALUE);
        Thread.sleep(5000);
        assertEquals(KEY, configMonitor.getChangedKey());
        assertEquals(NEW_VALUE, configMonitor.getChangedValue());
        assertEquals(1, configMonitor.getChangeCount());

        client.createAndPush(GROUP_KEY, GROUP_VALUE);
        client.setAndPush(GROUP_KEY, NEW_GROUP_VALUE);
        Thread.sleep(5000);
        assertEquals(KEY, configMonitor.getChangedKey());
        assertEquals(NEW_GROUP_VALUE, configMonitor.getChangedValue());
        assertEquals(2, configMonitor.getChangeCount());

        client.setAndPush(KEY, VALUE);
        Thread.sleep(5000);
        assertEquals(KEY, configMonitor.getChangedKey());
        assertEquals(NEW_GROUP_VALUE, configMonitor.getChangedValue());
        assertEquals(2, configMonitor.getChangeCount());

        configMgr.reset();
        zkServer.stop();
        removeEnvFile();
    }

    public static class ConfigMonitor implements ConfigChange {
        private String changedKey = null;
        private String changedValue = null;
        private int changeCount = 0;

        public String getChangedKey() {
            return changedKey;
        }

        public void setChangedKey(String changedKey) {
            this.changedKey = changedKey;
        }

        public String getChangedValue() {
            return changedValue;
        }

        public void setChangedValue(String changedValue) {
            this.changedValue = changedValue;
        }

        public int getChangeCount() {
            return changeCount;
        }

        public void setChangeCount(int changeCount) {
            this.changeCount = changeCount;
        }

        @Override
        public void onChange(String key, String value) {
            changeCount++;
            changedKey = key;
            changedValue = value;
            System.out.println(this);
        }

        @Override
        public String toString() {
            return changeCount + " : " + changedKey + " -> " + changedValue;
        }

    }
}
