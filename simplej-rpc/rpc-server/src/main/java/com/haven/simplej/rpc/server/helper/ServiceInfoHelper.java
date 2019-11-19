package com.haven.simplej.rpc.server.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.net.NetUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * rpc service缓存
 * @author: havenzhang
 * @date: 2019/1/18 16:30
 * @version 1.0
 */
@Slf4j
public class ServiceInfoHelper {

	/**
	 * 参数名称解析器
	 */
	private static final DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();

	/**
	 * 方法对象与方法参数的映射关系
	 */
	private static final Map<Method, List<Pair<String, Class>>> methodParamNamesMap = Maps.newConcurrentMap();

	/**
	 * service 名和对应的rpc实现类的映射关系
	 */
	private static final Map<String, Class> serviceClassMap = Maps.newConcurrentMap();

	/**
	 * methodId 与 service接口类 映射
	 */
	private static final Map<String, Class> methodIdServiceInterfaceMap = Maps.newConcurrentMap();

	/**
	 * 方法名+参数与对应的method对象的映射关系
	 * key = methodName + '@' methodParamTypes
	 */
	private static final Map<String, Method> methodMap = Maps.newConcurrentMap();

	/**
	 * service服务与对应的方法列表映射关系
	 */
	private static final Map<String, List<Method>> serviceMethodsMap = Maps.newHashMap();


	/**
	 * 接口定义类与实现类的关联关系
	 * key是接口类，pair--》right是实现类
	 */
	private static final Map<Class, Pair<RpcService, Class>> interfaceServiceMap = Maps.newHashMap();

	/**
	 * methodId与service实现类class与method实例的映射关系
	 */
	private static final Map<String, Pair<Class, Method>> methodIdServiceMethodMap = Maps.newHashMap();

	/**
	 * 服务元信息缓存
	 */
	private static final List<ServiceMeta> SERVICE_META_LIST = Lists.newArrayList();
	/**
	 * service服务与方法映射信息
	 */
	private static final Map<String, List<MethodMeta>> serviceMethodMap = Maps.newHashMap();

	/**
	 * 方法与参数映射关系, key = methodId，主要是防止同名覆盖
	 */
	private static final Map<String, List<ParamMeta>> methodParamsMap = Maps.newHashMap();

	/**
	 * methodid 和Method的映射关系
	 */
	private static final Map<String, Method> methodIdMethodMap = Maps.newHashMap();

	/**
	 * methodId 与methodMeta 的关系缓存
	 */
	private static final Map<String, MethodMeta> methodMetaMap = Maps.newHashMap();


	/**
	 * 获取service下的所有方法元信息
	 * @param serviceName service名称，如：com.haven.simplej.payment.rpc.service.impl.OrderRpcService
	 * @return List<MethodMeta>
	 */
	public static List<MethodMeta> getMethodDataList(String serviceName) {
		return serviceMethodMap.get(serviceName);
	}

	/**
	 * 获取方法参数元信息
	 * @param methodId 方法id
	 * @return List<ParamMeta>
	 */
	public static List<ParamMeta> getMethodParams(String methodId) {
		return methodParamsMap.get(methodId);
	}

	/**
	 * 获取方法对象，实现类的方法对象
	 * @param methodId 方法id
	 * @return Method
	 */
	public static Method getMethod(String methodId) {
		return methodIdMethodMap.get(methodId);
	}

	/**
	 * 当前rpc 服务的Heathcheck远程方法对象与接口类服务类的缓存关系
	 */
	private static Map<Method, Class> rpcHealthcheckMethodAndServiceMap = Maps.newHashMap();


	/**
	 * 增加rpc本地方法与class类的关系缓存
	 * @param method  本地方法
	 * @param interfaceClz 接口类 类
	 */
	public static void addHeathCheckMethod(Method method, Class interfaceClz) {
		rpcHealthcheckMethodAndServiceMap.put(method, interfaceClz);
	}

	/**
	 * 获取当前服务的所有健康检查方法缓存
	 * @return Map<Method , Class> 接口类
	 */
	public static Map<Method, Class> getHeathMethodMap() {
		return rpcHealthcheckMethodAndServiceMap;
	}

