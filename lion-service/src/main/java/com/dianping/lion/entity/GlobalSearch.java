package com.dianping.lion.entity;

import java.io.Serializable;
import java.util.Date;

import com.dianping.lion.util.EnumUtils;
import com.dianping.lion.util.StringUtils;

@SuppressWarnings("serial")
public class GlobalSearch implements Serializable {

	private static final int MORE_VALUE_DISPLAY_LEN = 210;

	private static final int MAX_VALUE_DISPLAY_LEN = 80;

	private String envLabel;

	private int id;

	private String name;

	private String key;

	private String value;

	private int configId;

	private int projectId;

	private int envId;

	public String getAbbrevValue() {
		return StringUtils.cutString(value, MAX_VALUE_DISPLAY_LEN);
	}

	public String getMoreValue() {
		return StringUtils.cutString(value, MORE_VALUE_DISPLAY_LEN);
	}

	public boolean isLongValue() {
		return StringUtils.cutString(value, MAX_VALUE_DISPLAY_LEN).length() != value.length();
	}

	public String getEnvLabel() {
		return envLabel;
	}

	public void setEnvLabel(String envLabel) {
		this.envLabel = envLabel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getEnvId() {
		return envId;
	}

	public void setEnvId(int envId) {
		this.envId = envId;
	}
}
