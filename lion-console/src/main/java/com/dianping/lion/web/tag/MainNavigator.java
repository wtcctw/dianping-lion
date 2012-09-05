/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-8
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
package com.dianping.lion.web.tag;

import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ConsoleConstants;
import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.PrivilegeDecider;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.tag.MenuManager.Menu;
import com.dianping.lion.web.tag.MenuManager.MenuGroup;
import com.dianping.lion.web.tag.MenuManager.NavMenus;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class MainNavigator extends StrutsTagSupport {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PrivilegeDecider privilegeDecider;
	
	private List<Team> teams;
	
	private NavMenus navMenus;
	
	public MainNavigator() {
		setTemplateName("main-nav.ftl");
	}
	
	@Override
	protected int doFinalStartTag() throws JspException {
		this.navMenus = MenuManager.getNavMenus();
		this.navMenus = filterWithPrivilege(this.navMenus);
		if (navMenus.hasProjectMenu()) {
			this.teams = projectService.getTeams();
		}
		return SKIP_BODY;
	}


	private NavMenus filterWithPrivilege(NavMenus navMenus) {
		if (navMenus == null) {
			return null;
		}
		try {
			NavMenus cloned = (NavMenus) navMenus.clone();
			Iterator<Object> iterator = cloned.menuOrGroups.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof MenuGroup) {
					if (hasPrivilege((MenuGroup) next) < 0) {
						iterator.remove();
					}
				} else if (next instanceof Menu) {
					if (hasPrivilege((Menu) next) < 0) {
						iterator.remove();
					}
				}
			}
			return cloned;
		} catch (Exception e) {
			logger.error("Generate main navigator failed.", e);
			throw new RuntimeException("Generate main navigator failed.", e);
		}
	}

	private int hasPrivilege(Menu menu) {
		if (menu.seprator) {
			return 0;
		}
		String menuName = menu.name;
		if (ConsoleConstants.MENU_PROJECT.equals(menuName)) {
			return 1;
		}
		Integer currentUserId = SecurityUtils.getCurrentUserId();
		// TODO 需要调整为功能级别的权限，暂时交由admin管理
		if ("envsetting".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_ENV, currentUserId) ? 1 : -1;
		} 
		if ("security".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_SECURITY, currentUserId) ? 1 : -1;
		}
		if ("jobmanagement".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_JOB, currentUserId) ? 1 : -1;
		} 
		if ("syssetting".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_SETTING, currentUserId) ? 1 : -1;
		}
		if ("oplog".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_OPLOG, currentUserId) ? 1 : -1;
		} 
		if ("cachemanagement".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_CACHE, currentUserId) ? 1 : -1;
		} 
		if ("projectconfig".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_PROJECT, currentUserId) ? 1 : -1;
		}
		if ("usersetting".equals(menuName)) {
			return privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_USER, currentUserId) ? 1 : -1;
		}
		return -1;
	}

	private int hasPrivilege(MenuGroup menuGroup) {
		if (menuGroup.menuOrGroups.isEmpty()) {
			return -1;
		}
		int menuSize = menuGroup.menuOrGroups.size();
		Iterator<Object> iterator = menuGroup.menuOrGroups.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof MenuGroup) {
				int hasPrivilege = hasPrivilege((MenuGroup) next);
				if (hasPrivilege < 0) {
					iterator.remove();
					menuSize--;
				}
			} else if (next instanceof Menu) {
				int hasPrivilege = hasPrivilege((Menu) next);
				if (hasPrivilege < 0) {
					iterator.remove();
					menuSize--;
				} else if (hasPrivilege == 0) {
					menuSize--;
				}
			}
		}
		return menuSize > 0 ? 1 : -1;
	}

	/**
	 * @return the teams
	 */
	public List<Team> getTeams() {
		return teams;
	}

	/**
	 * @return the navMenus
	 */
	public NavMenus getNavMenus() {
		return navMenus;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setPrivilegeDecider(PrivilegeDecider privilegeDecider) {
		this.privilegeDecider = privilegeDecider;
	}

}
