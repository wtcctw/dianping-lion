/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-31
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
package com.dianping.lion.service;

import java.util.ArrayList;
import java.util.List;

import com.dianping.lion.entity.Environment;

/**
 * @author danson.liu
 *
 */
public class ConfigDeleteResult {

	private List<Environment> failedEnvs;
	
	public void addFailedEnv(Environment env) {
		if (failedEnvs == null) {
			failedEnvs = new ArrayList<Environment>();
		}
		failedEnvs.add(env);
	}
	
	public boolean isSucceed() {
		return failedEnvs == null || failedEnvs.isEmpty();
	}

	/**
	 * @return the failedEnvs
	 */
	public List<Environment> getFailedEnvs() {
		return failedEnvs;
	}
	
}
