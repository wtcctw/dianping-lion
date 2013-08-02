package com.dianping.lion.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.lion.dao.ServiceDao;
import com.dianping.lion.entity.Service;
import com.dianping.lion.service.ServiceService;
import com.dianping.lion.service.ZookeeperService;
import com.dianping.lion.service.ZookeeperServiceFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath*:config/spring/appcontext-*.xml"
})
public class ServiceServiceImplTest {

    private static final int ENV_ID = 52;   // 52->192.168.7.86:2181
    private static final String ZOOKEEPER_SERVER = "192.168.7.86:2181";
    private static final String SERVICE_NAME = "http://service.dianping.com/test_project/test_service_1.0.0";
    private static final String GROUP = "test";
    private static final String HOSTS = "localhost:9090,127.0.0.1:8080";
    private static final String HOSTS2 = "127.0.0.1:8080,localhost:9090";

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceDao serviceDao;

    @Test
    public void testService() throws Exception {
        Service service = new Service();
        service.setProjectId(12345);
        service.setEnvId(ENV_ID);
        service.setName(SERVICE_NAME);
        service.setDesc("this is a test servcie");
        service.setGroup(GROUP);
        service.setHosts(HOSTS);

        // Create service
        serviceService.createService(service);
        // Verify service is created in DB and ZK
        service = serviceDao.getServiceByEnvNameGroup(ENV_ID, SERVICE_NAME, GROUP);
        assertEquals(HOSTS, service.getHosts());
        ZookeeperService zookeeper = ZookeeperServiceFactory.getZookeeperService(ZOOKEEPER_SERVER);
        String data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(HOSTS, data);

        // Update service
        service.setHosts(HOSTS2);
        serviceService.updateService(service);
        // Verify service is updated in DB and ZK
        service = serviceDao.getServiceById(service.getId());
        assertEquals(HOSTS2, service.getHosts());
        zookeeper = ZookeeperServiceFactory.getZookeeperService(ZOOKEEPER_SERVER);
        data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(HOSTS2, data);

        // Delete service
        serviceService.deleteService(service);
        // Verify service is deleted in DB and ZK
        service = serviceDao.getServiceById(service.getId());
        assertEquals(null, service);
        data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(null, data);
    }

}
