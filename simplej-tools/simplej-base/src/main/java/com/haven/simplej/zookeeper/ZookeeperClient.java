package com.haven.simplej.zookeeper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ZK客户端，保存在节点上的数据用UTF-8编码
 * @author haven.zhang
 * @date 2019/1/29.
 */
@Setter
@Getter
public class ZookeeperClient {

	private static int DEFAULT_RETRY_SLEEP_TIME = 1000;
	private static int DEFAULT_RETRY_SLEEP_COUNT = 3;
	private static int DEFAULT_SESSION_TIMEE = 5000;
	private static int DEFAULT_CONNECTION_TIMEOUT = 3000;

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODE = "UTF-8";

	private String connectionString;
	private String namespace;

	private CuratorFramework client;

	private static ZookeeperClient zkClient;

	public CuratorFramework getCuratorClient() {
		return client;
	}

	public static ZookeeperClient getInstance(String connectionString, String namespace) {
		if (zkClient != null) {
			return zkClient;
		}
		zkClient = new ZookeeperClient(connectionString, namespace);
		return zkClient;
	}

	/**
	 *
	 * @param connectionString 服务器列表，格式host1:port1,host2:port2,...
	 * @param namespace 为每一种业务定义应隔离的命名空间
	 */
	public ZookeeperClient(String connectionString, String namespace) {
		this.connectionString = connectionString;
		this.namespace = namespace;
	}


