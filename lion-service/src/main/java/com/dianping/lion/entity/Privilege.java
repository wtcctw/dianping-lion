/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-12
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.entity;

import java.io.Serializable;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public class Privilege implements Serializable {

	private int id;
	
	private int categoryId;
	
	private String name;

    public Privilege() {
    }

    public Privilege(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
