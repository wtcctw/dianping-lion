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
    
    Project_Related_All("项目相关---------------全部日志", -1000, 600, true),
    Config_All("项目相关-配置----------全部日志", 1, 20, true),
    Config_Add("项目相关-配置-添加", 5, true),
    Config_Delete("项目相关-配置-删除", 8, true),
    Config_Edit("项目相关-配置-设置", 11, true),
    Config_EditAttr("项目相关-配置-设置属性", 14, true),
    Config_Clear("项目相关-配置-清除", 17, true),
    API_All("项目相关-api-----------全部日志", 101, 120, true),
    API_SetConfig("项目相关-api-setconfig", 105, true),
    API_TakeEffect("项目相关-api-takeeffect", 106, true),
    API_Rollback("项目相关-api-rollback", 107, true),
    
    Job_DSFetcher("job-ds-fetcher", 660, false),
    Env_All("环境------------------全部日志", 701, 720, false),
    Env_Add("环境-新增", 705, false),
    Env_Delete("环境-删除", 706, false),
    Env_Edit("环境-修改", 707, false),
    Tpp_All("team/product/project---全部日志", 801, 820, false),
    Team_Add("team-新增", 805, false),
    Team_Delete("team-删除", 806, false),
    Team_Edit("team-修改", 807, false),
    Product_Add("product-新增", 808, false),
    Product_Delete("product-删除", 809, false),
    Product_Edit("product-修改", 810, false),
    Project_Add("project-新增", 811, false),
    Project_Delete("project-删除", 812, false),
    Project_Edit("project-修改", 813, false),
    User_All("user------------------全部日志", 901, 920, false),
    User_Add("user-新增", 905, false),
    User_Delete("user-删除", 906, false),
    User_Edit("user-修改", 907, false),
    Job_All("job-------------------全部日志", 1001, 1020, false),
    Job_Add("job-新增", 1005, false),
    Job_Delete("job-删除", 1006, false),
    Job_Edit("job-修改", 1007, false);
	
    private final String label;
    private int begin;
    private int end;
    private int value;
    private boolean projectRelated;
    private static volatile List<OperationTypeEnum> projectRelatedTypes;
    
    OperationTypeEnum(String label, int value, boolean projectRelated) {
        this.label = label;
        this.value = value;
        this.begin = value;
        this.end = value;
        this.projectRelated = projectRelated;
    }
    
    OperationTypeEnum(String label, int begin, int end, boolean projectRelated) {
        this.label = label;
        this.begin = begin;
        this.end = end;
        this.projectRelated = projectRelated;
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

    public static OperationTypeEnum fromValue(int value) {
        return EnumUtils.fromEnumProperty(OperationTypeEnum.class, "value", value);
    }
    
    public static List<OperationTypeEnum> projectRelatedTypes() {
        if (projectRelatedTypes == null) {
            synchronized (OperationTypeEnum.class) {
                if (projectRelatedTypes == null) {
                    projectRelatedTypes = new ArrayList<OperationTypeEnum>(20);
                    for (OperationTypeEnum typeEnum : OperationTypeEnum.values()) {
                        if (typeEnum.isProjectRelated()) {
                            projectRelatedTypes.add(typeEnum);
                        }
                    }
                }
            }
        }
        return projectRelatedTypes;
    }
    
}
