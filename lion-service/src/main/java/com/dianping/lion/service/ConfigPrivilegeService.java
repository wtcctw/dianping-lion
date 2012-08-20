/**
 * 
 */
package com.dianping.lion.service;

/**
 * @author danson.liu
 *
 */
public interface ConfigPrivilegeService {

    boolean hasLookPrivilege(int projectId, int envId, Integer userId);

    boolean hasEditPrivilege(int projectId, int envId, Integer userId);

}
