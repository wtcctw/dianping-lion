package com.dianping.lion.web.action.service;

import java.util.List;

import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Service;
import com.dianping.lion.web.action.config.AbstractConfigAction;

public class ServiceAction extends AbstractConfigAction {

    private int serviceId;
    private String serviceName;
    private String serviceDesc;
    private String serviceGroup;
    private String serviceHosts;

    private List<Service> services;

    public String deleteService() {
        try {
            Service service = serviceService.getService(serviceId);
            serviceService.deleteService(service);
            operationLogService.createOpLog(new OperationLog(
                    OperationTypeEnum.Service_Delete, projectId, envId, "删除服务：" + service));
            createSuccessStreamResponse();
        } catch (Exception ex) {
            logger.error("删除服务失败", ex);
            createErrorStreamResponse("删除服务失败：" + ex.getMessage());
        }
        return SUCCESS;
    }

    public String getServiceList() {
        services = serviceService.getServiceList(projectId, envId);
        return SUCCESS;
    }

    public String updateService() {
        try {
            Service service = new Service();
            service.setEnvId(envId);
            service.setProjectId(projectId);
            service.setId(serviceId);
            service.setName(serviceName.trim());
            service.setDesc(serviceDesc.trim());
            service.setGroup(serviceGroup.trim());
            service.setHosts(serviceHosts.trim());
            if(serviceId > 0) {
                serviceService.updateService(service);
                operationLogService.createOpLog(new OperationLog(
                        OperationTypeEnum.Service_Update, projectId, envId, "修改服务：" + service));
            } else {
                serviceService.createService(service);
                operationLogService.createOpLog(new OperationLog(
                        OperationTypeEnum.Service_Create, projectId, envId, "新增服务：" + service));
            }
            createSuccessStreamResponse();
        } catch (Exception ex) {
            logger.error(serviceId > 0 ? "修改服务失败" : "新增服务失败", ex);
//            createErrorStreamResponse(serviceId > 0 ? "修改服务失败：" : "新增服务失败：" + ex.getMessage());
            createErrorStreamResponse(serviceId > 0 ? "修改服务失败" : "新增服务失败");
        }
        return SUCCESS;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceHosts() {
        return serviceHosts;
    }

    public void setServiceHosts(String serviceHosts) {
        this.serviceHosts = serviceHosts;
    }

}
