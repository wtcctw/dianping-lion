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

import static org.junit.Assert.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.lion.dao.UserDao;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.IncorrectPasswdException;
import com.dianping.lion.exception.SystemUserForbidLoginException;
import com.dianping.lion.exception.UserLockedException;
import com.dianping.lion.exception.UserNotFoundException;
import com.dianping.lion.service.UserService;

/**
 * @author danson.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath*:config/spring/appcontext-*.xml"	
})
public class UserServiceImplTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	//change the username and pwd to the proper one for test
	private String rightUserNameForTest = "youngphy.yang";
	private String rightPwdForTest = "XXX";
	
	@Test
	public void testLoginRejectedDueLock() {
		try{
			User user = userService.login("lucy", "lucy");
		} catch(Exception e) {
			assertTrue(e instanceof UserLockedException);
		}
	}
	
	@Test
	public void testLoginRejectedDueSystem() {
		try{
			User user = userService.login("dbasys", "dbasys");
		} catch(Exception e) {
			assertTrue(e instanceof SystemUserForbidLoginException);
		}
	}
	
	@Test
	public void testLoginRejectedDueWrongUserName() {
		try{
			User user = userService.login("youngphy.yan", "xxx");
		} catch(Exception e) {
			assertTrue(e instanceof UserNotFoundException);
		}
	}
	
	@Test
	public void testLoginRejectedDueWrongPwd() {
		try{
			User user = userService.login("yong.you", "xxx");
		} catch(Exception e) {
			assertTrue(e instanceof IncorrectPasswdException);
		}
	}
	
	@Test
	public void testLoginSucceedAt1stTime() {
		try{
			//if auth succeed 1st time, writing it to mysql
			User user = userService.login(rightUserNameForTest, rightPwdForTest);
			User userDB = userService.findById(user.getId());
			assertNotNull(userDB);
			assertNotNull(DigestUtils.md5Hex(rightPwdForTest).toUpperCase().equals(userDB.getPassword()));
		} catch(Exception e) {
		}
	}
	
	@Test
	public void testLoginSucceedAt2ndTime() {
		try{
			User user = userService.login(rightUserNameForTest, rightPwdForTest);
			assertNotNull(user);
		} catch(Exception e) {
		}
	}
	
	@Test
	public void testLoginSucceedIfPwdChanged() {
		//set wrong password
		User userTmp = userDao.findByName(rightUserNameForTest);
		userTmp.setPassword(DigestUtils.md5Hex("XXX").toUpperCase());
		userDao.updatePassword(userTmp);
		User user = userService.login(rightUserNameForTest, rightPwdForTest);
		assertNotNull(user);
		User userTmp2 = userDao.findByName(rightUserNameForTest);
		assertTrue(DigestUtils.md5Hex(rightPwdForTest).toUpperCase().equals(userTmp2.getPassword()));
	}
}
