package com.dianping.lion.client;

public interface ConfigLoader {

    /** Default placeholder prefix: "${" */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    /** Default placeholder suffix: "}" */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    
    public static final String KEY_PROPERTIES_FILE = "propertiesFile";
    public static final String KEY_INCLUDE_LOCAL_PROPS = "includeLocalProps";

    public static final String DEFAULT_PROPERTIES_FILE = "config/applicationContext.properties";
    public static final String DEFAULT_INCLUDE_LOCAL_PROPS = "false";

    public static final int ORDER_PROPERTIES_FILE = 0;
    public static final int ORDER_ZOOKEEPER = 10000;

    public void init();
    
    public String get(String key) throws Exception;
    
    public int getOrder();
    
    public void addConfigListener(ConfigListener configListener);
    
    public void removeConfigListener(ConfigListener configListener);

}
