package com.dianping.lion.api.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.User;
import com.dianping.lion.util.SecurityUtils;

public class ConfigServlet extends AbstractLionServlet {

    private enum Key {env, id, project, key, keys, group, value, prefix};
    
    private enum Action {get, set, list};
    
    public ConfigServlet () {
        this.requestIdentityRequired = false;
    }
    
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response, String querystr) throws Exception {
        String pathInfo = request.getPathInfo();
        if(pathInfo == null) 
            throw new RuntimeException("Action is null");
        
        pathInfo = pathInfo.toLowerCase().substring(1);
        Action action = null;
        try {
            action = Action.valueOf(pathInfo);
        } catch(RuntimeException e) {
            throw new RuntimeException("Unknown action " + pathInfo);
        }
        
        String env, id, project, key, keys, group, value, prefix = null;
        switch (action) {
        case get:
            env = getParam(request, Key.env);
            id = getParam(request, Key.id);
            key = getParam(request, Key.key, "");
            keys = getParam(request, Key.keys, "");
            prefix = getParam(request, Key.prefix, "");
            group = getParam(request, Key.group, "");
            if(StringUtils.isNotBlank(key)) {
                value = getConfig(env, id, key, group);
            } else if(StringUtils.isNotBlank(keys)) {
                value = getConfigs(env, id, keys, group);
            } else if(StringUtils.isNotBlank(prefix)) {
                value = getConfigsByPrefix(env, id, prefix, group);
            } else {
                throw new NullPointerException("Key is null");
            }
            response.getWriter().write("0|" + value);
            break;
        case set:
            throw new UnsupportedOperationException("Not implemented yet");
        case list:
            prefix = getParam(request, Key.prefix);
            String keyList = getKeyList(prefix);
            response.getWriter().write("0|" + keyList);
            break;
        }
    }
    
    private String getConfigsByPrefix(String env, String id, String prefix, String group) {
        int envId = getEnvId(env);
        verifyIdentity(envId, id);
        
        prefix = prefix.trim();
        if(prefix.length() < 5) {
            throw new RuntimeException("Prefix is too short");
        }
        List<ConfigInstance> ciList = configService.findInstancesByPrefix(prefix, envId, group);
        
        Map<String, String> keyValue = new HashMap<String, String>();
        for(ConfigInstance ci : ciList) {
            keyValue.put(ci.getRefkey(), SecurityUtils.tryDecode(ci.getValue()));
        }
        return new JSONObject(keyValue).toString();
    }

    private String getConfigs(String env, String id, String keys, String group) {
        int envId = getEnvId(env);
        verifyIdentity(envId, id);
        
        List<String> keyList = convertToList(keys);
        List<ConfigInstance> ciList = configService.findInstancesByKeys(keyList, envId, group);
        
        Map<String, String> keyValue = new HashMap<String, String>();
        for(ConfigInstance ci : ciList) {
            keyValue.put(ci.getRefkey(), SecurityUtils.tryDecode(ci.getValue()));
        }
        return new JSONObject(keyValue).toString();
    }

    private List<String> convertToList(String keys) {
        if(StringUtils.isEmpty(keys)) 
            return Collections.emptyList();
        
        String[] keyArray = keys.split(",");
        List<String> keyList = new ArrayList<String>();
        for(String key : keyArray) {
            key = key.trim();
            if(!key.isEmpty())
                keyList.add(key);
        }
        return keyList;
    }

    private String getConfig(String env, String id, String key, String group) {
        int envId = getEnvId(env);
        verifyIdentity(envId, id);
        ConfigInstance ci = configService.findInstance(key, envId, group);
        return SecurityUtils.tryDecode(ci.getValue());
    }

    private String getKeyList(String prefix) {
        List<Config> configList = configService.findConfigByPrefix(prefix);
        StringBuilder sb = new StringBuilder();
        for(Config config : configList) {
            sb.append(config.getKey()).append(',');
        }
        return sb.toString();
    }

    private int getEnvId(String env) {
        Environment environment = environmentService.findEnvByName(env);
        if (environment == null) {
            throw new RuntimeException("Invalid environment " + env);
        }
        return environment.getId();
    }
    
    private void verifyIdentity(int envId, String id) {
        if(envId !=4 && envId != 5)
            return;
        int userId = 0;
        try {
            userId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid id " + id);
        }
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("Invalid id " + id);
        }
        if (!user.isSystem()) {
            throw new RuntimeException("Only support user with system level");
        }
    }
    
    private String getParam(HttpServletRequest request, Key key) {
        String param = request.getParameter(key.name());
        if(param == null)
            throw new RuntimeException(key.name() + " is null");
        return param;
    }

    private String getParam(HttpServletRequest request, Key key, String defaultValue) {
        String param = request.getParameter(key.name());
        return param == null ? defaultValue : param;
    }
}
