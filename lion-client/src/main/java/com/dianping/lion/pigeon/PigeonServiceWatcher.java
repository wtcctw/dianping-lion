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
	
	public PigeonServiceWatcher(PigeonCache pigeonCache){
		this.pigeonCache=pigeonCache;
	}
	
	private String replaceServiceName(String temp){
		return temp.replace(Constants.PLACEHOLD,"/");
	}
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDataChanged) {
			String keyPath = event.getPath();
			String key = keyPath.substring(keyPath.lastIndexOf("/") + 1);
			String value = "";
			try {
				value = pigeonCache.getServiceValue(key);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
			logger.info("Pigeon Cache Key Change! Key: "+key +" Value: " + value);
			if (pigeonCache.getService().containsKey(key)) {
				List<String[]> hostDetail = pigeonCache
						.getServiceIpPortWeight(value);
				pigeonCache.getServiceChange().onServiceHostChange(this.replaceServiceName(key), hostDetail);
			}
		} else {
			// 重新监听节点信息
			logger.info("Delete The Node " + event.getType() + " "
					+ event.getPath());
			String key = event.getPath().substring(
					event.getPath().lastIndexOf("/") + 1);
			try {
				pigeonCache.getServiceValue(key);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
