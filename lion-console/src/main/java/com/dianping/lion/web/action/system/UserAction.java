/**
 * 
 */
package com.dianping.lion.web.action.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.User;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.UserCriteria;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
public class UserAction extends AbstractLionAction {

    private static final long serialVersionUID = -7439336978173653883L;
    
    @Autowired
    private UserService userService;
    
    private UserCriteria userCriteria = new UserCriteria();
	private Paginater<User> paginater = new Paginater<User>();
    
	private int id;
	
	private User user;
	
    private String name;
    
    private boolean includeSystem;
    
    private String callback;
    
    @Override
    protected void checkModulePrivilege() {
    	if (!privilegeService.isUserHasResourcePrivilege(SecurityUtils.getCurrentUserId(), ServiceConstants.RES_CODE_USER)) {
			throw NoPrivilegeException.INSTANCE;
		}
    }
    
    public String list() {
    	paginater.setMaxResults(20);
    	this.paginater = userService.getUsers(userCriteria, paginater);
    	return SUCCESS;
    }
    
    public String ajaxList() {
    	paginater.setMaxResults(20);
    	this.paginater = userService.getUsers(userCriteria, paginater);
    	return SUCCESS;
    }
    
    public String ajaxGetUser() {
    	try {
	    	User user = userService.loadNoPasswdById(id);
	    	createSuccessStreamResponse(user);
    	} catch (RuntimeException e) {
    		createErrorStreamResponse("load user failed");
    	}
    	return SUCCESS;
    }
    
    public String ajaxGetUsers() throws JSONException {
        if (name.contains("/")) {
            name = StringUtils.substringBefore(name, "/");
        }
        List<User> users = userService.findByNameOrLoginNameLike(name, includeSystem);
        StringBuilder content = new StringBuilder();
        if (callback != null) {
            content.append(callback);
        }
        content.append("({array:");
        if (!users.isEmpty()) {
            content.append(JSONUtil.serialize(users).replaceAll("null", "\"\""));
        } else {
            content.append("[]");
        }
        content.append("})");
        createStreamResponse(content.toString());
        return SUCCESS;
    }
    
    public String editUser() {
    	try {
	    	User user = userService.findById(this.user.getId());
	    	if (user != null) {
	    		user.setLocked(this.user.isLocked());
	    		user.setOnlineConfigView(this.user.isOnlineConfigView());
	    		userService.update(user);
	    	}
	    	createSuccessStreamResponse();
    	} catch (Exception e) {
    		logger.error("Edit user failed.", e);
    		createErrorStreamResponse(e.getMessage());
    	}
    	return SUCCESS;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public void setQ(String query) {
        this.name = query;
    }

	public UserCriteria getUserCriteria() {
		return userCriteria;
	}

	public void setUserCriteria(UserCriteria userCriteria) {
		this.userCriteria = userCriteria;
	}

	public Paginater<User> getPaginater() {
		return paginater;
	}

	public void setPaginater(Paginater<User> paginater) {
		this.paginater = paginater;
	}

	/**
     * @return the callback
     */
    public String getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(String callback) {
        this.callback = callback;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isIncludeSystem() {
		return includeSystem;
	}

	public void setIncludeSystem(boolean includeSystem) {
		this.includeSystem = includeSystem;
	}

}
