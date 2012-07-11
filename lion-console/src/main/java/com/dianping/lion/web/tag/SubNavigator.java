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
	
	private String menu;
	
	private Menu mainMenu;
	
	private String query;	//include menu if exists
	
	public SubNavigator() {
		setTemplateName("sub-nav.ftl");
	}

	@Override
	protected int doFinalStartTag() throws JspException {
		if (menu != null) {
			mainMenu = MenuManager.getNavMenus().getMenu(menu);
//			query = "menu=" + ;
			if (MenuManager.MENU_PROJECT.equals(menu)) {
//				query
			}
		}
		return SKIP_BODY;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Menu getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

}
