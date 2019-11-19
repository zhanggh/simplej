package com.haven.simplej.rpc.proxy.init;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import com.haven.simplej.rpc.proxy.constant.RpcProxyConstant;
import com.haven.simplej.rpc.proxy.manager.ServiceManager;
import com.haven.simplej.rpc.server.helper.ServerHelper;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.spring.SpringContext;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务启动完成监听
 * @author: havenzhang
 * @date: 2018/8/19 20:51
 * @version 1.0
 */
@Component
@Slf4j
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {


	@Autowired
	private ServiceManager serviceManager;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			log.debug("-------------------StartupListener----------------------");
			//同步服务信息
			List<ServiceInfo> serviceInfos = syncServiceInfo();

			//加载外部插件
			loadPlugin(serviceInfos);
		} catch (Exception e) {
			log.error("rpc proxy start up fail", e);
			//如果加载服务列表失败，或者加载插件失败，则proxy启动失败，关闭相关线程
			ServerHelper.shutdownAll();
		}
	}


	/**
	 * 同步服务列表
	 */
	private List<ServiceInfo> syncServiceInfo() {
		//服务启动时，同步获取服务列表信息
		List<ServiceInfo> serviceInfoList = serviceManager.syncServiceList();

		//是否启动定时轮询更新服务列表
		boolean scheduleFlag = PropertyManager.getBoolean(RpcProxyConstant.RPC_PROXY_SERVICE_LISTEN_SCHEDULE, true);
		if (!scheduleFlag) {
			log.debug(" No need to query service information regularly ,scheduleFlag is false");
			return serviceInfoList;
		}

		/**
		 * 默认每秒同步一次
		 */
		int listenGap = PropertyManager.getInt(RpcProxyConstant.RPC_PROXY_SERVICE_LISTEN_TIME_GAP_KEY, 1);
		//服务启动之后从rpc-server同步所有服务列表
		ThreadPoolFactory.getScheduleExecutor().scheduleAtFixedRate(() -> serviceManager.syncServiceList(), 0,
				listenGap, TimeUnit.SECONDS);
		return serviceInfoList;
	}


	/**
	 * 加载外部插件
	 * @param serviceInfos 服务列表
	 */
	private void loadPlugin(List<ServiceInfo> serviceInfos) {
		log.debug("loadClass plugin list");
		List<RpcPlugin> pluginList = SpringContext.getBeansOfType(RpcPlugin.class);
		if (CollectionUtil.isNotEmpty(pluginList)) {
			for (RpcPlugin plugin : pluginList) {
				ThreadPoolFactory.getServerExecutor().execute(() -> plugin.init(serviceInfos,
						RpcHelper.getMethodIdInstanceMap()));
			}
		}

	}

}
