/**
 * 
 */
package com.dianping.lion.exception;

/**
 * @author danson.liu
 *
 */
public class IncorrectPasswdException extends RuntimeBusinessException {

    private static final long serialVersionUID = 195448339206889022L;
    
    public IncorrectPasswdException() {
        super("Password is incorrect.");
    }

}
