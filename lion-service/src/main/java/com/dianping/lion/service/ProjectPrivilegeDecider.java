/**
 * 
 */
package com.dianping.lion.service;



/**
 * @author danson.liu
 *
 */
public interface ProjectPrivilegeDecider {
    
    boolean hasAddConfigPrivilege(int projectId, int envId, Integer userId);

    boolean hasReadConfigPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasEditConfigPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasLockConfigPrivilege(Integer userId);
    
    boolean hasReadLogPrivilege(int projectId, Integer userId);
    
}
