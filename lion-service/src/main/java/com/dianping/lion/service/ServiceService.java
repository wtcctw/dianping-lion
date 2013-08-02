package com.dianping.lion.service;

import java.util.List;

import com.dianping.lion.entity.Service;

public interface ServiceService {

    public void updateService(Service service) throws Exception;

    public void deleteService(Service service) throws Exception;

    public void deleteService(int id) throws Exception;

    public void createService(Service service) throws Exception;

    public List<Service> getServiceList(int projectId, int envId);

    public Service getService(int id);

    public Service getService(int envId, String name, String group);

}
