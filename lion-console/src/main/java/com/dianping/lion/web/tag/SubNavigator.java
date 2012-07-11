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

import javax.servlet.jsp.JspException;

import com.dianping.lion.web.tag.MenuManager.Menu;

/**
 * 二级导航菜单
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class SubNavigator extends StrutsTagSupport {
	
	private Menu mainMenu;
	
	private String query;	//include menu if exists
	
	public SubNavigator() {
		setTemplateName("sub-nav.ftl");
	}

	@Override
	protected int doFinalStartTag() throws JspException {
		String menu = getRequest().getParameter("menu");
		if (menu != null) {
			mainMenu = MenuManager.getNavMenus().getMenu(menu);
			query = "menu=" + menu;
			if (MenuManager.MENU_PROJECT.equals(menu)) {
				query += "&pid=" + getRequest().getParameter("pid");
			}
		}
		return SKIP_BODY;
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

}
