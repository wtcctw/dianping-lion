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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.util.ThrowableUtils;

/**
 * TODO Comment of RegisterConfigsServlet url:
 * takeeffect?project=xxx&key=xxx&key=xxx&env=xxx("key" params not present if no
 * config is updated) 这里的key是包含了projectName前缀的完整key
 * 
 * @author danson.liu
 * 
 */
public class TakeEffectConfigsServlet extends AbstractLionServlet {

    private static final long serialVersionUID = 4596830426101363885L;

    private static final String PUSH_TO_APP = "1";
    private static final String CREATE_SNAPSHOT = "1";

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp, String querystr) throws Exception {
        String projectName = getNotBlankParameter(req, PARAM_PROJECT);
        String env = getNotBlankParameter(req, PARAM_ENV);
        String task = req.getParameter(PARAM_TASK);
        String[] keys = req.getParameterValues(PARAM_KEY);
        String push2App = req.getParameter(PARAM_PUSH);
        String snapshot = req.getParameter(PARAM_SNAPSHOT);
        push2App = push2App != null ? push2App : "0";
        boolean createSnapshot = snapshot != null ? CREATE_SNAPSHOT.equals(snapshot.trim()) : false;

        Environment environment = getRequiredEnv(env);

        if (keys != null && keys.length > 0) {
            Project project = getRequiredProject(projectName);

            checkConfigKeys(projectName, keys);
            String logcontent = "生效task: " + task + ", key: " + StringUtils.join(keys, ',');
            try {
                int snapshotId = -1;
                if (createSnapshot) {
                    snapshotId = configReleaseService.createSnapshotSet(project.getId(), environment.getId(),
                            task != null ? task : "");
                }

                String[] features = getRequiredParameters(req, PARAM_FEATURE);
                configReleaseService.executeSetTask(project.getId(), environment.getId(), features, keys,
                        PUSH_TO_APP.equals(push2App));

                resp.getWriter().write(SUCCESS_CODE + snapshotId);
                operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_TakeEffect, project.getId(),
                        environment.getId(), "成功: " + logcontent).key(StringUtils.join(keys, ','), "true", null, null,
                        querystr));
            } catch (Exception e) {
                operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_TakeEffect, project.getId(),
                        environment.getId(), "失败: " + logcontent).key(StringUtils.join(keys, ','), "false", null, null,
                        querystr, ThrowableUtils.extractStackTrace(e, 30000)));
                throw e;
            }
        } else {
            resp.getWriter().write(SUCCESS_CODE);
        }
        
        // relay pre to ppe
//        if (environment.getId() == 4) {
//            httpTakeEffect(querystr);
//        }
    }

    private void httpTakeEffect(String querystr) throws IOException {
        String url = "http://lion-api-ppe01.hm:8080/takeeffect?";
        String query = querystr.replace("e=prelease", "e=product");
        url = url + query;
        Content content = Request.Get(url).execute().returnContent();
        String result = content.asString();
        if(result.startsWith("0|")) {
            logger.info("take effect in ppe: " + result);
        } else {
            String message = "failed to take effect in ppe: " + result;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
    
    private void checkConfigKeys(String projectName, String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = checkConfigKey(projectName, keys[i]);
        }
    }

}
