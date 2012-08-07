/**
 * Project: com.dianping.lion.lion-api-0.0.1-new
 * 
 * File Created at 2012-8-2
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
import java.io.FileReader;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dianping.lion.db.Storager;
import com.dianping.lion.support.AbstractDaoTestSupport;
import com.dianping.lion.util.JsonParser;

/**
 * StoragerTest
 * @author youngphy.yang
 *
 */
public class StoragerTest extends AbstractDaoTestSupport{
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
	public void testCheckAndSaveDBConfig() throws Exception {
		Storager storager = new Storager();
		storager.setJsonParser(new JsonParser());
		storager.checkAndSaveDBConfig(content);
	}
}
