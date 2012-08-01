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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JsonParserTest
 * @author youngphy.yang
 *
 */
public class JsonParserTest {
	private static String content = null;
	@BeforeClass
	public static void setup() throws Exception {
		URL url = JsonParserTest.class.getClassLoader().getResource("dbjson.txt");
		File f = new File(url.getPath());
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = br.readLine()) != null)
			sb.append(line);
		content = sb.toString();
	}
	
	@Test
	public void testGetDBAlias() throws Exception {
		JSONObject jsonObj = new JSONObject(content);
		System.out.println(jsonObj.getString("timestamp"));
		String[] names = jsonObj.getNames(jsonObj);
		System.out.println(jsonObj.getJSONObject("group-db").getJSONObject("alpha").getString("readds"));
	}
	
	@Test
	public void testGetConfigInstances() {
		
	}
	
}
