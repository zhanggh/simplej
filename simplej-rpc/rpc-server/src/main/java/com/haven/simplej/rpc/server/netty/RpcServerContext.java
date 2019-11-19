package com.haven.simplej.rpc.server.netty;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.net.NetUtil;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.RpcServerType;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceLookupKey;
import com.haven.simplej.rpc.protocol.coder.Coder;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.heartbeat.HeartbeatManager;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.http.WebUrlManager;
import com.haven.simplej.rpc.server.loader.ServiceLoader;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.rpc.server.processor.impl.ServerProcessorImpl;
import com.haven.simplej.rpc.server.signal.AppSignalHandler;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.windows.WindowUtil;
import com.haven.simplej.windows.enums.EnumOS;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.Signal;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * rpc服务容器,当服务端需要使用本框架提供的rpc服务的时候
 * 可以直接在app bean 配置中配置实例化该类，如：new RpcServerContext()
 * @author: havenzhang
 * @date: 2018/4/28 16:46
 * @version 1.0
 */
@Slf4j
public class RpcServerContext {

	/**
	 * 服务状态
	 */
	private static volatile int state;

	/**
	 * rpc消息处理器
	 */
	private IServiceProcessor processor;

	/**
	 * 服务注册接口
	 */
	@Autowired
	private IServiceRegister serviceRegister;

	/**
	 * netty服务
	 */
	private NettyServer server;

	/**
	 * 服务启动时间
	 */
	private static Date startUpTime;

	/**
	 * 信号量监听器
	 */
	@Autowired
	private AppSignalHandler signalHandler;


	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters = Lists.newArrayList();

	public RpcServerContext(List<RpcFilter> filters) {

		this(null, filters);
	}

	public RpcServerContext(IServiceProcessor processor, List<RpcFilter> filters) {
		super();
		log.info("RpcServerContext start rpc server");
		if (CollectionUtil.isNotEmpty(filters)) {
			this.filters.addAll(filters);
		}

		this.processor = processor;
		//初始化相关环节
		init();
	}

	/**
	 * 启动服务
	 */
	public synchronized boolean startRpcServer() {
		if (state == 1) {
			log.warn("RpcServer already startup ");
			return false;
		}
		String port = PropertyManager.get(RpcConstants.RPC_SERVER_PORT_KEY);
		if (StringUtil.isEmpty(port)) {
			//当前服务属于proxy服务还是业务app服务
			String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
					RpcServerType.BUSINESS_SERVER.name());

			if (StringUtil.equalsIgnoreCase(serverType, RpcServerType.PROXY.name())) {
				throw new UncheckedException("rpc.server.port must set in properties or set  -Drpc.server.port in" +
						" start up command ,such as java -Drpc.server.port 8080 -jar xxxxx.jar ");
			}
			//非proxy服务的情况下，如果没有配置端口，则随机取一个本机可用的端口
			port = String.valueOf(NetUtil.findRandomAvailablePort(5000, 9000));
			log.info("no port set in properties,findRandomAvailablePort :{}", port);
			PropertyManager.getProp().setProperty(RpcConstants.RPC_SERVER_PORT_KEY, port);
		}
		//加载本地服务信息
		loadService();

		//当前服务属于proxy服务还是业务app服务
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		if (processor == null) {
			if (!StringUtil.equalsIgnoreCase(serverType, RpcServerType.PROXY.name())) {
				//默认的请求服务端处理器
				processor = new ServerProcessorImpl();
			}
		}

