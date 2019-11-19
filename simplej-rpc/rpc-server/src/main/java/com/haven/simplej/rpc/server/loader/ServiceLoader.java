package com.haven.simplej.rpc.server.loader;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.HealthCheck;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.RpcServerType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.MethodInfo;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceLookupKey;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.base.type.UncheckedException;
import com.vip.vjtools.vjkit.collection.MapUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载本地server所有的rpc服务
 * 仅用于每个app实例加载当前实例的所有公开服务信息
 * @author: havenzhang
 * @date: 2018/01/27 19:21
 * @version 1.0
 */
@Slf4j
@Data
public class ServiceLoader {

	/**
	 * 服务元信息缓存
	 */
	private static final Map<ServiceLookupKey, ServiceInfo> serviceMap = Maps.newHashMap();

	/**
	 * 服务列表
	 */
	private List<ServiceInfo> serviceList = Lists.newArrayList();

	/**
	 * 服务包路径
	 */
	private static final Set<String> serviceBasePackages = Sets.newHashSet();

	/**
	 * 注册服务接口
	 */
	private IServiceRegister serviceRegister;

	/**
	 * 增加rpc服务所在的包路径
	 * @param basePacke
	 */
	public void addServiceBasePackage(String basePacke) {
		serviceBasePackages.add(basePacke);
	}

	public void addServiceBasePackage() {
		if (SpringContext.getContext() != null) {
			List<String> beanNames = SpringContext.queryBeansNames();
			beanNames.forEach(e -> {
				Object bean = SpringContext.getBean(e);
				try {
					if (bean != null && ServiceInfoHelper.isRpcService(bean.getClass()) && bean.getClass().getPackage() != null) {
						log.debug("rpc service name:{}", e);
						log.debug("addServiceBasePackage:{}", bean.getClass().getPackage().getName());
						serviceBasePackages.add(bean.getClass().getPackage().getName());
					}
				} catch (Exception e1) {
					log.error("add package error", e1);
				}
			});
		}
	}


	private static ServiceLoader loader;

	private ServiceLoader() {
		super();
	}

	public static ServiceLoader getInstance() {

		if (loader == null) {
			synchronized (ServiceLoader.class) {
				if (loader == null) {
					loader = new ServiceLoader();
					loader.serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
				}
			}
		}

		return loader;
	}

	/**
	 * 加载rpc service class 类
	 */
	public void loadServiceClz() {
		List<Class> classList = Lists.newArrayList();
		serviceBasePackages.forEach(e -> classList.addAll(loadClass(e)));
		for (Class aClass : classList) {
			if (aClass.isInterface()) {
				log.debug("class:{} is interface ,continue", aClass.getName());
				continue;
			}
			//获取类的接口定义
			Class<?>[] interfaceClzs = aClass.getInterfaces();
			if (interfaceClzs == null) {
				log.debug("class:{} has no interface ,continue", aClass.getName());
				continue;
			}
			if (interfaceClzs != null && interfaceClzs.length > 0) {
				for (Class<?> interfaceClz : interfaceClzs) {
					RpcService rpcService = interfaceClz.getAnnotation(RpcService.class);
					if (rpcService != null) {
						log.debug("mapping interface:{},class:{}", interfaceClz.getName(), aClass.getName());
						Pair<RpcService, Class> pair = new Pair<>(rpcService, aClass);
						if (ServiceInfoHelper.getInterfaceServiceMap().containsKey(interfaceClz)) {

							Pair<RpcService, Class> p = ServiceInfoHelper.getInterfaceServiceMap().get(interfaceClz);
							log.error("duplicate interface class of {} ,implement class:{} ,exist implement class:{}",
									interfaceClz.getName(), aClass.getName(),
									ServiceInfoHelper.getInterfaceServiceMap().get(interfaceClz).getRight().getName());

							if (!p.getRight().equals(aClass)) {
								throw new com.haven.simplej.exception.UncheckedException("duplicate Implementation " + "class " + "of " + interfaceClz.getName());
							}
						}
						//保存serviceName和对应rpc 实现类的映射关系到内存中
						ServiceInfoHelper.addInterfaceAndService(interfaceClz, pair);
					}
				}
			}
		}
	}

