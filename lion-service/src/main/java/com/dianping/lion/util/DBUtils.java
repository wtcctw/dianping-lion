/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-24
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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author danson.liu
 *
 */
public class DBUtils {
	
	/**
	 * warn: 使用spring delegate sqlmapclient调用时可用
	 * @param throwable
	 * @return
	 */
	public static boolean isDuplicateKeyError(Throwable throwable) {
		if (throwable == null) {
			return false;
		}
		return ExceptionUtils.indexOfType(throwable, DataIntegrityViolationException.class) != -1
			&& ExceptionUtils.getRootCauseMessage(throwable).contains("Duplicate entry");
	}

}
