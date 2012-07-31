/**
 * Project: com.dianping.lion.lion-client-0.3.0
 * 
 * File Created at 2012-7-28
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
package com.dianping.lion.client.zookeeper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO Comment of SessionRecoverableZookeeper
 * @author danson.liu
 *
 */
public class SessionRecoverableZookeeper implements Watcher {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionRecoverableZookeeper.class);

	private final String addresses;
	private final int timeout;
	private final Watcher watcher;
	private ZooKeeper zookeeper;
	private ConcurrentMap<String, Set<Watcher>> watcherMap = new ConcurrentHashMap<String, Set<Watcher>>();
	private Object reconnectMonitor = new Object();

	public SessionRecoverableZookeeper(String addresses, int timeout, Watcher watcher) throws IOException {
		this.addresses = addresses;
		this.timeout = timeout;
		this.watcher = watcher;
		this.zookeeper = new ZooKeeper(addresses, timeout, this);
	}
	
	/**
	 * 谨慎使用default watcher(watch = true), session expired时重建该watcher不可恢复
	 * @param path
	 * @param watch
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public Stat exists(final String path, final boolean watch) throws KeeperException, InterruptedException, IOException {
		return (Stat) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				return zookeeper.exists(path, watch);
			}
		});
	}
	
	public Stat exists(final String path, final Watcher watcher) throws KeeperException, InterruptedException, IOException{
		return (Stat) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				Stat stat = zookeeper.exists(path, watcher);
				if (watcher != null) {
					saveWatcherForRestore(path, watcher);
				}
				return stat;
			}
		});
	}
	
	public String create(final String path, final byte[] data, final List<ACL> acl, final CreateMode createMode) throws KeeperException, InterruptedException, IOException{
		return (String) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				return zookeeper.create(path, data, acl, createMode);
			}
			
		});
	}
	
	public byte[] getData(final String path, final Watcher watcher, final Stat stat) throws KeeperException, InterruptedException, IOException{
		return (byte[]) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				byte[] data = zookeeper.getData(path, watcher, stat);
				if (watcher != null) {
					saveWatcherForRestore(path, watcher);
				}
				return data;
			}
		});
	}
	
	public byte[] getData(final String path, final boolean watch, final Stat stat) throws KeeperException, InterruptedException, IOException{
		return (byte[]) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				return zookeeper.getData(path, watch, stat);
			}
		});
	}
	
	public Stat setData(final String path, final byte[] data, final int version) throws KeeperException, InterruptedException, IOException{
		return (Stat) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				return zookeeper.setData(path, data, version);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getChildren(final String path, final boolean watch) throws KeeperException, InterruptedException, IOException{
		return (List<String>) executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				return zookeeper.getChildren(path, watch);
			}
		});
	}
	
	public void delete(final String path, final int version) throws KeeperException, InterruptedException, IOException{
		executeOperation(new ZookeeperOperation() {
			@Override
			public Object execute() throws KeeperException, InterruptedException {
				zookeeper.delete(path, version);
				return null;
			}
		});
	}
	
	public void removeWatcher(String path, Watcher watcher) {
		Set<Watcher> watchers = this.watcherMap.get(path);
		if (watchers != null) {
			watchers.remove(watcher);
		}
	}

	private void saveWatcherForRestore(String path, Watcher watcher) {
		Set<Watcher> watchers = watcherMap.get(path);
		if (watchers == null) {
			watchers = new HashSet<Watcher>();
			Set<Watcher> watchers_ = watcherMap.putIfAbsent(path, watchers);
			if (watchers_ != null) {
				watchers = watchers_;
			}
		}
		watchers.add(watcher);
	}

	private Object executeOperation(ZookeeperOperation operation) throws KeeperException, InterruptedException, IOException {
		Object result = null;
		ZooKeeper oldZk = this.zookeeper;
		try {
			result = operation.execute();
		} catch (SessionExpiredException e) {
			logger.warn("Execute zookeeper operation failed of session expiration, try it again with reconnect.", e);
			reconnectToZookeeper(oldZk);
			result = operation.execute();
		}
		return result;
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == Event.EventType.None && event.getState() == KeeperState.Expired) {
			try {
				reconnectToZookeeper(this.zookeeper);
			} catch (Exception e) {
				logger.warn("Reconnect to zookeeper cluster failed while session expired.", e);
			}
		}
		if (this.watcher != null) {
			this.watcher.process(event);
		}
	}

	private void reconnectToZookeeper(ZooKeeper oldZk) throws InterruptedException, IOException, KeeperException {
		logger.info("Zookeeper client's session is expired, try to reconnect.");
		if (oldZk != null) {
			try {
				oldZk.close();
			} catch (Exception e) {
				logger.warn("Close invalid zookeeper client failed, detail: " + e.getMessage());
			}
		}
		synchronized (reconnectMonitor) {
			if (oldZk == this.zookeeper) {
				this.zookeeper = new ZooKeeper(addresses, timeout, this);
				for (Entry<String, Set<Watcher>> entry : watcherMap.entrySet()) {
					Set<Watcher> watchers = entry.getValue();
					if (watchers != null) {
						for (Watcher watcher : watchers) {
							//TODO check，老版本使用getData接口来从新绑定watcher，是否有影响
							this.zookeeper.exists(entry.getKey(), watcher);
						}
					}
				}
				oldZk = null;
			}
		}
	}
	
	public void close() throws InterruptedException {
		zookeeper.close();
	}
	
	/**
	 * @return the addresses
	 */
	public String getAddresses() {
		return addresses;
	}

	interface ZookeeperOperation {
		Object execute() throws KeeperException, InterruptedException;
	}
	
}