	/**
	 * 加载本地service,正常情况是服务启动的时候执行，主要用于获取当前服务下的所有暴露外部的service信息，用于服务注册
	 */
	public Map<ServiceLookupKey, ServiceInfo> loadLocalService() {
		if (MapUtil.isNotEmpty(serviceMap)) {
			return serviceMap;
		}
		//加载本地rpc服务类
		loadServiceClz();

		if (MapUtil.isEmpty(ServiceInfoHelper.getInterfaceServiceMap())) {
			log.info("service implement of rpcService can not be found");
			return Maps.newHashMap();
		}
		//解析类元信息，如方法，服务名，超时时间
		ServiceInfoHelper.getInterfaceServiceMap().forEach((k, v) -> {
			// v 的左边是RpcService注解类，右边是rpc service 的实现类
			RpcService rpcService = v.getLeft();
			String serviceName = rpcService.serviceName();
			if (StringUtil.isEmpty(serviceName)) {
				serviceName = k.getName();
			}
			ServiceLookupKey lookupKey = new ServiceLookupKey();
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.setServiceName(serviceName);
			//properties属性配置文件中读取app.name信息
			serviceInfo.setNamespace(PropertyManager.get(RpcConstants.RPC_APP_NAME));
			serviceInfo.setVersion(rpcService.version());
			serviceInfo.setCreateTime(new Date());
			serviceInfo.setUpdateTime(new Date());
			serviceInfo.setTimeout(rpcService.timeout());
			BeanUtils.copyProperties(serviceInfo, lookupKey);
			String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
					RpcServerType.BUSINESS_SERVER.name());
			serviceInfo.setServerType(serverType);

			//服务实例信息
			ServiceInstance instance = ServiceInfoHelper.getLocalInstance(serviceInfo.getNamespace());
			serviceInfo.getInstances().add(instance);

			//加载服务方法信息
			loadMethods(serviceInfo, k, v.getRight());

			//保存serviceName和对应的服务实现类class关系
			ServiceInfoHelper.addServiceClass(serviceName, v.getRight());
			serviceList.add(serviceInfo);
			serviceMap.put(lookupKey, serviceInfo);

			//没有指定namespace的服务信息
			ServiceLookupKey lookupKey2 = new ServiceLookupKey();
			lookupKey2.setVersion(serviceInfo.getVersion());
			lookupKey2.setServiceName(serviceInfo.getServiceName());
			serviceMap.put(lookupKey2, serviceInfo);
		});

