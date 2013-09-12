package com.dianping.lion.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.dianping.lion.Utils;

public class ZooKeeperWrapperMock extends ZooKeeperWrapper {



	private Watcher watcher;
	private KeeperState state ;
	private static final String PREFIX = "/DP/CONFIG/lion.configcache.mock.mockdata";
	private static final String SUFFIX = "/TIMESTAMP";
	private Map<String,Watcher> watcherMap = new ConcurrentHashMap<String,Watcher>();
	private Map<String,byte[]> configMap = new ConcurrentHashMap<String,byte[]> ();
	
	public ZooKeeperWrapperMock(Watcher watcher){
		this.watcher = watcher;
		setUp(10);
	}
	
	public void reset(){
		configMap.clear();
		setUp(10);
		state = KeeperState.SyncConnected;
	}
	
	private void setUp(int num) {
		for(int i = 0; i < num; i ++){
			byte[] value = ("mockdata"+i).getBytes();
			configMap.put(PREFIX+i, value);
			configMap.put(PREFIX+i+SUFFIX, Utils.getLongBytes(System.nanoTime()));
		}
	}
	
	public void toggleExpiration(){
		state = KeeperState.Expired;
	}
	
	public void toggleExpirationWatcher(){
		watcher.process(new WatchedEvent(null,state,"lion.configcache.mock"));
	}
	
	public void toggleWrittingAction(){
		state = KeeperState.SyncConnected;
		for(Watcher watcher : watcherMap.values()){
			watcher.process(new WatchedEvent(EventType.NodeDataChanged,state,"lion.configcache.mock"));
		}
	}
	
	public String create(String path,byte[] data,List<ACL> acl,CreateMode createMode) throws KeeperException, InterruptedException, IOException{
		configMap.put(path+SUFFIX, (System.currentTimeMillis()+"").getBytes());
		return new String(configMap.put(path, data),"utf-8");
	}
	public Stat exists(String path,boolean watch) throws KeeperException, InterruptedException, IOException{
		Stat stat = null;
		if(configMap.containsKey(path)){
			stat = new Stat();
		}
		return stat;
	}
	
	public byte[] getData(String path,Watcher watcher,Stat stat) throws KeeperException, InterruptedException, IOException{
		if(watcher != null){
			this.watcherMap.put(path, watcher);
		}
		return configMap.get(path);
	}
	
	public byte[] getData(String path,boolean watch,Stat stat) throws KeeperException, InterruptedException, IOException{
		return configMap.get(path);
	}
	
	public Stat setData(String path, byte[] data,int version) throws KeeperException, InterruptedException, IOException{
		if(configMap.containsKey(path)){
			configMap.put(path, data);
			configMap.put(path+SUFFIX, Utils.getLongBytes(System.nanoTime()));
			return new Stat();
		}else{
			return null;
		}
	}
	
	public Stat setDataWithOutTimestamp(String path, byte[] data,int version) throws KeeperException, InterruptedException, IOException{
		if(configMap.containsKey(path)){
			configMap.put(path, data);
			return new Stat();
		}else{
			return null;
		}
	}
	
	public void reconnectSession(){
		state = KeeperState.SyncConnected;
		System.out.println("Session reconnected");
	}

	public void removeWatcher(String path){
		this.watcherMap.remove(path);
	}

	public KeeperState getState() {
		return state;
	}
	
}
