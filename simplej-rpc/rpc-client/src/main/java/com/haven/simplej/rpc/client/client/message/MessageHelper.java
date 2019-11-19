package com.haven.simplej.rpc.client.client.message;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.client.client.callback.ISendCallBack;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 客户端消息接收池
 * 注意，消息返回，获取消息之后，必须删除缓存里面的消息，否则会导致oom
 * @author: havenzhang
 * @date: 2018/4/28 15:50
 * @version 1.0
 */
@Slf4j
public class MessageHelper {

	/**
	 * 消息池
	 */
	private static final Map<String, Object> messageMap = Maps.newConcurrentMap();
	/**
	 * 消息回调
	 */
	private static final Map<String, List<ISendCallBack>> messageCallBackMap = Maps.newConcurrentMap();

	/**
	 * promise 缓存
	 */
	private static final Map<String, ChannelPromise> promiseMap = Maps.newConcurrentMap();


	/**
	 * 把消息放到池子里面去
	 * @param msgId 消息的唯一id
	 * @param object 响应消息
	 */
	public static <T> void addMessage(String msgId, T object) {
		if (StringUtil.isEmpty(msgId)) {
			log.error("invalid response,msg:{}", JSON.toJSONString(object, true));
			throw new RpcException(RpcError.INVAILD_RESPONSE, "msgId is empty");
		}
		messageMap.put(msgId, object);
	}

	/**
	 * 获取消息
	 * @param msgId 消息的唯一id
	 * @return 响应消息
	 */
	public static <T> T take(String msgId) {
		return (T) messageMap.remove(msgId);
	}

	/**
	 * 增加promise
	 * @param msgId 消息的唯一id
	 * @param promise 令牌
	 */
	public static void addPromise(String msgId, ChannelPromise promise) {
		promiseMap.put(msgId, promise);
	}

	public static ChannelPromise getPromise(String msgId) {
		return promiseMap.get(msgId);
	}


	public static ChannelPromise removePromise(String msgId) {
		return promiseMap.remove(msgId);
	}


	/**
	 * 根据msgid获取回调类列表
	 */
	public static List<ISendCallBack> getCallBack(String msgId) {

		List<ISendCallBack> callBackList = messageCallBackMap.remove(msgId);
		return callBackList;
	}

	/**
	 * 为指定的msgid添加回调类
	 */
	public static void addCallback(String msgId, ISendCallBack callBack) {
		if (callBack == null) {
			return;
		}
		List<ISendCallBack> list = Lists.newArrayList();
		list.add(callBack);
		list = messageCallBackMap.putIfAbsent(msgId, list);
		if (CollectionUtil.isNotEmpty(list)) {
			messageCallBackMap.get(msgId).add(callBack);
		}
	}
}
