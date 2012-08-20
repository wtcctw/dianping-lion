/**
 * 
 */
package com.dianping.lion.util;

import java.util.Calendar;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;


/**
 * @author danson.liu
 *
 */
public class LoginUtils {
    
    public static final String AUTH_COOKIE_NAME = "lioner";
    
    private static final char SPLIT = '|';
    private static final int SECONDS_OF_DAY = 86400; // 一天的总秒数
    private static final int EXPIRE_DATE = 31;// 保持登录的天数
    
    public static void signon(int userId, boolean keepLogin) {
        String authToken = EncryptionUtils.encryptText(getAuthenticateToken(userId, keepLogin));
        addCookie(authToken, keepLogin);
    }
    
    public static void signout() {
        addCookie(AUTH_COOKIE_NAME, "", 0);
    }
    
    public static Integer getUserId(String authToken) {
        if (StringUtils.isBlank(authToken)) {
            return null;
        }
        String decryptedText = EncryptionUtils.decryptText(authToken);
        String[] tokens = StringUtils.splitPreserveAllTokens(decryptedText, SPLIT);
        if (tokens == null || tokens.length < 5) {
            return null;
        }
        String userId = tokens[0];
        Double expired = Double.valueOf(tokens[3]);
        long currentSeconds = Calendar.getInstance().getTimeInMillis() / 1000;
        if (expired.intValue() < currentSeconds) {
            return null;
        }
        return Integer.parseInt(userId);
    }
    
    private static void addCookie(String authToken, boolean keepLogin) {
        int maxAge = -1;// 默认为会话cookie
        if (keepLogin && !StringUtils.isBlank(authToken)) {
            maxAge = EXPIRE_DATE * SECONDS_OF_DAY;
        }

        Cookie cookie = new Cookie(AUTH_COOKIE_NAME, authToken);
//        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        ServletActionContext.getResponse().addCookie(cookie);
    }
    
    private static void addCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        ServletActionContext.getResponse().addCookie(cookie);
    }

    private static String getAuthenticateToken(int userId, boolean keepLogin) {
        Calendar expireTime = Calendar.getInstance();
        if (keepLogin) {
            // 保持登录时,　dper中存储过期时间为31天(cookie过期时间也是31天)
            expireTime.add(Calendar.DAY_OF_MONTH, EXPIRE_DATE);
        } else {
            // 未保持登录时, dper中存储过期时间为2小时(cookie过期时间为会话cookie)
            expireTime.add(Calendar.HOUR_OF_DAY, 2);
        }
        
        int loginMask = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        sb.append(SPLIT);
        sb.append("");
        sb.append(SPLIT);
        sb.append(loginMask);
        sb.append(SPLIT);
        sb.append(expireTime.getTime().getTime() / 1000);
        sb.append(SPLIT);
        sb.append(keepLogin ? '1' : '0');

        return sb.toString();
    }

}
