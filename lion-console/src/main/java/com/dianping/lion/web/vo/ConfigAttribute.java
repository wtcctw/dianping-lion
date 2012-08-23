/**
 * 
 */
package com.dianping.lion.web.vo;

import java.io.Serializable;

/**
 * @author danson.liu
 *
 */
public class ConfigAttribute implements Serializable {

    private static final long serialVersionUID = 4607633743914540315L;
    
    private boolean isPublic;

    /**
     * @return the isPublic
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

}
