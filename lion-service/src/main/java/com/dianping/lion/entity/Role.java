/**
 * 
 */
package com.dianping.lion.entity;

import java.io.Serializable;

/**
 * @author danson.liu
 *
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -6883653252780235581L;
    
    private int id;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
