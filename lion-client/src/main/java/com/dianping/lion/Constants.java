/**
 *
 */
package com.dianping.lion;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Title: Constants.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * @author saber miao
 * @version 1.0
 * @created 2011-5-23 下午04:12:28
 */
public class Constants {

	public static final String CHARSET = "UTF-8";
	public static final String DP_PATH = "/DP";
	public static final String CONFIG_PATH = "/DP/CONFIG";
	public static final String CONFIG_TIMESTAMP = "TIMESTAMP";
	public static final String CONFIG_CONTEXTVAL = "CONTEXTVALUE";
	public static final String SERVICE_PATH ="/DP/SERVER";
	public static final String WEIGHT_PATH ="/DP/WEIGHT";
	public static final String PLACEHOLD="^";
	public static final String PATH_SEPARATOR = "/";
	public static final String ENVIRONMENT_FILE = "/data/webapps/appenv";
	public static final String PROPERTIES_FILE = "config/applicationContext.properties";
	
	public static final String KEY_DEPLOYENV = "deployenv";
	public static final String KEY_ZKSERVER = "zkserver";
	public static final String KEY_SWIMLANE = "swimlane";
	public static final String DEFAULT_DEPLOYENV = "dev";
	public static final String DEFAULT_ZKSERVER = "dev.lion.dp:2181";
	public static final String DEFAULT_SWIMLANE = "";
	
	public static final int DEFAULT_SESSION_TIMEOUT = 60000;

	public static Set<String> avatarBizKeySet = new HashSet<String>();

	static{
		avatarBizKeySet.add("main.master.jdbc.url");
		avatarBizKeySet.add("main.master.jdbc.username");
		avatarBizKeySet.add("main.master.jdbc.password");
		avatarBizKeySet.add("main.master.jdbc.driverClassName");
		avatarBizKeySet.add("main.master.c3p0.minSize");
		avatarBizKeySet.add("main.master.c3p0.maxSize");
		avatarBizKeySet.add("main.master.c3p0.initialSize");
		avatarBizKeySet.add("main.slave.jdbc.url");
		avatarBizKeySet.add("main.slave.jdbc.username");
		avatarBizKeySet.add("main.slave.jdbc.password");
		avatarBizKeySet.add("main.slave.jdbc.driverClassName");
		avatarBizKeySet.add("main.slave.c3p0.minSize");
		avatarBizKeySet.add("main.slave.c3p0.maxSize");
		avatarBizKeySet.add("main.slave.c3p0.initialSize");
		avatarBizKeySet.add("sqlserver.jdbc.url");
		avatarBizKeySet.add("sqlserver.jdbc.username");
		avatarBizKeySet.add("sqlserver.jdbc.password");
		avatarBizKeySet.add("sqlserver.jdbc.driverClassName");
		avatarBizKeySet.add("sqlserver.c3p0.minSize");
		avatarBizKeySet.add("sqlserver.c3p0.maxSize");
		avatarBizKeySet.add("sqlserver.c3p0.initialSize");
		avatarBizKeySet.add("mail.fromname");
		avatarBizKeySet.add("mail.host");
		avatarBizKeySet.add("mail.username");
		avatarBizKeySet.add("mail.password");
		avatarBizKeySet.add("mail.error.sender");
		avatarBizKeySet.add("mail.error.receiver");
		avatarBizKeySet.add("cache.jms.url");
		avatarBizKeySet.add("cache.jms.topic.name");
		avatarBizKeySet.add("cache.jms.username");
		avatarBizKeySet.add("cache.jms.password");
		avatarBizKeySet.add("remoteServer.cacheService");
		avatarBizKeySet.add("Environment");
	}
}
