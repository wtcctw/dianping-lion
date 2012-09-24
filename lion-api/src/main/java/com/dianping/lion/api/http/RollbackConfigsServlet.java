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
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.service.ConfigRollbackResult;
import com.dianping.lion.util.ThrowableUtils;


/**
 * TODO Comment of RollbackConfigsServlet
 * @author danson.liu
 *
 */
public class RollbackConfigsServlet extends AbstractLionServlet {

	private static final long serialVersionUID = -7249785353868729405L;

	@Override
	protected void doService(HttpServletRequest req, HttpServletResponse resp, String querystr) throws Exception {
		String projectName = getNotBlankParameter(req, PARAM_PROJECT);
		String env = getNotBlankParameter(req, PARAM_ENV);
		String task = getNotBlankParameter(req, PARAM_TASK);
		
		Environment environment = getRequiredEnv(env);
		Project project = projectService.findProject(projectName);
		
		boolean hasRollbacked = false;
		String keysNotRemoved = "";
		
		if (project != null) {
		    String logcontent = "回滚task: " + task;
		    try {
            		ConfigSnapshotSet snapshotSet = configReleaseService.findSnapshotSetToRollback(project.getId(), environment.getId(), task);
            		
            		if (snapshotSet != null) {
            			ConfigRollbackResult rollbackResult = configReleaseService.rollbackSnapshotSet(snapshotSet);
            			Set<String> notRemovedKeys = rollbackResult.getNotRemovedKeys();
            			if (CollectionUtils.isNotEmpty(notRemovedKeys)) {
            				keysNotRemoved = StringUtils.join(notRemovedKeys, ",") + " not removed.";
            			}
            			hasRollbacked = true;
            			operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_Rollback, project.getId(), environment.getId(),
            			        "成功: " + logcontent + (!notRemovedKeys.isEmpty() ? ", 未清除key: " + StringUtils.join(notRemovedKeys, ",") : ""))
            			        .key(null, "true", null, null, querystr));
            		} else {
            		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_Rollback, project.getId(), environment.getId(), 
            		            "成功: " + logcontent + ", 无镜像需要回滚")
            		            .key(null, "true", null, null, querystr));
            		}
		    } catch (Exception e) {
		        operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_Rollback, project.getId(), environment.getId(),
		                "失败: " + logcontent).key(null, "false", null, null, querystr, ThrowableUtils.extractStackTrace(e, 30000)));
		        throw e;
		    }
		}
		resp.getWriter().print(SUCCESS_CODE + (hasRollbacked ? "1|" + keysNotRemoved : "0"));
	}

}
