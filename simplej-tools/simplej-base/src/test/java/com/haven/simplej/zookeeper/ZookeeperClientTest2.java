package com.haven.simplej.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author haven.zhang
 * @date 2019/1/29.
 */
public class ZookeeperClientTest2 {

	public static void main(String[] args) throws Exception {
		ZookeeperClient client = new ZookeeperClient("127.0.0.1:2181", "test");
		client.start();
		String path = "/test12";
		client.registerListener(path, new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("Node data update, new data: " );
			}
		});

		client.registerListener(path, new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent)
					throws Exception {
				System.out.println("type:"+pathChildrenCacheEvent.getType().name());
			}
		});

		Thread.sleep(Integer.MAX_VALUE);
		System.out.println("------------------");

	}
}
