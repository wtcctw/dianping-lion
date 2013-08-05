package com.dianping.lion.pigeon;


/**
 * Comment of PigeonInit
 * @author yong.you
 *
 */
public class PigeonClientImpl implements PigeonClient {
	private PigeonCache pigeonCache;

	public PigeonClientImpl(ServiceChange serviceChange) throws Exception{
		pigeonCache = new PigeonCache();
		pigeonCache.setServiceChange(serviceChange);
	}	
	
	@Override
	public int getHostWeigth(String hostName) throws Exception{
		return pigeonCache.queryHostWeight(hostName);
	}
	
	@Override
	public String getServiceAddress(String serviceName) throws Exception {
		return  pigeonCache.queryServiceAddress(serviceName);
	}
	
	@Override
   public String getServiceAddress(String serviceName, String group) throws Exception {
		return  pigeonCache.queryServiceAddress(serviceName,group);
   }
}
