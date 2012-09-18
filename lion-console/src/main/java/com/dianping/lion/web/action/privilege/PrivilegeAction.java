/**
 * Project: com.dianping.lion.lion-console-0.0.1
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
package com.dianping.lion.web.action.privilege;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Privilege;
import com.dianping.lion.entity.PrivilegeCategory;
import com.dianping.lion.entity.Role;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.PrivilegeService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.RoleUserCriteria;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * TODO Comment of PrivilegeAction
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class PrivilegeAction extends AbstractLionAction {
	
	@Autowired
	private PrivilegeService privilegeService;
	
	private List<Role> roles;
	
	private List<PrivilegeCategory> categories;
	
	private List<Integer> privilegeIds;
	
	private int roleId;
	
	private Role role;
	
	private int userId;
	
	private RoleUserCriteria userCriteria = new RoleUserCriteria();
	private Paginater<User> paginater = new Paginater<User>(15);
	
	protected void checkModulePrivilege() {
		if (!privilegeService.isUserHasResourcePrivilege(SecurityUtils.getCurrentUserId(), ServiceConstants.RES_CODE_SECURITY)) {
			throw NoPrivilegeException.INSTANCE;
		}
	}
	
	public String list() throws Exception {
		this.roles = privilegeService.getRoles();
		return SUCCESS;
	}
	
	public String createRole() {
		try {
			privilegeService.createRole(role);
			createSuccessStreamResponse();
		} catch (RuntimeException e) {
			logger.error("Create role[" + role.getName() + "] failed.", e);
			createErrorStreamResponse();
		}
		return SUCCESS;
	}
	
	public String updateRole() {
		Role role = privilegeService.getRole(roleId);
		if (role != null) {
			try {
				role.setName(this.role.getName());
				role.setRemark(this.role.getRemark());
				privilegeService.updateRole(role);
				createSuccessStreamResponse();
			} catch (RuntimeException e) {
				createErrorStreamResponse("编辑角色失败.");
			}
		} else {
			createErrorStreamResponse("该角色已不存在!");
		}
		return SUCCESS;
	}
	
	public String deleteRole() {
		try {
			privilegeService.deleteRole(roleId);
			createSuccessStreamResponse();
		} catch (RuntimeException e) {
			createErrorStreamResponse();
		}
		return SUCCESS;
	}
	
	public String roleList() {
		this.roles = privilegeService.getRoles();
		return SUCCESS;
	}
	
	public String rolePrivilegeList() {
		this.categories = privilegeService.getPrivilegeCategories();
		List<Privilege> privileges = privilegeService.getRolePrivileges(roleId);
		this.privilegeIds = new ArrayList<Integer>();
		for (Privilege privilege : privileges) {
			this.privilegeIds.add(privilege.getId());
		}
		return SUCCESS;
	}
	
	public String roleUserList() {
		this.role = privilegeService.getRole(userCriteria.getRoleId());
		paginater = privilegeService.paginateRoleUsers(userCriteria, paginater);
		return SUCCESS;
	}
	
	public String addRoleUser() {
		privilegeService.addRole2User(userCriteria.getRoleId(), userId);
		this.role = privilegeService.getRole(userCriteria.getRoleId());
		paginater = privilegeService.paginateRoleUsers(userCriteria, paginater);
		return SUCCESS;
	}
	
	public String deleteRoleUser() {
		privilegeService.deleteRoleFromUser(userCriteria.getRoleId(), userId);
		this.role = privilegeService.getRole(userCriteria.getRoleId());
		paginater = privilegeService.paginateRoleUsers(userCriteria, paginater);
		return SUCCESS;
	}
	
	public String saveRolePrivileges() {
		try {
			privilegeService.saveRolePrivilege(roleId, privilegeIds);
			createSuccessStreamResponse();
		} catch (RuntimeException e) {
			logger.error("Save role[id=" + roleId + "] privilege failed.", e);
			createErrorStreamResponse();
		}
		return SUCCESS;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<PrivilegeCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<PrivilegeCategory> categories) {
		this.categories = categories;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Integer> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<Integer> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public Paginater<User> getPaginater() {
		return paginater;
	}

	public void setPaginater(Paginater<User> paginater) {
		this.paginater = paginater;
	}

	public RoleUserCriteria getUserCriteria() {
		return userCriteria;
	}

	public void setUserCriteria(RoleUserCriteria userCriteria) {
		this.userCriteria = userCriteria;
	}

}
