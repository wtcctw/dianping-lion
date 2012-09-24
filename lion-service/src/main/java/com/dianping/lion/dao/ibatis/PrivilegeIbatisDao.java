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
package com.dianping.lion.dao.ibatis;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.PrivilegeDao;
import com.dianping.lion.entity.Privilege;
import com.dianping.lion.entity.PrivilegeCategory;
import com.dianping.lion.entity.Resource;
import com.dianping.lion.entity.Role;
import com.dianping.lion.entity.User;
import com.dianping.lion.util.Maps;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.RoleUserCriteria;

/**
 * @author danson.liu
 *
 */
public class PrivilegeIbatisDao extends SqlMapClientDaoSupport implements PrivilegeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PrivilegeCategory> getPrivilegeCategories() {
		return getSqlMapClientTemplate().queryForList("Privilege.getPrivilegeCategories");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoles() {
		return getSqlMapClientTemplate().queryForList("Privilege.getRoles");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Privilege> getRolePrivileges(int roleId) {
		return getSqlMapClientTemplate().queryForList("Privilege.getRolePrivileges", roleId);
	}

	@Override
	public void deleteRolePrivileges(int roleId) {
		getSqlMapClientTemplate().delete("Privilege.deleteRolePrivileges", roleId);
	}

	@Override
	public void insertRolePrivileges(int roleId, List<Integer> privilegeIds) {
		if (CollectionUtils.isNotEmpty(privilegeIds)) {
			getSqlMapClientTemplate().insert("Privilege.insertRolePrivileges", Maps.entry("roleId", roleId)
				.entry("privilegeIds", privilegeIds).get());
		}
	}

	@Override
	public int createRole(Role role) {
		return (Integer) getSqlMapClientTemplate().insert("Privilege.insertRole", role);
	}

	@Override
	public Role getRole(int roleId) {
		return (Role) getSqlMapClientTemplate().queryForObject("Privilege.getRole", roleId);
	}

	@Override
	public int updateRole(Role role) {
		return getSqlMapClientTemplate().update("Privilege.updateRole", role);
	}

	@Override
	public void deleteUserRoleRelation(int roleId) {
		getSqlMapClientTemplate().delete("Privilege.deleteUserRoleRelation", roleId);
	}

	@Override
	public void deleteRolePrivilegeRelation(int roleId) {
		getSqlMapClientTemplate().delete("Privilege.deleteRolePrivilegeRelation", roleId);
	}

	@Override
	public void deleteRole(int roleId) {
		getSqlMapClientTemplate().delete("Privilege.deleteRole", roleId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getAllResource() {
		return getSqlMapClientTemplate().queryForList("Privilege.getAllResource");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getUserResources(int userId) {
		return getSqlMapClientTemplate().queryForList("Privilege.getUserResources", userId);
	}

	@Override
	public Resource findResourceByCode(String resourceCode) {
		return (Resource) getSqlMapClientTemplate().queryForObject("Privilege.findResourceByCode", resourceCode);
	}

	@Override
	public long getUserCount(RoleUserCriteria criteria) {
		return (Long) getSqlMapClientTemplate().queryForObject("Privilege.getUserCount", Maps.entry("criteria", criteria).get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList(RoleUserCriteria criteria, Paginater<User> paginater) {
		return getSqlMapClientTemplate().queryForList("Privilege.getUserList", Maps.entry("criteria", criteria).entry("paginater", paginater).get());
	}

	@Override
	public void addRole2User(int roleId, int userId) {
		getSqlMapClientTemplate().insert("Privilege.addRole2User", Maps.entry("roleId", roleId).entry("userId", userId).get());
	}

	@Override
	public void deleteRoleFromUser(int roleId, int userId) {
		getSqlMapClientTemplate().delete("Privilege.deleteRoleFromUser", Maps.entry("roleId", roleId).entry("userId", userId).get());
	}

}
