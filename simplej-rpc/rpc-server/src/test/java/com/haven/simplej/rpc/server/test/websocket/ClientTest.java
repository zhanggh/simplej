package com.haven.simplej.rpc.server.test.websocket;

import com.haven.simplej.rpc.client.websocket.RpcWebSocketClient;
import com.haven.simplej.rpc.websocket.MessageListener;
import org.junit.Test;

import java.net.URI;

/**
 * @author: havenzhang
 * @date: 2019/10/27 23:01
 * @version 1.0
 */
public class ClientTest {

	@Test
	public void send() throws Exception {
		URI uri = new URI("ws://127.0.0.1:9393/rpc/websocket/test/00001");
		RpcWebSocketClient client = new RpcWebSocketClient(uri);
		client.addMessageListner(new MessageListener() {
			@Override
			public void onMessage(String message) {
				System.out.println(message);
			}

			@Override
			public String getMessageType() {
				return null;
			}
		});
		client.sendMessage("test message");

		Thread.sleep(10000);
		client.sendMessage("test message1");
		Thread.sleep(10000);
		client.sendMessage("test message2");
		Thread.sleep(900000);
	}
}
