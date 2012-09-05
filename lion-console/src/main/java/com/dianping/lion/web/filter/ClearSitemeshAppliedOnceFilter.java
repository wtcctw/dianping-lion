/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-9-4
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
package com.dianping.lion.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author danson.liu
 *
 */
public class ClearSitemeshAppliedOnceFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
	throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request; 
        request.removeAttribute("com.opensymphony.sitemesh.APPLIED_ONCE"); 
        chain.doFilter(servletRequest, response); 
	}

	@Override
	public void destroy() {
	}

}
