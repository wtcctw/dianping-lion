package com.dianping.lion.web.tag;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.service.EnvironmentService;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainGlobalSearch extends StrutsTagSupport {

	private static final int ALL_ENV_ID = 0;

	private static final String ALL_ENV_LABEL = "全选";

	@Autowired
	private EnvironmentService environmentService;

	private List<Environment> environments;

	private boolean listSearchPage;

	public MainGlobalSearch() {
		setTemplateName("main-global-search.ftl");
	}

	protected int doFinalStartTag() {
		ServletRequest servletRequest = getRequest();
		if (((StrutsRequestWrapper)servletRequest).getServletPath().equals("/index.ftl")) {
			listSearchPage = true;
		} else {
			listSearchPage = false;
		}

		environments = new ArrayList<Environment>();
		Environment allEnvironment = new Environment();
		allEnvironment.setId(ALL_ENV_ID);
		allEnvironment.setLabel(ALL_ENV_LABEL);
		environments.add(allEnvironment);
		environments.addAll(environmentService.findAll());
		return SKIP_BODY;
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public boolean isListSearchPage() {
		return listSearchPage;
	}
}
