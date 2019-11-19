package com.haven.simplej.rpc.protocol.coder;

import java.util.List;

/**
 * 自定义编解码器
 * @author: havenzhang
 * @date: 2018/11/14 23:15
 * @version 1.0
 */
public interface Coder {

	/**
	 * 编码
	 * @param object 消息对象
	 * @return byte数组
	 */
	byte[] encode(List<Object> object);


	/**
	 * 解码
	 * @param bytes byte数组
	 * @return 消息对象
	 */
	List<Object> decode(byte[] bytes);
}
