/**
 * 
 */
package com.dianping.lion.web.action.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.User;
import com.dianping.lion.exception.IncorrectPasswdException;
import com.dianping.lion.exception.SystemUserForbidLoginException;
import com.dianping.lion.exception.UserLockedException;
import com.dianping.lion.exception.UserNotFoundException;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.LoginUtils;

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
    
    @Autowired
    private UserService userService;
    
    public String login() throws Exception {
        try {
            User user = userService.login(loginName, passwd);
            if (user != null) {
                LoginUtils.signon(user.getId(), false);
                createSuccessStreamResponse();
            } else {
                createErrorStreamResponse("ID/密码错误!");
            }
        } catch (SystemUserForbidLoginException e) {
            createErrorStreamResponse("系统用户禁止登陆!");
        } catch (UserLockedException e) {
            createErrorStreamResponse("该用户已被锁定，禁止登陆!");
        } catch (UserNotFoundException e) {
            createErrorStreamResponse("该用户不存在!");
        } catch (IncorrectPasswdException e) {
            createErrorStreamResponse("密码不正确!");
        } catch (RuntimeException e) {
            createErrorStreamResponse("登陆失败, 未知异常!");
        }
        return SUCCESS;
    }
    
    public String logout() throws Exception {
        LoginUtils.signout();
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

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
