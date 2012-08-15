/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-7-28
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
package com.dianping.lion.register;

import com.dianping.lion.entity.Environment;

/**
 * TODO Comment of ConfigMediumServiceFactory
 * @author danson.liu
 *
 */
public interface ConfigRegisterServiceFactory {

	ConfigRegisterService createRegisterService(Environment environment);

}
