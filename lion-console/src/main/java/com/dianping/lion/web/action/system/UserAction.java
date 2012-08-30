/**
 * 
 */
package com.dianping.lion.web.action.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.User;
import com.dianping.lion.service.UserService;
import com.dianping.lion.web.action.common.AbstractLionAction;

/**
 * @author danson.liu
 *
 */
public class UserAction extends AbstractLionAction {

    private static final long serialVersionUID = -7439336978173653883L;
    
    @Autowired
    private UserService userService;
    
    private String name;
    
    private String callback;
    
    public String ajaxGetUsers() throws JSONException {
        //TODO refactor me!
        if (name.contains("/")) {
            name = StringUtils.substringBefore(name, "/");
        }
        List<User> users = userService.findByNameOrLoginNameLike(name);
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

}
