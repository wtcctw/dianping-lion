/**
 * Project: com.dianping.lion.lion-client-0.3.0
 * 
 * File Created at 2012-7-29
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
package com.dianping.lion.client;

/**
 * @author danson.liu
 * 
 */
public class LionException extends Exception {

    private static final long serialVersionUID = -277294587317829825L;

    public LionException(String msg) {
        super(msg);
    }

    public LionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LionException(Throwable cause) {
        super(cause);
    }

}
