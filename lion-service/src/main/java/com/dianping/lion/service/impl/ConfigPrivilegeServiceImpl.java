/**
 * 
 */
package com.dianping.lion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ConfigPrivilegeService;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.UserService;

/**
 * @author danson.liu
 *
 */
public class ConfigPrivilegeServiceImpl implements ConfigPrivilegeService {
    
    @Autowired
    private EnvironmentService environmentService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private UserService userService;

    @Override
    public boolean hasReadPrivilege(int projectId, int envId, int configId, Integer userId) {
        Environment environment = environmentService.loadEnvByID(envId);
        User user = userId != null ? userService.loadById(userId) : null;
        if (user != null && (user.isAdmin() || user.isSystem() || user.isSA())) {
            return true;
        }
        if (environment != null) {
            if (environment.isOnline()) {
                if (user != null) {
                    Config config = configService.getConfig(configId);
                    if (config != null && !config.isPrivatee()) {
                        return user.isOnlineConfigView() || projectService.isMember(projectId, userId) || projectService.isOwner(projectId, userId)
                                        || projectService.isOperator(projectId, userId);
                    }
                    return false;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasAddPrivilege(int projectId, int envId, Integer userId) {
        if (userId == null) {
            return false;
        }
        Environment environment = environmentService.loadEnvByID(envId);
        User user = userService.loadById(userId);
        if (user != null && (user.isAdmin() || user.isSystem() || user.isSA())) {
            return true;
        }
        if (environment != null) {
            if (environment.isOnline()) {
                return projectService.isOwner(projectId, userId) || projectService.isOperator(projectId, userId);
            } else {
                return projectService.isMember(projectId, userId) || projectService.isOwner(projectId, userId) 
                            || projectService.isOperator(projectId, userId);
            }
        }
        return false;
    }

    @Override
    public boolean hasEditPrivilege(int projectId, int envId, int configId, Integer userId) {
        if (userId == null) {
            return false;
        }
        Environment environment = environmentService.loadEnvByID(envId);
        User user = userService.loadById(userId);
        if (user != null && (user.isAdmin() || user.isSystem() || user.isSA())) {
            return true;
        }
        if (environment != null) {
            if (environment.isOnline()) {
                Config config = configService.getConfig(configId);
                if (config != null && !config.isPrivatee()) {
                    return projectService.isOwner(projectId, userId) || projectService.isOperator(projectId, userId);
                }
                return false;
            } else {
                return projectService.isMember(projectId, userId) || projectService.isOwner(projectId, userId) 
                            || projectService.isOperator(projectId, userId);
            }
        }
        return false;
    }

    @Override
    public boolean hasLockPrivilege(Integer userId) {
        if (userId == null) {
            return false;
        }
        User user = userService.loadById(userId);
        return user.isAdmin() || user.isSA();
    }

    /**
     * @param environmentService the environmentService to set
     */
    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    /**
     * @param projectService the projectService to set
     */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
