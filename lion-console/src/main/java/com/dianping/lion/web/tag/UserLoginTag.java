/**
 * 
 */
package com.dianping.lion.web.tag;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;

import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.entity.User;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class UserLoginTag extends StrutsTagSupport {

    private static final long serialVersionUID = 5707012869295885283L;
    
    private User currentUser;
    
    private String logoutUrl;
    
    public UserLoginTag() {
        setTemplateName("user-login.ftl");
        String logoutUrl = ConfigCache.getInstance().getProperty("cas-server-webapp.logoutUrl");
        String serverName = ConfigCache.getInstance().getProperty("lion-console.serverName");
        try {
            this.logoutUrl = logoutUrl + "?service=" + URLEncoder.encode(serverName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
    
    public String getLogoutUrl() {
        return logoutUrl;
    }

}
