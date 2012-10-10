/**
 * File Created at 12-9-27
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
package com.dianping.lion.dao.ibatis;

import com.dianping.lion.dao.PrivilegeDao;
import com.dianping.lion.entity.Privilege;
import com.dianping.lion.entity.PrivilegeCategory;
import com.dianping.lion.entity.Resource;
import com.dianping.lion.entity.Role;
import com.dianping.lion.support.AbstractDaoTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO Comment of The Class
 *
 * @author danson.liu
 */
public class PrivilegeIbatisDaoTest extends AbstractDaoTestSupport {

    @Autowired
    private PrivilegeDao privilegeDao;
    private static final String Resource1_Name = "res1_name";
    private static final String Resource2_Name = "res2_name";
    private static final String Resource1_Code = "res1_code";
    private static final String Resource2_Code = "res2_code";
    private static final String Resource1_Url = "res1_url";
    private static final String Resource2_Url = "res2_url";
    private static final String Role1_Name = "role1_name";
    private static final String Role2_Name = "role2_name";
    private static final String Priv_Category1_Name = "priv_category1_name";
    private static final String Priv_Category2_Name = "priv_category2_name";
    private int resource1Id;
    private int resource2Id;
    private int role1Id;
    private int role2Id;
    private int category1Id;
    private int category2Id;
    private static final String Priv1_Name = "priv1_name";

    @Before
    public void setUp() throws Exception {
        resource1Id = privilegeDao.insertResource(buildResource(Resource1_Name, Resource1_Code, Resource1_Url));
        resource2Id = privilegeDao.insertResource(buildResource(Resource2_Name, Resource2_Code, Resource2_Url));
        category1Id = privilegeDao.insertPrivilegeCategory(new PrivilegeCategory(Priv_Category1_Name, 1));
        category2Id = privilegeDao.insertPrivilegeCategory(new PrivilegeCategory(Priv_Category2_Name, 2));
        privilegeDao.insertPrivilege(new Privilege(category1Id, Priv1_Name));
        role1Id = privilegeDao.createRole(buildRole(Role1_Name));
        role2Id = privilegeDao.createRole(buildRole(Role2_Name));
    }

    @Test
    public void testGetPrivilegeCategories() {
        List<PrivilegeCategory> categories = privilegeDao.getPrivilegeCategories();
        assertNotNull(categories);
    }

    @Test
    public void testGetRoles() {
        List<Role> roles = privilegeDao.getRoles();
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }

    private Role buildRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    private Resource buildResource(String name, String code, String url) {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setCode(code);
        resource.setUrl(url);
        return resource;
    }

}
