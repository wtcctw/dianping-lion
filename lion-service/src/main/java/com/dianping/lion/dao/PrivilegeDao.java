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
package com.dianping.lion.dao;

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
public interface PrivilegeDao {

	List<PrivilegeCategory> getPrivilegeCategories();

	List<Role> getRoles();

	List<Privilege> getRolePrivileges(int roleId);

	void deleteRolePrivileges(int roleId);

	void insertRolePrivileges(int roleId, List<Integer> privilegeIds);

	int createRole(Role role);

	Role getRole(int roleId);

	int updateRole(Role role);

	void deleteUserRoleRelation(int roleId);

	void deleteRolePrivilegeRelation(int roleId);

	void deleteRole(int roleId);

	List<Resource> getAllResource();

	List<Resource> getUserResources(int userId);

	Resource findResourceByCode(String resourceCode);

	long getUserCount(RoleUserCriteria criteria);

	List<User> getUserList(RoleUserCriteria criteria, Paginater<User> paginater);

	void addRole2User(int roleId, int userId);

	void deleteRoleFromUser(int roleId, int userId);

}
