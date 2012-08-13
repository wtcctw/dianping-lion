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

import java.util.HashMap;
import java.util.Map;

/**
 * @author danson.liu
 *
 */
public class Maps {

	public static MapBuilder entry(Object key, Object value) {
		return new MapBuilder(key, value);
	}
	
	public static class MapBuilder {
		private Map<Object, Object> map = new HashMap<Object, Object>();

		public MapBuilder(Object key, Object value) {
			map.put(key, value);
		}
		
		public MapBuilder entry(Object key, Object value) {
			map.put(key, value);
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K, V> Map<K, V> get() {
			return (Map<K, V>) map;
		}
		
	}
	
}
