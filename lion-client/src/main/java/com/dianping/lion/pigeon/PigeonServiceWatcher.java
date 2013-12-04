package com.dianping.lion.pigeon;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.dianping.lion.Constants;
import com.dianping.lion.client.LionException;

/**
 * Comment of PigeonServiceWatcher
 * 
 * @author yong.you
 * 
 */
public class PigeonServiceWatcher implements Watcher {

	private static Logger logger = Logger.getLogger(PigeonServiceWatcher.class);

	private PigeonCache pigeonCache;

	public PigeonServiceWatcher(PigeonCache pigeonCache) {
		this.pigeonCache = pigeonCache;
	}

	private String replaceServiceName(String temp) {
		return temp.replace(Constants.PLACEHOLD, "/");
	}

	private ServiceNameInfo parseKey(String path) {
		ServiceNameInfo info = new ServiceNameInfo();
		String serviceName = path.substring(Constants.SERVICE_PATH.length() + 1);
		String group = "";
		int index = serviceName.indexOf("/");

		if (index > -1) {
			group = serviceName.substring(index + 1);
			serviceName = serviceName.substring(0, index);
		}
		info.setGroup(group);
		info.setServiceName(serviceName);
		return info;
	}

	@Override
	public void process(WatchedEvent event) {
		String keyPath = event.getPath();
		if(keyPath == null) 
			return;
		EventType type = event.getType();
		ServiceNameInfo info = parseKey(keyPath);
		String key = info.getServiceName();
		String group =info.getGroup();

		if (type == EventType.NodeDataChanged || type == EventType.NodeCreated) {
			String value = "";
			try {
				value = pigeonCache.queryServiceAddressFromZK(key, group);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
			logger.info("Pigeon Cache Key Change! Key: " + key + " Value: " + value);

			List<String[]> hostDetail = pigeonCache.buildServiceAdress(value);
			ServiceChange serviceChange = pigeonCache.getServiceChange();

			if (group.length() > 0) {
				serviceChange.onServiceHostChange(this.replaceServiceName(key), group, hostDetail);
			} else if (group.length() == 0) {
				serviceChange.onServiceHostChange(this.replaceServiceName(key), hostDetail);
			}

		} else {
			// 重新监听节点信息
			logger.info("Re Watch The Node " + type + " " + event.getPath());
			try {
				pigeonCache.queryServiceAddressFromZK(key, group);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static class ServiceNameInfo {
		private String m_serviceName;

		private String m_group;

		public String getServiceName() {
			return m_serviceName;
		}

		public void setServiceName(String serviceName) {
			m_serviceName = serviceName;
		}

		public String getGroup() {
			return m_group;
		}

		public void setGroup(String group) {
			m_group = group;
		}
	}
}
