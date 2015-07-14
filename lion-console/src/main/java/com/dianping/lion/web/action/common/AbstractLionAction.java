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

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.PrivilegeService;
import com.dianping.lion.service.ProjectPrivilegeDecider;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class AbstractLionAction extends ActionSupport implements ServletRequestAware, Preparable {

    public static final int INFO_CODE = 0;
    public static final int WARN_CODE = 1;
    public static final int ERROR_CODE = -1;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String menu;

	protected String warnMessage;

	protected String errorMessage;

	protected String infoMessage;

	protected HttpServletRequest request;

	protected InputStream inputStream;

	@Autowired
	protected OperationLogService operationLogService;

	@Autowired
	protected ProjectPrivilegeDecider privilegeDecider;

	@Autowired
	protected PrivilegeService privilegeService;

	@Override
	public void prepare() throws Exception {
		if (!isAjaxRequest()) {
			checkModulePrivilege();
		}
	}

	protected void checkModulePrivilege() {
	}

	protected boolean isAjaxRequest() {
		String actionName = ActionContext.getContext().getName();
		return StringUtils.endsWith(actionName, "Ajax");
	}

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

	protected void createStreamResponse(String content) {
		try {
			this.inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			//never be here
			throw new RuntimeException("Create content inputstream[response fetch content from] failed.", e);
		}
	}

	protected void createErrorStreamResponse(String errorMsg) {
	    createStreamResponse(ERROR_CODE, errorMsg);
	}

	protected void createErrorStreamResponse() {
		createErrorStreamResponse("");
	}

	protected void createWarnStreamResponse(String warnMsg) {
	    createStreamResponse(WARN_CODE, warnMsg);
	}

	protected void createSuccessStreamResponse(String infoMsg) {
	    createStreamResponse(INFO_CODE, infoMsg);
	}

	protected void createStreamResponse(int code, String msg) {
	    try {
            JSONObject result = new JSONObject();
            result.put("code", code);
            result.put("msg", msg);
            createStreamResponse(result.toString());
        } catch (Exception e) {
            logger.error("Failed to create response", e);
            throw new RuntimeBusinessException(e);
        }
	}

	protected void createSuccessStreamResponse() {
		createSuccessStreamResponse("");
	}

	protected void createSuccessStreamResponse(Object result) {
	    JSONObject resultJSON = new JSONObject();
	    try {
	        resultJSON.put("code", 0);
            resultJSON.put("result", JSONUtil.serialize(result));
            createStreamResponse(resultJSON.toString());
        } catch (Exception e) {
            logger.error("Create json response failed.", e);
            throw new RuntimeBusinessException(e);
        }
	}

	protected void createJsonStreamResponse(Object result) {
	    try {
            createStreamResponse(JSONUtil.serialize(result));
        } catch (JSONException e) {
            logger.error("Create json response failed.", e);
            throw new RuntimeBusinessException(e);
        }
	}

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

	public void setPrivilegeDecider(ProjectPrivilegeDecider privilegeDecider) {
		this.privilegeDecider = privilegeDecider;
	}

	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

}
