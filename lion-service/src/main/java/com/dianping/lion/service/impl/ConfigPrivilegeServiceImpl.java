/**
 * 
 */
package com.dianping.lion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ConfigPrivilegeService;
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
    private UserService userService;

    @Override
    public boolean hasLookPrivilege(int projectId, int envId, Integer userId) {
        Environment environment = environmentService.loadEnvByID(envId);
        if (environment.isOnline()) {
            if (userId != null) {
                User user = userService.loadById(userId);
                if (user.isAdmin() || user.isSA() || user.isSystem()) {
                    return true;
                }
                Project project = projectService.loadProject(projectId);
                Integer managerId = project.getManagerId();
                Integer techLeaderId = project.getTechLeaderId();
                if (managerId == userId || techLeaderId == userId) {
                    return true;
                }
                boolean isMember = projectService.isMember(projectId, userId);
                if (isMember) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasEditPrivilege(int projectId, int envId, Integer userId) {
        if (userId == null) {
            return false;
        }
        User user = userService.loadById(userId);
        if (user.isAdmin() || user.isSystem()) {
            return true;
        }
        Environment environment = environmentService.loadEnvByID(envId);
        Project project = projectService.loadProject(projectId);
        Integer managerId = project.getManagerId();
        Integer techLeaderId = project.getTechLeaderId();
        if (managerId == userId || techLeaderId == userId) {
            return true;
        }
        if (environment.isOnline()) {
            if (user.isSA()) {
                return true;
            }
        } else {
            boolean isMember = projectService.isMember(projectId, userId);
            if (isMember || user.isSCM()) {
                return true;
            }
        }
        return false;
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

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
