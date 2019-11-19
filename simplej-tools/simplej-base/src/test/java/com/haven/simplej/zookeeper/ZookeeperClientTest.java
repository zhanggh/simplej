package com.haven.simplej.zookeeper;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.time.DateUtils;
import com.haven.simplej.time.enums.DateStyle;

import java.util.Date;

/**
 * @author haven.zhang
 * @date 2019/1/29.
 */
public class ZookeeperClientTest {

	public static void main(String[] args) throws Exception {
		ZookeeperClient client = null;
		String path = "/test11/000001/epay/refund";
		//		client = new ZookeeperClient("127.0.0.1:2181", "test");
		//		client.start();
		////		client.deleteForce("/test11");

		//		client.publish(path, DateUtils.getDate(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
		//		String path2 = "/test11/000002/epay2";
		//		client.publish(path2, "111111" + DateUtils.getDate(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
		//		//		Thread.sleep(10000);
		//		System.out.println(client.isLeaf(path2));
		//		System.out.println(client.getData(path, String.class));
		//
		//		//		ZkNode node = client.getZkNode("/");
		//		//		System.out.println(JSON.toJSONString(node, true));
		//		//
		//		//		boolean isExisit = client.isPathInNode("/test11/000001/epay3", node);
		//		//
		//		//		System.out.println("isExisit:" + isExisit);
		//
		//
		//		Thread.sleep(3000);
		//		client.close();
		path = "/test12";
		client = new ZookeeperClient("127.0.0.1:2181", "test");
		client.start();
		if (!client.exisit(path)) {
			System.out.println("create node:" + path);
			client.publish(path, DateUtils.getDate(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue()));
		}else {
			System.out.println(path + " is exisit " + client.exisit(path));
		}
		System.out.println(client.getData(path, String.class));
		System.out.println(client.getNodeType(path));
		client.close();
		System.out.println("------------------");

	}
}
