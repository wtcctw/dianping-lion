package com.dianping.lion.web.action.system;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.LocalConfigDiff;
import com.dianping.lion.entity.Project;
import com.dianping.lion.service.EnvironmentService;
import com.dianping.lion.service.LocalConfigDiffService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.vo.LocalConfigDiffCriteria;
import com.dianping.lion.web.action.common.AbstractLionAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LocalConfigDiffAction extends AbstractLionAction {

	private static Project project = new Project();

	private LocalConfigDiffCriteria criteria;

	private List<Environment> environments;

	private Environment lenvironment;

	private Environment renvironment;

	private List<LocalConfigDiff> localConfigDiffList;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private LocalConfigDiffService localConfigDiffService;

	public LocalConfigDiffAction() {
		if (criteria == null) {
			criteria = new LocalConfigDiffCriteria();
		}
	}

	public String localConfigDiff() {
		environments = environmentService.findAll();
		project = projectService.getProject(project.getId());

		localConfigDiffList = localConfigDiffService.getLocalConfigDiff(project.getId(), criteria.getLenvId(),
		      criteria.getRenvId());

		lenvironment = environmentService.findEnvByID(criteria.getLenvId());
		renvironment = environmentService.findEnvByID(criteria.getRenvId());

		return SUCCESS;
	}

	public String localConfigDiffNumDetail() {
		environments = environmentService.findAll();
		project = projectService.getProject(criteria.getProjectId());

		localConfigDiffList = localConfigDiffService.getLocalConfigDiffNumDetail(criteria.getProjectId(),
		      criteria.getLenvId(), criteria.getRenvId());

		lenvironment = environmentService.findEnvByID(criteria.getLenvId());
		renvironment = environmentService.findEnvByID(criteria.getRenvId());

		return SUCCESS;
	}

	public String localConfigOmitNumDetail() {
		environments = environmentService.findAll();
		project = projectService.getProject(criteria.getProjectId());

		localConfigDiffList = localConfigDiffService.getLocalConfigOmitNumDetail(criteria.getProjectId(),
		      criteria.getLenvId(), criteria.getRenvId());

		lenvironment = environmentService.findEnvByID(criteria.getLenvId());
		renvironment = environmentService.findEnvByID(criteria.getRenvId());

		return SUCCESS;
	}

	public LocalConfigDiffCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(LocalConfigDiffCriteria criteria) {
		this.criteria = criteria;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<LocalConfigDiff> getLocalConfigDiffList() {
		return localConfigDiffList;
	}

	public void setLocalConfigDiffList(List<LocalConfigDiff> localConfigDiffList) {
		this.localConfigDiffList = localConfigDiffList;
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<Environment> environments) {
		this.environments = environments;
	}

	public Environment getLenvironment() {
		return lenvironment;
	}

	public void setLenvironment(Environment lenvironment) {
		this.lenvironment = lenvironment;
	}

	public Environment getRenvironment() {
		return renvironment;
	}

	public void setRenvironment(Environment renvironment) {
		this.renvironment = renvironment;
	}
}
