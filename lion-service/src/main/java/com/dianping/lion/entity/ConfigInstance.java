/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-11
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
package com.dianping.lion.entity;

import java.io.Serializable;
import java.util.Date;

import com.dianping.lion.util.StringUtils;


/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class ConfigInstance implements Serializable {
	public static final int MAX_VALUE_DISPLAY_LEN = 48;
	public static final int MORE_VALUE_DISPLAY_LEN = 210;
	public static final String NO_CONTEXT = "";

	protected int id;
	protected int configId;
	protected Config config;
	protected int envId;
	protected String desc;
	protected String value;
	protected String context = NO_CONTEXT;
	protected String contextmd5;
	protected int createUserId;
	protected int modifyUserId;
	protected Date createTime;
	protected Date modifyTime;
	protected int seq;
	
	public ConfigInstance() {
	}
	
	public ConfigInstance(int configId, int envId, String context, String value) {
		this.configId = configId;
		this.envId = envId;
		this.context = context;
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}
	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
	}
	/**
	 * @param config the config to set
	 */
	public void setConfig(Config config) {
		this.config = config;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	public String getAbbrevValue() {
		return StringUtils.cutString(value, MAX_VALUE_DISPLAY_LEN);
	}
	public String getAbbrevValue(int length) {
		return StringUtils.cutString(value, length);
	}
	public String getMoreValue() {
		return StringUtils.cutString(value, MORE_VALUE_DISPLAY_LEN);
	}
	public boolean isLongValue() {
		return StringUtils.cutString(value, MAX_VALUE_DISPLAY_LEN).length() != value.length();
	}
	public String getAbbrevDesc(int length) {
		return StringUtils.cutString(desc, length);
	}
	public boolean isLongDesc(int length) {
		return getAbbrevDesc(length).length() != desc.length();
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isDefault() {
		return NO_CONTEXT.equals(context);
	}
	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}
	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null.");
		}
		this.context = context;
	}
	/**
	 * @return the contextmd5
	 */
	public String getContextmd5() {
		return contextmd5;
	}
	/**
	 * @param contextmd5 the contextmd5 to set
	 */
	public void setContextmd5(String contextmd5) {
		this.contextmd5 = contextmd5;
	}
	/**
	 * @return the envId
	 */
	public int getEnvId() {
		return envId;
	}
	/**
	 * @param envId the envId to set
	 */
	public void setEnvId(int envId) {
		this.envId = envId;
	}
	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the modifyUserId
	 */
	public int getModifyUserId() {
		return modifyUserId;
	}
	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
}
