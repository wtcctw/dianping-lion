package com.dianping.lion.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.dianping.lion.service.impl.ZookeeperServiceImpl;

public class ZookeeperServiceFactory {

    private static ConcurrentHashMap<String, ZookeeperService> serviceMap = new ConcurrentHashMap<String, ZookeeperService>();

    public static ZookeeperService getZookeeperService(String server, String namespace) throws Exception {
        String key = namespace == null ? server : server + '_' + namespace;
        ZookeeperService zkService = serviceMap.get(key);
        if(zkService == null) {
            zkService = new ZookeeperServiceImpl(server, namespace);
            ZookeeperService zkService0 = serviceMap.putIfAbsent(key, zkService);
            if(zkService0 != null)
                zkService = zkService0;
        }
        return zkService;
    }

    public static ZookeeperService getZookeeperService(String server) throws Exception {
        return getZookeeperService(server, null);
    }

}
