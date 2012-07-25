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
package com.dianping.lion.entity;

/**
 * 配置状态的枚举类
 * @author danson.liu
 *
 */
public enum ConfigStatusEnum {
	
	Noset(1, "未设置"), Ineffective(5, "未生效"), Effective(10, "已生效");
	
	private int value;
	
	private String label;

	private ConfigStatusEnum(int value, String label) {
		this.value = value;
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
