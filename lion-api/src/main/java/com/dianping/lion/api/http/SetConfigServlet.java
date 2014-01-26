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

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigSetTask;
import com.dianping.lion.entity.ConfigSetType;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.util.StringUtils;
import com.dianping.lion.util.ThrowableUtils;

/**
 * 根据项目名和环境，设置配置项的http接口
 * 
 * @author danson.liu
 * 
 */
public class SetConfigServlet extends AbstractLionServlet {

    private static final long serialVersionUID = 1420817678507296960L;

    private static final String EFFECT_SOON = "1";

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp, String querystr) throws Exception {
        String projectName = getNotBlankParameter(req, PARAM_PROJECT);
        String env = getNotBlankParameter(req, PARAM_ENV);
        String key = getNotBlankParameter(req, PARAM_KEY);
        String value = getRequiredParameter(req, PARAM_VALUE);
        String effect = getNotBlankParameter(req, PARAM_EFFECT); // 是否立即生效
        String configKey = checkConfigKey(projectName, key);
        String group = getGroupParameter(req);

        Project project = getRequiredProject(projectName);
        Environment environment = getRequiredEnv(env);

        String cutvalue = StringUtils.cutString(value, 80);
        String logcontent = "设置配置项: " + configKey + ", value: " + cutvalue;
        Config existConfig = configService.findConfigByKey(configKey);
        String existValue = null;
        
        if (existConfig != null) {
            ConfigInstance existInstance = configService.findInstance(existConfig.getId(), environment.getId(), group);
            if (existInstance != null) {
                existValue = existInstance.getValue();
            }
        }
        
        try {
            if (EFFECT_SOON.equals(effect)) {
                configService.setConfigValue(project.getId(), environment.getId(), configKey, "", group, value,
                        ConfigSetType.RegisterAndPush);
            } else {
                String feature = getNotBlankParameter(req, PARAM_FEATURE);
                ConfigSetTask configSetTask = new ConfigSetTask();
                configSetTask.setProjectId(project.getId());
                configSetTask.setEnvId(environment.getId());
                configSetTask.setFeature(feature);
                configSetTask.setKey(configKey);
                configSetTask.setValue(value);
                configSetTask.setContext(group);
                configSetTask.setDelete(false);
                configReleaseService.createSetTask(configSetTask);
            }
            operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_SetConfig, project.getId(),
                    environment.getId(), "成功: " + logcontent).key(getKeyWithGroup(configKey, group), "true",
                    existValue, value, querystr));
            resp.getWriter().print(SUCCESS_CODE);
        } catch (Exception e) {
            operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_SetConfig, project.getId(),
                    environment.getId(), "失败: " + logcontent).key(getKeyWithGroup(configKey, group), "false",
                    existValue, value, querystr, ThrowableUtils.extractStackTrace(e, 30000)));
            throw e;
        }
        
        // relay pre to ppe
        if (environment.getId() == 4) {
            httpSetConfig(querystr);
        }
    }

    private void httpSetConfig(String querystr) throws IOException {
        String url = "http://lion-api-ppe01.hm:8080/setconfig?";
        String query = querystr.replace("e=prelease", "e=product");
        url = url + query;
        Content content = Request.Get(url).execute().returnContent();
        String result = content.asString();
        if(result.startsWith("0|")) {
            logger.info("set config to ppe: " + result);
        } else {
            String message = "failed to set config to ppe: " + result;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

}
