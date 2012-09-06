/**
 * 
 */
package test;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.lion.Utils;


public class TestBean {
	
	@Test
	public void testStringByteAry() throws UnsupportedEncodingException{
		byte[] byteAry = ("mockdata"+0).getBytes();
		String string = new String(byteAry,"utf-8");
		System.out.println(string);
	}
	
	@Test
	public void testNanoSeconds(){
		long now = System.nanoTime();
		byte[] bytes = Utils.getLongBytes(now);
		long after = Utils.getLong(bytes);
		Assert.assertEquals(true, now == after);
	}
}
