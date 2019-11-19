package com.haven.simplej.rpc.protocol.context;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.Server;
import com.haven.simplej.sequence.SequenceUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 执行上下文
 * @author: havenzhang
 * @date: 2019/1/28 20:55
 * @version 1.0
 */
@Slf4j
public class InvocationContext {

	private static ThreadLocal<RpcHeader> threadLocalHeader =
			ThreadLocal.withInitial(() -> new RpcHeader(SequenceUtil.getTraceId()));
	private static ThreadLocal<Server> threadServer = ThreadLocal.withInitial(() -> null);
	private static ThreadLocal<String> threadTraceId = ThreadLocal.withInitial(() -> SequenceUtil.getTraceId());


	public static void setHeader(RpcHeader header) {
		threadLocalHeader.set(header);
	}

	public static RpcHeader getHeader() {

		return new RpcHeader(SequenceUtil.getTraceId());
	}

	public static void setServer(String host, int port) {
		threadServer.set(new Server(host, port));
	}

	public static Server getServer() {
		return threadServer.get();
	}

	public static void removeServerInfo() {
		threadServer.remove();
	}

	/**
	 * 向请求头增加更多信息
	 * @param key key
	 * @param value 值
	 */
	public static void addHeader(String key,String value){
		getHeader().getAddition().put(key,value);
	}

	/**
	 * 获取链路id
	 */
	public static String getTraceId() {
		return threadTraceId.get();
	}

	public static void removeTraceId(){
		threadTraceId.remove();
	}

	public static void setTraceId(String traceId) {
		threadTraceId.set(traceId);
	}
}

