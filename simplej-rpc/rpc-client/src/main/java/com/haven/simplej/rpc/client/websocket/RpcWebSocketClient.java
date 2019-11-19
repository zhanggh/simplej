package com.haven.simplej.rpc.client.websocket;

import com.google.common.collect.Lists;
import com.haven.simplej.rpc.websocket.MessageListener;
import com.haven.simplej.spring.SpringContext;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/10/27 22:20
 * @version 1.0
 */
@Slf4j
public class RpcWebSocketClient extends WebSocketClient {


	/**
	 * 消息监听器
	 */
	private List<MessageListener> listeners = Lists.newArrayList();


	public RpcWebSocketClient(URI serverUri) {
		super(serverUri);
		List<MessageListener> temListencers = SpringContext.getBeansOfType(MessageListener.class);
		if (CollectionUtil.isNotEmpty(temListencers)) {
			listeners.addAll(temListencers);
		}
	}

	/**
	 * 增加消息监听器
	 * @param listener
	 */
	public void addMessageListner(MessageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {

		log.debug("websocket onOpen");
	}

	@Override
	public void onMessage(String message) {
		log.debug("websocket onMessage,msg:{}", message);
		listeners.parallelStream().forEach(listener -> listener.onMessage(message));
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.debug("websocket onClose,reason:{}", reason);
	}

	@Override
	public void onError(Exception e) {
		log.error("websocket onError", e);
	}

	/**
	 * 发送消息
	 * @param message 消息
	 * @return boolean
	 */
	public boolean sendMessage(String message) throws InterruptedException {
		log.debug("state:{}", this.getReadyState());
		if (this.getReadyState().equals(READYSTATE.NOT_YET_CONNECTED)) {
			this.connectBlocking();
		}else if (!this.getReadyState().equals(READYSTATE.OPEN)) {
			//重连
			this.reconnectBlocking();
		}
		this.send(message);
		return true;
	}
}
