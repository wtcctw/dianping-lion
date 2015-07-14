package com.dianping.lion.web.action.system;

import com.dianping.cat.Cat;
import com.dianping.lion.entity.*;
import com.dianping.lion.exception.RuntimeBusinessException;
import com.dianping.lion.service.*;
import com.dianping.lion.vo.GlobalConfigDiffCriteria;
import com.dianping.lion.web.action.common.AbstractLionAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GlobalConfigDiffAction extends AbstractLionAction {

	private static final int ALL_ID = -1;

	private static final String ALL_NAME = "全部";

	private static final Team ALL_TEAM = new Team();

	private static final Product ALL_PRODUCT = new Product();

	private static final Project ALL_PROJECT = new Project();

	private static final int UPDATE_INTERVAL_MINUTE = 10;

	static {
		ALL_TEAM.setId(ALL_ID);
		ALL_TEAM.setName(ALL_NAME);
		ALL_PRODUCT.setId(ALL_ID);
		ALL_PRODUCT.setName(ALL_NAME);
		ALL_PROJECT.setId(ALL_ID);
		ALL_PROJECT.setName(ALL_NAME);
	}

	;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private LocalConfigDiffService localConfigDiffService;

	@Autowired
	private GlobalConfigDiffService globalConfigDiffService;

	private boolean tableDisplay;

	private static List<Team> selectorTeams = new ArrayList<Team>();

	private static List<Product> selectorProducts = new ArrayList<Product>();

	private static List<Project> selectorProjects = new ArrayList<Project>();

	private List<GlobalConfigDiff> globalConfigDiffs;

	private List<Environment> environments;

	private GlobalConfigDiffCriteria criteria;

	public GlobalConfigDiffAction() {
		tableDisplay = false;
	}

	public String selectTeam() {
		environments = environmentService.findAll();

		initSelectorTeams();
		initSelectorProducts();
		initSelectorProjects();

		selectorTeams.addAll(teamService.findAll());

		return SUCCESS;
	}

	public String ajaxSelectProduct() {
		initSelectorProducts();
		selectorProducts.addAll(productService.findProductByTeamID(criteria.getTeamId()));
		createSuccessStreamResponse(selectorProducts);

		return SUCCESS;
	}

	public String ajaxSelectProject() {
		initSelectorProjects();
		selectorProjects.addAll(projectService.getProjectsByProduct(criteria.getProductId()));
		createSuccessStreamResponse(selectorProjects);

		return SUCCESS;
	}

	public String globalConfigDiff() {
		tableDisplay = true;
		environments = environmentService.findAll();

		boolean exchanged = exchange();

		List<Project> projects = getProjects(criteria.getTeamId(), criteria.getProductId(), criteria.getProjectId());

		List<GlobalConfigDiff> trackedGlobalConfigDiffs = getTrackedGlobalConfigDiffs(criteria.getLenvId(),
		      criteria.getRenvId(), projects2Ids(projects));

		trackedGlobalConfigDiffs = getUpdateGlobalConfigDiffs(trackedGlobalConfigDiffs);

		trackedGlobalConfigDiffs = getUntrackedGlobalConfigDiffs(trackedGlobalConfigDiffs, projects2Ids(projects));

		globalConfigDiffs = trackedGlobalConfigDiffs;

		Collections.sort(globalConfigDiffs);

		restore(exchanged);

		return SUCCESS;
	}

	private List<GlobalConfigDiff> getTrackedGlobalConfigDiffs(int lenvId, int renvId, List<Integer> projectIds) {
		return globalConfigDiffService.getGlobalConfigDiffList(lenvId, renvId, projectIds);
	}

	private List<GlobalConfigDiff> getUpdateGlobalConfigDiffs(List<GlobalConfigDiff> trackedGlobalConfigDiffs) {
		for (GlobalConfigDiff trackedGlobalConfigDiff : trackedGlobalConfigDiffs) {
			if (needToUpdate(trackedGlobalConfigDiff)) {
				trackedGlobalConfigDiff = queryUpdateGlobalConfigDiff(trackedGlobalConfigDiff);
				setUpdateGlobalConfigDiff(trackedGlobalConfigDiff);
			}
		}

		return trackedGlobalConfigDiffs;
	}

	private List<GlobalConfigDiff> getUntrackedGlobalConfigDiffs(List<GlobalConfigDiff> trackedGlobalConfigDiffs,
	      List<Integer> projectIds) {
		int trackedGlobalConfigDiffNum = trackedGlobalConfigDiffs.size();
		int gloablConfigDiffNum = projectIds.size();
		if (trackedGlobalConfigDiffNum == gloablConfigDiffNum) {
			return trackedGlobalConfigDiffs;
		} else if (trackedGlobalConfigDiffNum < gloablConfigDiffNum) {
			List<Integer> untrackedProjectIds = getUntrackedProjectIds(globalConfigDiffs2Ids(trackedGlobalConfigDiffs),
			      projectIds);

			for (Integer untrackedProjectId : untrackedProjectIds) {
				GlobalConfigDiff untrackedGlobalConfigDiff = queryUntrackedGlobalConfigDiff(untrackedProjectId);
				trackedGlobalConfigDiffs.add(untrackedGlobalConfigDiff);
				setUntrackedGlobalConfigDiff(untrackedGlobalConfigDiff);
			}

			return trackedGlobalConfigDiffs;
		} else {
			System.out.println("HELLO WORLD");
			throw new RuntimeException();
		}
	}

	private boolean needToUpdate(GlobalConfigDiff globalConfigDiff) {
		long oldTime = globalConfigDiff.getUpdateTime().getTime();
		long curTime = System.currentTimeMillis();
		long interval = 60 * 1000 * UPDATE_INTERVAL_MINUTE;
		if (oldTime + interval < curTime) {
			return true;
		} else {
			return false;
		}
	}

	private GlobalConfigDiff queryUpdateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff) {
		Object[] objects = localConfigDiffService.getLocalConfigDiffOverview(globalConfigDiff.getProjectId(),
		      criteria.getLenvId(), criteria.getRenvId());
		globalConfigDiff.setDiffNum((Integer) objects[0]);
		globalConfigDiff.setOmitNum((Integer) objects[1]);
		globalConfigDiff.setUpdateTime(new Date(System.currentTimeMillis()));

		return globalConfigDiff;
	}

	private void setUpdateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff) {
		globalConfigDiffService.updateGlobalConfigDiff(globalConfigDiff);
	}

	private List<Integer> getUntrackedProjectIds(List<Integer> trackedProjectIds, List<Integer> projectIds) {
		List<Integer> untrackedProjectIds = new ArrayList<Integer>();

		for (Integer projectId : projectIds) {
			if (!trackedProjectIds.contains(projectId)) {
				untrackedProjectIds.add(projectId);
			}
		}

		return untrackedProjectIds;
	}

	private GlobalConfigDiff queryUntrackedGlobalConfigDiff(int untrackedProjectId) {

		Object[] objects = localConfigDiffService.getLocalConfigDiffOverview(untrackedProjectId, criteria.getLenvId(),
		      criteria.getRenvId());

		Project project = projectService.getProjectExtra(untrackedProjectId);

		GlobalConfigDiff untrackedGlobalConfigDiff = new GlobalConfigDiff();

		untrackedGlobalConfigDiff.setProjectId(untrackedProjectId);
		untrackedGlobalConfigDiff.setTeamName(project.getTeamName());
		untrackedGlobalConfigDiff.setProductName(project.getProductName());
		untrackedGlobalConfigDiff.setProjectName(project.getName());
		untrackedGlobalConfigDiff.setLenvId(criteria.getLenvId());
		untrackedGlobalConfigDiff.setRenvId(criteria.getRenvId());
		untrackedGlobalConfigDiff.setDiffNum((Integer) objects[0]);
		untrackedGlobalConfigDiff.setOmitNum((Integer) objects[1]);
		untrackedGlobalConfigDiff.setUpdateTime(new Date(System.currentTimeMillis()));

		return untrackedGlobalConfigDiff;
	}

	private void setUntrackedGlobalConfigDiff(GlobalConfigDiff untrackedGlobalConfigDiff) {
		globalConfigDiffService.setGlobalConfigDiff(untrackedGlobalConfigDiff);
	}

	private boolean exchange() {
		if (criteria.getLenvId() > criteria.getRenvId()) {
			int t = criteria.getLenvId();
			criteria.setLenvId(criteria.getRenvId());
			criteria.setRenvId(t);
			return true;
		} else {
			return false;
		}
	}

	private void restore(boolean exchanged) {
		if (exchanged) {
			int t = criteria.getLenvId();
			criteria.setLenvId(criteria.getRenvId());
			criteria.setRenvId(t);
		}
	}

	private List<Project> getProjects(int teamId, int productId, int projectId) {
		if (isAllSelected(teamId)) {
			return getProjectsByALL();
		} else if (isAllSelected(productId)) {
			return getProjectsByTeam(teamId);
		} else if (isAllSelected(projectId)) {
			return getProjectsByProduct(productId);
		} else {
			return getProjectsByProject(projectId);
		}
	}

	private List<Project> getProjectsByALL() {
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(projectService.getProjects());
		return projects;
	}

	private List<Project> getProjectsByTeam(int teamId) {
		List<Project> projects = new ArrayList<Project>();
		for (Product product : productService.findProductByTeamID(teamId)) {
			projects.addAll(projectService.getProjectsByProduct(product.getId()));
		}
		return projects;
	}

	private List<Project> getProjectsByProduct(int productId) {
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(projectService.getProjectsByProduct(productId));
		return projects;
	}

	private List<Project> getProjectsByProject(int projectId) {
		List<Project> projects = new ArrayList<Project>();
		projects.add(projectService.getProject(projectId));
		return projects;
	}

	private List<Integer> projects2Ids(List<Project> projects) {
		List<Integer> projectIds = new ArrayList<Integer>();
		for (Project project : projects) {
			projectIds.add(project.getId());
		}
		return projectIds;
	}

	private List<Integer> globalConfigDiffs2Ids(List<GlobalConfigDiff> globalConfigDiffs) {
		List<Integer> projectIds = new ArrayList<Integer>();
		for (GlobalConfigDiff globalConfigDiff : globalConfigDiffs) {
			projectIds.add(globalConfigDiff.getProjectId());
		}
		return projectIds;
	}

	private boolean isAllSelected(int id) {
		return id == ALL_ID;
	}

	private void initSelectorTeams() {
		selectorTeams.clear();
		selectorTeams.add(ALL_TEAM);
	}

	private void initSelectorProducts() {
		selectorProducts.clear();
		selectorProducts.add(ALL_PRODUCT);
	}

	private void initSelectorProjects() {
		selectorProjects.clear();
		selectorProjects.add(ALL_PROJECT);
	}

	public List<Project> getSelectorProjects() {
		return selectorProjects;
	}

	public void setSelectorProjects(List<Project> selectorProjects) {
		this.selectorProjects = selectorProjects;
	}

	public List<Product> getSelectorProducts() {
		return selectorProducts;
	}

	public void setSelectorProducts(List<Product> selectorProducts) {
		this.selectorProducts = selectorProducts;
	}

	public List<Team> getSelectorTeams() {
		return selectorTeams;
	}

	public void setSelectorTeams(List<Team> selectorTeams) {
		this.selectorTeams = selectorTeams;
	}

	public static int getAllId() {
		return ALL_ID;
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<Environment> environments) {
		this.environments = environments;
	}

	public GlobalConfigDiffCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(GlobalConfigDiffCriteria criteria) {
		this.criteria = criteria;
	}

	public List<GlobalConfigDiff> getGlobalConfigDiffs() {
		return globalConfigDiffs;
	}

	public void setGlobalConfigDiffs(List<GlobalConfigDiff> globalConfigDiffs) {
		this.globalConfigDiffs = globalConfigDiffs;
	}

	public static String getAllName() {
		return ALL_NAME;
	}

	public boolean getTableDisplay() {
		return tableDisplay;
	}
}
