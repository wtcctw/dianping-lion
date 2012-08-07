/**
 * Project: com.dianping.lion.lion-api-0.0.1
 * 
 * File Created at 2012-8-1
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
package com.dianping.lion.api.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dianping.lion.entity.User;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.SecurityUtils;

/**
 * TODO Comment of AbstractLionServlet
 * @author danson.liu
 *
 */
public abstract class AbstractLionServlet extends HttpServlet {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = -9154351276219364129L;
	
	public static final String 		PARAM_IDENTITY 	= "identity";
	public static final String 		PARAM_PASSWD 	= "passwd";
	public static final String 		PARAM_PROJECT 	= "project";	//项目名
	public static final String 		PARAM_ENV 		= "env";		//环境名
	public static final String 		PARAM_KEY 		= "key";		//配置名，不包含项目名前缀
	
	protected ApplicationContext 	applicationContext;
	protected ProjectService 		projectService;
	protected EnvironmentService 	environmentService;
	protected ConfigService 		configService;
	
	@Override
	public void init() throws ServletException {
		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		projectService = getBean(ProjectService.class);
		environmentService = getBean(EnvironmentService.class);
		configService = getBean(ConfigService.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getBean(Class<T> clazz) {
		return (T) BeanFactoryUtils.beanOfType(applicationContext, clazz);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}
	
	protected String getRequiredParameter(HttpServletRequest servletRequest, String param) {
		String paramVal = servletRequest.getParameter(param);
		if (paramVal == null) {
			throw new RuntimeBusinessException("Parameter[" + param + "] is required.");
		}
		return paramVal;
	}
	
	protected User getRequiredIdentity(HttpServletRequest servletRequest) {
		String identity = getRequiredParameter(servletRequest, PARAM_IDENTITY);
		int userId = 0;
		try {
			userId = Integer.parseInt(identity);
		} catch (NumberFormatException e) {
			throw new RuntimeBusinessException("Illegal format with identity parameter, must be integer.");
		}
		UserService userService = getBean(UserService.class);
		User user = userService.findById(userId);
		if (user == null) {
			throw new RuntimeBusinessException("User with identity[" + userId + "] not found.");
		}
		return user;
	}
	
	protected void checkUserPassword(HttpServletRequest servletRequest) {
		String passwd = getRequiredParameter(servletRequest, PARAM_PASSWD);
		//TODO do check
	}
	
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		try {
			User user = getRequiredIdentity(req);
			SecurityUtils.setCurrentUser(user);
			doService(req, resp);
		} catch (Exception e) {
			logger.warn("Error happend in [" + getClass().getSimpleName() + "], detail: ", e);
			writer.print(e.getMessage());
		} finally {
			SecurityUtils.clearCurrentUser();
			writer.flush();
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param user
	 * @throws IOException 
	 * @throws ServletException 
	 */
	protected abstract void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception;
	
}
