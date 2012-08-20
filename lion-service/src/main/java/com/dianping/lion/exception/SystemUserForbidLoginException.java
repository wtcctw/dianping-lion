/**
 * 
 */
package com.dianping.lion.exception;

/**
 * 系统内建用户禁止登陆
 * @author danson.liu
 *
 */
public class SystemUserForbidLoginException extends RuntimeBusinessException {

    private static final long serialVersionUID = -5078451847259940807L;
    
    public SystemUserForbidLoginException() {
        super("System user is forbidden to login!");
    }
    
}
