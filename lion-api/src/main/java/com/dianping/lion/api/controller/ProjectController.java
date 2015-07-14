package com.dianping.lion.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.lion.api.exception.SecurityException;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.Team;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ProductService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.TeamService;
import com.dianping.lion.service.UserService;

@Controller
@RequestMapping("/project2")
public class ProjectController extends BaseController {

    @Autowired TeamService teamService;
    @Autowired ProjectService projectService;
    @Autowired ProductService productService;
    @Autowired UserService userService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(value="product", required=false) String product,
                       @RequestParam(value="team", required=false) String team) {
        int teamId = -1;
        int productId = -1;
        
        if(team != null) {
            Team tt = teamService.findTeam(team);
            if(tt == null) {
                return Result.createErrorResult(String.format("Team %s does not exist", team));
            }
            teamId = tt.getId();
        }
        if(product != null) {
            Product prd = productService.findProduct(product);
            if(prd == null) {
                return Result.createErrorResult(String.format("Product %s does not exist", product));
            }
            productId = prd.getId();
        }
        
        Map<String, Integer> param = new HashMap<String, Integer>();
        param.put("teamId", teamId);
        param.put("productId", productId);
        
        List<Project> projectList = projectService.getProjectsByTeamAndProduct(param);
        List<String> prjNameList = new ArrayList<String>();
        for(Project prj : projectList) {
            prjNameList.add(prj.getName());
        }
        
        return Result.createSuccessResult(prjNameList);
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public Result create(@RequestParam(value="id") int id, 
                      @RequestParam(value="project") String project,
                      @RequestParam(value="product") String product,
                      @RequestParam(value="owner", required=false) String owner) {
        try {
            verifyIdentity(id);
        } catch (SecurityException e) {
            return Result.createErrorResult(e.getMessage());
        }

        Project prj = projectService.findProject(project);
        if(prj != null) {
            return Result.createErrorResult(String.format("Project %s already exists", project));
        }
        
        Product prd = productService.findProduct(product);
        if(prd == null) {
            return Result.createErrorResult(String.format("Product %s does not exist", product));
        }
        
        prj = new Project();
        prj.setName(project);
        prj.setProductId(prd.getId());
        Date now = new Date();
        prj.setCreateTime(now);
        prj.setModifyTime(now);
        int projectId = projectService.addProject(prj);
        if(owner != null) {
            User user = userService.findUser(owner.trim());
            if(user != null)
                projectService.addMember(projectId, "owner", user.getId());
        }
        
        String message = String.format("Created project %s", project);
        opLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Add, message));
        return Result.createSuccessResult(message);
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result delete(@RequestParam(value="id") int id, 
                         @RequestParam(value="project") String project) {
        try {
            verifyIdentity(id);
        } catch (SecurityException e) {
            return Result.createErrorResult(e.getMessage());
        }

        Project prj = projectService.findProject(project);
        if(prj == null) {
            return Result.createErrorResult(String.format("Project %s does not exist", project));
        }
        
        projectService.delProject(prj.getId());
        
        String message = String.format("Deleted project %s", project);
        opLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Delete, message));
        return Result.createSuccessResult(message);
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public Result update(@RequestParam(value="id") int id, 
                         @RequestParam(value="project") String project,
                         @RequestParam(value="owner", required=false) String owner,
                         @RequestParam(value="member", required=false) String member,
                         @RequestParam(value="operator", required=false) String operator) {
        try {
            verifyIdentity(id);
        } catch (SecurityException e) {
            return Result.createErrorResult(e.getMessage());
        }

        Project prj = projectService.findProject(project);
        if(prj == null) {
            return Result.createErrorResult(String.format("Project %s does not exist", project));
        }
        
        if(owner != null) {
            updateProjectUsers(prj, "owner", owner);
        }
        if(member != null) {
            updateProjectUsers(prj, "member", member);
        }
        if(operator != null) {
            updateProjectUsers(prj, "operator", operator);
        }
        
        String message = String.format("Updated project %s", project);
        opLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Edit, message));
        return Result.createSuccessResult(message);
    }
    
    private void updateProjectUsers(Project project, String userType, String userList) {
        String[] users = userList.split(",");
        for(String u : users) {
            User user = userService.findUser(u.trim());
            if(user == null) {
                continue;
            }
            projectService.addMember(project.getId(), userType, user.getId());
        }
    }
    
}
