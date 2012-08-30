/**
 * 
 */
package com.dianping.lion.service;


/**
 * @author danson.liu
 *
 */
public interface ConfigPrivilegeService {
    
    boolean hasAddPrivilege(int projectId, int envId, Integer userId);

    boolean hasReadPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasEditPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasLockPrivilege(Integer userId);
    
}
