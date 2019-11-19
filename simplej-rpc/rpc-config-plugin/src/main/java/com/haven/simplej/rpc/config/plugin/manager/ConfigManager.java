package com.haven.simplej.rpc.config.plugin.manager;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.listener.ConfigListener;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.spring.SpringContext;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置管理器
 * @author: havenzhang
 * @date: 2018/06/11 23:47
 * @version 1.0
 */
@Slf4j
@Component
public class ConfigManager {


	private static IConfigService service;

	/**
	 * 属性更新操作，0-第一次启动，1-第二次之后的更新
	 */
	private static volatile int itemState = 0;

	/**
	 * 属性文件更新操作，0-第一次启动，1-第二次之后的更新
	 */
	private static volatile int fileState = 0;

	private List<ConfigListener> listenerList;

	public ConfigManager() {
		service = ServiceProxy.create().setInterfaceClass(IConfigService.class).build();
		listenerList = SpringContext.getBeansOfType(ConfigListener.class);
	}

	/**
	 * 监听配置属性,每一个rpc server 在启动的时候都会去监听配置
	 */
	public void listen(String namespace) {
		boolean listen = PropertyManager.getBoolean(RpcConstants.RPC_APP_SERVER_LINSTEN_CONFIG_ENABLE, true);
		if (!listen) {
			log.debug("config listen switch is off");
			return;
		}

		//配置属性监听者
		if (CollectionUtil.isEmpty(listenerList)) {
			log.info("ConfigListener list is empty, do not need to listening config",
					RpcConstants.RPC_APP_SERVER_LINSTEN_CONFIG_ENABLE);
			return;
		}

		//监听配置文件
		listenConfigFile(namespace);

		//监听属性参数
		listenConfigItem(namespace);

	}


	/**
	 * 监听某个服务配置文件
	 * @param namespace 服务命名空间
	 */
	private void listenConfigFile(String namespace) {
		ThreadPoolFactory.getConfigListenExecutor().execute(() -> {
			for (; ; ) {
				try {
					if (ThreadPoolFactory.getState() == 1) {
						//关闭状态，则退出
						break;
					}

					//非首次查询的情况下，需要等待属性变更或者等待超时再返回
					boolean waitForChange = itemState == 1;
					//查询最新配置
					ConfigFileResponse fileResponse = service.queryNewConfigFile(namespace, waitForChange);
					if (fileResponse != null && fileResponse.isChange()) {
						log.debug("------------config file list change-------------");
						//回调通知监听者，更新配置属性
						listenerList.forEach(e -> e.configFileListen(fileResponse));
						itemState = 1;
					}

				} catch (Exception e) {
					log.debug("listen queryNewConfigFile error:", e);
				}
			}

		});
	}


	/**
	 * 监听某个服务的属性
	 * @param namespace 服务命名空间
	 */
	private void listenConfigItem(String namespace) {

		ThreadPoolFactory.getConfigListenExecutor().execute(() -> {
			for (; ; ) {
				try {
					if (ThreadPoolFactory.getState() == 1) {
						break;
					}
					//非首次查询的情况下，需要等待属性变更或者等待超时再返回
					boolean waitForChange = itemState == 1;
					//查询最新配置
					ConfigItemResponse itemResponse = service.queryNewConfigItem(namespace, waitForChange);
					if (itemResponse != null && itemResponse.isChange()) {
						//添加属性值到缓存
						RpcPropertyManager.addConfigItemResponse(namespace, itemResponse);

						//回调通知监听者
						listenerList.forEach(e -> e.propertyListen(itemResponse));
						//更新PropertyManager属性缓存
						itemResponse.getNewProps().forEach(p -> {
							log.debug("new properties,key:{},value:{}", p.getKey(), p.getValue());
							//添加属性值到缓存
							RpcPropertyManager.addConfigItem(namespace, p.getKey(), p);
							RpcPropertyManager.addItem(namespace, p.getKey(), p.getValue());
						});
						fileState = 1;
					}
				} catch (Exception e) {
					log.debug("namespace:{},listen queryNewConfigItem error:", namespace, e);
				}
			}

		});
	}
}
