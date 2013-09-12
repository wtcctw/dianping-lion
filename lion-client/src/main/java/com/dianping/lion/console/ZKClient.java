/**
 *
 */
package com.dianping.lion.console;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

import com.dianping.lion.Constants;
import com.dianping.lion.Utils;
import com.dianping.lion.client.LionException;
import com.dianping.lion.client.ZooKeeperWrapper;

/**
 * <p>
 * Title: ZKClient.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * @author saber miao
 * @version 1.0
 * @created 2011-5-23 下午09:16:46
 */
public class ZKClient {

	private static Logger logger = Logger.getLogger(ZKClient.class);
	private static Map<String,ZKClient> clientMap = new ConcurrentHashMap<String,ZKClient>();
	private ZooKeeperWrapper zk;

	private int timeout = 15000;
	private String address;
	private String parentPath;

	public ZKClient(String path){
		this.parentPath=path;
	}

	public static ZKClient getInstance(String address) throws LionException{
		return getInstance(address,Constants.CONFIG_PATH);
	}

	public static ZKClient getInstance(String address,String path) throws LionException{
		ZKClient client = clientMap.get(address+":"+path);
		if(client == null){
			synchronized(clientMap){
				client = clientMap.get(address+":"+path);
				if(client == null){
					try {
						client = new ZKClient(path);
						client.address = address;
						client.init();
						clientMap.put(address+":"+path, client);
					} catch (IOException e) {
						throw new LionException(e);
					}
				}
			}
		}
		return client;
	}

	private void init() throws IOException{
			this.zk = new ZooKeeperWrapper(this.address,this.timeout,new Watcher(){
				@Override
				public void process(WatchedEvent event) {
				}} );
			try {
				if(this.zk.exists(Constants.DP_PATH, false) == null){
					this.zk.create(Constants.DP_PATH, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				if(this.zk.exists(this.parentPath, false) == null){
					this.zk.create(this.parentPath, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
	}

	public boolean exists(String key) throws LionException{
		try {
			return this.zk.exists(this.parentPath+"/"+key, false) != null;
		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public void create(String key,String value) throws LionException{
		try {
			this.zk.create(this.parentPath+"/"+key, value.getBytes(Constants.CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public void set(String key,String value) throws LionException{
		try {
			this.zk.setData(this.parentPath+"/"+key, value.getBytes(Constants.CHARSET), -1);
		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public void createAndPush(String key,String value)throws LionException{
		try {
			String keyPath = this.parentPath+"/"+key;
			if(this.zk.exists(keyPath, false)!=null)
				this.zk.setData(keyPath,value.getBytes(Constants.CHARSET), -1);
			else
				this.zk.create(this.parentPath+"/"+key, value.getBytes(Constants.CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (Exception e) {
			throw new LionException(e);
		}

	}

	public void setAndPush(String key,String value) throws LionException{
		try {
			String timestampPath = this.parentPath+"/"+key+"/"+Constants.CONFIG_TIMESTAMP;
			if(this.zk.exists(timestampPath, false) != null){
				this.zk.setData(timestampPath, Utils.getLongBytes(new Date().getTime()), -1);
			}else{
				this.zk.create(timestampPath, Utils.getLongBytes(new Date().getTime()),  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			this.zk.setData(this.parentPath+"/"+key, value.getBytes(Constants.CHARSET), -1);

		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public String get(String key) throws LionException{
		try {
			return new String(this.zk.getData(this.parentPath+"/"+key, null, null),Constants.CHARSET);
		}  catch (Exception e) {
			throw new LionException(e);
		}
	}

	public void delete(String key) throws LionException{
		try {
			String path = this.parentPath+"/"+key;
			List<String> children = this.zk.getChildren(path, false);
			if(children != null && children.size() > 0){
				for(String child : children){
					this.zk.delete(path+"/"+child, -1);
				}
			}
			this.zk.delete(this.parentPath+"/"+key, -1);
		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public List<String> getKeyList() throws LionException{
		try {
			return this.zk.getChildren(this.parentPath, false);
		} catch (Exception e) {
			throw new LionException(e);
		}
	}

	public String getAddress() {
		return address;
	}

}
