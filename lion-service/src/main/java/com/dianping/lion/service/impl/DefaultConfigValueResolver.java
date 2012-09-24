/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-22
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
package com.dianping.lion.service.impl;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.ConfigValueResolver;

/**
 * @author danson.liu
 *
 */
public class DefaultConfigValueResolver implements ConfigValueResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultConfigValueResolver.class);

	private final ConfigService configService;

	public DefaultConfigValueResolver(ConfigService configService) {
		this.configService = configService;
	}

	@Override
	public String resolve(String configval, int envId) {
		if (configval == null) {
			return null;
		}
		if (configval.contains(ServiceConstants.REF_CONFIG_PREFIX)) {
			try {
				Matcher matcher = ServiceConstants.REF_EXPR_PATTERN.matcher(configval);
				boolean referenceFound = false;
				StringBuffer buffer = new StringBuffer(configval.length() * 2);
				while (matcher.find()) {
					referenceFound = true;
					String refkey = matcher.group(1);
					Config refconfig = configService.findConfigByKey(refkey);
					if (refconfig == null) {
						logger.warn("Referenced config[" + refkey + "] not found.");
						return null;
					}
					ConfigInstance refinstance = configService.findInstance(refconfig.getId(), envId, ConfigInstance.NO_CONTEXT);
					if (refinstance == null) {
						logger.warn("Referenced config[" + refkey + "] with env[" + envId + "] not found.");
						return null;
					}
					String refval = refinstance.getValue();
					if (refval == null) {
						logger.warn("Reference null-valued config[" + refkey + "] with env[" + envId + "].");
						return null;
					}
					String refparams = matcher.group(3);
					if (StringUtils.isNotBlank(refparams)) {
						String[] paramList = StringUtils.split(refparams, "&");
						for (String paramstr : paramList) {
							String[] paramentry = StringUtils.split(paramstr, "=");
							refval = StringUtils.replace(refval, "${" + paramentry[0] + "}", paramentry[1]);
						}
					}
					matcher.appendReplacement(buffer, Matcher.quoteReplacement(refval));
				}
				if (referenceFound) {
					matcher.appendTail(buffer);
					return buffer.toString();
				}
			} catch (RuntimeException e) {
				logger.error("Resolve referenced config expression[" + configval + "] failed.", e);
				throw e;
			}
		}
		return configval;
	}

}
