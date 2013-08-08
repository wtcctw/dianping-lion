package com.dianping.lion.service;

import java.util.List;

public interface ZookeeperService {

    public void create(String path, String data) throws Exception;

    public void delete(String path) throws Exception;

    public String get(String path) throws Exception;

    public void set(String path, String data) throws Exception;

    public List<String> getChildren(String path) throws Exception;

    public boolean exists(String path) throws Exception;

}
