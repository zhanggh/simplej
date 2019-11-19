package com.haven.simplej.rpc.constant;

/**
 * 常量类
 * @author: havenzhang
 * @date: 2019/1/26 16:10
 * @version 1.0
 */
public class RpcConstants {


	/**
	 * 请求跟踪的链路id
	 */
	public static final String TRACE_ID = "traceId";

	/**
	 * rpc协议版本号 1.0对应的序列化工具是PROTOSTUFF
	 */
	public static final String VERSION_1 = "1.0";

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODE = "UTF-8";
	/**
	 * 服务列表路径前缀
	 */
	public static final String SERVICE_NAME_PATH_PREFIX = "/simplej/rpc/metadata/";
	/**
	 * 服务配置路径前缀
	 */
	public static final String SERVICE_CONFIG_PATH_PREFIX = "/simpej/rpc/config/";
	/**
	 * 服务实例路径前缀
	 */
	public static final String SERVICE_RUNTIME_PATH_PREFIX = "/simplej/rpc/runtime/";
	/**
	 * ZK连接地址
	 */
	public static final String RPC_ZK_CONNECTION_STRING_KEY = "RPC_ZK_CONNECTION_STRING";


	/**
	 * netty服务启动的监听端口号
	 */
	public static final String RPC_SERVER_PORT_KEY = "rpc.server.port";
	/**
	 * proxy的监听端口号
	 */
	public static final String RPC_PROXY_PORT_KEY = "rpc.proxy.port";
	/**
	 * proxy的监听http端口号
	 */
	public static final String RPC_PROXY_HTTP_PORT_KEY = "rpc.proxy.http.port";
	/**
	 * proxy的HOST
	 */
	public static final String RPC_PROXY_HOST_KEY = "rpc.proxy.host";
	/**
	 * 目标业务app的监听端口号
	 */
	public static final String RPC_APP_SERVER_PORT_KEY = "rpc.app.server.port";
	/**
	 * 目标业务app的HOST
	 */
	public static final String RPC_APP_SERVER_HOST_KEY = "rpc.app.server.host";
	/**
	 * 客户端连接池大小
	 */
	public static final String RPC_CLIENT_CONNECT_POOL_SIZE_KEY = "rpc.client.connect.pool.size";
	/**
	 * rpc服务定义类所在的包路径，多个以英文逗号分隔
	 */
	public static final String RPC_SERVICE_BASE_PACKAGE_KEY = "rpc.service.base.package";

	/**
	 * rpc服务定义类所在的包路径
	 */
	public static final String RPC_SPLIT_SYMBOL = ",";
	/**
	 * rpc服务role
	 */
	public static final String RPC_SERVER_ROLE_KEY = "rpc.server.role.name";

	/**
	 * 注册中心地址，如：127.0.0.1:8080,10.11.223.2:8080
	 */
	public static final String RPC_REGISTER_SERVERS_KEY = "rpc.register.servers";

	/**
	 *  app应用命名空间
	 */
	public static final String RPC_APP_NAME = "app.name";
	/**
	 * 序列号管理器每次向服务端请求序列号的时候，指定的步长
	 */
	public static final String SEQUENCE_STEP = "sequence.step";
	/**
	 *  app应用所在的idc
	 */
	public static final String RPC_APP_IDC = "rpc.app.idc";

	/**
	 * 虚拟区域
	 */
	public static final String RPC_APP_REGION_ID = "rpc.app.region.id";
	/**
	 * 默认的idc
	 */
	public static final String RPC_APP_IDC_DEFAULT = "default";
	/**
	 * 默认的region
	 */
	public static final String RPC_APP_REGION_DEFAULT = "default";


	/**
	 *  服务心跳请求在服务端waiting 时间
	 */
	public static final String RPC_HEARBEAT_WAIT_TIME_GAP = "rpc.heartbeat.waiting.gap";

	/**
	 * 客户端连接超时时间
	 */
	public static final String RPC_CLIENT_CONNECT_TIMEOUT = "rpc.client.connect.timeout";

	/**
	 * 客户端socket超时时间
	 */
	public static final String RPC_CLIENT_SOCKET_TIMEOUT = "rpc.client.socket.timeout";

	/**
	 * 用于判断当前server是否为rpc注册中心服务的标准，true：代表rpc-register
	 */
	public static final String RPC_REGISTER_SERVERS_FLAG = "rpc.register.server.flag";

