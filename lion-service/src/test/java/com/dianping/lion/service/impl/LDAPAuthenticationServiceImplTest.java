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

import javax.naming.NamingException;

import org.junit.Test;

import com.dianping.lion.entity.User;

/**
 * LDAPAuthenticationServiceImplTest
 * @author youngphy.yang
 *
 */
public class LDAPAuthenticationServiceImplTest {
	@Test
	public void testAuthenticate() throws NamingException {
		LDAPAuthenticationServiceImpl ldapAuthenticationServiceImpl = new LDAPAuthenticationServiceImpl();
		ldapAuthenticationServiceImpl.setLdapBaseDN("OU=Technolog Department,OU=shoffice,DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setLdapFactory("com.sun.jndi.ldap.LdapCtxFactory");
		ldapAuthenticationServiceImpl.setLdapUrl("ldap://192.168.50.11:389/DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setSolidDN("cn=Users,DC=dianpingoa,DC=com");
		ldapAuthenticationServiceImpl.setSolidUsername("lionauth");
		ldapAuthenticationServiceImpl.setSolidPwd("bxHxXopGJOy78Jze3LWi");
		User user = ldapAuthenticationServiceImpl.authenticate("xx", "xx");
		assertTrue(user==null);
		//replace with the real password for test
		user = ldapAuthenticationServiceImpl.authenticate("youngphy.yang", "XXX");
		assertTrue(user != null && "杨飞".equals(user.getName()));
	}
}
