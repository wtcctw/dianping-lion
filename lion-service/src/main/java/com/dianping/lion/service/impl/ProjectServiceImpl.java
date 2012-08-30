/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-9
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.ProjectDao;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Project;
import com.dianping.lion.entity.ProjectMember;
import com.dianping.lion.entity.Team;
import com.dianping.lion.exception.EntityNotFoundException;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProductService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.DBUtils;

/**
 * @author danson.liu
 *
 */
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private ProductService productService;
	
	private Ehcache ehcache;
	private Ehcache projectEhcache;

	@SuppressWarnings("unchecked")
    @Override
	public List<Team> getTeams() {
	    Element element = ehcache.get(ServiceConstants.CACHE_KEY_TEAMS);
	    if (element == null) {
	        List<Team> teams = projectDao.getTeams();
	        element = new Element(ServiceConstants.CACHE_KEY_TEAMS, teams);
	        ehcache.put(element);
	    }
        return (List<Team>) element.getObjectValue();
	}

	@SuppressWarnings("unchecked")
    @Override
	public List<Project> getProjects() {
	    Element element = ehcache.get(ServiceConstants.CACHE_KEY_PROJECTS);
	    if (element == null) {
	        List<Project> projects = this.projectDao.getProjects();
	        element = new Element(ServiceConstants.CACHE_KEY_PROJECTS, projects);
	        ehcache.put(element);
	    }
		return (List<Project>) element.getObjectValue();
	}
	
	@Override
	public Project getProject(int projectId) {
	    Element element = projectEhcache.get(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
	    if (element == null) {
	        Project project = projectDao.getProject(projectId);
	        if (project != null) {
	            element = new Element(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId, project);
	            projectEhcache.put(element);
	        }
	    }
	    return element != null ? (Project) element.getObjectValue(): null;
	}

	@Override
	public Project loadProject(int projectId) {
	    Project project = getProject(projectId);
	    if (project == null) {
            throw new EntityNotFoundException("Project[id=" + projectId + "] not found.");
        }
		return project;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Project> getProjectsByTeamAndProduct(Map param) {
		return projectDao.getProjectsByTeamAndProduct(param);
	}

	@Override
	public Integer addProject(Project project) {
	    try {
        		Integer projectId = projectDao.insertProject(project);
        		Product product = productService.findProductByID(project.getProductId());
        		operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Add, "创建项目: " + project.getName() 
        		        + ", 所属产品线: " + product.getName()));
        		return projectId;
	    } finally {
        		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public Integer editProject(Project project) {
	    try {
        	    Project existProject = projectDao.getProject(project.getId());
        		Integer updated = projectDao.updateProject(project);
        		Product existProduct = productService.findProductByID(existProject.getProductId());
        		Product product = productService.findProductByID(project.getProductId());
        		operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Edit, "编辑项目, before[名称: " + existProject.getName() 
        		        + ", 产品线: " + existProduct.getName() + "], after[名称: " + project.getName() + ", 产品线: " + product.getName() + "]"));
        		return updated;
	    } finally {
        		projectEhcache.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + project.getId());
        		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public Integer delProject(int projectId) {
	    try {
        	    Project project = getProject(projectId);
        		Integer deleted = projectDao.delProject(projectId);
        		if (project != null) {
        		    operationLogService.createOpLog(new OperationLog(OperationTypeEnum.Project_Delete, "删除项目: " + project.getName()));
        		}
        		return deleted;
	    } finally {
        		projectEhcache.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
        		ehcache.remove(ServiceConstants.CACHE_KEY_PROJECTS);
        		ehcache.remove(ServiceConstants.CACHE_KEY_TEAMS);
	    }
	}

	@Override
	public List<Project> getProjectsByProduct(int productId) {
		return projectDao.getProjectsByProduct(productId);
	}

	@Override
	public Project findProject(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Project name cannot be blank.");
		}
		return projectDao.findProject(name);
	}

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMember(int projectId, int userId) {
        Element element = ehcache.get(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
        if (element == null) {
            synchronized (this) {
                element = ehcache.get(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
                if (element == null) {
                    Set<Integer> memberIds = new HashSet<Integer>();
                    List<ProjectMember> members = getMembers(projectId);
                    for (ProjectMember member : members) {
                        memberIds.add(member.getUserId());
                    }
                    element = new Element(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId, memberIds);
                    ehcache.put(element);
                }
            }
        }
        return ((Set<Integer>) element.getObjectValue()).contains(userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isOwner(int projectId, int userId) {
        Element element = ehcache.get(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
        if (element == null) {
            synchronized (this) {
                element = ehcache.get(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
                if (element == null) {
                    Set<Integer> ownerIds = new HashSet<Integer>();
                    List<ProjectMember> owners = getOwners(projectId);
                    for (ProjectMember owner : owners) {
                        ownerIds.add(owner.getUserId());
                    }
                    element = new Element(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId, ownerIds);
                    ehcache.put(element);
                }
            }
        }
        return ((Set<Integer>) element.getObjectValue()).contains(userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isOperator(int projectId, int userId) {
        Element element = ehcache.get(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
        if (element == null) {
            synchronized (this) {
                element = ehcache.get(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
                if (element == null) {
                    Set<Integer> operatorIds = new HashSet<Integer>();
                    List<ProjectMember> operators = getOperators(projectId);
                    for (ProjectMember operator : operators) {
                        operatorIds.add(operator.getUserId());
                    }
                    element = new Element(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId, operatorIds);
                    ehcache.put(element);
                }
            }
        }
        return ((Set<Integer>) element.getObjectValue()).contains(userId);
    }

    @Override
    public List<ProjectMember> getMembers(int projectId) {
        return projectDao.getMembers(projectId);
    }

    @Override
    public List<ProjectMember> getOwners(int projectId) {
        return projectDao.getOwners(projectId);
    }

    @Override
    public List<ProjectMember> getOperators(int projectId) {
        return projectDao.getOperators(projectId);
    }

    @Override
    public void addMember(int projectId, String memberType, int userId) {
        try {
            if (ServiceConstants.PROJECT_OWNER.equals(memberType)) {
                projectDao.addOwner(projectId, userId);
                ehcache.remove(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
            } else if (ServiceConstants.PROJECT_MEMBER.equals(memberType)) {
                projectDao.addMember(projectId, userId);
                ehcache.remove(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
            } else if (ServiceConstants.PROJECT_OPERATOR.equals(memberType)) {
                projectDao.addOperator(projectId, userId);
                ehcache.remove(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
            }
        } catch (RuntimeException e) {
            if (!DBUtils.isDuplicateKeyError(e)) {
                throw e;
            }
        }
    }

    @Override
    public void deleteMember(int projectId, String memberType, int userId) {
        if (ServiceConstants.PROJECT_OWNER.equals(memberType)) {
            projectDao.deleteOwner(projectId, userId);
            ehcache.remove(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
        } else if (ServiceConstants.PROJECT_MEMBER.equals(memberType)) {
            projectDao.deleteMember(projectId, userId);
            ehcache.remove(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
        } else if (ServiceConstants.PROJECT_OPERATOR.equals(memberType)) {
            projectDao.deleteOperator(projectId, userId);
            ehcache.remove(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
        }
    }

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @param ehcache the ehcache to set
     */
    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    /**
     * @param projectEhcache the projectEhcache to set
     */
    public void setProjectEhcache(Ehcache projectEhcache) {
        this.projectEhcache = projectEhcache;
    }

}