	/**
	 * 本地ip
	 */
	public static final String LOCAHOST = "127.0.0.1";
	//监听全部ip地址
	public static final String WHOLE_NETWORK = "0.0.0.0";


	/**
	 * 是否允许输出同步服务列表的日志
	 */
	public static final String RPC_SYNC_SERVICE_LIST_LOG_ENABLE = "rpc.sync.service.list.log.enable";
	/**
	 * 空闲读时间
	 */
	public static final String RPC_READER_IDLE_TIME = "rpc.reader.idle.time";
	/**
	 * 写空闲 时间
	 */
	public static final String RPC_WRITER_IDLE_TIME = "rpc.writer.idle.time";
	/**
	 * 总空闲 时间
	 */
	public static final String RPC_ALL_IDLE_TIME = "rpc.all.idle.time";
	/**
	 * 接收缓冲区大小
	 */
	public static final String RPC_SERVER_RCVBUF = "rpc.server.rcvbuf";
	/**
	 * 队列大小
	 */
	public static final String RPC_SERVER_BACKLOG = "rpc.server.backlog";
	/**
	 * 写缓冲区大小
	 */
	public static final String RPC_SERVER_SENDBUF = "rpc.server.sendbuf";
	/**
	 * rpc 启动server监听的ip
	 */
	public static final String RPC_SERVER_HOST_KEY = "rpc.server.host";
	/**
	 * 是否开启心跳上报
	 */
	public static final String RPC_APP_SERVER_ENABLE_HEARTBEAT = "rpc.app.server.enable.heartbeat";


	/**
	 * rpc框架的日志
	 */
	public static final String RPC_SYSTEM_LOGGER = "rpc.system.logger";
	/**
	 * service监控等待时间
	 */
	public static final String RPC_REGISTER_SERVER_SERVICE_MONITOR_WAIT_TIME = "rpc.server.service.monitor.wait.time";

	/**
	 * 参数类型分隔符
	 */
	public static final String PARAMS_TYPE_SEPARATOR = ",";


	/**
	 * 空值
	 */
	public static final String NULL = "null";

	/**
	 * web服务启动的监听端口号
	 */
	public static final String WEB_SERVER_PORT_KEY = "server.port";

	/**
	 * web服务schema，http、https
	 */
	public static final String WEB_SERVER_SCHEMA_KEY = "rpc.web.server.schema";
	/**
	 * web服务默认的schema
	 */
	public static final String WEB_SERVER_DEFAULT_SCHEMA = "http";
	/**
	 * 是否允许注册web服务信息
	 */
	public static final String RPC_WEB_SERVER_ENABLE = "rpc.web.enable";
	/**
	 * 是否允许业务应用监听配置变更，默认是true
	 */
	public static final String RPC_APP_SERVER_LINSTEN_CONFIG_ENABLE = "rpc.app.server.listen.config.enable";
	/**
	 * 是否启动netty服务
	 */
	public static final String RPC_ENABLE_STARTUP_TCP_SERVER = "rpc.enable.startup.tcp.server";

	/**
	 * 默认的超时时间60s
	 */
	public static final long DEFAULT_TIMEOUT = 60000;

	/**
	 * rpc服务 命名空间（域名）定义的常量字段名，客户端反射获取
	 */
	public static final String RPC_SERVER_NAMESPACE_KEY = "NAMESPACE";

	/**
	 * 访问架构：app--> proxy --> proxy -->app 或者 app-->proxy--> app
	 */
	public static final String RPC_PROXY_TO_PROXY_KEY = "rpc.proxy.to.proxy";

	/**
	 * 客户端线程池大小
	 */
	public static final String RPC_CLIENT_THREAD_POOL_SIZE = "rpc.client.thread.pool.size";

	/**
	 * 是否允许在proxy进行编解码的服务命名空间，该属性配置在proxy
	 */
	public static final String ENABLE_PROXY_CODER_NAMESPACES = "enable.proxy.coder.namespaces";

	/**
	 * 服务关闭时，等待的时间（等待期内可能有数据还在处理中）
	 */
	public static final String RPC_SERVER_CLOSE_TIME_WAIT = "rpc.server.close.time.wait";

	/**
	 * boss EventLoopGroup 的线程数
	 */
	public static final String RPC_SERVER_NETTY_BOSS_THREAD_SIZE = "rpc.server.netty.boss.thread.size";

	/**
	 * workerGroup EventLoopGroup 的线程数
	 */
	public static final String RPC_SERVER_NETTY_WOKER_THREAD_SIZE = "rpc.server.netty.worker.thread.size";
}
