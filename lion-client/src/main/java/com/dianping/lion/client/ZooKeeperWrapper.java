/**
 *
 */
package com.dianping.lion.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * <p>
 * Title: ZooKeeperWrapper.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * @author saber miao
 * @version 1.0
 * @created 2011-7-14 下午05:38:01
 */
public class ZooKeeperWrapper {

	private static Logger logger = Logger.getLogger(ZooKeeperWrapper.class);
	private ZooKeeper zk;

	private String addresses;
	private int timeout;
	private Watcher watcher;
	
	private Map<String,Watcher> watcherMap = new ConcurrentHashMap<String,Watcher>();

	protected ZooKeeperWrapper(){
	}

	public ZooKeeperWrapper(String addresses,int timeout,final Watcher watcher) throws IOException{
		this.addresses = addresses;
		this.timeout = timeout;
		this.watcher = watcher;
		WatcherWrapper watcherWrapper = new WatcherWrapper(watcher);
		this.zk = new ZooKeeper(addresses, timeout, watcherWrapper);
		watcherWrapper.waitUntilConnected(2000);
	}

	private synchronized void init(ZooKeeper zk) throws IOException, KeeperException, InterruptedException{
		if(zk == this.zk){
		    WatcherWrapper watcherWrapper = new WatcherWrapper(watcher);
	        this.zk = new ZooKeeper(addresses, timeout, watcherWrapper);
	        watcherWrapper.waitUntilConnected(2000);
			for(Entry<String,Watcher> entry : watcherMap.entrySet()){
				this.zk.getData(entry.getKey(), entry.getValue(),null);
			}
		}
	}
	
	public void close() {
	    if(zk != null) {
	        try {
                zk.close();
            } catch (InterruptedException e) {
            }
	    }
	}

	public Stat exists(String path,boolean watch) throws KeeperException, InterruptedException, IOException{
		Stat stat = null;
		ZooKeeper zk_ = this.zk;
		try{
			stat = zk_.exists(path, watch);
		}catch(SessionExpiredException see){
			logger.error("OP:exists1---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			stat = this.zk.exists(path, watch);
		}
		return stat;
	}

	public Stat exists(String path,Watcher watcher) throws KeeperException, InterruptedException, IOException{
		Stat stat = null;
		ZooKeeper zk_ = this.zk;
		try{
			stat = zk_.exists(path, watcher);
			if(watcher!=null)
				this.watcherMap.put(path, watcher);
		}catch(SessionExpiredException see){
			logger.error("OP:exists2---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			stat = this.zk.exists(path, watcher);
		}
		return stat;
	}

	public String create(String path,byte[] data,List<ACL> acl,CreateMode createMode) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			return zk_.create(path, data, acl, createMode);
		}catch(SessionExpiredException see){
			logger.error("OP:create---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			return this.zk.create(path, data, acl, createMode);
		}
	}

	public byte[] getData(String path,Watcher watcher,Stat stat) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			byte[] data = zk_.getData(path, watcher, stat);
			if(watcher!=null)
				this.watcherMap.put(path, watcher);
			return data;
		}catch(SessionExpiredException see){
			logger.error("OP:getData1---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			return this.zk.getData(path, watcher, stat);
		}
	}
	public byte[] getData(String path,boolean watch,Stat stat) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			return zk_.getData(path, watch, stat);
		}catch(SessionExpiredException see){
			logger.error("OP:getData2---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			return this.zk.getData(path, watch, stat);
		}
	}

	public Stat setData(String path,byte[] data,int version) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			return zk_.setData(path, data, version);
		}catch(SessionExpiredException see){
			logger.error("OP:setData---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			return this.zk.setData(path, data, version);
		}
	}

	public List<String> getChildren(String path,boolean watch) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			return zk_.getChildren(path, watch);
		}catch(SessionExpiredException see){
			logger.error("OP:getChildren---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			return this.zk.getChildren(path, watch);
		}
	}

	public void delete(String path,int version) throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk_ = this.zk;
		try{
			zk_.delete(path, version);
		}catch(SessionExpiredException see){
			logger.error("OP:getChildren---"+see.getMessage(),see);
			zk_.close();
			init(zk_);
			this.zk.delete(path, version);
		}
	}

	public void removeWatcher(String path){
		this.watcherMap.remove(path);
	}
	public synchronized void reconnectSession() throws IOException, KeeperException, InterruptedException{
		ZooKeeper zk_ = this.zk;
		zk_.close();
		init(zk_);
	}

	/**
	 * @return the addresses
	 */
	public String getAddresses() {
		return addresses;
	}
	
	public class WatcherWrapper implements Watcher {
	    
	    private boolean connected;
	    private Watcher wrappedWatcher;
	    
	    public WatcherWrapper(Watcher watcher) {
	        this.connected = false;
	        this.wrappedWatcher = watcher;
	    }
	    
	    @Override
        public void process(WatchedEvent event) {
            if(event.getState() == KeeperState.SyncConnected) {
                logger.info("Zookeeper connected");
                synchronized(this) {
                    connected = true;
                    this.notifyAll();
                }
                return;
            }
            
            if(wrappedWatcher != null) {
                wrappedWatcher.process(event);
            }
        }
	    
	    public void waitUntilConnected(long milliseconds) throws IOException {
	        synchronized(this){
	            long start = System.currentTimeMillis();
	            while(!connected){
	                long waitMillis = milliseconds - (System.currentTimeMillis() - start);
	                if(waitMillis <= 0) {
	                    throw new IOException("Timeout while connecting to zookeeper");
	                } else {
	                    try {
	                        this.wait(waitMillis);
	                    } catch (InterruptedException e) {
	                        throw new IOException("Interrupted while connecting to zookeeper", e);
	                    }
	                }
	            }
	        }    
	    }
	}
}

