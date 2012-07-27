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

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author danson.liu
 *
 */
public class BeanUtils {

	private static transient final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T getDeclaredFieldValue(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object.getClass(), propertyName);

		boolean accessible = field.isAccessible();
		Object result = null;
		synchronized (field) {
			field.setAccessible(true);
			try {
				result = field.get(object);
			} catch (IllegalAccessException e) {
				throw new NoSuchFieldException("No such field: "
						+ object.getClass() + '.' + propertyName);
			} finally {
				field.setAccessible(accessible);
			}
		}
		return (T) result;
	}

	public static Field getDeclaredField(Class<?> clazz, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class<?> superClass = clazz; superClass != Object.class; 
			superClass = superClass.getSuperclass()) {
			
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
				logger.debug("no such method !");
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName()
				+ '.' + propertyName);
	}

	public static Field[] getDeclaredFields(Class<?> clazz) {
		Assert.notNull(clazz);
		Field[] fields = clazz.getDeclaredFields();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			fields = (Field[]) ArrayUtils.addAll(fields, superClass.getDeclaredFields());
		}
		return fields;
	}
	
}
