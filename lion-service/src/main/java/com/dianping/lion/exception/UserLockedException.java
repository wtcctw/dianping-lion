/**
 * 
 */
package com.dianping.lion.exception;

/**
 * @author danson.liu
 *
 */
public class UserLockedException extends RuntimeBusinessException {

    private static final long serialVersionUID = -1936199558247639003L;
    
    public UserLockedException() {
        super("User is locked.");
    }

}
