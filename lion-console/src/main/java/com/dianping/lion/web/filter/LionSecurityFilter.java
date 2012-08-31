/**
 * 
 */
package com.dianping.lion.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.dianping.lion.entity.User;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.LoginUtils;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class LionSecurityFilter implements Filter {
    
    private UserService userService;
    
    private static final String SECURITY_CHECKED = "security_checked";
    private static final Object securityObj = new Object();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
        Object securityChecked = request.getAttribute(SECURITY_CHECKED);
        boolean authEntry = false;
        if (securityChecked == null) {
            Cookie authCookie = WebUtils.getCookie((HttpServletRequest) request, LoginUtils.AUTH_COOKIE_NAME);
            Integer userId = null;
            if (authCookie != null) {
                userId = LoginUtils.getUserId(authCookie.getValue());
            }
            if (userId != null) {
                User user = userService.findById(userId);
                if (user != null) {
                    SecurityUtils.setCurrentUser(user);
                }
            } 
            authEntry = true;
            request.setAttribute(SECURITY_CHECKED, securityObj);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (authEntry) {
                SecurityUtils.clearCurrentUser();
                request.removeAttribute(SECURITY_CHECKED);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        userService = (UserService) BeanFactoryUtils.beanOfType(applicationContext, UserService.class);
    }
    
    @Override
    public void destroy() {
    }

}
