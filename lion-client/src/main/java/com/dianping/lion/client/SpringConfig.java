/**
 * 
 */
package com.dianping.lion.client;

import java.io.IOException;

/**
 * <p>
 * Title: SpringClient.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author saber miao
 * @version 1.0
 * @created 2010-12-30 下午03:32:26
 */
public class SpringConfig extends InitializeConfig {

    public SpringConfig() throws IOException {
        super();
    }

    private ConfigChange change;

    /**
     * @return the change
     */
    public ConfigChange getChange() {
        return change;
    }

    /**
     * @param change
     *            the change to set
     * @throws IOException
     */
    public void setChange(ConfigChange change) throws LionException {
        this.change = change;
        ConfigCache.getInstance().addChange(change);
    }

}
