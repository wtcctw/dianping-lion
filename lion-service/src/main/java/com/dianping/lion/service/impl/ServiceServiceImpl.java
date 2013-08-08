package com.dianping.lion.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ServiceDao;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Service;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ServiceService;
import com.dianping.lion.service.ZookeeperService;
import com.dianping.lion.service.ZookeeperServiceFactory;

/**
 * TODO: add transaction support
 * TODO: add cache support
 *
 * @author chen.hua
 *
 */
public class ServiceServiceImpl implements ServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private static final String SERVICE_NAMESPACE = "DP/SERVER";

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private OperationLogService operationLogService;

    private CacheClient cacheClient;

    @Override
    public void updateService(Service service) throws Exception {
        serviceDao.updateService(service);
        zkUpdateService(service);
    }

    private void zkUpdateService(Service service) throws Exception {
        ZookeeperService zkService = getZkService(service.getEnvId());
        String path = getPath(service);
        zkService.set(path, service.getHosts());
    }

    @Override
    public void deleteService(Service service) throws Exception {
        serviceDao.deleteService(service);
        zkDeleteService(service);
        if(StringUtils.isNotEmpty(service.getGroup())) {
            List<Service> services = serviceDao.getServiceListByEnvName(service.getEnvId(), service.getName());
            if(services.size() == 0) {
                // Last service for name in env, delete parent node in ZK
                service.setGroup("");
                zkDeleteService(service);
            }
        }
    }

    private void zkDeleteService(Service service) throws Exception {
        ZookeeperService zkService = getZkService(service.getEnvId());
        String path = getPath(service);
        List<String> children = zkService.getChildren(path);
        if(children!=null && children.size() > 0) {
            zkService.set(path, "");
        } else {
            zkService.delete(path);
        }
    }

    private ZookeeperService getZkService(int envId) throws IOException {
        Environment environment = environmentService.findEnvByID(envId);
        return ZookeeperServiceFactory.getZookeeperService(environment.getIps(), SERVICE_NAMESPACE);
    }

    private String getPath(Service service) {
        String path = escape(service.getName());
        if(StringUtils.isNotEmpty(service.getGroup())) {
            path = path + '/' + service.getGroup();
        }
        return path;
    }

    private String escape(String name) {
        String result = name.replace('/', '^');
        return result;
    }

    @Override
    public void deleteService(int id) throws Exception {
        Service service = serviceDao.getServiceById(id);
        deleteService(service);
    }

    @Override
    public void createService(Service service) throws Exception {
        serviceDao.createService(service);
        zkCreateService(service);
    }

    private void zkCreateService(Service service) throws Exception {
        ZookeeperService zkService = getZkService(service.getEnvId());
        String path = getPath(service);
        if(zkService.exists(path)) {
            zkService.set(path, service.getHosts());
        } else {
            zkService.create(path, service.getHosts());
        }
    }

    @Override
    public List<Service> getServiceList(int projectId, int envId) {
        List<Service> serviceList = serviceDao.getServiceList(projectId, envId);
        return serviceList;
    }

    @Override
    public Service getService(int id) {
        Service service = serviceDao.getServiceById(id);
        return service;
    }

    @Override
    public Service getService(int envId, String name, String group) {
        Service service = serviceDao.getServiceByEnvNameGroup(envId, name, group);
        return service;
    }

    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

}
