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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

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
	private String parameter;
	
	private HttpClient httcClient;
	private GetMethod dsGetter; 
	
	public void init() {
		httcClient = new HttpClient();
		httcClient.getHostConfiguration().setHost(host, port, protocal);
	}
	public String fetchDS() {
		String dsContent = null;
		dsGetter = new GetMethod("/lion/lion/lionapi?timestamp="+1333720713);
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
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String testDS() throws Exception {
		URL url = JsonParserTest.class.getClassLoader().getResource("dbjson.txt");
		File f = new File(url.getPath());
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine()) != null)
			sb.append(line);
		String content = sb.toString();
		return content;
	}
}
