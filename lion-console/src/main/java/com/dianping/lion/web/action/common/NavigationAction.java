/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-10
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
package com.dianping.lion.web.action.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.dianping.lion.util.UrlUtils;
import com.dianping.lion.web.tag.MenuManager;
import com.dianping.lion.web.tag.MenuManager.SubMenu;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class NavigationAction extends ActionSupport implements ServletRequestAware {

	private String menu;
	
	private String redirectUrl;

	private HttpServletRequest request;

	@SuppressWarnings("unchecked")
	public String execute() {
		SubMenu subMenu = MenuManager.getNavMenus().getDefaultAccessiableSubMenu(menu);
		String subMenuUrl = subMenu.url;
		redirectUrl = UrlUtils.resolveUrl(subMenuUrl, request.getParameterMap());
		return SUCCESS;
	}

	/**
	 * @return the menu
	 */
	public String getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(String menu) {
		this.menu = menu;
	}

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
