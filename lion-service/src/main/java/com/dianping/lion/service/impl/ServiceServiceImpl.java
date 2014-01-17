package com.dianping.lion.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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

    private static final String SERVICE_PATH = "/DP/SERVER/";
    private static final String WEIGHT_PATH = "/DP/WEIGHT/";
    private static final String DEFAULT_WEIGHT = "1";

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private OperationLogService operationLogService;

    private CacheClient cacheClient;

    @Override
    public void updateService(Service service) throws Exception {
        Service originalService = serviceDao.getServiceById(service.getId());
        serviceDao.updateService(service);
        zkUpdateService(originalService, service);
    }

    private void zkUpdateService(Service originalService, Service service) throws Exception {
        ZookeeperService zkService = getZkService(service.getEnvId());
        String path = getServicePath(service);
        zkService.createOrSet(path, service.getHosts());
        // Update weight node under /DP/WEIGHT
        Set<String> oriHostSet = new HashSet<String>();
        for(String host : originalService)
            oriHostSet.add(host);
        Set<String> hostSet = new HashSet<String>();
        for(String host : service)
            hostSet.add(host);

        Collection<String> removeSet = CollectionUtils.subtract(oriHostSet, hostSet);
        for(String host : removeSet) {
            String wp = WEIGHT_PATH + host;
            if(zkService.exists(wp)) {
//                zkService.delete(wp);
            }
        }

        Collection<String> addSet = CollectionUtils.subtract(hostSet, oriHostSet);
        for(String host : addSet) {
            String wp = WEIGHT_PATH + host;
            zkService.createOrSet(wp, DEFAULT_WEIGHT);
        }
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
        String path = getServicePath(service);
        List<String> children = zkService.getChildren(path);
        if(children!=null && children.size() > 0) {
            zkService.set(path, "");
        } else {
            zkService.delete(path);
        }
        // Delete weight node under /DP/WEIGHT
        for(String host : service) {
            String wp = WEIGHT_PATH + host;
            if(zkService.exists(wp)) {
//                zkService.delete(wp);
            }
        }
    }

    private ZookeeperService getZkService(int envId) throws Exception {
        Environment environment = environmentService.findEnvByID(envId);
        return ZookeeperServiceFactory.getZookeeperService(environment.getIps());
    }

    private String getServicePath(Service service) {
        String path = SERVICE_PATH + escape(service.getName());
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
        String path = getServicePath(service);
        if(zkService.exists(path)) {
            zkService.set(path, service.getHosts());
        } else {
            zkService.create(path, service.getHosts());
        }
        // Create weight node under /DP/WEIGHT
        for(String host : service) {
            String wp = WEIGHT_PATH + host;
            if(zkService.exists(wp)) {
                zkService.set(wp, DEFAULT_WEIGHT);
            } else {
                zkService.create(wp, DEFAULT_WEIGHT);
            }
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

	@Override
	public int getWeight(int envId, String ip, int port) throws Exception {
		ZookeeperService zkService = getZkService(envId);
		if(port == DEFAULT_PORT) {
			port = getServicePort(zkService, ip);
		}
		String path = WEIGHT_PATH + ip + ":" + port;
		String value = zkService.get(path);
		if(value == null) 
		    throw new RuntimeException("Failed to get weight for " + ip + ":" + port);
		return Integer.parseInt(value);
	}

	@Override
	public int setWeight(int envId, String ip, int port, int weight) throws Exception {
		ZookeeperService zkService = getZkService(envId);
		if(port == DEFAULT_PORT) {
			port = getServicePort(zkService, ip);
		}
		String path = WEIGHT_PATH + ip + ":" + port;
		zkService.set(path, "" + weight);
		return weight;
	}

	private int getServicePort(ZookeeperService zkService, String ip) throws Exception {
		List<String> children = zkService.getChildren("/DP/WEIGHT");
		String path = null;
		String prefix = ip + ":";
		for(String child : children) {
			if(child.startsWith(prefix)) {
				path = child;
				break;
			}
		}
		if(path == null) 
			throw new NullPointerException("Failed to find service port " + ip);
		path = path.substring(path.indexOf(':') + 1);
		return Integer.parseInt(path);
	}

	@Override
	public Integer getProjectId(String name) {
		Integer projectId = serviceDao.getProjectId(name);
        return projectId;
	}

}
