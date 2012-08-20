/**
 * 
 */
package com.dianping.lion.entity;

import java.io.Serializable;

/**
 * @author danson.liu
 *
 */
public class ProjectMember implements Serializable {

    private static final long serialVersionUID = -2587730801575947477L;
    
    private int id;
    
    private int projectId;
    
    private int userId;
    
    private int createUserId;

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
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
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

}
