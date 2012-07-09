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

import javax.servlet.jsp.JspException;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ProjectNavigator extends StrutsTagSupport {

	public ProjectNavigator() {
		setTemplateName("project-nav.ftl");
	}
	
	@Override
	protected int doFinalStartTag() throws JspException {
		// TODO Auto-generated method stub
		return SKIP_BODY;
	}

}
