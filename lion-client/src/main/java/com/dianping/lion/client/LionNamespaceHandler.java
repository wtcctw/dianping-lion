package com.dianping.lion.client;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LionNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("init", new LionBeanDefinitionParser());  
    }

}