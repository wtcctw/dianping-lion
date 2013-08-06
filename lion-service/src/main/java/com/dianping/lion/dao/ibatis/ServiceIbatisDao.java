/**
 * Project: com.dianping.lion.lion-console-0.0.1
 *
 * File Created at 2012-7-12
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

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ServiceDao;
import com.dianping.lion.entity.Service;
import com.dianping.lion.util.Maps;

/**
 * @author danson.liu
 *
 */
public class ServiceIbatisDao extends SqlMapClientDaoSupport implements ServiceDao {

    @Override
    public int updateService(Service service) {
        int result = getSqlMapClientTemplate().update("Service.updateService", service);
        return result;
    }

    @Override
    public int deleteService(Service service) {
        int result = getSqlMapClientTemplate().delete("Service.deleteService", service);
        return result;
    }

    @Override
    public int deleteServiceById(int id) {
        int result = getSqlMapClientTemplate().delete("Service.deleteServiceById", id);
        return result;
    }

    @Override
    public Object createService(Service service) {
        Object result = getSqlMapClientTemplate().insert("Service.createService", service);
        return result;
    }

    @Override
    public List<Service> getServiceList(int projectId, int envId) {
        List<Service> result = getSqlMapClientTemplate().queryForList("Service.getServiceList", Maps.entry("projectId", projectId).entry("envId", envId).get());
        return result;
    }

    @Override
    public Service getServiceById(int id) {
        Service result = (Service) getSqlMapClientTemplate().queryForObject("Service.getServiceById", id);
        return result;
    }

    @Override
    public Service getServiceByEnvNameGroup(int envId, String name, String group) {
        Service result = (Service) getSqlMapClientTemplate().queryForObject("Service.getServiceByEnvNameGroup", Maps.entry("envId", envId).entry("name", name).entry("group", group).get());
        return result;
    }

    @Override
    public List<Service> getServiceListByEnvName(int envId, String name) {
        List<Service> result = getSqlMapClientTemplate().queryForList("Service.getServiceListByEnvName", Maps.entry("envId", envId).entry("name", name).get());
        return result;
    }
}
