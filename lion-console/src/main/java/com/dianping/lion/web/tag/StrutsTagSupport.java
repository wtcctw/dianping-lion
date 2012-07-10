/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-8
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
package com.dianping.lion.web.tag;

import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.components.template.Template;
import org.apache.struts2.components.template.TemplateEngine;
import org.apache.struts2.components.template.TemplateEngineManager;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.spring.SpringObjectFactory;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author danson.liu
 *
 */
@SuppressWarnings("serial")
public abstract class StrutsTagSupport extends StrutsBodyTagSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String templatePath;
	private String templateDir = "/WEB-INF/tag/";
	@SuppressWarnings("rawtypes")
	protected Map parameters = new LinkedHashMap();
	protected TemplateEngineManager templateEngineManager;
	private static final Template FORCE_CFTL_ENGINE = new TemplateExt("/.cftl");

	private static SpringObjectFactory factory;

	private static boolean springContextInitialized;
	
	@Override
	public int doStartTag() throws JspException {
		Container container = Dispatcher.getInstance().getContainer();
        container.inject(this);
        injectWithSpringContainer();
        setProperties();
		return doFinalStartTag();
	}
	
	public int doEndTag() throws JspException {
		evaluateParams();
		JspWriter writer = pageContext.getOut();
		try {
			if (bodyContent != null && StringUtils.isNotBlank(bodyContent.getString())) {
				writer.write(bodyContent.getString().trim());
			}
			if (templatePath != null) {
				renderTemplateContent();
			}
		} catch (Exception e) {
			throw new RuntimeException("Render customized strutsTag[" + getClass() + "] failed.", e);
		}
        return EVAL_PAGE;
    }

	private void renderTemplateContent() throws Exception {
		TemplateEngine engine = templateEngineManager.getTemplateEngine(FORCE_CFTL_ENGINE, null);
		TemplateRenderingContext context = new TemplateRenderingContextExt(new TemplateExt(templatePath), pageContext.getOut(), getStack(), getParameters(), this);
		engine.renderTemplate(context);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map getParameters() {
		return this.parameters;
	}

	protected void evaluateParams() {
	}

	protected void setProperties() {
	}

	private void injectWithSpringContainer() {
		if (!springContextInitialized) {
			ApplicationContext applicationContext = (ApplicationContext) ActionContext.getContext().getApplication().get(
                    WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			if (applicationContext == null) {
				logger.warn("ApplicationContext could not be found.  Customized strutsTag classes will not be autowired.");
			} else {
				factory = new SpringObjectFactory();
                factory.setApplicationContext(applicationContext);
                factory.setAutowireStrategy(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
			}
			springContextInitialized = true;
		}
		if (factory != null) {
			factory.autoWireBean(this);
		}
	}

	protected abstract int doFinalStartTag() throws JspException;
	
	@Inject
    public void setTemplateEngineManager(TemplateEngineManager mgr) {
        this.templateEngineManager = mgr;
    }
	
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public void setTemplateName(String templateName) {
		setTemplatePath(templateDir + templateName);
	}

	static class TemplateExt extends Template {

		private final String templatePath;

		public TemplateExt(String templatePath) {
			super(StringUtils.substringBeforeLast(templatePath, "/"), "", StringUtils.substringAfterLast(templatePath, "/"));
			this.templatePath = templatePath;
		}
		
		@Override
		public List<Template> getPossibleTemplates(TemplateEngine engine) {
			List<Template> list = new ArrayList<Template>(3);
			list.add(this);
			return list;
		}
		
		@Override
		public String toString() {
			return this.templatePath;
		}
		
	}
	
	class TemplateRenderingContextExt extends TemplateRenderingContext {
		private Object tagObj;

		@SuppressWarnings("rawtypes")
		public TemplateRenderingContextExt(Template template, Writer writer,
				ValueStack stack, Map params, Object tag) {
			super(template, writer, stack, params, null);
			this.tagObj = tag;
		}

		public Object getTagObj() {
			return tagObj;
		}
		
	}
	
}
