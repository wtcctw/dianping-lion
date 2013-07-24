package com.dianping.lion.pigeon;

public interface PigeonClient {

	public abstract int getHostWeigth(String hostAndPort) throws Exception;

	public abstract String getServiceAddress(String serviceName) throws Exception;

	public abstract String getServiceAddress(String serviceName, String group) throws Exception;;

}