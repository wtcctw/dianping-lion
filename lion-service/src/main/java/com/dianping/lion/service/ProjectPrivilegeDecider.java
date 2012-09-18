/**
 * 
 */
package com.dianping.lion.service;

import com.dianping.lion.entity.User;



/**
 * @author danson.liu
 *
 */
public interface ProjectPrivilegeDecider {
    
    boolean hasAddConfigPrivilege(int projectId, int envId, Integer userId);

    boolean hasReadConfigPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasEditConfigPrivilege(int projectId, int envId, int configId, Integer userId);
    
    boolean hasLockConfigPrivilege(Integer userId);
    
    boolean hasReadApplogPrivilege(int projectId, Integer userId);

	boolean hasEditConfigAttrPrivilege(int projectId, Integer userId);
	
	boolean hasReadDSFetchLogPrivilege(Integer userId);
	
	boolean hasManageProjectMemberPrivilege(int projectId, User user);
	
}
