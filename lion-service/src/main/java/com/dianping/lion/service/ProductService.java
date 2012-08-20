/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-9
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
package com.dianping.lion.service;

import java.util.List;

import com.dianping.lion.entity.Product;

public interface ProductService {
	
	List<Product> findAll();
	
	Product findProductByID(int id);
	
	List<Product> findProductByTeamID(int teamId);
	
	int create(Product product);
	
	void update(Product product);
	
	void delete(int id);
}
