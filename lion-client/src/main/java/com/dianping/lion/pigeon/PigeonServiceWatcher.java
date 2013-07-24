package com.dianping.lion.pigeon;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.dianping.lion.Constants;
import com.dianping.lion.client.LionException;
import com.sun.tools.javac.util.Pair;

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

	private Pair<String, String> parseKey(String path) {
		String key = path.substring(Constants.SERVICE_PATH.length() + 1);
		String group = "";
		int index = key.indexOf("/");

		if (index > -1) {
			group = key.substring(index + 1);
			key = key.substring(0, index);
		}
		return new Pair<String, String>(key, group);
	}

	@Override
	public void process(WatchedEvent event) {
		String keyPath = event.getPath();
		EventType type = event.getType();
		Pair<String, String> pair = parseKey(keyPath);
		String key = pair.fst;
		String group = pair.snd;

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

			if (group.length() > 0 ) {
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
}
