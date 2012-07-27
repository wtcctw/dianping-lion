/**
 * Project: lion-console
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

/**
 * OperationTypeEnum
 * @author youngphy.yang
 *
 */
public enum OperationTypeEnum {
	ADD("增加"),
	MODIFY("修改"),
	DELETE("删除"),
	QUERY("查找");
	
    private final String value;
    static OperationTypeEnum[] opTypes = OperationTypeEnum.values();

    OperationTypeEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static OperationTypeEnum fromInt(int ordinal) {
    	return opTypes[ordinal];
    }
}
