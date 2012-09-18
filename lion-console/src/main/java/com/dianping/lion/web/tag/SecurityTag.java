/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-9-16
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

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.service.PrivilegeService;
import com.dianping.lion.util.SecurityUtils;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * TODO Comment of SecurityTag
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class SecurityTag extends StrutsTagSupport {
	
	private String resource;
	
	private String var;
	
	@Autowired
	private PrivilegeService privilegeService;

	@Override
	protected int doFinalStartTag() throws JspException {
		boolean hasResourcePrivilege = privilegeService.isUserHasResourcePrivilege(SecurityUtils.getCurrentUserId(), resource);;
		if (var != null) {
			ValueStack stack = getStack();
			if (stack != null) {
				stack.getContext().put(var, hasResourcePrivilege);
				stack.setValue("#attr['" + var + "']", hasResourcePrivilege, false);
			}
		}
		if (hasResourcePrivilege) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

}
