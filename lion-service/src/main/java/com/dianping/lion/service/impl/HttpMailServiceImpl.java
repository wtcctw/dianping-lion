/**
 * Project: middleware-console
 * 
 * File Created at 2012-8-10
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.dianping.lion.service.HttpMailService;

/**
 * HttpMailService
 * @author youngphy.yang
 *
 */
public class HttpMailServiceImpl implements HttpMailService{
	private static final Logger logger = Logger.getLogger(HttpMailServiceImpl.class);
	private String host;
	private int port;
	private String protocal;
	private String path;
	
	private HttpClient httpClient;
	private PostMethod method;
	
	public void init() {
		httpClient = new HttpClient();
		httpClient.getHostConfiguration().setHost(host, port, protocal);
	}
	
	public boolean sendMail(int mailType, String mail, String title, String body) throws UnsupportedEncodingException {
		String result = null;
		StringBuffer mailUrl = new StringBuffer(path);
		StringBuffer mailPara = new StringBuffer();
		mailPara.append("{type:").append(mailType).append(",to:").append(mail).append(",pair:{title:")
		.append(title).append(",body:").append(body).append("}}");
		method = new PostMethod(mailUrl.toString());
		method.addParameter("jsonm", mailPara.toString());
		method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		if(result.contains("200")) {
			return true;
		}
		return false;
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

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
		
}
