package com.dianping.lion.pigeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import com.dianping.lion.Constants;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.client.LionException;
import com.dianping.lion.client.ZooKeeperWrapper;

/**
 * Comment of PigeonCache
 * 
 * @author yong.you
 * 
 */
public class PigeonCache {

	private static Logger logger = Logger.getLogger(PigeonCache.class);
	private Map<String,Integer> host;
	private Map<String,String> service;
	private ZooKeeperWrapper zk;
	private ServiceChange serviceChange;
	private PigeonHostWatcher pigeonHostWatcher;
	private PigeonServiceWatcher pigeonServiceWatcher;
	private Properties pts;
	
	public PigeonCache() throws LionException {
		ConfigCache configCache = ConfigCache.getInstance();
		this.zk = configCache.getZk();
		this.pts = configCache.getPts();
		if(this.pts==null)
			pts=new Properties();
		host = new HashMap<String, Integer>();
		service = new HashMap<String, String>();
		pigeonHostWatcher=new PigeonHostWatcher(this);
		pigeonServiceWatcher=new PigeonServiceWatcher(this);
		this.init();
	}

	private void init() throws LionException {
		try {
			if (this.zk.exists(Constants.DP_PATH, false) == null) {
				this.zk.create(Constants.DP_PATH, new byte[0],
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			if (this.zk.exists(Constants.SERVICE_PATH, false) == null) {
				this.zk.create(Constants.SERVICE_PATH, new byte[0],
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			if (this.zk.exists(Constants.WEIGHT_PATH, false) == null) {
				this.zk.create(Constants.WEIGHT_PATH, new byte[0],
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new LionException(e);
		}
	}

	public ServiceChange getServiceChange() {
		return this.serviceChange;
	}

	public void setServiceChange(ServiceChange serviceChange) {
		this.serviceChange = serviceChange;
	}
/*	public String getConfigValue(String key) throws LionException{
		return ConfigCache.getInstance().getProperty(key);
	}*/
	public String getWeightValue(String key) throws LionException {
		String path = Constants.WEIGHT_PATH + "/" + key;
		try {
			if (this.zk.exists(path, false) != null) {
				String value = new String(this.zk.getData(path,
						this.pigeonHostWatcher, null), Constants.CHARSET);
				logger.info("Pigeon Get Host Weight Value  From ZooKeeper Server! ZooKeeper Address :" +this.zk.getAddresses() +  " Key:" + key + "  Value:"	+ value);
				return value;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new LionException(e);
		}
		return null;
	}

	public String getServiceValue(String key) throws LionException {
		String path = Constants.SERVICE_PATH + "/" + key;
		try {
			if (this.zk.exists(path, false) != null) {
				String value = new String(this.zk.getData(path,	this.pigeonServiceWatcher, null), Constants.CHARSET);
				logger.info("Pigeon Get Service Value From ZooKeeper Server! ZooKeeper Address :" +this.zk.getAddresses() + " Key:" + key.replace(Constants.PLACEHOLD,"/") + "  Value:"	+ value);
				return value;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new LionException(e);
		}
		return null;
	}

	public Integer getPigeonWeight(String key) throws LionException,NumberFormatException{
		if(host.containsKey(key)){
			logger.info("Pigeon Get Pigeon Weight From PigeonCache Map! Key:" + key + "  Value:"	+ host.get(key));
			return host.get(key);
		}
		else{
			String strWeight = this.getWeightValue(key);
			Integer result = 1;
			if(strWeight != null) {
				result=Integer.parseInt(strWeight);
			}
			host.put(key,result);
			return result;
		}
	}
	
	public String getPigeonService(String key) throws LionException{
		if(this.pts.containsKey(key)){
			logger.info("Pigeon Get Pigeon Service From Application Context! Key:" + key + "  Value:"	+this.pts.getProperty(key));
			return this.pts.getProperty(key);
		}
		String keyReplace=this.replaceServiceName(key);
		if(service.containsKey(keyReplace)){
			logger.info("Pigeon Get Pigeon Service From PigeonCache Map! Key:" + key + "  Value:"	+service.get(keyReplace));
			return service.get(keyReplace);
		}
		else{
			String result=this.getServiceValue(keyReplace);
			service.put(keyReplace, result);
			return result;
		}
	}
	// serviceAddress 规范为1.1.1.1:8080,2.2.2.2:8080,
	public List<String[]> getServiceIpPortWeight(String serviceAddress) {
		List<String[]> result = new ArrayList<String[]>();
		if (serviceAddress != null && serviceAddress.length() > 0) {
			String[] temp = serviceAddress.split(",");
			if (temp != null && temp.length > 0) {
				for (String total : temp) {
					String[] resultTemp =total.split(":");
					result.add(resultTemp);
				}
			}
		}
		return result;
	}
	/**
	 * @return the host
	 */
	public Map<String, Integer> getHost() {
		return host;
	}
	/**
	 * @return the service
	 */
	public Map<String, String> getService() {
		return service;
	}
	private String replaceServiceName(String temp){
		return temp.replace("/",Constants.PLACEHOLD);
	}
}
