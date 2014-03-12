package com.dianping.lion.api.http;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.User;

public class ProjectServlet extends AbstractLionServlet {

    private enum Key {id, project, product, name, owner};
    
    private enum Action {create, delete, rename};
    
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
        
        String project, product, name, owner, result = null;
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
        }
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