	/**
	 * 增加serviceData元信息
	 * @param serviceName 服务名
	 * @param serviceClz rpc服务实现类class类
	 */
	public static void addServiceData(String serviceName, Class serviceClz) {
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setServiceName(serviceName);
		//获取类的接口定义
		Class<?>[] interfaceClzs = serviceClz.getInterfaces();
		if (interfaceClzs != null) {
			log.debug("class:{} has no interface ,continue", serviceClz.getName());
			if (interfaceClzs != null && interfaceClzs.length > 0) {
				for (Class<?> interfaceClz : interfaceClzs) {
					RpcService rpcService = interfaceClz.getAnnotation(RpcService.class);
					if (rpcService == null) {
						continue;
					}
					serviceMeta.setVersion(rpcService.version());

					//解析方法
					Map<String, List<Method>> serviceMethods = getServiceMethodsMap();
					List<Method> methods = serviceMethods.get(serviceName);
					if (CollectionUtil.isEmpty(methods)) {
						continue;
					}
					for (Method method : methods) {
						//方法信息
						MethodMeta methodMeta = RpcHelper.getMethodMeta(method, serviceMeta);
						List<ParamMeta> params = Lists.newArrayList();
						//方法参数的主键
						Annotation[][] annotations = method.getParameterAnnotations();
						//解析参数
						List<Pair<String, Class>> methodInfo = getParamNameAndTypes(method);
						for (int i = 0; i < methodInfo.size(); i++) {
							params.addAll(RpcHelper.parseParams(methodInfo.get(i), annotations[i]));
						}

						methodMeta.setParams(params);
						methodParamsMap.put(methodMeta.getMethodId(), params);
						methodMetaMap.put(methodMeta.getMethodId(), methodMeta);
						methodIdMethodMap.put(methodMeta.getMethodId(), method);
						serviceMeta.getMethods().add(methodMeta);
						addServiceMethod(methodMeta.getMethodId(), new Pair<>(serviceClz, method));
					}
					serviceMethodMap.put(serviceMeta.getServiceName(), serviceMeta.getMethods());
					SERVICE_META_LIST.add(serviceMeta);
				}
			}

		}
	}

	/**
	 * 获取方法元信息
	 * @param methodId 方法唯一id
	 * @return MethodMeta
	 */
	public static MethodMeta getMethodMeta(String methodId) {
		return methodMetaMap.get(methodId);
	}

	public static List<ServiceMeta> getServiceMetaList() {
		return SERVICE_META_LIST;
	}

	/**
	 * 增加methodId与service实现类和对应方法的映射关系
	 */
	public static void addServiceMethod(String methodId, Pair<Class, Method> pair) {
		methodIdServiceMethodMap.put(methodId, pair);
	}

	public static Pair<Class, Method> getServiceAndMethod(String methodId) {
		return methodIdServiceMethodMap.get(methodId);
	}

	public static Map<String, List<Method>> getServiceMethodsMap() {
		return serviceMethodsMap;
	}

	/**
	 * 获取rpc service信息
	 * @return Map<String   ,   Class>
	 */
	public static Map<String, Class> getServiceClassMap() {
		return serviceClassMap;
	}

	/**
	 * 缓存方法参数名以及数据类型关系
	 * @param method 方法
	 */
	public static void addParamNameAndType(Method method) {

		String[] names = discover.getParameterNames(method);
		List<Pair<String, Class>> list = Lists.newArrayList();
		Class[] types = method.getParameterTypes();
		for (int i = 0; i < types.length; i++) {
			list.add(new Pair<>(names[i], types[i]));
		}
		methodParamNamesMap.put(method, list);
	}

	/**
	 * 获取方法的参数
	 * @param method 方法
	 * @return List<Pair   ,   Class>>
	 */
	public static List<Pair<String, Class>> getParamNameAndTypes(Method method) {
		return methodParamNamesMap.get(method);
	}

	/**
	 * 增加serviceName和对应rpc 实现类的映射关系到内存中
	 * @param serviceName 服务名
	 * @param clz rpc服务实现类class
	 */
	public static void addServiceClass(String serviceName, Class clz) {
		log.debug("addServiceClass,serviceName:{} class:{}", serviceName, clz.getName());
		addServiceData(serviceName, clz);
		serviceClassMap.put(serviceName, clz);
	}

	/**
	 * 增加方法id与接口类的映射关系
	 * @param methodId 方法id
	 * @param serviceClz 接口类
	 */
	public static void addMethodIdAndService(String methodId, Class serviceClz) {
		methodIdServiceInterfaceMap.put(methodId, serviceClz);
	}

	/**
	 * 根据方法id，返回所属的接口类
	 * @param methodId 方法id
	 * @return 接口类
	 */
	public static Class getServiceClass(String methodId) {
		return methodIdServiceInterfaceMap.get(methodId);
	}

	public static Class getServiceClsss(String serviceName) {
		return serviceClassMap.get(serviceName);
	}


	/**
	 * 获取rpc 接口与实现类的map对象
	 * @return
	 */
	public static Map<Class, Pair<RpcService, Class>> getInterfaceServiceMap() {

		return interfaceServiceMap;
	}

