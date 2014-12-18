package com.dianping.lion.client;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import jodd.util.RandomStringUtil;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.junit.Test;

public class ZooKeeperWrapperTest {

    {
        DOMConfigurator.configure(ZooKeeperWrapperTest.class.getResource("/log4j.xml"));
    }
    
    @Test
    public void testZooKeeperWrapperStringIntWatcher() throws Exception {
        for(int i=0; i<100; i++) {
            ZooKeeperWrapper zkClient = new ZooKeeperWrapper("dev.lion.dp:2181", 1000, null);
            String value = new String(zkClient.getData("/DP/CONFIG/pigeon.loadbalance", false, null));
            assertEquals(value, "autoaware");
            zkClient.close();
        }
    }

    @Test
    public void testZookeeperWrapper() throws Exception {
        final ZooKeeperWrapper zkClient = new ZooKeeperWrapper("dev.lion.dp:2181", 1000, null);
        final Random random = new Random();
        new Thread() {
            public void run() {
                try {
                    for(;;) {
                        try {
                            Thread.sleep((long) (120*1000*random.nextDouble()));
                            String value = RandomStringUtil.randomAlpha(8);
                            zkClient.setData("/DP/CONFIG/xxxxxx", value.getBytes(), -1);
                        } catch(NoNodeException e) {
                            System.out.println(e.getMessage());
                            zkClient.create("/DP/CONFIG/xxxxxx", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        new Thread() {
            public void run() {
                try {
                    for(;;) {
                        Thread.sleep(random.nextInt(60*1000));
                        try {
                            byte[] value = zkClient.getData("/DP/CONFIG/xxxxxx", true, null);
                            System.out.println("value: " + new String(value));
                        } catch(NoNodeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        new Thread() {
            public void run() {
                try {
                    for(;;) {
                        Thread.sleep((long) (120*1000*random.nextDouble()));
                        try {
                            byte[] value = zkClient.getData("/DP/CONFIG/xxxxxx", true, null);
                            System.out.println("value: " + new String(value));
                        } catch(NoNodeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        System.in.read();
    }
}
