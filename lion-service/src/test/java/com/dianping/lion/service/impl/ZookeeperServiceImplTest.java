package com.dianping.lion.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dianping.lion.service.ZookeeperService;
import com.dianping.lion.service.ZookeeperServiceFactory;

public class ZookeeperServiceImplTest {

    @Test
    public void testZookeeper() throws Exception {
        ZookeeperService zksrv = ZookeeperServiceFactory.getZookeeperService("192.168.6.25:2181");
        zksrv.create("/a/b/c", "dddd");

        String data = zksrv.get("/a/b/c");
        assertEquals("dddd", data);

        zksrv.set("/a/b/c", "eeee");
        data = zksrv.get("/a/b/c");
        assertEquals("eeee", data);

        zksrv.delete("/a/b/c");
        data = zksrv.get("/a/b/c");
        assertEquals(null, data);
    }
}
