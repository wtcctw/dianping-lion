package com.dianping.lion.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LionConfig {
    
    /**
     * Pattern of value:
     * {key} only key, default value is null
     * {key:defaultValue} key & default value 
     * {:defaultValue} only default value, key defaults to appName.fieldName
     * {} no key & default value: key defaults to appName.fieldName, defaultValue is null
     */
    String value() default "";
    
    /**
     * the key part of value 
     */
    String key() default "";
    
    /**
     * the default value part of value 
     */
    String defaultValue() default "";
    
}
