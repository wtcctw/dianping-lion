/**
 * 
 */
package com.dianping.lion.client;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: BeanData.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author saber miao
 * @version 1.0
 * @created 2011-5-24 下午06:40:55
 */
public class BeanData {

    private String beanName;
    private String fieldName;

    public BeanData(String beanName, String fieldName) {
        this.beanName = beanName;
        this.fieldName = fieldName;
    }

    /**
     * @return the beanName
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * @param beanName
     *            the beanName to set
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public boolean equals(Object object) {
        if(object instanceof BeanData) {
            BeanData bd = (BeanData)object;
            return (StringUtils.equals(bd.getBeanName(), beanName) && 
                    StringUtils.equals(bd.getFieldName(), fieldName));
        }
        return false;
    }

    public int hashCode() {
        int hash = 19;
        hash = hash * 17 + beanName.hashCode();
        hash = hash * 17 + fieldName.hashCode();
        return hash;
    }
}
