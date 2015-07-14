/**
 * Project: lion-service
 * 
 * File Created at 2012-8-21
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

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.dianping.lion.entity.User;

/**
 * LDAPAuthenticationServiceImplTest
 * @author youngphy.yang
 *
 */
@Ignore
public class LDAPAuthenticationServiceImplTest {
	@Test
	public void testAuthenticate() throws Exception {
		LDAPAuthenticationServiceImpl ldapAuthenticationServiceImpl = new LDAPAuthenticationServiceImpl();
		ldapAuthenticationServiceImpl.setLdapBaseDN("OU=SHA,OU=Normal,OU=1_UserAccount,DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setLdapFactory("com.sun.jndi.ldap.LdapCtxFactory");
		ldapAuthenticationServiceImpl.setLdapUrl("ldap://192.168.50.11:389/DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setSolidDN("cn=Users,DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setSolidUsername("lionauth");
		ldapAuthenticationServiceImpl.setSolidPwd("bxHxXopGJOy78Jze3LWi");
		User user = ldapAuthenticationServiceImpl.authenticate("xx", "xx");
		assertTrue(user==null);
		//replace with the real password for test
		user = ldapAuthenticationServiceImpl.authenticate("chen.hua", "xxxxxxxx");
		assertTrue(user != null && "陈华.sh".equals(user.getName()));
	}
}
