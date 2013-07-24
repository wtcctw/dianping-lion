package com.dianping.lion.pigeon;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.lion.Constants;
import com.dianping.lion.client.ConfigCache;
import com.dianping.lion.console.ZKClient;

public class PigeonGroupTest {
	private String m_address = "192.168.7.41:2181";

	private String m_mobile = "mobile";

	private String m_tuangou = "tuangou";

	private ZKClient m_client;

	private static int RESULT = 1;

	private static String SERVICE_NAME = "http://service.dianping.com/mockService/groupNoteShopService_1.0.0";

	private static final String DEFALUT_SEVICE = "DEFALUT_SEVICE";

	private static final String DEFALUT_NEW_SEVICE = "DEFALUT_NEW_SEVICE";

	private static final String DEFALUT_MOBILE_SEVICE = "DEFALUT_MOBILE_SEVICE";

	private static final String DEFALUT_MOBILE_NEW_SEVICE = "DEFALUT_MOBILE_NEW_SEVICE";

	private static final String DEFALUT_TUAN_NEW_SEVICE = "DEFALUT_TUAN_NEW_SEVICE";

	private void setUp() throws Exception {
		String defaultService = this.replaceServiceName(SERVICE_NAME);

		m_client = ZKClient.getInstance(m_address, Constants.SERVICE_PATH);
		ConfigCache.getInstance(m_address);

		if (!m_client.exists(defaultService)) {
			m_client.create(defaultService, DEFALUT_SEVICE);
		} else {
			m_client.set(defaultService, DEFALUT_SEVICE);
		}

		String mobileService = this.replaceServiceName(SERVICE_NAME) + "/" + m_mobile;

		if (!m_client.exists(mobileService)) {
			m_client.create(mobileService, DEFALUT_MOBILE_SEVICE);
		} else {
			m_client.set(mobileService, DEFALUT_MOBILE_SEVICE);
		}

		String tuangouService = this.replaceServiceName(SERVICE_NAME) + "/" + m_tuangou;

		if (m_client.exists(tuangouService)) {
			m_client.delete(tuangouService);
		}
	}

	private String replaceServiceName(String temp) {
		return temp.replace("/", Constants.PLACEHOLD);
	}

	@Test
	public void testNormal() throws Exception {
		System.out.println("TestNomal");
		RESULT = 1;
		setUp();
		PigeonCache pigeonCache = new PigeonCache();
		String key = replaceServiceName(SERVICE_NAME);

		Assert.assertEquals(DEFALUT_SEVICE, m_client.get(key));
		Assert.assertEquals(DEFALUT_SEVICE, pigeonCache.queryServiceAddress(key));

		ServiceChange serviceChange = new DefaultServiceChange();
		pigeonCache.setServiceChange(serviceChange);
		@SuppressWarnings("unused")
      PigeonClientImpl pigeonClientImpl = new PigeonClientImpl(serviceChange);

		m_client.setAndPush(key, DEFALUT_NEW_SEVICE);
		Thread.sleep(1000);
		Assert.assertEquals(RESULT, 2);
		Assert.assertEquals(DEFALUT_NEW_SEVICE, pigeonCache.queryServiceAddress(SERVICE_NAME));
		Assert.assertEquals(DEFALUT_NEW_SEVICE, m_client.get(key));

	}

	public class DefaultServiceChange implements ServiceChange {
		@Override
		public void onHostWeightChange(String host, int weight) {

		}

		@Override
		public void onServiceHostChange(String serviceName, List<String[]> hostList) {
			System.out.println("=======CallBack=======");
			RESULT = 2;
		}

		@Override
		public void onServiceHostChange(String serviceName, String group, List<String[]> hostList) {
			System.out.println("=======CallBack=onServiceHostChange======");
			RESULT = 3;
		}
	}

	@Test
	public void testGroupExist() throws Exception {
		System.out.println("testGroupExist");
		RESULT = 1;
		setUp();
		PigeonCache pigeonCache = new PigeonCache();
		String groupKey = this.replaceServiceName(SERVICE_NAME) + "/" + m_mobile;
		Assert.assertEquals(DEFALUT_MOBILE_SEVICE, m_client.get(groupKey));
		Assert.assertEquals(DEFALUT_MOBILE_SEVICE, pigeonCache.queryServiceAddress(SERVICE_NAME, m_mobile));

		ServiceChange serviceChange = new DefaultServiceChange();
		pigeonCache.setServiceChange(serviceChange);
		@SuppressWarnings("unused")
      PigeonClientImpl pigeonClientImpl = new PigeonClientImpl(serviceChange);

		m_client.setAndPush(groupKey, DEFALUT_MOBILE_NEW_SEVICE);
		Thread.sleep(2000);
		Assert.assertEquals(3, RESULT);
		Assert.assertEquals(DEFALUT_MOBILE_NEW_SEVICE, pigeonCache.queryServiceAddress(SERVICE_NAME, m_mobile));
		Assert.assertEquals(DEFALUT_MOBILE_NEW_SEVICE, m_client.get(groupKey));
	}

	@Test
	public void testGroupNotExist() throws Exception {
		System.out.println("testGroupNotExist");
		RESULT = 1;
		setUp();
		PigeonCache pigeonCache = new PigeonCache();

		String serviceName = replaceServiceName(SERVICE_NAME);
		String groupKey = serviceName + "/" + m_tuangou;
		Assert.assertEquals(false, m_client.exists(groupKey));
		Assert.assertEquals(DEFALUT_SEVICE, pigeonCache.queryServiceAddress(serviceName, m_tuangou));

		ServiceChange serviceChange = new DefaultServiceChange();
		pigeonCache.setServiceChange(serviceChange);
		@SuppressWarnings("unused")
      PigeonClientImpl pigeonClientImpl = new PigeonClientImpl(serviceChange);

		m_client.setAndPush(serviceName, DEFALUT_NEW_SEVICE);
		Thread.sleep(1000);
		Assert.assertEquals(2, RESULT);
		Assert.assertEquals(DEFALUT_NEW_SEVICE, pigeonCache.queryServiceAddress(serviceName));
		Assert.assertEquals(DEFALUT_NEW_SEVICE, pigeonCache.queryServiceAddress(serviceName, m_tuangou));
	}

	@Test
	public void testGroupNotExistAfterCreate() throws Exception {
		System.out.println("testGroupNotExist");
		RESULT = 1;
		setUp();
		PigeonCache pigeonCache = new PigeonCache();

		String serviceName = replaceServiceName(SERVICE_NAME);
		String groupKey = serviceName + "/" + m_tuangou;
		Assert.assertEquals(false, m_client.exists(groupKey));
		Assert.assertEquals(DEFALUT_SEVICE, pigeonCache.queryServiceAddress(serviceName, m_tuangou));

		ServiceChange serviceChange = new DefaultServiceChange();
		pigeonCache.setServiceChange(serviceChange);
		@SuppressWarnings("unused")
      PigeonClientImpl pigeonClientImpl = new PigeonClientImpl(serviceChange);

		m_client.creatAndPush(groupKey, DEFALUT_TUAN_NEW_SEVICE);
		Thread.sleep(1000);
		Assert.assertEquals(DEFALUT_TUAN_NEW_SEVICE, pigeonCache.queryServiceAddress(serviceName, m_tuangou));
		Assert.assertEquals(RESULT, 3);
	}

}
