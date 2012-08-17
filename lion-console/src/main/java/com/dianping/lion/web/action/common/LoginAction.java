/**
 * 
 */
package com.dianping.lion.web.action.common;

/**
 * @author danson.liu
 *
 */
public class LoginAction extends AbstractLionAction {

    /**
     * 
     */
    private static final long serialVersionUID = 6916064814214305113L;
    
    private String loginName;
    
    private String passwd;
    
    @Override
    public String execute() throws Exception {
        // TODO Auto-generated method stub
        if ("jian.liu".equals(loginName) && "liujian".equals(passwd)) {
            createSuccessStreamResponse();
        } else {
            createErrorStreamResponse("ID/密码错误!");
        }
        return SUCCESS;
    }

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return the passwd
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * @param passwd the passwd to set
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