		return serviceMap;
	}

	/**
	 * 加载服务方法，@TODO 该方法不够清晰，需要职责分离
	 */
	public void loadMethods(ServiceInfo serviceInfo, Class interfaceClz, Class serviceClz) {
		Method[] methods = interfaceClz.getMethods();
		if (methods == null) {
			log.debug("service :{} method list is empty", serviceInfo.getServiceName());
			return;
		}
		for (Method method : methods) {
			if (method.getModifiers() == 2 || method.getModifiers() == 4) {
				log.debug("not public method,continue,modifiers:{}", method.getModifiers());
				continue;
			}

			//获取实现类的方法对象
			Method implMethod = ReflectionUtil.getMethod(serviceClz, method.getName(), method.getParameterTypes());

			if (implMethod == null) {
				log.debug("no impl method ,continue,interface {} method:{}", serviceInfo.getServiceName(),
						method.getName());
				continue;
			}

			RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
			long timeout = RpcConstants.DEFAULT_TIMEOUT;
			String version = RpcConstants.VERSION_1;
			if (rpcMethod != null) {
				timeout = rpcMethod.timeout();
				version = rpcMethod.version();
			} else {
				HealthCheck healthCheck = method.getAnnotation(HealthCheck.class);
				if (healthCheck == null) {
					log.debug("local server:{} method:{} do not publish....", serviceInfo.getServiceName(),
							method.getName());
					continue;
				} else {
					//增加健康度监测方法缓存,接口类和实现方法信息缓存
					ServiceInfoHelper.addHeathCheckMethod(implMethod, interfaceClz);
				}
			}

			MethodInfo methodInfo = new MethodInfo();
			methodInfo.setTimeout(timeout);
			methodInfo.setVersion(version);
			methodInfo.setMethodName(method.getName());
			methodInfo.setServiceName(serviceInfo.getServiceName());
			//方法参数类型
			Class[] types = method.getParameterTypes();
			if (types != null && types.length > 0) {
				methodInfo.setParamsTypes(RpcHelper.getParamTypesStr(types));
				ServiceInfoHelper.addMethod(serviceInfo, methodInfo.getParamsTypes(), implMethod);
			} else {
				ServiceInfoHelper.addMethod(serviceInfo, null, implMethod);
			}
			methodInfo.setMethodId(RpcHelper.getMethodId(serviceInfo.getServiceName(), methodInfo.getMethodName(),
					methodInfo.getParamsTypes()));
			ServiceInfoHelper.addParamNameAndType(implMethod);
			methodInfo.setReturnType(method.getReturnType().getName());
			//增加service方法信息
			serviceInfo.getMethods().add(methodInfo);
			//接口api的方法对象
			RpcHelper.addMethod(methodInfo.getMethodId(), method);
			ServiceInfoHelper.addMethodIdAndService(methodInfo.getMethodId(), interfaceClz);

			//初始化方法线程池
			ThreadPoolFactory.createMethodExecutor(methodInfo.getMethodId());
		}
	}

	/**
	 * 加载class
	 * @param basePackage 指定包名
	 * @return List<Class>
	 */
	public List<Class> loadClass(String basePackage) {
		String path = basePackage.replaceAll("\\.", "\\/");
		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		List<Class> clsList = Lists.newArrayList();
		URL configUrl = ServiceLoader.class.getResource(path);
		if (configUrl == null) {
			configUrl = ServiceLoader.class.getClassLoader().getResource(path);
		}
		if (configUrl == null) {
			path = "/BOOT-INF/classes" + path;
			configUrl = ServiceLoader.class.getClassLoader().getResource(path);
		}
		if (configUrl == null) {
			configUrl = ServiceLoader.class.getResource(path);
		}
		if (configUrl == null) {
			throw new RpcException(RpcError.START_UP_FAIL, "can not find package :" + basePackage);
		}
		try {
			log.info("getResourceFiles protocl:{},path:{}", configUrl.getProtocol(), configUrl.getPath());
			if (StringUtils.equalsIgnoreCase("jar", configUrl.getProtocol())) {
				String jarPath = configUrl.toString().substring(0, configUrl.toString().indexOf("!/") + 2);
				log.info("getResourceFiles protocl:{},jarPath:{}", configUrl.getProtocol(), jarPath);
				URL jarURL = new URL(jarPath);
				JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
				JarFile jarFile = jarCon.getJarFile();
				Enumeration jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry) jarEntries.nextElement();

					String entryName = entry.getName();
					if (entryName.startsWith(path.substring(1)) && entryName.endsWith(".class")) {
						log.debug("loadClass class :{}", entryName);
						String className = entryName.substring(0, entryName.lastIndexOf("."));
						className = className.replaceAll("\\/", "\\.");
						Class cls = Class.forName(className);
						clsList.add(cls);
					}
				}
			} else {
				File[] files = new File(configUrl.getPath()).listFiles();

				if (files != null) {
					for (File file : files) {
						getClass(file, clsList, basePackage);
					}
				}
			}

		} catch (Exception e) {
			log.error("loadClass class fail", e);
			throw new UncheckedException(e);
		}
		log.debug("package:{},class list:{}", basePackage, JSON.toJSONString(clsList));
		return clsList;
	}

	private AtomicInteger counter = new AtomicInteger();

	/**
	 * 该方法会得到所有的类，将类的绝对路径写入到clsList中
	 * @param file 文件或目录
	 */
	private void getClass(File file, List<Class> clsList, String basePackage) throws ClassNotFoundException {
		if (file.isDirectory()) {
			//文件夹 //文件夹就递归
			File[] files = file.listFiles();
			for (File f1 : files) {
				if (counter.get() > 100000) {
					//递归超过10万层，则抛异常
					throw new RpcException(RpcError.SYSTEM_BUSY, "Recursion depth over 100000");
				}
				getClass(f1, clsList, basePackage);
				counter.incrementAndGet();
			}
		} else {//标准文件 //标准文件我们就判断是否是class文件
			if (file.getName().endsWith(".class")) {
				String className = file.getPath().replaceAll("\\\\", "\\.").replaceAll("\\/", "\\.");
				className = className.substring(className.indexOf(basePackage)).replaceAll("\\.class", "");
				Class cls = Class.forName(className);
				clsList.add(cls);
			}
		}

	}


	/**
	 * 获取本地service服务信息
	 * @param lookupKey 查询服务的key
	 * @return ServiceInfo
	 */
	public ServiceInfo getLocalService(ServiceLookupKey lookupKey) {

		return serviceMap.get(lookupKey);
	}

	/**
	 * 发布服务元信息以及实例信息
	 * 该请求发送至proxy，由proxy进行上报
	 * @return boolean
	 */
	public boolean register() {
		try {
			List<ServiceInfo> serviceList = getServiceList();
			if (StringUtil.isEmpty(PropertyManager.get(RpcConstants.RPC_PROXY_HOST_KEY))) {
				log.info("rpc.proxy.host not set ,do not register local services");
				return false;
			}

			//服务注册
			boolean result = serviceRegister.register(serviceList);
			log.debug("local serviceList:{}, response:{}", JSON.toJSONString(serviceList, true), result);
			return result;
		} catch (Exception e) {
			log.error("register service error", e);
		} finally {
			InvocationContext.removeServerInfo();
		}

		return false;
	}
}
