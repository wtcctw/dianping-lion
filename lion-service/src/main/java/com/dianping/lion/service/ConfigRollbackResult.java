/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-8-14
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
package com.dianping.lion.service;

import java.util.Set;

/**
 * TODO Comment of ConfigRollbackResult
 * @author danson.liu
 *
 */
public class ConfigRollbackResult {

	private Set<String> notRemovedKeys;

	/**
	 * @param notRemovedKeys
	 */
	public void setNotRemovedKeys(Set<String> notRemovedKeys) {
		this.notRemovedKeys = notRemovedKeys;
	}

	/**
	 * @return the notRemovedKeys
	 */
	public Set<String> getNotRemovedKeys() {
		return notRemovedKeys;
	}

}
