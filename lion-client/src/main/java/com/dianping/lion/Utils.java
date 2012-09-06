/**
 * 
 */
package com.dianping.lion;

/**    
 * <p>    
 * Title: Utils.java   
 * </p>    
 * <p>    
 * Description: 描述  
 * </p>   
 * @author saber miao   
 * @version 1.0    
 * @created 2011-6-16 下午01:37:39   
 */
public class Utils {
	
	public static long getLong(final byte[] b) {
		assert b.length == 8: "Invalid number of bytes for long conversion";
		int high = getInt(new byte[] {b[0], b[1], b[2], b[3]});
		int low = getInt(new byte[] {b[4], b[5], b[6], b[7]});
		return ((long) (high) << 32) + (low & 0xFFFFFFFFL);
	}
	
	public static int getInt(final byte[] b) {
		assert b.length == 4: "Invalid number of bytes for integer conversion";
		return ((b[0] << 24) & 0xFF000000) + ((b[1] << 16) & 0x00FF0000) +
				((b[2] << 8) & 0x0000FF00) + (b[3] & 0x000000FF);
	}
	
	public static byte[] getIntBytes(final int value) {
	    final byte[] b = new byte[4];
		b[0] = (byte) ((value >>> 24) & 0xFF);
		b[1] = (byte) ((value >>> 16) & 0xFF);
		b[2] = (byte) ((value >>> 8) & 0xFF);
		b[3] = (byte) ((value) & 0xFF);
		return b;
	}

	public static byte[] getLongBytes(final long value) {
	    final byte[] b = new byte[8];
		b[0] = (byte) ((int) (value >>> 56) & 0xFF);
		b[1] = (byte) ((int) (value >>> 48) & 0xFF);
		b[2] = (byte) ((int) (value >>> 40) & 0xFF);
		b[3] = (byte) ((int) (value >>> 32) & 0xFF);
		b[4] = (byte) ((int) (value >>> 24) & 0xFF);
		b[5] = (byte) ((int) (value >>> 16) & 0xFF);
		b[6] = (byte) ((int) (value >>> 8) & 0xFF);
		b[7] = (byte) ((int) (value) & 0xFF);
		return b;
	}
	
	public static void main(String[] args){
		Long d = 12l;
		Long f = 12l;
		System.out.println(d==f);
	}

}
