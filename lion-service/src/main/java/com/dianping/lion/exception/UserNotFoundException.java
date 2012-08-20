/**
 * 
 */
package com.dianping.lion.exception;

/**
 * @author danson.liu
 *
 */
public class UserNotFoundException extends RuntimeBusinessException {

    private static final long serialVersionUID = 6058651975867253640L;
    
    private final String loginName;

    public UserNotFoundException(String loginName) {
        super("User[" + loginName + "] is not  found.");
        this.loginName = loginName;
    }

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }
    
}
