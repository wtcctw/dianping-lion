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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.util.IPUtils;
import com.dianping.lion.util.SecurityUtils;


/**
 * TODO Comment of GetConfigServlet
 *
 * @author danson.liu
 */
public class GetConfigServlet extends AbstractLionServlet {

    private static final long serialVersionUID = 1512883947325409564L;

    public GetConfigServlet() {
        this.requestIdentityRequired = false;
    }

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp, String querystr) throws Exception {
        String env = getNotBlankParameter(req, PARAM_ENV);
        Environment environment = getRequiredEnv(env);
        if (environment.isOnline()) {
            checkAccessibility(req);
        }
        
        PrintWriter writer = resp.getWriter();
        String key = getNotBlankParameter(req, PARAM_KEY);
        String group = getGroupParameter(req);
        
        Config config = configService.findConfigByKey(key);
        if (config != null) {
            String configVal = configService.getConfigValue(config.getId(), environment.getId(), group);
            configVal = SecurityUtils.tryDecode(configVal);
            if (configVal != null) {
                writer.print(configVal);
                return;
            }
        }
        writer.print("<null>");
    }

    protected void checkAccessibility(HttpServletRequest req) {
        String userIP = IPUtils.getUserIP(req);
        String whiteList = systemSettingService.getSetting(ServiceConstants.SETTING_GETCONFIG_WHITELIST);
        if (StringUtils.isBlank(whiteList)) {
            return;
        }
        if (whiteList.contains(userIP) || whiteList.contains("*.*.*.*")) {
            return;
        }
        String[] ipSegments = StringUtils.split(userIP, '.');
        if (ipSegments.length == 4) {
            for (int i = 3; i >= 1; i--) {
                String left = StringUtils.join(ipSegments, '.', 0, i);
                String pattern = left + StringUtils.repeat(".*", 4 - i);
                if (whiteList.contains(pattern)) {
                    return;
                }
            }
        }
        throw new RuntimeBusinessException("You have no privilege.");
    }

}
