package com.dianping.lion;

import org.apache.struts2.dispatcher.Dispatcher;

public class ConsoleConstants {
	
	public static final String PROJECT_NAME = "project";
	public static final String PRODUCT_NAME = "product";
	public static final String TEAM_NAME = "team";
	
	public static final String CONFIG_OP_SAVE = "save";
	public static final String CONFIG_OP_FORPUB = "forpub";
	public static final String CONFIG_OP_DEPLOY = "deploy";
	
	
	public static Boolean IS_DEV_MODE;
	public static boolean isDevMode() {
		if (IS_DEV_MODE == null) {
			String devMode = Dispatcher.getInstance().getConfigurationManager().getConfiguration().getContainer()
				.getInstance(String.class, "struts.devMode");
			IS_DEV_MODE = "true".equals(devMode);
		}
		return IS_DEV_MODE;
	}
	
}