	/**
	 * 增加接口与实现类的映射关系
	 * @param interfaceClz 接口类
	 * @param pair 实现类和rpcservice 对
	 */
	public static void addInterfaceAndService(Class interfaceClz, Pair<RpcService, Class> pair) {
		interfaceServiceMap.put(interfaceClz, pair);
	}

	/**
	 * 增加rpc service的方法映射关系信息
	 * @param serviceInfo 服务名
	 * @param paramTypes 参数类型
	 * @param method 方法对象
	 */
	public static void addMethod(ServiceInfo serviceInfo, String paramTypes, Method method) {
		String serviceName = serviceInfo.getServiceName();
		String key = RpcHelper.getMethodId(serviceName, method.getName(), paramTypes);
		methodMap.put(key, method);
		log.debug("add method key :{},serviceName:{},methodName:{}", key, serviceName, method.getName());
		List<Method> methods;
		if (serviceMethodsMap.containsKey(serviceName)) {
			methods = serviceMethodsMap.get(serviceName);
		} else {
			methods = Lists.newArrayList();
		}
		methods.add(method);
		serviceMethodsMap.put(serviceName, methods);
	}

	/**
	 * 根据服务名+方法名+参数类型，获取方法method实例
	 * @param serviceName 服务名
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return Method
	 */
	public static Method getMethod(String serviceName, String methodName, String paramTypes) {
		String key = serviceName + "@" + methodName + "@" + StringUtil.trimToEmpty(paramTypes);
		log.debug("get method key :{}", key);
		return methodMap.get(key);
	}

	/**
	 * 是否为简单的类型
	 * @param type
	 * @return
	 */
	public static boolean isSimpleType(Class type) {
		if (type.equals(String.class)) {
			return true;
		}
		if (type.equals(Integer.class)) {
			return true;
		}
		if (type.equals(Float.class)) {
			return true;
		}
		if (type.equals(Double.class)) {
			return true;
		}
		if (type.equals(Short.class)) {
			return true;
		}
		if (type.equals(Byte.class)) {
			return true;
		}

		return false;
	}

	/**
	 * 判断是否为集合
	 * @param type class
	 * @return boolean
	 */
	public static boolean isCollection(Class type) {
		if (List.class.equals(type) || List.class.equals(type.getSuperclass())) {
			return true;
		}
		return Map.class.equals(type) || Map.class.equals(type.getSuperclass());

	}

	/**
	 * 判断某个class是否为rpc service实现类
	 * @param clz 接口类
	 * @return boolean
	 */
	public static boolean isRpcService(Class clz) {
		if (clz == null) {
			return false;
		}

		boolean rpcServiceFlag = false;
		if(clz.getInterfaces() == null || clz.getInterfaces().length == 0){
			return false;
		}
		for (Class anInterface : clz.getInterfaces()) {
			RpcService rpcService = (RpcService) anInterface.getAnnotation(RpcService.class);
			if (rpcService != null) {
				rpcServiceFlag = true;
				break;
			}
		}
		return rpcServiceFlag;
	}

