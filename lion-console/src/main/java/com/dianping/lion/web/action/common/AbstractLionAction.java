/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
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
package com.dianping.lion.web.action.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class AbstractLionAction extends ActionSupport implements ServletRequestAware {

	protected String menu;
	
	protected String warnMessage;
	
	protected String errorMessage;
	
	protected String infoMessage;

	protected HttpServletRequest request;
	
	protected InputStream inputStream;

	/**
	 * @return the menu
	 */
	public String getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(String menu) {
		this.menu = menu;
	}

	/**
	 * @return the warnMessage
	 */
	public String getWarnMessage() {
		return warnMessage;
	}

	/**
	 * @param warnMessage the warnMessage to set
	 */
	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the infoMessage
	 */
	public String getInfoMessage() {
		return infoMessage;
	}

	/**
	 * @param infoMessage the infoMessage to set
	 */
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	protected void createInputStream(String content) {
		try {
			this.inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			//never be here
			throw new RuntimeException("Create content inputstream[response fetch content from] failed.", e);
		}
	}
	
	protected void createErrorStreamResponse(String errorMsg) {
		createInputStream(String.format("{code:-1, msg:'%s'}", errorMsg));
	}
	
	protected void createWarnStreamResponse(String warnMsg) {
		createInputStream(String.format("{code:1, msg:'%s'}", warnMsg));
	}
	
	protected void createSuccessStreamResponse() {
		createInputStream("{code:0}");
	}
	
}
