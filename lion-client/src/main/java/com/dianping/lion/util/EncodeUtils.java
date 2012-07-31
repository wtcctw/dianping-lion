/**
 * Project: com.dianping.lion.lion-client-0.3.0
 * 
 * File Created at 2012-7-30
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
package com.dianping.lion.util;

/**
 * @author danson.liu
 *
 */
public class EncodeUtils {
	
	public static long getLong(final byte[] b) {
		assert b.length == 8: "Invalid number of bytes for long conversion";
		int high = getInt(new byte[] {b[0], b[1], b[2], b[3]});
		int low = getInt(new byte[] {b[4], b[5], b[6], b[7]});
		return ((long) (high) << 32) + (low & 0xFFFFFFFFL);
	}
	
	public static int getInt(final byte[] b) {
		assert b.length == 4: "Invalid number of bytes for integer conversion";
		return ((b[0] << 24) & 0xFF000000) + ((b[1] << 16) & 0x00FF0000) +
				((b[2] << 8) & 0x0000FF00) + (b[3] & 0x000000FF);
	}
	
	public static byte[] getIntBytes(final int value) {
	    final byte[] b = new byte[4];
		b[0] = (byte) ((value >>> 24) & 0xFF);
		b[1] = (byte) ((value >>> 16) & 0xFF);
		b[2] = (byte) ((value >>> 8) & 0xFF);
		b[3] = (byte) ((value) & 0xFF);
		return b;
	}

	public static byte[] getLongBytes(final long value) {
	    final byte[] b = new byte[8];
		b[0] = (byte) ((int) (value >>> 56) & 0xFF);
		b[1] = (byte) ((int) (value >>> 48) & 0xFF);
		b[2] = (byte) ((int) (value >>> 40) & 0xFF);
		b[3] = (byte) ((int) (value >>> 32) & 0xFF);
		b[4] = (byte) ((int) (value >>> 24) & 0xFF);
		b[5] = (byte) ((int) (value >>> 16) & 0xFF);
		b[6] = (byte) ((int) (value >>> 8) & 0xFF);
		b[7] = (byte) ((int) (value) & 0xFF);
		return b;
	}

}
