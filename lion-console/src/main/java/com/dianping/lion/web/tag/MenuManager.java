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

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dianping.lion.ConsoleConstants;

/**
 * 一级导航菜单
 * @author danson.liu
 *
 */
public class MenuManager {

	private static final String TAG_GROUP = "group";
	private static final String TAG_MENU = "menu";
	private static final String TAG_SUBMENU = "sub-menu";
	
	private static NavMenus navMenus;
	
	static {
		loadNavMenus();
	}
	
	public static NavMenus getNavMenus() {
		if (navMenus == null || ConsoleConstants.isDevMode()) {
			synchronized (MenuManager.class) {
				if (navMenus == null || ConsoleConstants.isDevMode()) {
					loadNavMenus();
				}
			}
		}
		return navMenus;
	}
	
	private static void loadNavMenus() {
		try {
			NavMenus navMenus = new NavMenus();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/other/menus.xml"));
			NodeList childNodes = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item instanceof Element) {
					Element element = (Element) item;
					if (TAG_MENU.equals(element.getTagName())) {
						navMenus.addMenu(parseMenu(element));
					} else if (TAG_GROUP.equals(element.getTagName())) {
						navMenus.addGroup(parseGroup(element));
					}
				}
			}
			MenuManager.navMenus = navMenus;
		} catch (Exception e) {
			throw new RuntimeException("Load nav-menu config failed.", e);
		}
	}

	private static MenuGroup parseGroup(Element element) {
		MenuGroup group = new MenuGroup();
		group.label = element.getAttribute("label");
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				Element childEle = (Element) item;
				if (TAG_GROUP.equals(childEle.getTagName())) {
					group.addGroup(parseGroup(childEle));
				} else if (TAG_MENU.equals(childEle.getTagName())) {
					group.addMenu(parseMenu(childEle));
				} else if (TAG_SUBMENU.equals(childEle.getTagName())) {
					group.addSubMenu(parseSubMenu(childEle));
				}
			}
		}
		return group;
	}

	private static Menu parseMenu(Element element) {
		Menu menu = new Menu();
		menu.name = element.getAttribute("name");
		menu.label = element.getAttribute("label");
		menu.url = element.getAttribute("url");
		menu.seprator = "true".equals(element.getAttribute("sperator"));
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				Element childEle = (Element) item;
				if (TAG_GROUP.equals(childEle.getTagName())) {
					menu.addGroup(parseGroup(childEle));
				} else if (TAG_SUBMENU.equals(childEle.getTagName())) {
					menu.addSubMenu(parseSubMenu(childEle));
				}
			}
		}
		return menu;
	}

	private static SubMenu parseSubMenu(Element element) {
		SubMenu subMenu = new SubMenu();
		subMenu.name = element.getAttribute("name");
		subMenu.label = element.getAttribute("label");
		subMenu.url = element.getAttribute("url");
		subMenu.seprator = "true".equals(element.getAttribute("sperator"));
		return subMenu;
	}

	public static class NavMenus implements Cloneable {
		
		public List<Object> menuOrGroups = new ArrayList<Object>();

		public void addMenu(Menu menu) {
			menuOrGroups.add(menu);
		}

		public void addGroup(MenuGroup group) {
			menuOrGroups.add(group);
		}
		
		public boolean hasProjectMenu() {
			for (Object item : menuOrGroups) {
				if ((item instanceof Menu) && ConsoleConstants.MENU_PROJECT.equals(((Menu) item).name)) {
					return true;
				}
			}
			return false;
		}
		
		public SubMenu getDefaultAccessiableSubMenu(String menu) {
			Menu menuObj = getMenu(menu);
			if (menuObj == null) {
				return null;
			}
			return menuObj.getDefaultSubMenu();
		}
		
		public Menu getMenu(String menu) {
			for (Object menuOrGroup : menuOrGroups) {
				if (menuOrGroup instanceof MenuGroup) {
					MenuGroup menuGroup = (MenuGroup) menuOrGroup;
					Menu menu_ = menuGroup.getMenu(menu);
					if (menu_ != null) {
						return menu_;
					}
				} else if (menuOrGroup instanceof Menu) {
					if (menu.equals(((Menu) menuOrGroup).name)) {
						return (Menu) menuOrGroup;
					}
				}
			}
			return null;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			NavMenus cloned = new NavMenus();
			List<Object> menuOrGroups = new ArrayList<Object>();
			for (Object menuOrGroup : this.menuOrGroups) {
				if (menuOrGroup instanceof Menu) {
					menuOrGroups.add(((Menu) menuOrGroup).clone());
				} else if (menuOrGroup instanceof MenuGroup) {
					menuOrGroups.add(((MenuGroup) menuOrGroup).clone());
				}
			}
			cloned.menuOrGroups = menuOrGroups;
			return cloned;
		}
		
	}
	
	public static class MenuGroup implements Cloneable {
		public String label;
		public List<Object> menuOrGroups = new ArrayList<Object>();	//maybe group, menu*|submenu*
		
		public void addGroup(MenuGroup group) {
			menuOrGroups.add(group);
		}
		public Menu getMenu(String menu) {
			for (Object menuOrGroup : menuOrGroups) {
				if (menuOrGroup instanceof MenuGroup) {
					Menu menuObj = ((MenuGroup) menuOrGroup).getMenu(menu);
					if (menuObj != null) {
						return menuObj;
					}
				} else if (menuOrGroup instanceof Menu) {
					if (menu.equals(((Menu) menuOrGroup).name)) {
						return (Menu) menuOrGroup;
					}
				}
			}
			return null;
		}
		public void addMenu(Menu menu) {
			menuOrGroups.add(menu);
		}
		public void addSubMenu(SubMenu subMenu) {
			menuOrGroups.add(subMenu);
		}
		public SubMenu getDefaultSubMenu() {
			for (Object menuOrGroup : menuOrGroups) {
				if (menuOrGroup instanceof SubMenu) {
					return (SubMenu) menuOrGroup;
				} else if (menuOrGroup instanceof MenuGroup) {
					SubMenu subMenu = ((MenuGroup) menuOrGroup).getDefaultSubMenu();
					if (subMenu != null) {
						return subMenu;
					}
				}
			}
			return null;
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			MenuGroup cloned = new MenuGroup();
			cloned.label = this.label;
			List<Object> menuOrGroups = new ArrayList<Object>();
			if (this.menuOrGroups != null) {
				for (Object menuOrGroup : this.menuOrGroups) {
					if (menuOrGroup instanceof Menu) {
						menuOrGroups.add(((Menu) menuOrGroup).clone());
					} else if (menuOrGroup instanceof MenuGroup) {
						menuOrGroups.add(((MenuGroup) menuOrGroup).clone());
					} else if (menuOrGroup instanceof SubMenu) {
						menuOrGroups.add(menuOrGroup);
					}
				}
			}
			cloned.menuOrGroups = menuOrGroups;
			return cloned;
		}
	}
	
	public static class Menu implements Cloneable {
		public String name;
		public String label;
		public String url;
		public boolean seprator;
		public List<Object> subMenuOrGroups = new ArrayList<Object>();	//maybe group, submenu
		public void addGroup(MenuGroup group) {
			subMenuOrGroups.add(group);
		}
		public void addSubMenu(SubMenu subMenu) {
			subMenuOrGroups.add(subMenu);
		}
		public SubMenu getDefaultSubMenu() {
			for (Object subMenuOrGroup : subMenuOrGroups) {
				if (subMenuOrGroup instanceof SubMenu) {
					return (SubMenu) subMenuOrGroup;
				} else if (subMenuOrGroup instanceof MenuGroup) {
					SubMenu subMenu = ((MenuGroup) subMenuOrGroup).getDefaultSubMenu();
					if (subMenu != null) {
						return subMenu;
					}
				}
			}
			return null;
		}
		public boolean hasSubMenu() {
			return !subMenuOrGroups.isEmpty();	//不对空的group进行判断
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			Menu cloned = new Menu();
			cloned.name = this.name;
			cloned.label = this.label;
			cloned.url = this.url;
			cloned.seprator = this.seprator;
			List<Object> subMenuOrGroups = new ArrayList<Object>();
			if (this.subMenuOrGroups != null) {
				for (Object menuOrGroup : this.subMenuOrGroups) {
					if (menuOrGroup instanceof MenuGroup) {
						subMenuOrGroups.add(((MenuGroup) menuOrGroup).clone());
					} else {
						subMenuOrGroups.add(menuOrGroup);
					}
				}
			}
			cloned.subMenuOrGroups = subMenuOrGroups;
			return cloned;
		}
	}
	
	public static class SubMenu {
		public String name;
		public String label;
		public String url;
		public boolean seprator;
	}
	
}

