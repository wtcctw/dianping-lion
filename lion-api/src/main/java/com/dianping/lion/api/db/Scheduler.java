/**
 * Project: com.dianping.lion.lion-api-0.0.1-new
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
package com.dianping.lion.api.db;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Scheduler
 * @author youngphy.yang
 *
 */
public class Scheduler {
	private static Logger logger = Logger.getLogger(Scheduler.class);
	
	@Autowired
	private DataSourceFetcher dataSourceFetcher;
	
	public void work() {
		String dsContent = dataSourceFetcher.fetchDS();
		logger.debug("running");
	}
}
