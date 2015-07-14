package com.dianping.lion.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jodd.bean.BeanUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dianping.lion.Environment;

public class LionPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements 
        ConfigChange, ApplicationContextAware, BeanFactoryAware, BeanNameAware, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(LionPlaceholderConfigurer.class);

    private String basePackage;
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    private String beanName;
    private String appName = Environment.getAppName();
    private ConfigCache configCache;
    private String propertiesPath;
    private boolean includeLocalProps;
    private Map<String, List<BeanData>> propertyMap = new HashMap<String, List<BeanData>>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String beanName) {
        super.setBeanName(beanName);
        this.beanName = beanName;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    public void setIncludeLocalProps(boolean includeLocalProps) {
        this.includeLocalProps = includeLocalProps;
    }
    
    @Override
    protected String resolvePlaceholder(String key, Properties props) {
        String value = configCache.getProperty(key);
        return value;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties)
            throws BeansException {
        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (int i = 0; i < beanNames.length; i++) {
            // Check that we're not parsing our own bean definition,
            // to avoid failing on unresolvable placeholders in properties file locations.
            if (!(beanNames[i].equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
                processPlaceholderProperties(beanFactoryToProcess, beanNames[i]);
                processAnnotatedProperties(beanFactoryToProcess, beanNames[i]);
            }
        }
        super.processProperties(beanFactoryToProcess, properties);
    }

    private void processPlaceholderProperties(ConfigurableListableBeanFactory beanFactory, String beanName) {
        MutablePropertyValues mpvs = beanFactory.getBeanDefinition(beanName).getPropertyValues();
        PropertyValue[] pvs = mpvs.getPropertyValues();
        if(pvs != null) {
            for(PropertyValue pv : pvs) {
                Object objValue = pv.getValue();
                if(objValue instanceof TypedStringValue) {
                    String value = ((TypedStringValue) objValue).getValue();
                    if(value.startsWith(DEFAULT_PLACEHOLDER_PREFIX) && value.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
                        String key = value.substring(2, value.length()-1);
                        List<BeanData> beanDataList = propertyMap.get(key);
                        if(beanDataList == null) {
                            beanDataList = new ArrayList<BeanData>();
                            propertyMap.put(key, beanDataList);
                        }
                        beanDataList.add(new BeanData(beanName, pv.getName()));
                    }
                }
            }
        }
    }

    private void processAnnotatedProperties(ConfigurableListableBeanFactory beanFactory, String beanName) {
        Class clazz = null;
        try {
            clazz = Class.forName(beanFactory.getBeanDefinition(beanName).getBeanClassName());
        } catch (Exception e) {
            logger.error("failed to load class: " + beanFactory.getBeanDefinition(beanName).getBeanClassName(), e);
        }
        
        MutablePropertyValues mpvs = beanFactory.getBeanDefinition(beanName).getPropertyValues();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(LionConfig.class)) {
                LionConfig annotation = field.getAnnotation(LionConfig.class);
                if(!field.isAccessible()) {
                    field.setAccessible(true);
                }
                setProperty(beanName, field.getName(), annotation, mpvs);
            }
        }
    }

    private void setProperty(String beanName, String fieldName, LionConfig annotation, MutablePropertyValues mpvs) {
        String key = getKey(fieldName, annotation);
        String value = getValue(fieldName, annotation);

        if(value != null) {
            mpvs.addPropertyValue(fieldName, value);
            List<BeanData> beanDataList = propertyMap.get(key);
            if(beanDataList == null) {
                beanDataList = new ArrayList<BeanData>();
                propertyMap.put(key, beanDataList);
            }
            BeanData beanData = new BeanData(beanName, fieldName);
            if(!beanDataList.contains(beanData)) {
                beanDataList.add(beanData);
            }
        }

    }

    @Override
    public void onChange(String key, String value) {
        List<BeanData> beanDataList = propertyMap.get(key);
        if (beanDataList != null) {
            for(BeanData bd : beanDataList) {
                Object bean = beanFactory.getBean(bd.getBeanName());
                if (bean != null) {
                    BeanUtil.setProperty(bean, bd.getFieldName(), value);
                }
            }
        }
    }

    private String getValue(String fieldName, LionConfig lionConfig) {
        String key = getKey(fieldName, lionConfig);
        String defaultValue = getDefaultValue(lionConfig);
        String value = configCache.getProperty(key);
        return value == null ? defaultValue : value;
    }

    private String getDefaultValue(LionConfig lionConfig) {
        String defaultValue = lionConfig.defaultValue();
        if(StringUtils.isBlank(defaultValue)) {
            String value = lionConfig.value();
            int idx = value.indexOf(':');
            if(idx != -1) {
                defaultValue = value.substring(idx + 1);
            }
        }
        return StringUtils.trimToNull(defaultValue);
    }

    private String getKey(String fieldName, LionConfig lionConfig) {
        String key = lionConfig.key();
        if(StringUtils.isBlank(key)) {
            key = lionConfig.value();
            int idx = key.indexOf(':');
            if(idx != -1) {
                key = key.substring(0, idx);
            }
            if(StringUtils.isBlank(key) && appName != null) {
                // last resort
                key = appName + "." + StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(fieldName), '.');
            }
        }
        return StringUtils.trimToNull(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty(ConfigLoader.KEY_PROPERTIES_FILE, propertiesPath);
        System.setProperty(ConfigLoader.KEY_INCLUDE_LOCAL_PROPS, "" + includeLocalProps);
        configCache = ConfigCache.getInstance();
        configCache.addChange(this);
    }
    
}
