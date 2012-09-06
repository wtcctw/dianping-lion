package com.dianping.lion.client;



import java.io.IOException;

import junit.framework.Assert;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ConfigCacheTest {
	private static ConfigCache configCache;
	private static ZooKeeperWrapperMock zkwMock;
	private static final String MOCK_KEY = "lion.configcache.mock.mockdata0";
	private static final String MOCK_VALUE = "mockdata0";
	
	@BeforeClass 
	public static void init(){
		configCache = ConfigCache.getMockInstance();
		configCache.setZk(new ZooKeeperWrapperMock(configCache.new ConfigWatcher()));
		zkwMock = (ZooKeeperWrapperMock)configCache.getZk();
	}
	
	@Before
	public void initialize(){
		configCache.reset();
		zkwMock.reset();
	}
	
	
	@Test
	public void testSessionReconnect(){
		zkwMock.toggleExpiration();
		Assert.assertEquals(true, zkwMock.getState() == KeeperState.Expired);
		zkwMock.toggleExpirationWatcher();
		Assert.assertEquals(true, zkwMock.getState() == KeeperState.SyncConnected);
	}
	
	/*
	 * 推数据到zookeeper,并强制本地缓存更新
	 */
	@Test
	public void testDataRefresh() throws KeeperException, InterruptedException, IOException, LionException{
		String value = configCache.getProperty(MOCK_KEY);
		System.out.println(value);
		Assert.assertEquals(true, value.equalsIgnoreCase(MOCK_VALUE));
		zkwMock.setData("/DP/CONFIG/"+MOCK_KEY, "AHA".getBytes(), -1);
		zkwMock.toggleWrittingAction();
		value = configCache.getProperty(MOCK_KEY);
		Assert.assertEquals(true, value.equalsIgnoreCase("AHA"));
	}
	
	
	/*
	 * 推送数据到ZooKeeper,本地缓存不更新
	 */
	@Test
	public void testDataNotRefresh() throws KeeperException, InterruptedException, IOException, LionException{
		String value = configCache.getProperty(MOCK_KEY);
		System.out.println(value);
     		zkwMock.setDataWithOutTimestamp("/DP/CONFIG/"+MOCK_KEY, "AHA".getBytes(), -1);
		zkwMock.toggleWrittingAction();
		value = configCache.getProperty(MOCK_KEY);
		Assert.assertEquals(true, value.equalsIgnoreCase(MOCK_VALUE));
	}
	
	@Test
	public void testConfigChange() throws LionException, KeeperException, InterruptedException, IOException{
		final StringValue flag = new StringValue();
//		String value = configCache.getProperty(MOCK_KEY);
		configCache.addChange(new ConfigChange(){
			@Override
			public void onChange(String key, String value) {
				flag.setValue("wow");
			}
		});
		zkwMock.setData("/DP/CONFIG/"+MOCK_KEY, "AHA".getBytes(), -1);
		zkwMock.toggleWrittingAction();
		Assert.assertEquals(true, flag.getValue().equals("wow"));
	}
	
	static class StringValue{
		String value;
		public String getValue(){
			return value;
		}
		public void setValue(String value){
			this.value = value;
		}
	}

	
}
