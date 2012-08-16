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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.lion.job.SyncJob;
import com.dianping.lion.util.ThrowableUtils;


/**
 * TODO Comment of GetConfigServlet
 * @author youngphy.yang
 *
 */
public class StartJobServlet extends AbstractLionServlet{
	
	private static final long serialVersionUID = 1512883947325409564L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    PrintWriter writer = resp.getWriter();
		String jobName = req.getParameter("jobName");
		SyncJob sJob = (SyncJob) getBean(jobName);
		if (sJob == null) {
		    writer.print("Job[" + jobName + "] not found.");
		} else {
		    try {
                sJob.work();
                writer.print("OK!");
            }
            catch (Exception e) {
                writer.print(ThrowableUtils.extractStackTrace(e));
            }
		}
	}
}
