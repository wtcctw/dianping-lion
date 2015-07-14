package com.dianping.lion.web.action.system;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.GlobalSearch;
import com.dianping.lion.service.GlobalSearchService;
import com.dianping.lion.service.ProjectPrivilegeDecider;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.GlobalSearchCriteria;
import com.dianping.lion.web.action.config.AbstractConfigAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GlobalSearchAction extends AbstractConfigAction {

	private static final int ALL_ENV_ID = 0;

	private static final String ALL_ENV_LABEL = "全选";

	private static final Environment ALL_ENV = new Environment();

	private static boolean hasSearched = false;

	private GlobalSearchCriteria criteria = new GlobalSearchCriteria();

	private static List<Environment> environments;

	private List<GlobalSearch> globalSearches;

	@Autowired
	private GlobalSearchService globalSearchService;

	@Autowired
	private ProjectPrivilegeDecider projectPrivilegeDecider;

	static {
		ALL_ENV.setId(ALL_ENV_ID);
		ALL_ENV.setLabel(ALL_ENV_LABEL);
	}

	private void findEnvironments() {
		this.environments = new ArrayList<Environment>();
		this.environments.add(ALL_ENV);
		this.environments.addAll(environmentService.findAll());
	}

	public String globalSearchForm() {
		this.hasSearched = false;
		findEnvironments();
		return SUCCESS;
	}

	public String globalSearch() {
		this.hasSearched = true;
		findEnvironments();

		if (criteria.getKey().isEmpty()) {
			criteria.setKey(null);
		}
		if (criteria.getEnvId() == ALL_ENV_ID) {
			criteria.setEnvId(null);
		}

		this.globalSearches = globalSearchService.getGlobalSearchList(criteria);

		hideConfigByPrivilege(globalSearches);

		return SUCCESS;
	}

	private void hideConfigByPrivilege(List<GlobalSearch> globalSearches) {
		for (GlobalSearch globalSearch : globalSearches) {
			if (!projectPrivilegeDecider.hasReadConfigPrivilege(globalSearch.getId(),
					globalSearch.getEnvId(), globalSearch.getConfigId(), SecurityUtils.getCurrentUserId())) {
				globalSearch.setValue("=无权查看=");
			}
		}
	}

	public boolean getHasSearched() {
		return hasSearched;
	}

	public GlobalSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(GlobalSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public List<GlobalSearch> getGlobalSearches() {
		return globalSearches;
	}
}
