package com.dianping.lion.pigeon;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import com.dianping.lion.client.LionException;

/**
 * Comment of PigeonHostWatcher
 * 
 * @author yong.you
 * 
 */
public class PigeonHostWatcher implements Watcher {

	private static Logger logger = Logger.getLogger(PigeonHostWatcher.class);
	private PigeonCache pigeonCache;
	
	public PigeonHostWatcher(PigeonCache pigeonCache){
		this.pigeonCache=pigeonCache;
	} 
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDataChanged) {
			String keyPath = event.getPath();
			String key = keyPath.substring(keyPath.lastIndexOf("/") + 1);
			String weight = null;
			try {
				weight = pigeonCache.queryHostWeightFromZK(key);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
			if (pigeonCache.getHost().containsKey(key))
				pigeonCache.getServiceChange().onHostWeightChange(key, Integer.parseInt(weight));
		} else  {
			logger.info("Host Node " + event.getType() + " " + event.getPath());
			String keyPath = event.getPath();
			String key = keyPath.substring(keyPath.lastIndexOf("/") + 1);
			try {
				pigeonCache.queryHostWeightFromZK(key);
			} catch (LionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
