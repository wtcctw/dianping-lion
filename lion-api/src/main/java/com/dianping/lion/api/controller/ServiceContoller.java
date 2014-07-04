package com.dianping.lion.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.lion.api.exception.SecurityException;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Service;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.ServiceService;

@Controller
@RequestMapping("/service2")
public class ServiceContoller extends BaseController {

    @Autowired ServiceService serviceService;
    @Autowired ProjectService projectService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(value="env") String env,
                       @RequestParam(value="project") String project) {
        if(project==null) {
            return Result.createErrorResult("Project is null");
        }
        
        Project prj = projectService.findProject(project);
        if(prj == null) {
            return Result.createErrorResult(String.format("Project %s does not exist", project));
        }
        
        int envId = getEnvId(env);
        if(envId == -1) {
            return Result.createErrorResult("Invalid environment " + env);
        }
        
        List<Service> serviceList = serviceService.getServiceList(prj.getId(), envId);
        List<String> srvNameList = new ArrayList<String>();
        for(Service service : serviceList) {
            srvNameList.add(service.getName());
        }
        
        return Result.createSuccessResult(srvNameList);
    }
    
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Result get(@RequestParam(value="env") String env,
                      @RequestParam(value="service") String service,
                      @RequestParam(value="group", required=false, defaultValue="") String group) {
        int envId = getEnvId(env);
        if(envId == -1) {
            return Result.createErrorResult("Invalid environment " + env);
        }
        
        Service srv = serviceService.getService(envId, service, group);
        if(srv == null) {
            return Result.createErrorResult(String.format("Service %s for group [%s] in env %s does not exist", service, group, env));
        } else {
            return Result.createSuccessResult(srv.getHosts());
        }
    }
    
    @RequestMapping(value = "/set", method = RequestMethod.GET)
    @ResponseBody
    public Result set(HttpServletRequest request,
                      @RequestParam(value="id") int id,
                      @RequestParam(value="env") String env,
                      @RequestParam(value="service") String service,
                      @RequestParam(value="group", required=false, defaultValue="") String group,
                      @RequestParam(value="address") String address) {
        int envId = getEnvId(env);
        if(envId == -1) {
            return Result.createErrorResult("Invalid environment " + env);
        }
        
        try {
            verifyIdentity(request, envId, id);
        } catch (SecurityException e) {
            return Result.createErrorResult(e.getMessage());
        }
        
        Service srv = serviceService.getService(envId, service, group);
        if(srv == null) {
            return Result.createErrorResult(String.format("Service %s for group [%s] in env %s does not exist", service, group, env));
        } else {
            srv.setHosts(address);
            try {
                serviceService.updateService(srv);
                String message = String.format("Updated service %s for group [%s] in env %s to address %s", service, group, env, address);
                opLogService.createOpLog(new OperationLog(OperationTypeEnum.Service_Update, message));
                return Result.createSuccessResult(message);
            } catch (Exception e) {
                return Result.createErrorResult(String.format("Failed to update service %s: %s",service, e.getMessage()));
            }
        }
    }
}
