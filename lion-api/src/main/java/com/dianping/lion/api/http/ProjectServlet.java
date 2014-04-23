package com.dianping.lion.api.http;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.User;

public class ProjectServlet extends AbstractLionServlet {

    private enum Key {id, project, product, name, owner, member, operator};
    
    private enum Action {create, delete, rename, update, moveconfig};
    
    public ProjectServlet() {
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
        
        String project, product, name, owner, member, operator, result = null;
        switch (action) {
        case create:
            project = getParam(request, Key.project);
            product = getParam(request, Key.product);
            owner = getParam(request, Key.owner, "");
            result = createProject(project, product, owner);
            response.getWriter().write("0|" + result);
            break;
        case delete:
            project = getParam(request, Key.project);
            result = deleteProject(project);
            response.getWriter().write("0|" + result);
            break;
        case rename:
            project = getParam(request, Key.project);
            name = getParam(request, Key.name);
            result = renameProject(project, name);
            response.getWriter().write("0|" + result);
            break;
        case update:
            project = getParam(request, Key.project);
            name = getParam(request, Key.name, "");
            product = getParam(request, Key.product, "");
            owner = getParam(request, Key.owner, "");
            member = getParam(request, Key.member, "");
            operator = getParam(request, Key.operator, "");
            result = updateProject(project, name, product, owner, member, operator);
            response.getWriter().write("0|" + result);
            break;
        case moveconfig:
            project = getParam(request, Key.project);
            name = getParam(request, Key.name);
            result = moveConfig(project, name);
            response.getWriter().write("0|" + result);
            break;
        }
    }
    
    public String moveConfig(String oldProjectName, String newProjectName) {
        Project oldProject = getProject(oldProjectName);
        Project newProject = getProject(newProjectName);
        List<Config> configs = configService.findConfigs(oldProject.getId());
        
        int total = configs.size(), count = 0;
        for(Config config : configs) {
            int oldConfigId = config.getId();
            String newKey = config.getKey().replace(oldProjectName, newProjectName);
            config.setProjectId(newProject.getId());
            config.setKey(newKey);
            int newConfigId = configService.createConfig(config);
            
            List<ConfigInstance> instances = configService.findInstancesByConfig(oldConfigId, null);
            for(ConfigInstance instance : instances) {
                instance.setConfigId(newConfigId);
                configService.createInstance(instance);
            }
            count++;
        }
        return "Moved " + count + "/" + total + " configs";
    }
    
    public String createProject(String project, String product, String owner) {
        Project prj = projectService.findProject(project);
        if(prj != null) {
            throw new RuntimeException("Project " + project + " already exists");
        }
        prj = new Project();
        prj.setName(project);
        int productId = getProductId(product);
        prj.setProductId(productId);
        Date now = new Date();
        prj.setCreateTime(now);
        prj.setModifyTime(now);
        int projectId = projectService.addProject(prj);
        User user = null;
        if(StringUtils.isNotBlank(owner)) {
            user = getUser(owner);
            if(user != null)
                projectService.addMember(projectId, "owner", user.getId());
        }
        String message = "Created project " + project + "(" + projectId + ")";
        if(user == null) 
            message += " but user " + owner + " does not exist";
        return message;
    }

    public String deleteProject(String project) {
        Project prj = getProject(project);
        projectService.delProject(prj.getId());
        return "Deleted project " + project + "(" + prj.getId() + ")";
    }

    public String renameProject(String project, String name) {
        Project prj = getProject(project);
        prj.setName(name);
        prj.setModifyTime(new Date());
        projectService.editProject(prj);
        return "Renamed project " + project + " to " + name;
    }
    
    // TODO Update product
    private String updateProject(String project, String name, String product, String owner, String member, String operator) {
        Project prj = getProject(project);
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotBlank(name)) {
            prj.setName(name);
            prj.setModifyTime(new Date());
            projectService.editProject(prj);
            sb.append("Renamed project " + project + " to " + name + ", ");
        }
        if(StringUtils.isNotBlank(product)) {
            int productId = getProductId(product);
            prj.setProductId(productId);
            prj.setModifyTime(new Date());
            projectService.editProject(prj);
            sb.append("Moved project " + project + " to " + product + ", ");
        }
        if(StringUtils.isNotBlank(owner)) {
            String[] owners = owner.split(",");
            for(String o : owners) {
                User user = userService.findUser(o);
                if(user == null) {
                    sb.append("user ").append(o).append(" does not exist, ");
                    continue;
                }
                projectService.addMember(prj.getId(), "owner", user.getId());
                sb.append("added owner ").append(o).append(", ");
            }
        }
        if(StringUtils.isNotBlank(member)) {
            String[] members = member.split(",");
            for(String m : members) {
                User user = userService.findUser(m);
                if(user == null) {
                    sb.append("user ").append(m).append(" does not exist, ");
                    continue;
                }
                projectService.addMember(prj.getId(), "member", user.getId());
                sb.append("added member ").append(m).append(", ");
            }
        }
        if(StringUtils.isNotBlank(operator)) {
            String[] operators = operator.split(",");
            for(String o : operators) {
                User user = userService.findUser(o);
                if(user == null) {
                    sb.append("user ").append(o).append(" does not exist, ");
                    continue;
                }
                projectService.addMember(prj.getId(), "operator", user.getId());
                sb.append("added operator ").append(o).append(", ");
            }
        }
        if(sb.length() == 0) {
            sb.append("Did nothing");
        }
        return sb.toString();
    }

    private Project getProject(String name) {
        Project prj = projectService.findProject(name);
        if(prj == null) {
            throw new RuntimeException("Project " + name + " does not exist");
        }
        return prj;
    }
    
    private int getProductId(String name) {
        Product prd = productService.findProduct(name);
        if(prd == null) {
            throw new RuntimeException("Product " + name + " does not exist");
        }
        return prd.getId();
    }
    
    private User getUser(String name) {
        User user = userService.findUser(name);
        return user;
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
