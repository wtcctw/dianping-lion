package com.dianping.lion.client;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class LionConfigDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String propertiesPath = element.getAttribute("propertiesPath");
        String includeLocalProps = element.getAttribute("includeLocalProps");
        String order = element.getAttribute("order");
        
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(LionPlaceholderConfigurer.class);
        if (StringUtils.hasText(id)) {
            definition.getPropertyValues().addPropertyValue("id", id);
        }
        if (StringUtils.hasText(propertiesPath)) {  
            definition.getPropertyValues().addPropertyValue("propertiesPath", propertiesPath);  
        }
        if (StringUtils.hasText(includeLocalProps)) {  
            definition.getPropertyValues().addPropertyValue("includeLocalProps", Boolean.valueOf(includeLocalProps));
        }
        if (StringUtils.hasText(order)) {  
            definition.getPropertyValues().addPropertyValue("order", Integer.valueOf(order));
        }
        
        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, "lion-config");
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return null;
    }


}
