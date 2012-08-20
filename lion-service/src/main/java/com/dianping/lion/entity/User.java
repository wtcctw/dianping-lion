/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-12
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dianping.lion.ServiceConstants;

/**
 * User
 * 
 * @author youngphy.yang
 * 
 */
public class User {
    private int id;
    private String loginName;
    private String name;
    private String email;
    private Date createTime;
    private boolean system; // 是否系统用户
    private boolean locked; // 是否被锁定(禁止登陆)
    private List<Role> roles = new ArrayList<Role>();
    
    private Boolean isSCM;
    private Boolean isSA;

    public User(int id) {
        this.id = id;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the system
     */
    public boolean isSystem() {
        return system;
    }

    /**
     * @param system
     *            the system to set
     */
    public void setSystem(boolean system) {
        this.system = system;
    }

    /**
     * @return the locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked
     *            the locked to set
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public boolean isAdmin() {
        return id == ServiceConstants.USER_LION_ID;
    }
    
    public boolean isSCM() {
        if (isSCM == null) {
            for (Role role : roles) {
                if (role.getId() == ServiceConstants.ROLE_SCM_ID) {
                    isSCM = true;
                    break;
                }
            }
            if (isSCM == null) {
                isSCM = false;
            }
        }
        return isSCM;
    }
    
    public boolean isSA() {
        if (isSA == null) {
            for (Role role : roles) {
                if (role.getId() == ServiceConstants.ROLE_SA_ID) {
                    isSA = true;
                    break;
                }
            }
            if (isSA == null) {
                isSA = false;
            }
        }
        return isSA;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
