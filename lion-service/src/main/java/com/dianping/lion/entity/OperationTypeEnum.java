/**
 * Project: lion-console
 * 
 * File Created at 2012-7-11
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

import java.util.ArrayList;
import java.util.List;

import com.dianping.lion.util.EnumUtils;

/**
 * OperationTypeEnum
 * @author youngphy.yang
 *
 */
public enum OperationTypeEnum {
    
//    Project_Related_All("项目相关---------------全部日志", -1000, 600, true),
//    Config_All("项目相关-配置----------全部日志", 1, 20, true),
    Config_All("配置管理", 1, 20, true),
    Config_Add("配置-添加", 5, true),
    Config_Delete("配置-删除", 8, true),
    Config_Edit("配置-设置", 11, true),
    Config_EditAttr("配置-设置属性", 14, true),
    Config_Clear("配置-清除", 17, true),
//    API_All("项目相关-api-----------全部日志", 101, 120, true),
    API_All("API调用", 101, 120, true),
    API_SetConfig("API-setconfig", 105, true),
    API_TakeEffect("API-takeeffect", 106, true),
    API_Rollback("API-rollback", 107, true),
    
    Job_DSFetcher("Job-DS-Fetcher", 660, false, true),
    Env_All("环境管理", 701, 720, false),
    Env_Add("环境-新增", 705, false),
    Env_Delete("环境-删除", 706, false),
    Env_Edit("环境-修改", 707, false),
    TPP_All("Team/Product/Project管理", 801, 820, false),
    Team_Add("Team-新增", 805, false),
    Team_Delete("Team-删除", 806, false),
    Team_Edit("Team-修改", 807, false),
    Product_Add("Product-新增", 808, false),
    Product_Delete("Product-删除", 809, false),
    Product_Edit("Product-修改", 810, false),
    Project_Add("Project-新增", 811, false),
    Project_Delete("Project-删除", 812, false),
    Project_Edit("Project-修改", 813, false),
    User_All("User管理", 901, 920, false),
    User_Add("User-新增", 905, false),
    User_Delete("User-删除", 906, false),
    User_Edit("User-修改", 907, false),
    Job_All("Job管理", 1001, 1020, false),
    Job_Add("Job-新增", 1005, false),
    Job_Delete("Job-删除", 1006, false),
    Job_Edit("Job-修改", 1007, false);
	
    private final String label;
    private int begin;
    private int end;
    private int value;
    private boolean projectRelated;
    private boolean forPageSelect;
    private static volatile List<OperationTypeEnum> pageSelectTypes;
    private static volatile List<OperationTypeEnum> projectPageSelectTypes;
    
    OperationTypeEnum(String label, int value, boolean projectRelated) {
        this(label, value, projectRelated, false);
    }
    
    OperationTypeEnum(String label, int value, boolean projectRelated, boolean forPageSelect) {
    	this.label = label;
        this.value = value;
        this.begin = value;
        this.end = value;
        this.projectRelated = projectRelated;
        this.forPageSelect = forPageSelect;
    }
    
    OperationTypeEnum(String label, int begin, int end, boolean projectRelated) {
        this.label = label;
        this.begin = begin;
        this.end = end;
        this.projectRelated = projectRelated;
        this.forPageSelect = true;
    }
    
    public String getLabel() {
        return label;
    }
    
    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isProjectRelated() {
        return projectRelated;
    }

    public void setProjectRelated(boolean projectRelated) {
        this.projectRelated = projectRelated;
    }

    public boolean isForPageSelect() {
		return forPageSelect;
	}

	public void setForPageSelect(boolean forPageSelect) {
		this.forPageSelect = forPageSelect;
	}

	public static OperationTypeEnum fromValue(int value) {
        return EnumUtils.fromEnumProperty(OperationTypeEnum.class, "value", value);
    }
	
	public static List<OperationTypeEnum> pageSelectTypes() {
		if (pageSelectTypes == null) {
            synchronized (OperationTypeEnum.class) {
                if (pageSelectTypes == null) {
                	pageSelectTypes = new ArrayList<OperationTypeEnum>(20);
                    for (OperationTypeEnum typeEnum : OperationTypeEnum.values()) {
                        if (typeEnum.isForPageSelect()) {
                        	pageSelectTypes.add(typeEnum);
                        }
                    }
                }
            }
        }
        return pageSelectTypes;
	}
    
    public static List<OperationTypeEnum> projectPageSelectTypes() {
        if (projectPageSelectTypes == null) {
            synchronized (OperationTypeEnum.class) {
                if (projectPageSelectTypes == null) {
                    projectPageSelectTypes = new ArrayList<OperationTypeEnum>(20);
                    for (OperationTypeEnum typeEnum : OperationTypeEnum.values()) {
                        if (typeEnum.isProjectRelated() && typeEnum.isForPageSelect()) {
                            projectPageSelectTypes.add(typeEnum);
                        }
                    }
                }
            }
        }
        return projectPageSelectTypes;
    }
    
}
