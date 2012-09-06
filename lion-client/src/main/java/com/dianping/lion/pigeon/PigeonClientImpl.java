package com.dianping.lion.pigeon;
/**
 * Comment of PigeonInit
 * @author yong.you
 *
 */
public class PigeonClientImpl implements PigeonClient {
	PigeonCache pigeonCache;

	public PigeonClientImpl(ServiceChange serviceChange) throws Exception{
		pigeonCache = new PigeonCache();
		pigeonCache.setServiceChange(serviceChange);
	}	
	/* (non-Javadoc)
	 * @see com.dianping.lion.pigeon.PigeonClient#getHostWeigth(java.lang.String)
	 */
	@Override
	public int getHostWeigth(String hostName) throws Exception{
		return pigeonCache.getPigeonWeight(hostName);
	}
	/* (non-Javadoc)
	 * @see com.dianping.lion.pigeon.PigeonClient#getServiceAddress(java.lang.String)
	 */
	@Override
	public String getServiceAddress(String serviceName) throws Exception {
		return  pigeonCache.getPigeonService(serviceName);
	}
	//public String 
/*	@Override
	public String getPigeonConfig(String key) throws Exception {
		return pigeonCache.getConfigValue(key);
	}*/
}
