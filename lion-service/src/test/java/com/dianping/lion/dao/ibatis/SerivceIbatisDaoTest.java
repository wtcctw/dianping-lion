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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ServiceDao;
import com.dianping.lion.entity.Service;
import com.dianping.lion.support.AbstractDaoTestSupport;

/**
 * @author danson.liu
 *
 */
public class SerivceIbatisDaoTest extends AbstractDaoTestSupport {

    @Autowired
    private ServiceDao serviceDao;

    @Test
    public void testService() {
        Service service = new Service();
        service.setProjectId(99);
        service.setEnvId(99);
        service.setName("http://test/test_service_1.0.0");
        service.setDesc("hahahaha");
        service.setGroup("mobile");
        service.setHosts("localhost:9090");

        serviceDao.createService(service);

        List<Service> serviceList = serviceDao.getServiceList(99, 99);
        assertEquals(1, serviceList.size());

        service = serviceList.get(0);
        System.out.println(serviceList.get(0));
        assertEquals("localhost:9090", service.getHosts());

        service.setHosts("a.b.c.d.e.f.g");
        int rows = serviceDao.updateService(service);
        assertEquals(1, rows);

        service = serviceDao.getServiceById(service.getId());
        assertEquals("a.b.c.d.e.f.g", service.getHosts());

        rows = serviceDao.deleteService(service);
        assertEquals(1, rows);

        rows = serviceDao.deleteServiceById(service.getId());
        assertEquals(0, rows);
    }

}
