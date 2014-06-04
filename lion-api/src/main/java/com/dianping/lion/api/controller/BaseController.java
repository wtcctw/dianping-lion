package com.dianping.lion.api.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.api.exception.SecurityException;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.SecurityUtils;

public class BaseController {
    
    @Autowired
    protected OperationLogService opLogService;
    
    @Autowired
    protected UserService userService;
    
    @Autowired
    protected EnvironmentService envService;
    
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
    
    protected int getEnvId(String env) {
        Environment environment = envService.findEnvByName(env);
        return environment==null ? -1 : environment.getId();
    }
}
