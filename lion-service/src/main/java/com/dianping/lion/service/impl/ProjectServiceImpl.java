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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.dianping.lion.service.CacheClient;
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
	
	private CacheClient cacheClient;
	private CacheClient projectCacheClient;

    @Override
	public List<Team> getTeams() {
	    List<Team> teams = cacheClient.get(ServiceConstants.CACHE_KEY_TEAMS);
	    if (teams == null) {
	        teams = projectDao.getTeams();
	        cacheClient.set(ServiceConstants.CACHE_KEY_TEAMS, (Serializable) teams);
	    }
        return teams;
	}

    @Override
	public List<Project> getProjects() {
	    List<Project> projects = cacheClient.get(ServiceConstants.CACHE_KEY_PROJECTS);
	    if (projects == null) {
	        projects = this.projectDao.getProjects();
	        cacheClient.set(ServiceConstants.CACHE_KEY_PROJECTS, (Serializable) projects);
	    }
		return projects;
	}
	
	@Override
	public Project getProject(int projectId) {
	    Project project = projectCacheClient.get(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
	    if (project == null) {
	        project = projectDao.getProject(projectId);
	        if (project != null) {
	        	projectCacheClient.set(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId, project);
	        }
	    }
	    return project;
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
    		cacheClient.remove(ServiceConstants.CACHE_KEY_PROJECTS);
    		cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
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
    		projectCacheClient.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + project.getId());
    		cacheClient.remove(ServiceConstants.CACHE_KEY_PROJECTS);
    		cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
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
    		projectCacheClient.remove(ServiceConstants.CACHE_KEY_PROJECT_PREFIX + projectId);
    		cacheClient.remove(ServiceConstants.CACHE_KEY_PROJECTS);
    		cacheClient.remove(ServiceConstants.CACHE_KEY_TEAMS);
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

    @Override
    public boolean isMember(int projectId, int userId) {
        Set<Integer> memberIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
        if (memberIds == null) {
            synchronized (this) {
            	memberIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
                if (memberIds == null) {
                    memberIds = new HashSet<Integer>();
                    List<ProjectMember> members = getMembers(projectId);
                    for (ProjectMember member : members) {
                        memberIds.add(member.getUserId());
                    }
                    cacheClient.set(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId, (Serializable) memberIds);
                }
            }
        }
        return memberIds.contains(userId);
    }

    @Override
    public boolean isOwner(int projectId, int userId) {
        Set<Integer> ownerIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
        if (ownerIds == null) {
            synchronized (this) {
            	ownerIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
                if (ownerIds == null) {
                    ownerIds = new HashSet<Integer>();
                    List<ProjectMember> owners = getOwners(projectId);
                    for (ProjectMember owner : owners) {
                        ownerIds.add(owner.getUserId());
                    }
                    cacheClient.set(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId, (Serializable) ownerIds);
                }
            }
        }
        return ownerIds.contains(userId);
    }

    @Override
    public boolean isOperator(int projectId, int userId) {
        Set<Integer> operatorIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
        if (operatorIds == null) {
            synchronized (this) {
            	operatorIds = cacheClient.get(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
                if (operatorIds == null) {
                    operatorIds = new HashSet<Integer>();
                    List<ProjectMember> operators = getOperators(projectId);
                    for (ProjectMember operator : operators) {
                        operatorIds.add(operator.getUserId());
                    }
                    cacheClient.set(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId, (Serializable) operatorIds);
                }
            }
        }
        return operatorIds.contains(userId);
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
                cacheClient.remove(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
            } else if (ServiceConstants.PROJECT_MEMBER.equals(memberType)) {
                projectDao.addMember(projectId, userId);
                cacheClient.remove(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
            } else if (ServiceConstants.PROJECT_OPERATOR.equals(memberType)) {
                projectDao.addOperator(projectId, userId);
                cacheClient.remove(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
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
            cacheClient.remove(ServiceConstants.CACHE_PROJECT_OWNER_PREFIX + projectId);
        } else if (ServiceConstants.PROJECT_MEMBER.equals(memberType)) {
            projectDao.deleteMember(projectId, userId);
            cacheClient.remove(ServiceConstants.CACHE_PROJECT_MEMBER_PREFIX + projectId);
        } else if (ServiceConstants.PROJECT_OPERATOR.equals(memberType)) {
            projectDao.deleteOperator(projectId, userId);
            cacheClient.remove(ServiceConstants.CACHE_PROJECT_OPERATOR_PREFIX + projectId);
        }
    }

    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

	public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}

	public void setProjectCacheClient(CacheClient projectCacheClient) {
		this.projectCacheClient = projectCacheClient;
	}

	public Project getProjectExtra(int projectId) {
		 return projectDao.getProjectExtra(projectId);
	}
}
