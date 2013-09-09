package com.dianping.lion.api.http;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.dao.ConfigReleaseDao;
import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.ConfigInstanceSnapshot;
import com.dianping.lion.entity.ConfigSnapshot;
import com.dianping.lion.entity.ConfigSnapshotSet;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.Project;
import com.dianping.lion.exception.RuntimeBusinessException;

public class GetConfigDiffServlet extends GetConfigServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ConfigReleaseDao configReleaseDao;

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse resp, String querystr) throws Exception {
        String env = getNotBlankParameter(req, PARAM_ENV);
        String projectName = getNotBlankParameter(req, PARAM_PROJECT);
        String task = getNotBlankParameter(req, PARAM_TASK);
        String[] keys = req.getParameterValues(PARAM_KEY);

        Environment environment = getRequiredEnv(env);
        if (environment.isOnline()) {
            checkAccessibility(req);
        }

        Project project = getRequiredProject(projectName);
        ConfigSnapshotSet snapshotSet = configReleaseService.findSnapshotSetToRollback(project.getId(), environment.getId(), task);

        if(snapshotSet == null) {
            throw new RuntimeBusinessException("No rollback snapshot found for task " + task);
        }

        List<Config> currentConfigs = configService.findConfigs(project.getId());
        List<ConfigInstance> currentConfigInsts = configService.findInstances(project.getId(), environment.getId());
        List<ConfigSnapshot> configSnapshots = configReleaseDao.findConfigSnapshots(snapshotSet.getId());
        List<ConfigInstanceSnapshot> configInstSnapshots = configReleaseDao.findConfigInstSnapshots(snapshotSet.getId());

        Map<Integer, String> currentIdValueMap = new HashMap<Integer, String>();
        for (ConfigInstance currentConfigInst : currentConfigInsts) {
            currentIdValueMap.put(currentConfigInst.getConfigId(), currentConfigInst.getValue());
        }
        Map<String, String> currentKeyValueMap = new HashMap<String, String>();
        for (Config currentConfig : currentConfigs) {
            String value = currentIdValueMap.get(currentConfig.getId());
            currentKeyValueMap.put(currentConfig.getKey(), value);
        }

        Map<Integer, String> snapshotIdValueMap = new HashMap<Integer, String>();
        for (ConfigInstanceSnapshot configInstSnapshot : configInstSnapshots) {
            snapshotIdValueMap.put(configInstSnapshot.getConfigId(), configInstSnapshot.getValue());
        }
        Map<String, String> snapshotKeyValueMap = new HashMap<String, String>();
        for (ConfigSnapshot configSnapshot : configSnapshots) {
            String value = snapshotIdValueMap.get(configSnapshot.getConfigId());
            snapshotKeyValueMap.put(configSnapshot.getKey(), value);
        }

        if(keys==null || keys.length==0) {
            Set<String> keySet = new HashSet<String>();
            keySet.addAll(currentKeyValueMap.keySet());
            keySet.addAll(snapshotKeyValueMap.keySet());
            keys = keySet.toArray(new String[0]);
        }

        List<ConfigDiff> configDiffList = new ArrayList<ConfigDiff>();
        for(String key : keys) {
            String current = currentKeyValueMap.get(key);
            String snapshot = snapshotKeyValueMap.get(key);

            if((current == null && snapshot == null) ||
               (current != null && current.equals(snapshot))) {
                continue;
            }

            ConfigDiff configDiff = new ConfigDiff(key);
            configDiff.setCurrent(current);
            configDiff.setSnapshot(snapshot);
            configDiffList.add(configDiff);
        }

        PrintWriter writer = resp.getWriter();
        writer.print(new JSONObject(configDiffList).toString());
    }

    private class ConfigDiff {
        private String key;
        private String current;
        private String snapshot;

        public ConfigDiff(String key, String current, String snapshot) {
            this.key = key;
            this.current = current;
            this.snapshot = snapshot;
        }

        public ConfigDiff(String key) {
            this(key, null, null);
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getSnapshot() {
            return snapshot;
        }

        public void setSnapshot(String snapshot) {
            this.snapshot = snapshot;
        }

        public String toString() {
            return new JSONObject(this).toString();
        }
    }
}
