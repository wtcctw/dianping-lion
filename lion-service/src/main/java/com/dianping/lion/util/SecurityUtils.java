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
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.util.Random;

import org.apache.log4j.Logger;
import org.unidal.helper.Bytes;

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
    
    public static String tryEncode(String value) {
        if (value != null && !(value.startsWith("~{") && value.endsWith("}"))) {
            try {
                String code = encode(value);
                return "~{" + code + "}";
            } catch (Exception e) {
                logger.error("failed to encode: " + value + ", " + e);
            }
        }
        return value;
    }
    
    private static String encode(String src) throws Exception {
        int p = new Random().nextInt(5) + 3;

        return encode(src, p, p / 2 + 1, p * 2 + 1);
    }

    private static String encode(String src, int p, int q, int k) throws Exception {
        byte[] data = padding(src);

        Bytes.forBits().swap(data, p, q);
        Bytes.forBits().mask(data, k);

        return wrapup(data, p, q, k);
    }

    private static byte[] padding(String str) throws Exception {
        byte[] data = str.getBytes("utf-8");
        ByteBuffer bb = ByteBuffer.allocate(data.length + 13);

        bb.put(data);
        bb.put((byte) 0);
        bb.put(Inet4Address.getLocalHost().getAddress());
        bb.putLong(System.currentTimeMillis());

        return (byte[]) bb.flip().array();
    }

    private static String wrapup(byte[] data, int p, int q, int k) {
        StringBuilder sb = new StringBuilder(data.length * 2 + 3);

        sb.append(Integer.toHexString(p | 0x08));
        sb.append(Integer.toHexString(q));
        sb.append(Integer.toHexString(k));

        for (byte d : data) {
            sb.append(Integer.toHexString(d >> 4 & 0x0F));
            sb.append(Integer.toHexString(d & 0x0F));
        }

        return sb.toString();
    }
    
    private static void mask(byte[] data, int k) {
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] ^= k;
        }
    }

    private static void swap(byte[] data, int p, int q) {
        int len = data.length * 8;

        for (int i = 0; i < len; i += p) {
            int j = i + q;

            if (j < len) {
                byte b1 = data[i / 8];
                byte b2 = data[j / 8];
                int f1 = b1 & (1 << (i % 8));
                int f2 = b2 & (1 << (j % 8));

                if ((f1 != 0) != (f2 != 0)) {
                    data[i / 8] ^= 1 << (i % 8);
                    data[j / 8] ^= 1 << (j % 8);
                }
            }
        }
    }

    private static String decode(String src) {
        int len = src.length();
        ByteBuffer bb = ByteBuffer.allocate((len - 3) / 2);
        int p = Character.digit(src.charAt(0), 16) & 0x07;
        int q = Character.digit(src.charAt(1), 16);
        int k = Character.digit(src.charAt(2), 16);

        for (int i = 3; i < len; i += 2) {
            byte high = (byte) (Character.digit(src.charAt(i), 16) & 0xFF);
            byte low = (byte) (Character.digit(src.charAt(i + 1), 16) & 0xFF);

            bb.put((byte) (high << 4 | low));
        }

        byte[] data = (byte[]) bb.flip().array();

        mask(data, k);
        swap(data, p, q);

        try {
            return new String(data, 0, data.length - 13, "utf-8");
        } catch (IOException e) {
            return new String(data, 0, data.length - 13);
        }
    }
}
