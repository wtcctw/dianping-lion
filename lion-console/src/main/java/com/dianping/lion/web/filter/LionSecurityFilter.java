/**
 * 
 */
package com.dianping.lion.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.dianping.lion.entity.Resource;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.PrivilegeService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.LoginUtils;
import com.dianping.lion.util.SecurityUtils;

/**
 * @author danson.liu
 *
 */
public class LionSecurityFilter implements Filter {
    
    private UserService userService;
    private PrivilegeService privilegeService;
    
    private static final String SECURITY_CHECKED = "security_checked";
    private static final Object securityObj = new Object();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
        Object securityChecked = request.getAttribute(SECURITY_CHECKED);
        boolean authEntry = false;
        if (securityChecked == null) {
//            Cookie authCookie = WebUtils.getCookie((HttpServletRequest) request, LoginUtils.AUTH_COOKIE_NAME);
//            Integer userId = null;
//            if (authCookie != null) {
//                userId = LoginUtils.getUserId(authCookie.getValue());
//            }
//            if (userId != null) {
//                User user = userService.findById(userId);
//                if (user != null) {
//                    SecurityUtils.setCurrentUser(user);
//                }
//            } 
            // 接入方可以通过HttpServletRequest对象获取身份信息，方法是request.getRemoteUser()，
            // 返回是一个字符串，类似"zhijun.ding|-44215|0009562|丁志君"，此处的身份信息用竖线隔开，从左到右依次是 点评通行证，LoginId，工号，姓名。
            String userInfo = ((HttpServletRequest) request).getRemoteUser();
            if(userInfo != null) {
                String[] parts = userInfo.split("\\|");
                if(parts.length == 4) {
                    User user = userService.findByName(parts[0]);
                    if (user != null) {
                        SecurityUtils.setCurrentUser(user);
                    } else {
                        user = new User();
                        user.setLoginName(parts[0]);
                        user.setEmail(parts[0] + "@dianping.com");
                        user.setName(parts[3]);
                        user.setSystem(false);
                        user.setLocked(false);
                        user.setOnlineConfigView(false);
                        user.setCreateTime(new Date());
                        int userId = userService.insertUser(user);
                        user.setId(userId);
                        SecurityUtils.setCurrentUser(user);
                    }
                }
            }
            authEntry = true;
            request.setAttribute(SECURITY_CHECKED, securityObj);
        }
        
        checkIfHasUrlPrivilege((HttpServletRequest) request);
        
        try {
            chain.doFilter(request, response);
        } finally {
            if (authEntry) {
                SecurityUtils.clearCurrentUser();
                request.removeAttribute(SECURITY_CHECKED);
            }
        }
    }

    private void checkIfHasUrlPrivilege(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (StringUtils.endsWith(requestUri, ".vhtml")) {
			if (StringUtils.isNotEmpty(contextPath) && requestUri.startsWith(contextPath)) {
				requestUri = requestUri.substring(contextPath.length());
			}
			Resource resource = privilegeService.getResourceMatchUrl(requestUri);
			if (resource != null) {
				Integer currentUserId = SecurityUtils.getCurrentUserId();
				if (currentUserId != null) {
					boolean hasResourcePrivilege = privilegeService.isUserHasResourcePrivilege(currentUserId, resource.getId());
					if (!hasResourcePrivilege) {
						throw NoPrivilegeException.INSTANCE;
					}
				} else {
					throw NoPrivilegeException.INSTANCE;
				}
			}
		}
	}

	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        userService = (UserService) BeanFactoryUtils.beanOfType(applicationContext, UserService.class);
        privilegeService = (PrivilegeService) BeanFactoryUtils.beanOfType(applicationContext, PrivilegeService.class);
    }
    
    @Override
    public void destroy() {
    }

}
