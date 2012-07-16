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
package com.dianping.lion.util;

import org.apache.commons.lang.ObjectUtils;

/**
 * @author danson.liu
 *
 */
public class EnumUtils {

	/**
	 * 从指定的枚举类中根据property搜寻匹配指定值的枚举实例
	 * @param <T>
	 * @param enumClass
	 * @param property
	 * @param propValue
	 * @return
	 */
	public static <T extends Enum<T>> T fromEnumProperty(Class<T> enumClass, String property, Object propValue) {
		T[] enumConstants = enumClass.getEnumConstants();
		for (T t : enumConstants) {
			Object constantPropValue;
			try {
				constantPropValue = BeanUtils.getDeclaredFieldValue(t, property);
				if (ObjectUtils.equals(constantPropValue, propValue)) {
					return t;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;

	}

	/**
	 * 从指定的枚举类中根据名称匹配指定值
	 * @param <T>
	 * @param enumClass
	 * @param constantName
	 * @return
	 */
    public static <T extends Enum<T>> T fromEnumConstantName(Class<T> enumClass, String constantName) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if (((Enum<?>) t).name().equals(constantName)) {
                return t;
            }
        }
        return null;
    }
	
}
