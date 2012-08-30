/**
 * Project: lion-service
 * 
 * File Created at 2012-8-20
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

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.dianping.lion.entity.User;
import com.dianping.lion.service.LDAPAuthenticationService;

/**
 * TODO Comment of LDAPAuthenticationServiceImpl
 * @author youngphy.yang
 *
 */
public class LDAPAuthenticationServiceImpl implements LDAPAuthenticationService {
	private String ldapUrl = null;
	private String ldapBaseDN = null;
	private String ldapFactory = null; 
	private LdapContext ctx = null;
	private Hashtable<String, String> env = null;
	private Control[] connCtls = null;
	
	/**
	 * @return if authentication succeeded, return user info; otherwise, return null;
	 */
	@Override
	public User authenticate(String userName, String password) {
		User user = null;
		env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,ldapFactory);
        env.put(Context.PROVIDER_URL, ldapUrl);//LDAP server
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn="+userName+","+ldapBaseDN);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try{
            ctx = new InitialLdapContext(env,connCtls);
        }catch(javax.naming.AuthenticationException e){
            System.out.println("Authentication faild: "+e.toString());
        }catch(Exception e){
            System.out.println("Something wrong while authenticating: "+e.toString());
        }
        if(ctx != null) {
        	user = getUserInfo(userName);
        }
		return user;
	}

	@SuppressWarnings("rawtypes")
    @Override
	public User getUserInfo(String cn) {
		User user = new User();
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration en = ctx.search("", "cn=" + cn, constraints);
			if (en == null) {
				System.out.println("Have no NamingEnumeration.");
			}
			if (!en.hasMoreElements()) {
				System.out.println("Have no element.");
			}
			while (en != null && en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult sr = (SearchResult) obj;
					System.out.println(sr);
					Attributes attrs = sr.getAttributes();
					user.setLoginName((String)attrs.get("cn").get());
					user.setName((String)attrs.get("displayName").get());
					user.setEmail((String)attrs.get("mail").get());
					user.setSystem(false);
					user.setCreateTime(new Date(System.currentTimeMillis()));
					user.setLocked(false);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in search():" + e);
		}

		return user;
	}

	public String getLdapUrl() {
		return ldapUrl;
	}

	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	public String getLdapBaseDN() {
		return ldapBaseDN;
	}

	public void setLdapBaseDN(String ldapBaseDN) {
		this.ldapBaseDN = ldapBaseDN;
	}

	public String getLdapFactory() {
		return ldapFactory;
	}

	public void setLdapFactory(String ldapFactory) {
		this.ldapFactory = ldapFactory;
	}

	
}
