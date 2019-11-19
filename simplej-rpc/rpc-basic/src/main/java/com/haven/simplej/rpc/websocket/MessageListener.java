package com.haven.simplej.rpc.websocket;

/**
 * websocket 消息监听
 * @author: havenzhang
 * @date: 2018/10/27 22:35
 * @version 1.0
 */
public interface MessageListener {

	/**
	 * 消息回调
	 * @param message 消息
	 */
	void onMessage(String message);

	/**
	 * 需要自定义该监听器监听的消息类型，为空，则监听所有类型的消息
	 * @return String
	 */
	String getMessageType();
}
