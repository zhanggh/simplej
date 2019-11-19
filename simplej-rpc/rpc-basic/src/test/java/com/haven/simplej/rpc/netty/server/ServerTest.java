//package com.haven.simplej.rpc.netty.server;
//
//import com.google.common.collect.Lists;
//import com.haven.simplej.rpc.filter.RpcFilter;
//import com.haven.simplej.rpc.filter.impl.RpcLogFilter;
//import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
//import com.haven.simplej.rpc.protocol.processor.impl.CoreServiceProcessorImpl;
//import com.haven.simplej.rpc.server.NettyServer;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.List;
//
///**
// * @author: havenzhang
// * @date: 2019/4/26 16:03
// * @version 1.0
// */
//@Slf4j
//public class ServerTest {
//
//	private int port = 9091;
//
//
//	public static void main(String[] args) {
//		new ServerTest().linsterTest();
//	}
//	public void linsterTest() {
//		List<RpcFilter> filters = Lists.newArrayList();
//		filters.add(new RpcLogFilter());
//		IServiceProcessor processor = new CoreServiceProcessorImpl();
//		NettyServer.build(port).addFilters(filters).setProcessor(processor).start();
//		System.out.println("server startup......");
//	}
//
//}
