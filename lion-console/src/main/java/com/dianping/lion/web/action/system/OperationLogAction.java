/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-10
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
package com.dianping.lion.web.action.system;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.NoPrivilegeException;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.OperationLogService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.OperationLogCriteria;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class OperationLogAction extends AbstractLionAction implements ServletRequestAware{
	
	private List<Project> projects;
	private List<Environment> envs;
	private Map<String, String> opTypes = new LinkedHashMap<String, String>();
	
	private Integer projectId;
	private Project project;
	private int logid;
	private String keys;
	private Map<String, String> keyMap = new HashMap<String, String>();
	
	private OperationLogCriteria logCriteria = new OperationLogCriteria();
	private Paginater<OperationLog> paginater = new Paginater<OperationLog>();
	
	@Autowired
	private OperationLogService operationLogService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private EnvironmentService environmentService;
	
	public String getOpLogs() {
		if (!privilegeDecider.hasModulePrivilege(ServiceConstants.MODULE_OPLOG, SecurityUtils.getCurrentUserId())) {
			throw NoPrivilegeException.INSTANCE; 
		}
		initializePage();
		paginater.setMaxResults(20);
		paginater = operationLogService.getLogList(logCriteria, paginater);
		return SUCCESS;
	}
	
	public String getOpLogsByProject() {
		if (!privilegeDecider.hasReadLogPrivilege(projectId, SecurityUtils.getCurrentUserId())) {
			throw NoPrivilegeException.INSTANCE; 
		}
		initializePage();
		logCriteria.setProjectId(projectId);
		if (logCriteria.getOpType() == null) {
		    logCriteria.setOpTypeStart(OperationTypeEnum.Config_All.getBegin());
		    logCriteria.setOpTypeEnd(OperationTypeEnum.Config_All.getEnd());
		}
		paginater.setMaxResults(20);
		paginater = operationLogService.getLogList(logCriteria, paginater);
		return SUCCESS;
	}
	
	public String viewKeys() {
	    String[] keyTokens = StringUtils.split(keys, ",");
	    for (String keyToken : keyTokens) {
	        String[] tokens = StringUtils.split(keyToken, ":");
	        String keycontent = operationLogService.getLogKey(logid, tokens[1]);
	        keyMap.put(tokens[0], keycontent);
	    }
	    return SUCCESS;
	}

    protected void initializePage() {
        List<OperationTypeEnum> typeEnumList;
        if(projectId != null) {
            project = projectService.getProject(projectId);
            typeEnumList = OperationTypeEnum.projectPageSelectTypes();
        } else {
            projects = projectService.getProjects();
                Collections.sort(projects, new Comparator<Project>() {
                    @Override
                    public int compare(Project p1, Project p2) {
                        return p1.getName().compareTo(p2.getName());
                    }
                });
                typeEnumList = OperationTypeEnum.pageSelectTypes();
                opTypes.put("||false", "任意类型");
        }
        envs = environmentService.findAll();
        for (OperationTypeEnum typeEnum : typeEnumList) {
            opTypes.put(typeEnum.getBegin() + "|" + typeEnum.getEnd() + "|" + typeEnum.isProjectRelated(), typeEnum.getLabel());
        }
    }
	
	public Integer getPid() {
		return projectId;
	}

	public void setPid(Integer pid) {
		this.projectId = pid;
	}
	
	public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public Map<String, String> getOpTypes() {
		return opTypes;
	}

	public List<Environment> getEnvs() {
		return envs;
	}

    public OperationLogCriteria getLogCriteria() {
        return logCriteria;
    }

    public void setLogCriteria(OperationLogCriteria logCriteria) {
        this.logCriteria = logCriteria;
    }

    public Paginater<OperationLog> getPaginater() {
        return paginater;
    }

    public void setPaginater(Paginater<OperationLog> paginater) {
        this.paginater = paginater;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }

    public int getLogid() {
        return logid;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }
	
}
