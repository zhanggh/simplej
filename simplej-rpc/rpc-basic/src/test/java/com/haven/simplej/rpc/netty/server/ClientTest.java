//package com.haven.simplej.rpc.netty.server;
//
//import com.alibaba.fastjson.JSON;
//import com.haven.simplej.rpc.client.NettyClient;
//import com.haven.simplej.rpc.enums.SerialType;
//import com.haven.simplej.rpc.model.RpcBody;
//import com.haven.simplej.rpc.model.RpcHeader;
//import com.haven.simplej.rpc.model.RpcRequest;
//import com.haven.simplej.rpc.model.RpcResponse;
//
//import java.util.UUID;
//
///**
// * @author: havenzhang
// * @date: 2019/4/26 17:26
// * @version 1.0
// */
//public class ClientTest {
//
//	public static void main(String[] args) throws Exception {
//		NettyClient client = NettyClient.getInstance("127.0.0.1", 9091);
//
//		//消息体
//		RpcRequest request = new RpcRequest();
//		RpcHeader header = new RpcHeader();
//		header.setMsgId(UUID.randomUUID().toString());
//		header.setVersion("1.0");
//		header.setSerialType(SerialType.PROTOSTUFF.getValue());
//		request.setHeader(header);
//
//		RpcBody body = new RpcBody();
////		body.put("name", "zhangsan----");
////		body.put("age", "38");
//
//		request.setBody(body);
//		//		log.debug("send msg:{}", JSON.toJSONString(request, true));
//		//channel对象可保存在map中，供其它地方发送消息
//		RpcResponse response = client.send(request);
//		System.out.println("client resp.........");
//		System.out.println(JSON.toJSONString(response,true));
//	}
//}
