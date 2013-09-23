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

    private static final int ENV_ID = 52;   // 52->192.168.7.41:2181
    private static final String ZOOKEEPER_SERVER = "192.168.7.41:2181";
    private static final String SERVICE_NAME = "http://service.dianping.com/test_project/test_service_1.0.0";
    private static final String GROUP = "test";
    private static final String HOST1 = "1.1.1.1:1111";
    private static final String HOST2 = "2.2.2.2:2222";
    private static final String HOST3 = "3.3.3.3:3333";
    private static final String HOST_LIST = HOST1 + "," + HOST2;
    private static final String HOST_LIST2 = HOST2 + "," + HOST3;

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
        service.setHosts(HOST_LIST);

        // Create service
        serviceService.createService(service);
        // Verify service is created in DB and ZK
        service = serviceDao.getServiceByEnvNameGroup(ENV_ID, SERVICE_NAME, GROUP);
        assertEquals(HOST_LIST, service.getHosts());
        ZookeeperService zookeeper = ZookeeperServiceFactory.getZookeeperService(ZOOKEEPER_SERVER);
        String data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(HOST_LIST, data);
        boolean exist = zookeeper.exists("/DP/WEIGHT/" + HOST1);
        assertTrue(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST2);
        assertTrue(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST3);
        assertFalse(exist);

        // Update service
        service.setHosts(HOST_LIST2);
        serviceService.updateService(service);
        // Verify service is updated in DB and ZK
        service = serviceDao.getServiceById(service.getId());
        assertEquals(HOST_LIST2, service.getHosts());
        zookeeper = ZookeeperServiceFactory.getZookeeperService(ZOOKEEPER_SERVER);
        data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(HOST_LIST2, data);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST1);
        assertFalse(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST2);
        assertTrue(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST3);
        assertTrue(exist);

        // Delete service
        serviceService.deleteService(service);
        // Verify service is deleted in DB and ZK
        service = serviceDao.getServiceById(service.getId());
        assertEquals(null, service);
        data = zookeeper.get("/DP/SERVER/" + SERVICE_NAME.replace('/', '^') + '/' + GROUP);
        assertEquals(null, data);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST1);
        assertFalse(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST2);
        assertFalse(exist);
        exist = zookeeper.exists("/DP/WEIGHT/" + HOST3);
        assertFalse(exist);
    }

}
