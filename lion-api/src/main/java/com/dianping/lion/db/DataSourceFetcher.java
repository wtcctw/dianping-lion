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
package com.dianping.lion.db;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * DataSourceFetcher
 * @author youngphy.yang
 *
 */
public class DataSourceFetcher {
	private static Logger logger = Logger.getLogger(DataSourceFetcher.class);
	
	private String host;
	private int port;
	private String protocal;
	private String path;
	
	private HttpClient httcClient;
	private GetMethod dsGetter;
	
	public void init() {
		httcClient = new HttpClient();
		httcClient.getHostConfiguration().setHost(host, port, protocal);
	}
	public String fetchDS(long lastFetchTime) {
		String dsContent = null;
		
		dsGetter = new GetMethod(path+lastFetchTime);
		try {
			httcClient.executeMethod(dsGetter);
			dsContent = dsGetter.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("Job execution failed this time.", e);
			return null;
		}
		return dsContent;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
