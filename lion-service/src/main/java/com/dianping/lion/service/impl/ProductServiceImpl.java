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
package com.dianping.lion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ProductDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Product;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProductService;

public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
    private OperationLogService operationLogService;
	
	private CacheClient cacheClient;
	private CacheClient projectCacheClient;

	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public void delete(int id) {
	    try {
    	    Product product = findProductByID(id);
    		productDao.delete(id);
    		if (product != null) {
    		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Product_Delete, "删除产品线: " + product.getName()));
    		}
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public Product findProduct(String name) {
	    return productDao.findProduct(name);
	}
	
	@Override
	public Product findProductByID(int id) {
		return productDao.findProductByID(id);
	}
	
	@Override
	public List<Product> findProductByTeamID(int teamId) {
		return productDao.findProductByTeamID(teamId);
	}

	@Override
	public int create(Product product) {
	    try {
    		productDao.create(product);
    		operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Product_Add, "创建产品线: " + product.getName()));
    		return product.getId();
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public void update(Product product) {
	    try {
    	    Product existProduct = findProductByID(product.getId());
    		productDao.update(product);
    		if (existProduct != null) {
    		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Product_Edit, "编辑产品线, before[名称: " 
                        + existProduct.getName() + "], after[名称: " + product.getName() + "]"));
    		}
	    } finally {
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    	cacheClient.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        	projectCacheClient.removeAll();
	    }
	}

    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

	public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}

	public void setProjectCacheClient(CacheClient cacheClient) {
		this.projectCacheClient = cacheClient;
	}

}
