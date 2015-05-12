package com.dianping.lion.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.api.exception.SecurityException;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.SystemSettingService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.IPUtils;
import com.dianping.lion.util.SecurityUtils;

public class BaseController {
    
    @Autowired
    protected OperationLogService opLogService;
    
    @Autowired
    protected UserService userService;
    
    @Autowired
    protected EnvironmentService envService;
    
    @Autowired
    protected SystemSettingService systemSettingService;
    
    public void verifyIdentity(int userId) throws SecurityException {
        User user = userService.findById(userId);
        if (user == null) {
            throw new SecurityException("Invalid user id " + userId);
        }
        if (!user.isSystem()) {
            throw new SecurityException(String.format("User id %d is not system level", userId));
        }
        SecurityUtils.setCurrentUser(user);
    }
    
    public void verifyIdentity(HttpServletRequest request, int envId, int userId) throws SecurityException {
        if(envId == 4 || envId == 5) {
            String ip = IPUtils.getUserIP(request);
            if(!ip.startsWith("10.1.") && !ip.startsWith("10.2.") && !ip.startsWith("10.101.")) {
                checkAccessibility(ip);
            }
        }
        verifyIdentity(userId);
    }
    
    protected void checkAccessibility(String userIP) throws SecurityException {
        String whiteList = systemSettingService.getSetting(ServiceConstants.SETTING_GETCONFIG_WHITELIST);
        if (StringUtils.isBlank(whiteList)) {
            return;
        }
        if (whiteList.contains(userIP) || whiteList.contains("*.*.*.*")) {
            return;
        }
        String[] ipSegments = StringUtils.split(userIP, '.');
        if (ipSegments.length == 4) {
            for (int i = 3; i >= 1; i--) {
                String left = StringUtils.join(ipSegments, '.', 0, i);
                String pattern = left + StringUtils.repeat(".*", 4 - i);
                if (whiteList.contains(pattern)) {
                    return;
                }
            }
        }
        throw new SecurityException("You have no privilege.");
    }
    
    protected int getEnvId(String env) {
        Environment environment = envService.findEnvByName(env);
        return environment==null ? -1 : environment.getId();
    }
}