	public synchronized void start() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(DEFAULT_RETRY_SLEEP_TIME, DEFAULT_RETRY_SLEEP_COUNT);
		client = CuratorFrameworkFactory.builder().connectString(connectionString)
				.sessionTimeoutMs(DEFAULT_SESSION_TIMEE).connectionTimeoutMs(DEFAULT_CONNECTION_TIMEOUT)
				.retryPolicy(retryPolicy).namespace(namespace).build();
		client.start();
	}


	/**
	 * 发布数据到临时节点上，如果节点不存在则创建
	 * @param path
	 * @param data
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public <T> boolean publish(String path, T data) {

		return publish(path, data, CreateMode.EPHEMERAL);
	}


	/**
	 * 发布数据到临时节点上，如果节点不存在则创建
	 * @param path
	 * @param data
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public <T> boolean publish(String path, T data, CreateMode mode) {
		boolean resp;
		try {
			byte[] bytes = JSON.toJSONString(data).getBytes(DEFAULT_ENCODE);
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				client.create().creatingParentContainersIfNeeded().withMode(mode).forPath(path, bytes);
			} else {
				client.setData().forPath(path, bytes);
			}
			resp = true;
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return resp;
	}

	/**
	 * 从某个指定的节点读取数据
	 * @param path
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public <T> T getData(String path, Class<T> clz) {

		byte[] bytes = new byte[0];
		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return null;
			}
			bytes = getCuratorClient().getData().forPath(path);
			if (bytes == null) {
				return null;
			}
			return JSON.parseObject(new String(bytes,DEFAULT_ENCODE), clz);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 从某个指定的节点读取数据
	 * @param path
	 * @return
	 */
	public byte[] getData(String path) {

		byte[] bytes = new byte[0];
		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return null;
			}
			bytes = getCuratorClient().getData().forPath(path);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return bytes;
	}

	/**
	 * 关闭zk客户端连接
	 */
	public void close() {
		if (client != null) {
			this.client.close();
		}
	}

	/**
	 * 判断节点是否是持久化节点
	 * @param path 路径
	 * @return null-节点不存在  | CreateMode.PERSISTENT-是持久化 | CreateMode.EPHEMERAL-临时节点
	 */
	public CreateMode getNodeType(String path) {
		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return null;
			}
			if (stat.getEphemeralOwner() > 0) {
				return CreateMode.EPHEMERAL;
			}
			return CreateMode.PERSISTENT;
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 删除节点
	 * @param path
	 * @return
	 */
	public boolean deleteCurrent(String path) {

		try {
			client.delete().forPath(path);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return true;
	}

	/**
	 * 强制删除节点，如果存在子节点则递归删除下面的所有子节点
	 * @param path
	 * @return
	 */
	public boolean deleteForce(String path) {

		try {
			client.delete().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return true;
	}

	/**
	 * 判断某个节点是否为叶子节点
	 * @param path
	 * @return
	 */
	public boolean isLeaf(String path) {

		try {
			List<String> childs = client.getChildren().forPath(path);
			if (CollectionUtil.isNotEmpty(childs)) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 判断节点是否存在
	 * @param path
	 * @return
	 */
	public boolean exisit(String path) {
		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 获取指定路径的节点信息，如果非叶子节点，则递归遍历所有子节点信息
	 * @param path
	 * @return
	 */
	public ZkNode getZkNode(String path) {

		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return null;
			}
			ZkNode node = new ZkNode();
			node.setPath(path);
			byte[] data = client.getData().forPath(path);
			node.setData(data);
			if (stat.getEphemeralOwner() > 0) {
				node.setMode(CreateMode.EPHEMERAL);
			} else {
				node.setMode(CreateMode.PERSISTENT);
			}

			List<String> childs = client.getChildren().forPath(path);
			if (CollectionUtil.isNotEmpty(childs)) {
				List<ZkNode> nodes = Lists.newArrayList();
				String childPath;
				for (String child : childs) {
					childPath = path + "/" + child;
					ZkNode childNode = getZkNode(childPath.replaceAll("//", "/"));
					nodes.add(childNode);
				}
				node.setChildrens(nodes);
			}
			return node;
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 从跟节点开始扫描，返回所有的叶子节点
	 * @param root
	 * @return
	 */
	public List<ZkNode> getLeafs(ZkNode root) {
		List<ZkNode> leafs = Lists.newArrayList();
		if(root == null){
			return leafs;
		}
		if (root.isLeaf()) {
			leafs.add(root);
		} else {
			for (ZkNode zkNode : root.getChildrens()) {
				leafs.addAll(getLeafs(zkNode));
			}
		}
		return leafs;
	}

	/**
	 * 判断节点下是否存在某个目录
	 * @param path
	 * @param zkNode
	 * @return
	 */
	public boolean isPathInNode(String path, ZkNode zkNode) {

		if (zkNode.getPath().equals(path)) {
			return true;
		}
		if (CollectionUtil.isNotEmpty(zkNode.getChildrens())) {
			for (ZkNode node : zkNode.getChildrens()) {
				if (isPathInNode(path, node)) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 注册节点内容监听者
	 * @param path
	 * @param listener
	 */
	public NodeCache registerListener(String path, NodeCacheListener listener) throws Exception {
		NodeCache cache = new NodeCache(client, path, false);
		cache.start(false);
		cache.getListenable().addListener(listener);

		return cache;
	}

	/**
	 * 注册节点内容监听者
	 * @param path
	 * @param listener
	 */
	public PathChildrenCache registerListener(String path, PathChildrenCacheListener listener) throws Exception {
		PathChildrenCache cache = new PathChildrenCache(client, path, true);
		cache.start(PathChildrenCache.StartMode.NORMAL);
		cache.getListenable().addListener(listener);
		return cache;
	}

	/**
	 *
	 * @描述：第一种监听器的添加方式: 对指定的节点进行添加操作
	 * 仅仅能监控指定的本节点的数据修改,删除 操作 并且只能监听一次 --->不好
	 * @return void
	 * @exception
	 * @createTime：2016年5月18日
	 * @author: songqinghu
	 * @throws Exception
	 */
	private static void setListenterOne(CuratorFramework client, String path) throws Exception {
		// 注册观察者，当节点变动时触发
		byte[] data = client.getData().usingWatcher(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("获取 two 节点 监听器 : " + event);
			}
		}).forPath(path);
		System.out.println("two 节点数据: " + new String(data));
	}

	/**
	 *
	 * @描述：第二种监听器的添加方式:
	 * 也是一次性的监听操作, 使用后就无法在继续监听了
	 * @return void
	 * @exception
	 * @createTime：2016年5月18日
	 * @author: songqinghu
	 * @throws Exception
	 */
	private static void setListenterTwo(CuratorFramework client, String path) throws Exception {

		ExecutorService pool = Executors.newCachedThreadPool();

		CuratorListener listener = new CuratorListener() {
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("监听器  : " + event.toString());
			}
		};
		client.getCuratorListenable().addListener(listener, pool);
		client.getData().inBackground().forPath(path);
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 *
	 * @描述：第三种监听器的添加方式: Cache 的三种实现 实践
	 *   Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。
	 *                  产生的事件会传递给注册的PathChildrenCacheListener。
	 *  Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
	 *  Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
	 * @return void
	 * @exception
	 * @createTime：2016年5月18日
	 * @author: songqinghu
	 * @throws Exception
	 */
	//1.path Cache  连接  路径  是否获取数据
	//能监听所有的字节点 且是无限监听的模式 但是 指定目录下节点的子节点不再监听
	private static void setListenterThreeOne(CuratorFramework client, String path) throws Exception {
		ExecutorService pool = Executors.newCachedThreadPool();
		PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
		PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.out.println("开始进行事件分析:-----");
				ChildData data = event.getData();
				switch (event.getType()) {
					case CHILD_ADDED:
						System.out.println("CHILD_ADDED : " + data.getPath() + "  数据:" + data.getData());
						break;
					case CHILD_REMOVED:
						System.out.println("CHILD_REMOVED : " + data.getPath() + "  数据:" + data.getData());
						break;
					case CHILD_UPDATED:
						System.out.println("CHILD_UPDATED : " + data.getPath() + "  数据:" + data.getData());
						break;
					default:
						break;
				}
			}
		};
		childrenCache.getListenable().addListener(childrenCacheListener);
		System.out.println("Register zk watcher successfully!");
		childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
	}

	//2.Node Cache  监控本节点的变化情况   连接 目录 是否压缩
	//监听本节点的变化  节点可以进行修改操作  删除节点后会再次创建(空节点)
	private static void setListenterThreeTwo(CuratorFramework client, String path) throws Exception {
		ExecutorService pool = Executors.newCachedThreadPool();
		//设置节点的cache
		final NodeCache nodeCache = new NodeCache(client, path, false);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("the test node is change and result is :");
				System.out.println("path : " + nodeCache.getCurrentData().getPath());
				System.out.println("data : " + new String(nodeCache.getCurrentData().getData()));
				System.out.println("stat : " + nodeCache.getCurrentData().getStat());
			}
		});
		nodeCache.start();
	}

	//3.Tree Cache
	// 监控 指定节点和节点下的所有的节点的变化--无限监听  可以进行本节点的删除(不在创建)
	private static void setListenterThreeThree(CuratorFramework client, String path) throws Exception {
		ExecutorService pool = Executors.newCachedThreadPool();
		//设置节点的cache
		TreeCache treeCache = new TreeCache(client, path);
		//设置监听器和处理过程
		treeCache.getListenable().addListener(new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				ChildData data = event.getData();
				if (data != null) {
					switch (event.getType()) {
						case NODE_ADDED:
							System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
							break;
						case NODE_REMOVED:
							System.out
									.println("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
							break;
						case NODE_UPDATED:
							System.out
									.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
							break;

						default:
							break;
					}
				} else {
					System.out.println("data is null : " + event.getType());
				}
			}
		});
		//开始监听
		treeCache.start();

	}

}

