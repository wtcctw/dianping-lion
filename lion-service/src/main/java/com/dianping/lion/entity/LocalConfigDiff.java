package com.dianping.lion.entity;

import com.dianping.lion.util.StringUtils;

public class LocalConfigDiff {

	private static final int MORE_VALUE_DISPLAY_LEN = 210;

	private static final int MAX_VALUE_DISPLAY_LEN = 60;

	private String teamName;

	private String productName;

	private String projectName;

	private String key;

	private String lvalue;

	private String rvalue;

	private ConfigInstance lconfigInstance;

	private ConfigInstance rconfigInstance;

	private boolean equal;

	private boolean diff;

	private boolean omit;

	private boolean ignored;

	public LocalConfigDiff(String key, String lvalue, String rvalue, boolean diff, boolean omit) {
		this.key = key;
		this.lvalue = lvalue;
		this.rvalue = rvalue;
		this.diff = diff;
		this.omit = omit;
		lconfigInstance = new ConfigInstance();
		lconfigInstance.setValue(lvalue);
		rconfigInstance = new ConfigInstance();
		rconfigInstance.setValue(rvalue);
	}

	public String getAbbrevLValue() {
		return StringUtils.cutString(lvalue, MAX_VALUE_DISPLAY_LEN);
	}

	public String getAbbrevRValue() {
		return StringUtils.cutString(rvalue, MAX_VALUE_DISPLAY_LEN);
	}

	public String getMoreLValue() {
		return StringUtils.cutString(lvalue, MORE_VALUE_DISPLAY_LEN);
	}

	public String getMoreRValue() {
		return StringUtils.cutString(rvalue, MORE_VALUE_DISPLAY_LEN);
	}

	public boolean isLongLValue() {
		return StringUtils.cutString(lvalue, MAX_VALUE_DISPLAY_LEN).length() != lvalue.length();
	}

	public boolean isLongRValue() {
		return StringUtils.cutString(rvalue, MAX_VALUE_DISPLAY_LEN).length() != rvalue.length();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLvalue() {
		return lvalue;
	}

	public void setLvalue(String lvalue) {
		this.lvalue = lvalue;
	}

	public String getRvalue() {
		return rvalue;
	}

	public void setRvalue(String rvalue) {
		this.rvalue = rvalue;
	}

	public boolean getEqual() {
		return equal;
	}

	public void setEqual(boolean equal) {
		this.equal = equal;
	}

	public boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public ConfigInstance getLconfigInstance() {
		return lconfigInstance;
	}

	public void setLconfigInstance(ConfigInstance lconfigInstance) {
		this.lconfigInstance = lconfigInstance;
	}

	public ConfigInstance getRconfigInstance() {
		return rconfigInstance;
	}

	public void setRconfigInstance(ConfigInstance rconfigInstance) {
		this.rconfigInstance = rconfigInstance;
	}

	public boolean isDiff() {
		return diff;
	}

	public void setDiff(boolean diff) {
		this.diff = diff;
	}

	public boolean isOmit() {
		return omit;
	}

	public void setOmit(boolean omit) {
		this.omit = omit;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
