/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-13
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.service;

import java.util.List;

import com.dianping.lion.entity.Privilege;
import com.dianping.lion.entity.PrivilegeCategory;
import com.dianping.lion.entity.Resource;
import com.dianping.lion.entity.Role;
import com.dianping.lion.entity.User;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.RoleUserCriteria;

/**
 * @author danson.liu
 *
 */
public interface PrivilegeService {
	
	List<Role> getRoles();

	List<PrivilegeCategory> getPrivilegeCategories();

	List<Privilege> getRolePrivileges(int roleId);

	void saveRolePrivilege(int roleId, List<Integer> privilegeIds);

	int createRole(Role role);

	Role getRole(int roleId);

	int updateRole(Role role);

	void deleteRole(int roleId);

	Resource getResourceMatchUrl(String url);

	boolean isUserHasResourcePrivilege(Integer userId, String resource);
	
	boolean isUserHasResourcePrivilege(Integer userId, int resourceId);

	Resource findResourceByCode(String resourceCode);

	Paginater<User> paginateRoleUsers(RoleUserCriteria userCriteria, Paginater<User> paginater);

	void addRole2User(int roleId, int userId);

	void deleteRoleFromUser(int roleId, int userId);

}
