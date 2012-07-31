/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-28
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
package com.dianping.lion.medium.zookeeper;

import java.io.IOException;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.exception.RegisterRelatedException;
import com.dianping.lion.medium.ConfigRegisterService;
import com.dianping.lion.medium.ConfigRegisterServiceFactory;

/**
 * @author danson.liu
 *
 */
public class ConfigZookeeperServiceFactory implements ConfigRegisterServiceFactory {

	@Override
	public ConfigRegisterService createRegisterService(Environment environment) {
		try {
			String serverIps = environment.getIps();
			ConfigZookeeperService registerService = new ConfigZookeeperService(serverIps);
			registerService.init();
			return registerService;
		} catch (IOException e) {
			throw new RegisterRelatedException("Create config's zookeeper register service failed.", e);
		}
	}

}
