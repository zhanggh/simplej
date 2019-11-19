package com.haven.simplej.rpc.server.websocket;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.websocket.MessageListener;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: havenzhang
 * @date: 2018/10/27 21:52
 * @version 1.0
 * type:请求类型：业务端自定义
 * sid:客户端唯一id
 * 每一个新的连接，都会产生一个新的WebSocketServer 实例
 */
@Slf4j
@ServerEndpoint("/rpc/websocket/{type}/{sid}")
@Component
@Getter
public class WebSocketServer {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static LongAdder onlineCount = new LongAdder();
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	private static Map<String, CopyOnWriteArraySet<WebSocketServer>> webSocketMap = Maps.newConcurrentMap();

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	//接收sid
	private String sid = "";

	/**
	 * 业务分类
	 */
	private String type = "";

	/**
	 * 消息监听器
	 */
	private List<MessageListener> listeners = Lists.newArrayList();

	public WebSocketServer() {
		List<MessageListener> temListencers = SpringContext.getBeansOfType(MessageListener.class);
		if (CollectionUtil.isNotEmpty(temListencers)) {
			listeners.addAll(temListencers);
		}
	}

	/**
	 * 连接建立成功调用的方法*/
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("type") String type) {
		this.session = session;
		if (webSocketMap.containsKey(type)) {
			webSocketMap.get(type).add(this);
		} else {
			CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
			webSocketSet.add(this);
			webSocketMap.put(type, webSocketSet);     //加入set中
		}

		//在线数加1
		addOnlineCount();
		log.info("websocket onPen,sid:{},total connect size:{}", sid, getOnlineCount());
		this.sid = sid;
		this.type = type;
		try {
			sendMessage("websocket connect success");
		} catch (IOException e) {
			log.error("websocket IOException", e);
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		//从set中删除
		if (CollectionUtil.isNotEmpty(webSocketMap.get(this.getType()))) {
			webSocketMap.get(this.getType()).remove(this);
		}

		//在线数减1
		subOnlineCount();
		log.debug("websocket client close, total connection size:{}", getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息*/
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("onMessage,sid:{},message:{}", sid, message);
		listeners.parallelStream().forEach(listener -> {
			if (StringUtil.isEmpty(listener.getMessageType()) || StringUtil.equalsIgnoreCase(listener.getMessageType()
					, type)) {
				listener.onMessage(message);
			}
		});
	}

	/**
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("websocket server error", error);
		//从set中删除
		if (CollectionUtil.isNotEmpty(webSocketMap.get(this.getType()))) {
			webSocketMap.get(this.getType()).remove(this);
		}
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 * @param type 消息类型
	 * @param message 消息
	 */
	public static void sendToClient(String type, String message) {

		sendToClient(null, type, message);
	}

	/**
	 * 群发自定义消息
	 * */
	public static void sendToClient(String sid, String type, String message) {
		log.info("推送消息到窗口" + sid + "，推送内容:" + message);
		CopyOnWriteArraySet<WebSocketServer> webSocketSet = webSocketMap.get(type);
		if (CollectionUtil.isEmpty(webSocketSet)) {
			return;
		}
		for (WebSocketServer item : webSocketSet) {
			try {
				//这里可以设定只推送给这个sid的，为null则全部推送
				if (sid == null) {
					item.sendMessage(message);
				} else if (item.sid.equals(sid)) {
					item.sendMessage(message);
				}
			} catch (IOException e) {
				item.onError(item.getSession(),e);
				log.warn("websocket send error,sessionId:{}", item.getSession().getId());
				continue;
			}
		}
	}

	/**
	 * 获取当前连接总数
	 * @return long
	 */
	public static long getOnlineCount() {
		return onlineCount.sum();
	}

	/**
	 * 增加连接数
	 */
	public static void addOnlineCount() {
		onlineCount.increment();
	}

	/**
	 * 减少连接数
	 */
	public static void subOnlineCount() {
		onlineCount.decrement();
	}
}
