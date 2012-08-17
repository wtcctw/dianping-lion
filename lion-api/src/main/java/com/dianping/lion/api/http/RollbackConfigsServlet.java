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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.dianping.lion.entity.ConfigSnapshotSet;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.service.ConfigRollbackResult;


/**
 * TODO Comment of RollbackConfigsServlet
 * @author danson.liu
 *
 */
public class RollbackConfigsServlet extends AbstractLionServlet {

	private static final long serialVersionUID = -7249785353868729405L;

	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName = getNotBlankParameter(req, PARAM_PROJECT);
		String env = getNotBlankParameter(req, PARAM_ENV);
		String task = getNotBlankParameter(req, PARAM_TASK);
		
		Project project = getRequiredProject(projectName);
		Environment environment = getRequiredEnv(env);
		
		ConfigSnapshotSet snapshotSet = configReleaseService.findFirstSnapshotSet(project.getId(), environment.getId(), task);
		
		boolean hasRollbacked = false;
		String keysNotRemoved = "";
		if (snapshotSet != null) {
			ConfigRollbackResult rollbackResult = configReleaseService.rollbackSnapshotSet(snapshotSet);
			Set<String> notRemovedKeys = rollbackResult.getNotRemovedKeys();
			if (CollectionUtils.isNotEmpty(notRemovedKeys)) {
				keysNotRemoved = StringUtils.join(notRemovedKeys, ",") + " not removed.";
			}
			hasRollbacked = true;
		}
		resp.getWriter().print(SUCCESS_CODE + (hasRollbacked ? "1|" + keysNotRemoved : "0"));
	}

}
