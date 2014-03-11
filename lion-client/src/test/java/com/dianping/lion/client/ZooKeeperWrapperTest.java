package com.dianping.lion.client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;

public class ZooKeeperWrapperTest {

    @Test
    public void testZooKeeperWrapperStringIntWatcher() throws Exception {
        for(int i=0; i<100; i++) {
            ZooKeeperWrapper zkClient = new ZooKeeperWrapper("dev.lion.dp:2181", 1000, null);
            String value = new String(zkClient.getData("/DP/CONFIG/pigeon.loadbalance", false, null));
            assertEquals(value, "autoaware");
            zkClient.close();
        }
    }

}
