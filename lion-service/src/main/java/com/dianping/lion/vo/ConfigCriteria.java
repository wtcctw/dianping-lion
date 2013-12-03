/**
 * Project: com.dianping.lion.lion-console-0.0.1
 *
 * File Created at 2012-7-15
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
package com.dianping.lion.vo;

/**
 * @author danson.liu
 *
 */
public class ConfigCriteria {

	public static final int HAS_VALUE_ALL = -1;
	public static final int HAS_VALUE_YES = 1;
	public static final int HAS_VALUE_NO = 0;

	private int projectId;

	private int envId;

	private String key;

	private String value;

	private String group;

	private String status;
	
	private int hasValue = HasValueEnum.All.getValue();

	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the hasValue
	 */
	public int getHasValue() {
		return hasValue;
	}

	/**
	 * @param hasValue the hasValue to set
	 */
	public void setHasValue(int hasValue) {
		this.hasValue = hasValue;
	}

	public String getGroup() {
	    return group;
	}

    public void setGroup(String group) {
        this.group = group;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
