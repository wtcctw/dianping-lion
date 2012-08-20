/**
 * 
 */
package com.dianping.lion.web.tag;

import javax.servlet.jsp.JspException;

import com.dianping.lion.entity.User;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class UserLoginTag extends StrutsTagSupport {

    private static final long serialVersionUID = 5707012869295885283L;
    
    private User currentUser;
    
    public UserLoginTag() {
        setTemplateName("user-login.ftl");
    }

    @Override
    protected int doFinalStartTag() throws JspException {
        this.currentUser = SecurityUtils.getCurrentUser();
        return SKIP_BODY;
    }

    /**
     * @return the currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

}
