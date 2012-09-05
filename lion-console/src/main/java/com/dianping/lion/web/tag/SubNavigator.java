/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-11
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

import javax.servlet.jsp.JspException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ConsoleConstants;
import com.dianping.lion.service.PrivilegeDecider;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.web.tag.MenuManager.Menu;
import com.dianping.lion.web.tag.MenuManager.MenuGroup;
import com.dianping.lion.web.tag.MenuManager.SubMenu;

/**
 * 二级导航菜单
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class SubNavigator extends StrutsTagSupport {
	
	private Menu mainMenu;
	
	private String query;	//include menu if exists
	
	@Autowired
	private PrivilegeDecider privilegeDecider;
	
	public SubNavigator() {
		setTemplateName("sub-nav.ftl");
	}

	@Override
	protected int doFinalStartTag() throws JspException {
		String menu = getRequest().getParameter("menu");
		if (menu != null) {
			mainMenu = MenuManager.getNavMenus().getMenu(menu);
			mainMenu = filterWithPrivilege(mainMenu);
			query = "menu=" + menu;
			if (ConsoleConstants.MENU_PROJECT.equals(menu)) {
				query += "&pid=" + getRequest().getParameter("pid");
			}
		}
		return SKIP_BODY;
	}

	private Menu filterWithPrivilege(Menu menu) {
		if (menu == null) {
			return null;
		}
		try {
			Menu cloned = (Menu) menu.clone();
			Iterator<Object> iterator = cloned.subMenuOrGroups.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof SubMenu) {
					if (hasPrivilege((SubMenu) next) < 0) {
						iterator.remove();
					}
				} else if (next instanceof MenuGroup) {
					if (hasPrivilege((MenuGroup) next) < 0) {
						iterator.remove();
					}
				}
			}
			return cloned;
		} catch (Exception e) {
			logger.error("Generate sub navigator failed.", e);
			throw new RuntimeException("Generate sub navigator failed.", e);
		}
	}

	private int hasPrivilege(MenuGroup menuGroup) {
		if (menuGroup.menuOrGroups.isEmpty()) {
			return -1;
		}
		int menuSize = menuGroup.menuOrGroups.size();
		Iterator<Object> iterator = menuGroup.menuOrGroups.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof SubMenu) {
				int hasPrivilege = hasPrivilege((SubMenu) next);
				if (hasPrivilege < 0) {
					iterator.remove();
					menuSize--;
				} else if (hasPrivilege == 0) {
					menuSize--;
				}
			} else if (next instanceof MenuGroup) {
				int hasPrivilege = hasPrivilege((MenuGroup) next);
				if (hasPrivilege < 0) {
					iterator.remove();
					menuSize--;
				}
			}
			//二级菜单中没有Menu
		}
		return menuSize > 0 ? 1 : -1;
	}

	private int hasPrivilege(SubMenu subMenu) {
		if (subMenu.seprator) {
			return 0;
		}
		if (ConsoleConstants.MENU_APPLOG.equals(subMenu.name)) {
			String pid = getRequest().getParameter("pid");
			Integer projectId = pid != null ? Integer.parseInt(pid) : null;
			if (projectId != null) {
				boolean hasReadLogPrivilege = privilegeDecider.hasReadApplogPrivilege(projectId, SecurityUtils.getCurrentUserId());
				return hasReadLogPrivilege ? 1 : -1;
			}
			return -1;
		}
		return 1;
	}

	public Menu getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public void setPrivilegeDecider(PrivilegeDecider projectPrivilegeDecider) {
		this.privilegeDecider = projectPrivilegeDecider;
	}

}
