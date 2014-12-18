/**
 *
 */
package com.dianping.lion.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jodd.bean.BeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import com.dianping.lion.EnvZooKeeperConfig;

/**
 * <p>
 * Title: InitializeConfig.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author saber miao
 * @version 1.0
 * @created 2010-12-30 下午06:12:13
 */
public class InitializeConfig implements BeanFactoryPostProcessor, PriorityOrdered, BeanNameAware, BeanFactoryAware {

    private static Logger logger = LoggerFactory.getLogger(InitializeConfig.class);
    
    /** Default placeholder prefix: "${" */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    /** Default placeholder suffix: "}" */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
    private int order = Ordered.LOWEST_PRECEDENCE; // default: same as
                                                   // non-Ordered
    private BeanFactory beanFactory;
    private String beanName;
    private String nullValue = "";
    protected String address;
    private String environment;
    private String propertiesPath;
    private boolean includeLocalProps; // 是否使用本地的propertiesPath指定的配置文件中的配置
    private Properties localProps;
    private Map<String, List<BeanData>> propertyMap = new HashMap<String, List<BeanData>>();

    public void init() throws IOException {
        this.localProps = new Properties();

        if (this.propertiesPath != null) {
            String[] propPaths = propertiesPath.split(",");
            for (String propPath : propPaths) {
                final InputStream propIn = this.getClass().getClassLoader().getResourceAsStream(propPath);
                if (propIn == null) {
                    logger.error("can not find properties file " + propPath);
                    continue;
                }

                this.localProps.load(new InputStream() {
                    boolean temp = false;

                    public int read() throws IOException {
                        if (temp) {
                            temp = false;
                            return ':';
                        }
                        int result = propIn.read();
                        if (result == (int) ':') {
                            temp = true;
                            return '\\';
                        }
                        return result;
                    }
                });

                propIn.close();
            }
        }
        
        this.address = EnvZooKeeperConfig.getZKAddress();
        logger.info(">>>>>>>>>>>zookeeper address is " + this.address);
        this.environment = EnvZooKeeperConfig.getEnv();
        logger.info(">>>>>>>>>>>project environment is " + this.environment);
        logger.info(">>>>>>>>>>>project swimlane is " + EnvZooKeeperConfig.getSwimlane());
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactoryToProcess) throws BeansException {
        try {
            init();
        } catch (IOException e) {
            throw new BeansException("init properties error", e) {
                private static final long serialVersionUID = 6866582836134406673L;
            };
        }
        /*
         * if(this.address == null ||
         * this.address.startsWith(DEFAULT_PLACEHOLDER_PREFIX)){ if(this.pts !=
         * null){ this.address = this.pts.getProperty("lion.zk.address"); } }
         */
        StringValueResolver valueResolver;
        try {
            ConfigCache cache = ConfigCache.getInstance(this.address);
            List<String> keyList = new ArrayList<String>();
            if (!this.environment.equalsIgnoreCase("dev") && !this.environment.equalsIgnoreCase("alpha")
                    && !includeLocalProps) {
                for (Object key : localProps.keySet()) {
                    String value = localProps.getProperty((String) key);
                    if (!(value.startsWith(DEFAULT_PLACEHOLDER_PREFIX) && value.endsWith(DEFAULT_PLACEHOLDER_SUFFIX))) {
                        keyList.add((String) key);
                    }
                }
            }
            for (String key : keyList) {
                this.localProps.remove(key);
                logger.warn("ignore key " + key + " in applicationContext in env " + environment);
            }
            cache.setPts(this.localProps);
            valueResolver = new PlaceholderResolvingStringValueResolver(cache);
        } catch (LionException e) {
            throw new BeansException("new instance ConfigCache error", e) {
                private static final long serialVersionUID = -1546036309824048670L;
            };
        }
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            // Check that we're not parsing our own bean definition,
            // to avoid failing on unresolvable placeholders in properties file
            // locations.
            if (!(beanNames[i].equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
                BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(beanNames[i]);
                try {
                    // cache beanName and fieldName
                    MutablePropertyValues mpvs = bd.getPropertyValues();
                    PropertyValue[] pvs = mpvs.getPropertyValues();
                    if (pvs != null) {
                        for (PropertyValue pv : pvs) {
                            Object value = pv.getValue();
                            if (value instanceof TypedStringValue) {
                                String value_ = ((TypedStringValue) value).getValue();
                                if (value_.startsWith(this.placeholderPrefix)
                                        && value_.endsWith(this.placeholderSuffix)) {
                                    value_ = value_.substring(2);
                                    value_ = value_.substring(0, value_.length() - 1);
                                    List<BeanData> beanDataList = propertyMap.get(value_);
                                    if(beanDataList == null) {
                                        beanDataList = new ArrayList<BeanData>();
                                        propertyMap.put(value_, beanDataList);
                                    }
                                    beanDataList.add(new BeanData(beanNames[i], pv.getName()));
                                }
                            }
                        }
                    }
                    visitor.visitBeanDefinition(bd);
                } catch (BeanDefinitionStoreException ex) {
                    throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanNames[i], ex.getMessage());
                }
            }
        }
        // New in Spring 2.5: resolve placeholders in alias target names and
        // aliases as well.
        beanFactoryToProcess.resolveAliases(valueResolver);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * Parse the given String value recursively, to be able to resolve nested
     * placeholders (when resolved property values in turn contain placeholders
     * again).
     * 
     * @param strVal
     *            the String value to parse
     * @param props
     *            the Properties to resolve placeholders against
     * @param visitedPlaceholders
     *            the placeholders that have already been visited during the
     *            current resolution attempt (used to detect circular references
     *            between placeholders). Only non-null if we're parsing a nested
     *            placeholder.
     * @throws BeanDefinitionStoreException
     *             if invalid values are encountered
     * @see #resolvePlaceholder(String, java.util.Properties, int)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected String parseStringValue(String strVal, ConfigCache cache, Set visitedPlaceholders)
            throws BeanDefinitionStoreException {
        StringBuffer buf = new StringBuffer(strVal);
        int startIndex = strVal.indexOf(this.placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                if (!visitedPlaceholders.add(placeholder)) {
                    throw new BeanDefinitionStoreException("Circular placeholder reference '" + placeholder
                            + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the
                // placeholder key.
                placeholder = parseStringValue(placeholder, cache, visitedPlaceholders);
                // Now obtain the value for the fully resolved key...
                String propVal = null;
                String placeholder_ = null;
                if (this.localProps != null) {
                    placeholder_ = this.localProps.getProperty(placeholder);
                }
                if (placeholder_ != null) {
                    if (placeholder_.startsWith(DEFAULT_PLACEHOLDER_PREFIX)
                            && placeholder_.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
                        placeholder_ = placeholder_.substring(2);
                        placeholder_ = placeholder_.substring(0, placeholder_.length() - 1);
                        try {
                            propVal = cache.getProperty(placeholder_);
                            placeholder = placeholder_;
                        } catch (LionException e) {
                            throw new BeanDefinitionStoreException("get config error", e);
                        }
                    } else {
                        if (this.environment.equalsIgnoreCase("dev") || this.environment.equalsIgnoreCase("alpha")) {
                            propVal = placeholder_;
                            logger.info(">>>>>>>>>>>>getProperty key from applicationContext: " + placeholder_
                                    + "  value:" + propVal);
                        } else {
                            try {
                                logger.warn(">>>>>>>>>>>>please delete key from applicationContext, Key:" + placeholder);
                                propVal = cache.getProperty(placeholder);
                                logger.info(">>>>>>>>>>>>getProperty key from applicationContext: " + placeholder
                                        + "  value:" + propVal);
                            } catch (LionException e) {
                                throw new BeanDefinitionStoreException("get config error", e);
                            }
                        }
                    }
                } else {
                    try {
                        propVal = cache.getProperty(placeholder);
                    } catch (LionException e) {
                        throw new BeanDefinitionStoreException("get config error", e);
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in
                    // the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, cache, visitedPlaceholders);
                    buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                } else {
                    throw new BeanDefinitionStoreException("Could not resolve placeholder '" + placeholder + "'");
                }
                visitedPlaceholders.remove(placeholder);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                } else {
                    return index;
                }
            } else if (StringUtils.substringMatch(buf, index, this.placeholderPrefix)) {
                withinNestedPlaceholder++;
                index = index + this.placeholderPrefix.length();
            } else {
                index++;
            }
        }
        return -1;
    }

    /**
     * BeanDefinitionVisitor that resolves placeholders in String values,
     * delegating to the <code>parseStringValue</code> method of the containing
     * class.
     */
    @SuppressWarnings("rawtypes")
    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {
        private final ConfigCache cache;

        public PlaceholderResolvingStringValueResolver(ConfigCache cache) {
            this.cache = cache;
            this.cache.addChange(new BeanConfigChange());
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = parseStringValue(strVal, this.cache, new HashSet());
            return (value.equals(nullValue) ? null : value);
        }
    }

    /**
     * @param address
     *            the address to set
     */
    /*
     * public void setAddress(String address) { this.address = address; }
     */

    /**
     * @return the propertiesPath
     */
    public String getPropertiesPath() {
        return propertiesPath;
    }

    /**
     * @param propertiesPath
     *            the propertiesPath to set
     */
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public void setIncludeLocalProps(boolean includeLocalProps) {
        this.includeLocalProps = includeLocalProps;
    }

    private class BeanConfigChange implements ConfigChange {

        @Override
        public void onChange(String key, String value) {
            List<BeanData> beanDataList = InitializeConfig.this.propertyMap.get(key);
            if (beanDataList != null) {
                for(BeanData bd : beanDataList) {
                    Object bean = InitializeConfig.this.beanFactory.getBean(bd.getBeanName());
                    if (bean != null) {
                        BeanUtil.setProperty(bean, bd.getFieldName(), value);
                    }
                }
            }
        }
    }
}