	/**
	 * 获取本进程内的service信息
	 * @return ServiceInstance
	 * @param namespace 命名空间/域名
	 */
	public static ServiceInstance getLocalInstance(String namespace) {
		int proxyPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_PORT_KEY, -1);
		int proxyHttpPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_HTTP_PORT_KEY, -1);
		int httpPort = PropertyManager.getInt(RpcConstants.WEB_SERVER_PORT_KEY, -1);
		int port = PropertyManager.getInt(RpcConstants.RPC_SERVER_PORT_KEY, -1);
		if (port == -1 && httpPort == -1) {
			log.error("{} or {} not set,please check your connect up vm parameter or properties setting,set rpc" +
					".server" + ".port=xxx", RpcConstants.RPC_SERVER_PORT_KEY, RpcConstants.WEB_SERVER_PORT_KEY);
			throw new com.haven.simplej.exception.UncheckedException("please check your connect up vm parameter or " + "properties setting,set rpc.server.port=xxx");
		}

		ServiceInstance instance = new ServiceInstance();
		instance.setNamespace(namespace);
		instance.setPort(port);
		instance.setProxyPort(proxyPort);
		instance.setProxyHttpPort(proxyHttpPort);
		instance.setHttpPort(httpPort);
		instance.setHost(NetUtil.getLocalHost());
		instance.setIdc(PropertyManager.get(RpcConstants.RPC_APP_IDC, RpcConstants.RPC_APP_IDC_DEFAULT));
		instance.setRegionId(PropertyManager.get(RpcConstants.RPC_APP_REGION_ID, RpcConstants.RPC_APP_REGION_DEFAULT));
		return instance;
	}

	/**
	 * 判断本机是否配置了localproxy
	 * @return
	 */
	public static boolean hasProxy() {
		String proxyPort = PropertyManager.get(RpcConstants.RPC_PROXY_PORT_KEY);
		String proxyHost = PropertyManager.get(RpcConstants.RPC_PROXY_HOST_KEY);
		if (StringUtil.isNotEmpty(proxyHost) && StringUtil.isNotEmpty(proxyPort)) {
			log.info("local proxy host:{},port:{}", proxyHost, proxyPort);
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 执行service 特定的方法
	 * @param serviceClz service类
	 * @param method 方法对象
	 * @param request http request
	 * @return Object
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object invokeMethod(Class serviceClz, Method method, HttpServletRequest request)
			throws IllegalAccessException, InstantiationException {
		// 代理实例
		Object seviceInstance = SpringContext.getBean(serviceClz);

		Object resp;
		Class<?>[] types = method.getParameterTypes();
		if (types == null || types.length == 0) {
			//反射执行本地方法
			resp = ReflectionUtil.invokeMethod(seviceInstance, method);
		} else {
			String[] names = discover.getParameterNames(method);
			List<Object> args = Lists.newArrayList();
			//结构体参数,key是参数名,可以是rpcStruct结构，也可以是map
			Map<String, List<Pair<String, String>>> structParams = parseStructParams(request);
			for (int i = 0; i < names.length; i++) {
				if (isStruct(types[i])) {//xxx.yyyy
					Object obj = types[i].newInstance();
					List<Pair<String, String>> params = structParams.get(names[i]);
					for (Pair<String, String> param : params) {
						if (StringUtil.isNotEmpty(param.getRight())) {
							Field field = ReflectionUtil.getField(types[i], param.getLeft());
							Object value = RpcHelper.string2Obj(param.getRight(), field.getType());
							ReflectionUtil.setFieldValue(obj, param.getLeft(), value);
						}

					}
					args.add(obj);
				} else if (RpcHelper.isMap(types[i])) {
					args.add(parseMapParam(names[i], request));
				} else {
					String paramValue = request.getParameter(names[i]);
					Object value = RpcHelper.string2Obj(paramValue, types[i]);
					args.add(value);
				}

			}
			resp = ReflectionUtil.invokeMethod(seviceInstance, method, args.toArray());
		}

		return resp;
	}


	/**
	 * 请求参数转换成map，请求参数，如：map.key=xxx ,map.value=xxxx
	 * @param mapName 远程方法参数名
	 * @param request http请求
	 * @return Map
	 */
	public static Map<String, String> parseMapParam(String mapName, HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		request.getParameterMap().forEach((k, v) -> {
			if (StringUtil.startsWith(k, mapName + ".")) {
				String[] params = StringUtil.split(k, ".");
				if (params != null && params.length == 2) {
					if (StringUtil.startsWith(params[1], "key")) {
						String valueKey =
								params[0] + "." + "value" + params[1].substring(StringUtil.length(params[1]) - 1);
						String key = request.getParameter(k);
						String value = request.getParameter(valueKey);
						map.put(key, value);
					}
				}

			}
		});
		return map;
	}


	/**
	 * 解析结构体的参数，如：user.age,以点号分隔的, 非结构体的参数不会解析进去
	 * Pair 左边是参数名称，右边是值
	 * @param request http 请求
	 * @return Map<String   ,   String>>>
	 */
	private static Map<String, List<Pair<String, String>>> parseStructParams(HttpServletRequest request) {
		Map<String, List<Pair<String, String>>> paramMap = Maps.newHashMap();
		request.getParameterMap().forEach((k, v) -> {
			String[] params = StringUtil.split(k, ".");
			if (params != null && params.length == 2) {
				String value = request.getParameter(k);
				Pair<String, String> pair = new Pair<>(params[1], value);
				if (paramMap.containsKey(params[0])) {
					paramMap.get(params[0]).add(pair);
				} else {
					List<Pair<String, String>> p = Lists.newArrayList();
					p.add(pair);
					paramMap.put(params[0], p);
				}
			} else if (params != null && params.length == 3) {
				//3层，如：company.deparment.groupId
				log.debug("key:{} value:{} can not be parse", k, v);
			}
			//
		});

		return paramMap;
	}

	/**
	 * 数据类型是否为rpcstruct
	 * @param type 数据类型
	 * @return boolean
	 */
	private static boolean isStruct(Class type) {
		if (type.getAnnotation(RpcStruct.class) != null) {
			return true;
		}
		return false;
	}


}
