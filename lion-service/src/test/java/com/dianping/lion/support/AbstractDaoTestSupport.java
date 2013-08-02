/**
 * Project: com.dianping.lion.lion-console-0.0.1
 *
 * File Created at 2012-7-17
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
package com.dianping.lion.support;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author danson.liu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:config/spring/appcontext-core.xml",
	"classpath:config/spring/appcontext-db.xml",
	"classpath:config/spring/appcontext-ibatis.xml",
	"classpath:config/spring/appcontext-dao.xml"
})
@TransactionConfiguration(transactionManager = "Lion.TransactionManager", defaultRollback = false)
@Transactional
public abstract class AbstractDaoTestSupport {

}
