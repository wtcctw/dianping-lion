/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-15
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;

import org.apache.log4j.Logger;

import com.dianping.lion.entity.User;

/**
 * @author danson.liu
 * 
 */
public class SecurityUtils {

    private static Logger logger = Logger.getLogger(SecurityUtils.class);
    
    private static ThreadLocal<User> currentUser = new InheritableThreadLocal<User>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static Integer getCurrentUserId() {
        User currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }

    public static void setCurrentUser(User user) {
        clearCurrentUser();
        currentUser.set(user);
    }

    public static void clearCurrentUser() {
        currentUser.remove();
    }

    private static String decode(String src) {
        int len = src.length();
        ByteBuffer bb = ByteBuffer.allocate((len - 3) / 2);
        int p = Character.digit(src.charAt(0), 16);
        int q = Character.digit(src.charAt(1), 16);
        int k = Character.digit(src.charAt(2), 16);

        for (int i = 3; i < len; i += 2) {
            byte high = (byte) (Character.digit(src.charAt(i), 16) & 0xFF);
            byte low = (byte) (Character.digit(src.charAt(i + 1), 16) & 0xFF);

            bb.put((byte) (high << 4 | low));
        }

        byte[] data = (byte[]) bb.flip().array();
        byte[] result = massage(data, p, q, k);

        try {
            return new String(result, 0, result.length - 13, "utf-8");
        } catch (IOException e) {
            return new String(result, 0, result.length - 13);
        }
    }

    public static String tryDecode(String value) {
        if (value != null && value.startsWith("~{") && value.endsWith("}")) {
            try {
                return decode(value.substring(2, value.length() - 1));
            } catch (Exception e) {
                logger.error("failed to decode: " + value + ", " + e);
            }
        }
        return value;
    }

    private static byte[] massage(byte[] data, int p, int q, int k) {
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] ^= k;
        }

        BitSet bs = toBitSet(data);
        int len = bs.size();

        for (int i = 0; i < len; i += p) {
            int j = i + q;

            if (j < len) {
                boolean flag = bs.get(i);

                bs.set(i, bs.get(j));
                bs.set(j, flag);
            }
        }

        byte[] ba = toByteArray(bs);

        return ba;
    }

    private static BitSet toBitSet(byte[] data) {
        int len = data.length;
        BitSet bs = new BitSet(len * 8);

        for (int i = 0; i < len; i++) {
            byte b = data[i];

            for (int j = 0; j < 8; j++) {
                bs.set(i * 8 + j, (b & 0x01) != 0);

                b >>= 1;
            }
        }

        return bs;
    }

    private static byte[] toByteArray(BitSet bs) {
        int len = bs.length() / 8;
        byte[] data = new byte[len];

        for (int i = 0; i < len; i++) {
            byte b = 0;

            for (int j = 7; j >= 0; j--) {
                b <<= 1;

                if (bs.get(i * 8 + j)) {
                    b++;
                }
            }

            data[i] = b;
        }

        return data;
    }
    
    
}