		//启动netty服务
		log.info("rpc server listening port : {}", port);
		server = NettyServer.listen(Integer.parseInt(port));
		//增加信号监听
		addSignal();
		state = 1;
		List<StartUpListener> listeners = Lists.newArrayList();
		//增加启动服务成功之后的后置处理
		listeners.add(() -> afterServerUp());
		//启动服务！！！
		server.addStartUpListener(listeners).addFilters(filters).setProcessor(processor).start();
		state = 0;
		return true;
	}

	/**
	 * 加载服务信息，用于服务注册
	 */
	private void loadService() {
		String packages = PropertyManager.get(RpcConstants.RPC_SERVICE_BASE_PACKAGE_KEY);
		String[] pkgs = StringUtil.split(packages, RpcConstants.RPC_SPLIT_SYMBOL);
		if (pkgs != null && pkgs.length > 0) {
			//指定加载的rpcservice 包路径
			for (String pkg : pkgs) {
				log.info("loadClass rpc package:{}", pkg);
				ServiceLoader.getInstance().addServiceBasePackage(pkg);
			}
		}

		ServiceLoader.getInstance().addServiceBasePackage();
		//		//把系统默认的监控检测服务也加进去
		//		ServiceLoader.getInstance().addServiceBasePackage(HeathCheckServiceImpl.class.getPackage().getName());
		//加载本地所有的rpc service，这个加载很费时，必须注意，需要指定包名
		Map<ServiceLookupKey, ServiceInfo> serviceInfoMap = ServiceLoader.getInstance().loadLocalService();
		log.debug("serviceInfo map :{}", JSON.toJSONString(serviceInfoMap, true));
	}


	/**
	 * 信号监听
	 */
	private void addSignal() {
		//		Signal.handle(new Signal("TERM"), signalHandler);
		if (WindowUtil.getOs().equals(EnumOS.linux) || WindowUtil.getOs().equals(EnumOS.solaris)) {
			Signal.handle(new Signal("USR1"), signalHandler);
			Signal.handle(new Signal("USR2"), signalHandler);
			Signal.handle(new Signal("QUIT"), signalHandler);
		}
	}

	/**
	 * 启动之前对相关配置检查
	 */
	private void checkBeforeStartUp() {
		String appName = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		if (StringUtil.isEmpty(appName)) {
			throw new UncheckedException("can not find app.name in application.properties file,set it please");
		}
	}

	/**
	 * 初始化相关bean/配置
	 */
	private void init() {
		try {
			//参数检查
			checkBeforeStartUp();
			startUpTime = new Date();

			//缓存自定义coder
			initCoder();

			//启动netty服务
			startNetty();
		} catch (Exception e) {
			log.error("startup error", e);
			System.exit(-1);
		}
		//优雅停机
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("---------------------------shutdown---------------------------");
			signalHandler.shutdown();
		}));
	}

	/**
	 * 缓存自定义编码器
	 */
	private void initCoder() {
		List<Coder> coders = SpringContext.getBeansOfType(Coder.class);
		if(CollectionUtil.isEmpty(coders)){
			return;
		}
		coders.forEach(e -> RpcHelper.addCoder(e));
	}

	/**
	 * 服务启动完成之后执行
	 */
	private void afterServerUp() {
		//启动心跳,向proxy发送心跳,long poll，长连接
		HeartbeatManager.heartbeatStart();

		//服务注册
		registryService();
	}

	/**
	 * 启动netty服务
	 */
	private void startNetty() {
		boolean startNetty = PropertyManager.getBoolean(RpcConstants.RPC_ENABLE_STARTUP_TCP_SERVER, false);
		if (startNetty) {

			//异步启动netty服务
			ThreadPoolFactory.getServerExecutor().submit(() -> {
				try {
					startRpcServer();
				} catch (Throwable e) {
					log.error("start up netty error", e);
					//netty服务启动失败，则关闭所有服务
					signalHandler.shutdown();
				}
			});
		}
	}


	/**
	 * 服务注册
	 */
	private void registryService() {
		//proxy和注册中心服务不需要做服务注册
		if (!RpcHelper.isRpcProxy() && !RpcHelper.isRpcRegister()) {
			//非proxy服务，只要有配置本地proxy，都发起服务注册
			if (ServiceInfoHelper.hasProxy()) {
				log.info("register service asynchronous");
				//异步向注册中心注册服务信息
				ThreadPoolFactory.getServerExecutor().execute(() -> {
					//注册web url 列表
					boolean enableWebRegister = PropertyManager.getBoolean(RpcConstants.RPC_WEB_SERVER_ENABLE, false);
					if (enableWebRegister) {
						WebUrlManager.getInstance().register();
					} else {
						log.info("web server not enable");
					}

					//注册rpc服务列表
					boolean startNetty = PropertyManager.getBoolean(RpcConstants.RPC_ENABLE_STARTUP_TCP_SERVER, false);
					if (startNetty) {
						ServiceLoader.getInstance().register();
					} else {
						log.info("netty server not enable");
					}
				});
			} else {
				log.warn("local proxy host is empty,do not register service");
			}
		}
	}

	/**
	 * 服务启动时间
	 * @return Date
	 */
	public static Date getStartUpTime() {
		return RpcServerContext.startUpTime;
	}

}
