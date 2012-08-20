/**
 * 
 */
package com.dianping.lion.util;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * @author danson.liu
 *
 */
public class EncryptionUtils {
    
    private static final String DEFAULT_AES_KEY = "2b5eb1088acaf2cc";
    private static final String DEFAULT_AES_IV =  "2Y5ZjBmY2IwNjA1N";
    
    private static final String SPACE2 = String.valueOf((char) 0);
    private static final String SPACELIST = " " + SPACE2;
    
    private static final String SPACE_INSTEAD = "~$#!^";
    private static final String SPACE2_INSTEAD = "^#~$!";
    
    public static String encryptText(String cipherText) {
        Assert.isTrue(StringUtils.isNotBlank(cipherText), "Input plainText cannot be blank.");
        try {
            byte[] bytes = cipherText.getBytes("UTF-8");
            byte[] encryptedBytes = encrypt(bytes, DEFAULT_AES_KEY.getBytes("ASCII"), DEFAULT_AES_IV.getBytes("ASCII"));
            if (!ArrayUtils.isEmpty(encryptedBytes)) {
                return parseByte2Hex(encryptedBytes);
            }
        } catch (UnsupportedEncodingException e) {
            // do nothing
        }
        return EMPTY;
    }
    
    public static String decryptText(String cipherText) {
        Assert.isTrue(StringUtils.isNotBlank(cipherText), "Input cipherText cannot be blank.");
        try {
            byte[] cipherBytes = parseHex2Byte(cipherText);
            byte[] decryptedBytes = decrypt(cipherBytes, DEFAULT_AES_KEY.getBytes("ASCII"), DEFAULT_AES_IV.getBytes("ASCII"));
            if (!ArrayUtils.isEmpty(decryptedBytes)) {
                String decrypted = new String(decryptedBytes, "UTF-8");
                return StringUtils.strip(decrypted, SPACELIST).replace(SPACE_INSTEAD, " ").replace(SPACE2_INSTEAD, SPACE2);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return EMPTY;
    }
    
    private static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(padWithZeros(bytes));
        } catch (Exception e) {
            return null;
        }
    }
    
    private static byte[] decrypt(byte[] bytes, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static byte[] padWithZeros(byte[] input) {
        int rest = input.length % 16;
        if (rest > 0) {
            byte[] result = new byte[input.length + (16 - rest)];
            System.arraycopy(input, 0, result, 0, input.length);
            return result;
        }
        return input;
    }
    
    public static byte[] parseHex2Byte(String hexText) {
        if (StringUtils.isEmpty(hexText)) {
            return null;
        }
        byte[] result = new byte[hexText.length() / 2];
        for (int i = 0; i < hexText.length() / 2; i++) {
            int high = Integer.parseInt(hexText.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexText.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    private static String parseByte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

}
