package com.haven.simplej.util;


/**
 * @author: havenzhang
 * @date: 2018/10/8 21:10
 * @version 1.0
 */
public class BytesUtil {

	/**
	 * byte数组转换成整型
	 * @param bytes byte数组
	 * @return 整型
	 */
	public static int bytes2Int(byte[] bytes) {
		int result = 0;
		//将每个byte依次搬运到int相应的位置
		result = bytes[0] & 0xff;
		result = result << 8 | bytes[1] & 0xff;
		result = result << 8 | bytes[2] & 0xff;
		result = result << 8 | bytes[3] & 0xff;
		return result;
	}

	/**
	 * 整型转换成byte数组
	 * @param num 整数
	 * @return byte数组
	 */
	public static byte[] int2Bytes(int num) {
		byte[] bytes = new byte[4];
		//通过移位运算，截取低8位的方式，将int保存到byte数组
		bytes[0] = (byte) (num >>> 24);
		bytes[1] = (byte) (num >>> 16);
		bytes[2] = (byte) (num >>> 8);
		bytes[3] = (byte) num;
		return bytes;
	}

}