package com.dianping.lion.entity;

import java.util.Date;

public class GlobalConfigDiff implements Comparable<GlobalConfigDiff> {

	private String teamName;

	private String productName;

	private String projectName;

	private int id;

	private int projectId;

	private int lenvId;

	private int renvId;

	private int diffNum;

	private int omitNum;

	private Date updateTime;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getOmitNum() {
		return omitNum;
	}

	public void setOmitNum(int omitNum) {
		this.omitNum = omitNum;
	}

	public int getDiffNum() {
		return diffNum;
	}

	public void setDiffNum(int diffNum) {
		this.diffNum = diffNum;
	}

	public int getRenvId() {
		return renvId;
	}

	public void setRenvId(int renvId) {
		this.renvId = renvId;
	}

	public int getLenvId() {
		return lenvId;
	}

	public void setLenvId(int lenvId) {
		this.lenvId = lenvId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int compareTo(GlobalConfigDiff globalConfigDiff) {
		int result = this.teamName.compareTo(globalConfigDiff.teamName);
		if (result != 0) {
			return result;
		} else {
			result = this.productName.compareTo(globalConfigDiff.productName);
			if (result != 0) {
				return result;
			} else {
				return this.projectName.compareTo(globalConfigDiff.projectName);
			}
		}
	}
}
