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
package com.dianping.lion.web.tag;

import java.util.List;

/**
 * @author danson.liu
 *
 */
public class MenuManager {

	private static NavMenus navMenus;
	
	static {
		loadNavMenus();
	}
	
	public static NavMenus getNavMenus() {
		if (navMenus == null) {
			synchronized (MenuManager.class) {
				if (navMenus == null) {
					loadNavMenus();
				}
			}
		}
		return navMenus;
	}
	
	private static void loadNavMenus() {
		// TODO Auto-generated method stub
		
	}

	public static class NavMenus {
		
		private List<Object> menuOrGroups;
		
	}
	
	public static class MenuGroup {
		public String label;
		public List<Menu> menus;
	}
	
	public static class Menu {
		public String name;
		public String label;
		public String url;
		private List<Object> subMenuOrGroups;
	}
	
	public static class SubMenu {
		public String name;
		public String label;
		public String url;
	}
	
}

