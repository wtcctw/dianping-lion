package com.dianping.lion.entity;

public class Service {

    private int id;
    private int projectId;
    private int envId;
    private String name = "";
    private String desc = "";
    private String group = "";
    private String hosts = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(">>").append(projectId).append(">>");
        sb.append(envId).append(">>").append(name).append(">>");
        sb.append(group);
        return sb.toString();
    }

}
