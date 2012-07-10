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

import java.util.List;

import javax.servlet.jsp.JspException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.web.tag.MenuManager.NavMenus;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class MainNavigator extends StrutsTagSupport {
	
	@Autowired
	private ProjectService projectService;
	
	private List<Team> teams;
	
	private NavMenus navMenus;
	
	public MainNavigator() {
		setTemplateName("main-nav.ftl");
	}
	
	@Override
	protected int doFinalStartTag() throws JspException {
		this.navMenus = MenuManager.getNavMenus();
		if (navMenus.hasProjectMenu()) {
			this.teams = projectService.getTeams();
		}
		return SKIP_BODY;
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

}
