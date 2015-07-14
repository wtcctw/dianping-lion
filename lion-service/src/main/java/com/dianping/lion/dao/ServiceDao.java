package com.dianping.lion.dao;

import java.util.List;

import com.dianping.lion.entity.Service;

public interface ServiceDao {

    public int updateService(Service service);

    public int deleteService(Service service);

    public int deleteServiceById(int id);

    public Object createService(Service service);

    public List<Service> getServiceList(int projectId, int envId);

    public Service getServiceById(int id);

    public Service getServiceByEnvNameGroup(int envId, String name, String group);

    public List<Service> getServiceListByEnvName(int envId, String name);

	public Integer getProjectId(String name);

}
