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
package com.dianping.lion.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author danson.liu
 *
 */
public class UrlUtils {
	
	public static String resolveUrl(String url, Map<String, ?> parameters) {
		try{
			StringBuilder finalUrl = new StringBuilder(url);
			int index = 0;
			for (Entry<String, ?> entry : parameters.entrySet()) {
				Collection<Object> paramValues = new ArrayList<Object>();
				Object paramValue = entry.getValue();
				Class<? extends Object> paramClass = paramValue.getClass();
				if (Collection.class.isInstance(paramValue)) {
					paramValues.addAll((Collection<?>) paramValue);
				} else if (paramClass.isArray()) {
					Object[] valueArray = (Object[]) paramValue;
					for (Object value : valueArray) {
						paramValues.add(value);
					}
				} else {
					paramValues.add(paramValue);
				}
				for (Object value : paramValues) {
					finalUrl.append(index ++ == 0 && finalUrl.indexOf("?") == -1 ? "?" : "&")
							.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(value.toString(), "utf-8"));
				}
			}
			return finalUrl.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
