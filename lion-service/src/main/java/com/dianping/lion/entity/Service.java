package com.dianping.lion.entity;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

public class Service implements Iterable<String> {

    private int id;
    private int projectId;
    private int envId;
    private String name = "";
    private String desc = "";
    private String group = "";
    private String hosts = "";      // host1:port1, host2:port2, ...

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
        StringBuilder sb = new StringBuilder(name);
        if(StringUtils.isNotBlank(group)) {
            sb.append('/').append(group);
        }
//        if(StringUtils.isNotBlank(hosts)) {
//            sb.append(':').append(hosts);
//        }
        return sb.toString();
    }

    @Override
    public Iterator<String> iterator() {
        return new HostIterator();
    }

    private class HostIterator implements Iterator<String> {

        private String[] hostArray;
        private int current;

        public HostIterator() {
            if(hosts==null)
                throw new NullPointerException("hosts is null");
            hostArray = hosts.split(",");
            current = 0;
        }

        @Override
        public boolean hasNext() {
            while(current < hostArray.length) {
                if(hostArray[current].trim().length() > 0)
                    return true;
                current++;
            }
            return false;
        }

        @Override
        public String next() {
            return hostArray[current++].trim();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
