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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author danson.liu
 *
 */
public class UrlUtils {
	
	public static String resolveUrl(Map<String, ?> parameters, String... includes) {
		StringBuilder url = new StringBuilder();
		int index = 0;
		try {
			if (parameters != null) {
				for (Entry<String, ?> entry : parameters.entrySet()) {
					Collection<Object> paramValues = new ArrayList<Object>();
					Object paramValue = entry.getValue();
					if (ArrayUtils.isEmpty(includes) || ArrayUtils.contains(includes, entry.getKey())) {
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
							url.append(index ++ == 0 ? "" : "&")
									.append(entry.getKey())
									.append("=")
									.append(URLEncoder.encode(value.toString(), "utf-8"));
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return url.toString();
	}
	
	public static String resolveUrl(String url, Map<String, ?> parameters, String... includes) {
		if (url == null) {
			throw new NullPointerException("Param[url] cannot be null.");
		}
		return resolveUrl(url, resolveUrl(parameters, includes));
	}
	
	public static String resolveUrl(String url, String paramStr) {
		return url + (url.contains("?") ? "&" : "?") + paramStr;
	}
	
	public static List<String> getParameterNames(String url) {
		if (url == null) {
			throw new NullPointerException("Param[url] cannot be null.");
		}
		List<String> parameterNames = new ArrayList<String>();
		String queryString = StringUtils.substringAfter(url, "?");
		if (queryString != null) {
			String[] segments = StringUtils.split(queryString, "&");
			for (String segment : segments) {
				String param = StringUtils.substringBefore(segment, "=");
				if (!parameterNames.contains(param)) {
					parameterNames.add(param);
				}
			}
		}
		return parameterNames;
	}

}
