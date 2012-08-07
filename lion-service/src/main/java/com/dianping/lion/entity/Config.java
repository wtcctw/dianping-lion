/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-11
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
package com.dianping.lion.entity;

import java.io.Serializable;
import java.util.Date;

import com.dianping.lion.util.EnumUtils;
import com.dianping.lion.util.StringUtils;

/**
 * 配置项
 * 
 * @author danson.liu
 * 
 */
@SuppressWarnings("serial")
public class Config implements Serializable {
	
	public static final int MAX_DESC_DISPLAY_LEN = 36;

	protected int id;
	protected String key;
	protected String desc;
	protected int type;
	protected ConfigTypeEnum typeEnum;
	protected int projectId;
	protected int createUserId;
	protected int modifyUserId;
	protected Date createTime;
	protected Date modifyTime;
	protected int seq;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	public String getAbbrevDesc() {
		return StringUtils.cutString(desc, MAX_DESC_DISPLAY_LEN);
	}
	public boolean isLongDesc() {
		return StringUtils.cutString(desc, MAX_DESC_DISPLAY_LEN).length() != desc.length();
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	public ConfigTypeEnum getTypeEnum() {
		if (this.typeEnum == null) {
			synchronized (this) {
				if (this.typeEnum == null) {
					this.typeEnum = EnumUtils.fromEnumProperty(ConfigTypeEnum.class, "value", this.type);
				}
			}
		}
		return this.typeEnum;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
		this.typeEnum = null;
	}
	public void setTypeEnum(ConfigTypeEnum typeEnum) {
		this.type = typeEnum.getValue();
		this.typeEnum = typeEnum;
	}
	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the createUserId
	 */
	public int getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the modifyUserId
	 */
	public int getModifyUserId() {
		return modifyUserId;
	}
	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}


}
