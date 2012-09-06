package com.dianping.lion.pigeon;

public interface PigeonClient {
	
	/*public String getPigeonConfig(String key) throws Exception;
	*/
	public abstract int getHostWeigth(String hostAndPort) throws Exception;

	public abstract String getServiceAddress(String serviceName)
			throws Exception;

}