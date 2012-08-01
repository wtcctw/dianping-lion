/**
 * Project: com.dianping.lion.lion-api-0.0.1-new
 * 
 * File Created at 2012-7-31
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
package com.dianping.lion.api.db;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;

/**
 * JsonParser
 * @author youngphy.yang
 *
 */
public class JsonParser {
	public String[] getDBAlias(String dbContent) throws Exception {
		String[] dbAliases = null;
		JSONObject jsonObj = new JSONObject(dbContent);
		String[] names = jsonObj.getNames(jsonObj);
//		dbAliases = Arrays.copyOfRange(original, from, to)
		return dbAliases;
	}
	
	public ConfigInstance[] getConfigInstances(String dbContent, Config[] configs) {
		ConfigInstance[] ci = null;
		return ci;
	}
}
