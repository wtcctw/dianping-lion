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

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.RegisterRelatedException;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.job.SyncJob;


/**
 * TODO Comment of GetConfigServlet
 * @author danson.liu
 *
 */
public class StartJobServlet extends AbstractLionServlet{
	
	private static final long serialVersionUID = 1512883947325409564L;

	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		String jobName = req.getParameter("jobName");
		SyncJob sJob = (SyncJob)context.getBean(jobName);
		sJob.work();
	}
}
