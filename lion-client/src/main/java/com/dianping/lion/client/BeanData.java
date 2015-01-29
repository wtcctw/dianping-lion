/**
 * 
 */
package com.dianping.lion.client;

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

}
