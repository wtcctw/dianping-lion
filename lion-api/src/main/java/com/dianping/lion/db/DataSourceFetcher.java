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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * DataSourceFetcher 定期获取增量DB信息
 * @author youngphy.yang
 *
 */
public class DataSourceFetcher {
	private static Logger logger = Logger.getLogger(DataSourceFetcher.class);
	
	private String host;
	private int port;
	private String protocal;
	private String path;
	/**
	 * 获取DB增量信息的密码，生成策略：'lionsrt1234_'+System.currentTimeMillis() / 60000 + 10
	 */
	private String srtkey;
	
	private HttpClient httcClient;
	private GetMethod dsGetter;
	
	public void init() {
		httcClient = new HttpClient();
		httcClient.getHostConfiguration().setHost(host, port, protocal);
	}
	
	/**
	 * 增量获取DB修改信息
	 * @param lastFetchTime 上次获取时间
	 * @return
	 */
	public String fetchDS(long lastFetchTime) {
		String dsContent = null;
		long minEffectionTime = System.currentTimeMillis() / 60000 + 10;
		String url = path+lastFetchTime+"&srtkey="+srtkey+minEffectionTime;
		dsGetter = new GetMethod(url);
		try {
			httcClient.executeMethod(dsGetter);
			InputStream inputStream = dsGetter.getResponseBodyAsStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
	        StringBuffer stringBuffer = new StringBuffer();   
	        String str= "";   
	        while((str = br.readLine()) != null){
	            stringBuffer .append(str );   
	        }
	        dsContent = stringBuffer.toString();
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
	public String getSrtkey() {
		return srtkey;
	}
	public void setSrtkey(String srtkey) {
		this.srtkey = srtkey;
	}
	
}
