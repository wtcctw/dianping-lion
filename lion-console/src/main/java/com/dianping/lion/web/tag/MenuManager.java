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

/**
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

	public static class NavMenus {
		
		public List<Object> menuOrGroups = new ArrayList<Object>();

		public void addMenu(Menu menu) {
			menuOrGroups.add(menu);
		}

		public void addGroup(MenuGroup group) {
			menuOrGroups.add(group);
		}
		
		public boolean hasProjectMenu() {
			for (Object item : menuOrGroups) {
				if ((item instanceof Menu) && "project".equals(((Menu) item).name)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	public static class MenuGroup {
		public String label;
		public List<Object> menuOrGroups = new ArrayList<Object>();	//maybe group, menu*|submenu*
		public void addGroup(MenuGroup group) {
			menuOrGroups.add(group);
		}
		public void addMenu(Menu menu) {
			menuOrGroups.add(menu);
		}
		public void addSubMenu(SubMenu subMenu) {
			menuOrGroups.add(subMenu);
		}
	}
	
	public static class Menu {
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
		public boolean hasSubMenu() {
			return !subMenuOrGroups.isEmpty();	//不对空的group进行判断
		}
	}
	
	public static class SubMenu {
		public String name;
		public String label;
		public String url;
		public boolean seprator;
	}
	
}

