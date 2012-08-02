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
	
	@Autowired
	private Storager storager;
	
	public void work() throws Exception {
		String dsContent = dataSourceFetcher.testDS();
		try {
			storager.store(dsContent);
		} catch (Exception e) {
			logger.debug("Failed to store the config.",e);
		}
		logger.debug("running");
	}

	public DataSourceFetcher getDataSourceFetcher() {
		return dataSourceFetcher;
	}

	public void setDataSourceFetcher(DataSourceFetcher dataSourceFetcher) {
		this.dataSourceFetcher = dataSourceFetcher;
	}

	public Storager getStorager() {
		return storager;
	}

	public void setStorager(Storager storager) {
		this.storager = storager;
	}
	
}
