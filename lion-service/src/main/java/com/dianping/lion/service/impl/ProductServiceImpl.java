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

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ProductDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Product;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProductService;

public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
    private OperationLogService operationLogService;
	
	private Ehcache ehcache;
	private Ehcache projectEhcache;

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
	        ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
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
	        ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
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
        		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
        		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        		projectEhcache.removeAll();
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

    /**
     * @param ehcache the ehcache to set
     */
    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    /**
     * @param projectEhcache the projectEhcache to set
     */
    public void setProjectEhcache(Ehcache projectEhcache) {
        this.projectEhcache = projectEhcache;
    }

}
