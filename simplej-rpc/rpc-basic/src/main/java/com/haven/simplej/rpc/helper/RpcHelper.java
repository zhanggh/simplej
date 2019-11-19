package com.haven.simplej.rpc.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.*;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.MsgType;
import com.haven.simplej.rpc.enums.RpcServerType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.coder.Coder;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.net.NetUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.*;

/**
 * rpc帮助类
 * @author: havenzhang
 * @date: 2018/9/21 17:15
 * @version 1.0
 */
@Slf4j
public class RpcHelper {

	/**
	 * methodId与method实例的缓存关系
	 * method为api类的方法对象，非实现类的方法对象
	 */
	private static final Map<String, Method> methodMap = Maps.newConcurrentMap();
	/**
	 * rpcHeader的所有字段
	 */
	private static final Map<String, Field> rpcHeaderFields = Maps.newHashMap();


	/**
	 *  服务列表信息,包含所有注册在rpc-server上的服务信息
	 */
	private static final Map<ServiceLookupKey, ServiceInfo> serviceMap = Maps.newConcurrentMap();

	/**
	 * key = methodId+serviceVersion
	 * 与实例的缓存关系
	 */
	private static final Map<String, List<ServiceInstance>> methodIdInstanceMap = Maps.newConcurrentMap();

	/**
	 * 服务与实例关系映射
	 */
	private static final Map<ServiceLookupKey, List<ServiceInstance>> serviceInstanceMap = Maps.newConcurrentMap();


	/**
	 * 参数名称解析器
	 */
	private static final DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();

	/**
	 * 自定义 编解码器 类名与 class对象的映射关系
	 * key 是类名 如：xxx.getClass().getName
	 */
	private static final Map<String, Class> customCoderClassMap = Maps.newHashMap();
	/**
	 * 自定义编解码器 类名与实例对象的映射关系
	 */
	private static final Map<String, Coder> customCoderMap = Maps.newHashMap();

	static {
		addHeaderFileds();
	}


	/**
	 * 获取服务实例
	 * @param lookupKey 查询服务的key
	 */
	public static List<ServiceInstance> getServiceInstance(ServiceLookupKey lookupKey) {
		//		if (!isNormal()) {
		//			waitNotify();
		//		}
		List<ServiceInstance> instances = serviceInstanceMap.get(lookupKey);
		return instances;
	}

	/**
	 * 服务与实例映射关系缓存
	 * @return Map
	 */
	public static Map<ServiceLookupKey, List<ServiceInstance>> getServiceInstanceMap() {
		return serviceInstanceMap;
	}

	/**
	 * 方法实例映射关系缓存
	 * @return Map
	 */
	public static Map<String, List<ServiceInstance>> getMethodIdInstanceMap() {
		return methodIdInstanceMap;
	}

	/**
	 * 删除实例缓存
	 */
	public static void clearInstanceCache() {
		serviceInstanceMap.clear();
		methodIdInstanceMap.clear();
	}

	/**
	 * 根据methodId 获取实例信息
	 * @param methodId 远程方法的唯一id
	 * @return List<ServiceInstance>
	 */
	public static List<ServiceInstance> getServiceInstance(String methodId, String serviceVersion) {
		//		if (!isNormal()) {
		//			waitNotify();
		//		}
		return methodIdInstanceMap.get(RpcHelper.getLookupKey(methodId, serviceVersion));
	}

	/**
	 * 保存远程方法与实例的缓存
	 * @param methodId  远程方法的唯一id
	 * @param serviceVersion 服务版本号
	 * @param instances 实例集合
	 */
	public static void addInstance(String methodId, String serviceVersion, List<ServiceInstance> instances) {
		methodIdInstanceMap.put(RpcHelper.getLookupKey(methodId, serviceVersion), instances);
	}

	/**
	 * 保存远程方法与实例的缓存
	 * @param lookupKey 服务查询key
	 * @param instances 实例集合
	 */
	public static void addInstance(ServiceLookupKey lookupKey, List<ServiceInstance> instances) {
		serviceInstanceMap.put(lookupKey, instances);
	}


	/**
	 * 增加methodId 到 method实例的关系缓存
	 * @param methodId 远程方法的唯一id
	 * @param method 方法对象
	 */
	public static void addMethod(String methodId, Method method) {
		methodMap.put(methodId, method);
	}

	/**
	 * 客户端使用，method为api类的方法对象，非实现类的方法对象
	 * 获取方法实例
	 * @param methodId 远程方法的唯一id
	 * @return method 方法对象
	 */
	public static Method getMethod(String methodId) {
		return methodMap.get(methodId);
	}

	/**
	 * 获取远程方法的唯一id
	 * @param header rpc 请求头部
	 * @return MethodId
	 */
	public static String getMethodId(RpcHeader header) {
		if (StringUtil.isNotEmpty(header.getMethodId())) {
			return header.getMethodId();
		}

		return getMethodId(header.getServiceName(), header.getMethodName(), header.getMethodParamTypes());
	}

	/**
	 * 获取远程方法的唯一id
	 * @param request rpc 请求
	 * @return MethodId
	 */
	public static String getMethodId(Map<String, String> request) {
		if (request.containsKey("header.methodId")) {
			return request.get("header.methodId");
		}

		String serviceName = request.get("header.serviceName");
		String methodName = request.get("header.methodName");
		String methodParamTypes = request.get("header.methodParamTypes");
		return getMethodId(serviceName, methodName, methodParamTypes);
	}

	/**
	 * 获取methodId，每一个方法都有一个唯一的id
	 * @param serviceName 服务名
	 * @param methodName 方法名
	 * @param paramTypesString 方法参数字符串，如：java.lang.String,java.lang.Integer
	 * @return methodIdKey
	 */
	public static String getMethodId(String serviceName, String methodName, String paramTypesString) {
		if (StringUtil.equalsIgnoreCase(RpcConstants.NULL, paramTypesString)) {
			paramTypesString = StringUtil.EMPTY;
		}
		String methodIdKey = serviceName + "@" + methodName + "@" + StringUtil.trimToEmpty(paramTypesString);
		return DigestUtils.md2Hex(methodIdKey);
	}

	/**
	 * 通过methodId 和服务版本号查找服务时，key为methodId + "@" + version
	 * @param methodId 远程方法的唯一id
	 * @param version 服务版本号
	 * @return String
	 */
	public static String getLookupKey(String methodId, String version) {
		return methodId + "@" + version;
	}


	public static String getMethodId(Class service, Method method) {

		return getMethodId(service.getName(), method);
	}

	/**
	 * 生成远程方法的唯一id
	 * @param serviceName 服务名
	 * @param method 方法实例
	 * @return 方法的唯一id
	 */
	public static String getMethodId(String serviceName, Method method) {
		Class[] types = method.getParameterTypes();
		String typeStr = getParamTypesStr(types);
		String methodIdKey = serviceName + "@" + method.getName() + "@" + StringUtil.trimToEmpty(typeStr);
		return DigestUtils.md2Hex(methodIdKey);
	}

	public static String getLookupKey(String serviceName, String methodName, String serviceVersion,
			String paramTypes) {

		return serviceName + "@" + methodName + "@" + serviceVersion + "@" + paramTypes;
	}

	/**
	 * 把数据类型转成string
	 * @param types 数据类型
	 * @return String
	 */
	public static String getParamTypesStr(Class[] types) {
		if (types == null || types.length == 0) {
			return null;
		}
		StringBuilder typeStr = new StringBuilder();
		for (Class<?> parameterType : types) {
			typeStr.append(parameterType.getName()).append(RpcConstants.PARAMS_TYPE_SEPARATOR);
		}

		return typeStr.substring(0, typeStr.length() - 1);
	}


	/**
	 * 解析参数信息
	 */
	public static List<ParamMeta> parseParams(Pair<String, Class> pair, Annotation[] paramAnnotation) {

		List<ParamMeta> params = Lists.newArrayList();
		Annotation annotation = null;
		if (paramAnnotation != null && paramAnnotation.length > 0) {
			for (Annotation an : paramAnnotation) {
				if (an instanceof RpcParam || an instanceof RpcStruct) {
					annotation = an;
					break;
				}
			}
		}
		parseParams(pair.getLeft(), pair.getRight(), params, annotation);
		return params;
	}

	/**
	 * 解析结构体参数
	 * @param struct 结构体类
	 * @return List
	 */
	public static List<ParamMeta> parseStructParams(Class struct) {
		List<ParamMeta> childParams = Lists.newArrayList();
		PropertyDescriptor[] pds = BeanUtil.getPDs(struct);
		for (PropertyDescriptor pd : pds) {
			if (StringUtil.equalsIgnoreCase(pd.getName(), "class")) {
				continue;
			}
			Field field = ReflectionUtil.getField(struct, pd.getName());
			if (field == null) {
				continue;
			}
			RpcStruct rpcStruct = field.getClass().getAnnotation(RpcStruct.class);
			if (rpcStruct != null) {
				parseParams(pd.getName(), pd.getPropertyType(), childParams, rpcStruct);
			} else {
				RpcParam rpcParam = field.getAnnotation(RpcParam.class);
				parseParams(pd.getName(), pd.getPropertyType(), childParams, rpcParam);
			}
		}
		return childParams;
	}


	/**
	 *  解析方法参数
	 * @param paramName 参数名
	 * @param clzType 参数类型
	 * @param params 参数列表
	 * @param annotation 参数注解
	 */
	private static void parseParams(String paramName, Class clzType, List<ParamMeta> params, Annotation annotation) {
		ParamMeta paramMeta = new ParamMeta();
		paramMeta.setDataType(clzType.getName());
		paramMeta.setName(paramName);
		paramMeta.setListFlag(RpcHelper.isList(clzType) ? 1 : 0);
		paramMeta.setMapFlag(RpcHelper.isMap(clzType) ? 1 : 0);
		paramMeta.setArrayFlag(clzType.isArray() ? 1 : 0);

		boolean required = false;
		if (annotation != null) {
			if (annotation instanceof RpcParam) {
				RpcParam rpcParam = (RpcParam) annotation;
				required = rpcParam.required();
				paramMeta.setMaxLen(rpcParam.maxLength());
				paramMeta.setMinLen(rpcParam.minLength());
				paramMeta.setRegular(rpcParam.regular());
			}
			if (annotation instanceof RpcStruct) {
				RpcStruct rpcStruct = (RpcStruct) annotation;
				required = rpcStruct.required();
			}
		}

		paramMeta.setRequired(required);
		RpcStruct rpcStruct = (RpcStruct) clzType.getAnnotation(RpcStruct.class);
		if (rpcStruct == null) {
			paramMeta.setStructFlag(0);
			params.add(paramMeta);
		} else {
			paramMeta.setStructFlag(1);
			paramMeta.getChild().addAll(parseStructParams(clzType));
			params.add(paramMeta);
			paramMeta.setStructId(DigestUtils.md2Hex(clzType.getName()));
		}
	}


	/**
	 * 判断类型是否为List
	 * @param type 类型的class 类
	 * @return boolean
	 */
	public static boolean isList(Class type) {
		if (type.equals(List.class) || ArrayList.class.equals(type)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断类型是否为Map
	 * @param type 类型的class 类
	 * @return boolean
	 */
	public static boolean isMap(Class type) {
		if (Map.class.equals(type) || HashMap.class.equals(type) || LinkedHashMap.class.equals(type) || TreeMap.class.equals(type)) {
			return true;
		}
		return false;
	}


	/**
	 * 获取方法元信息
	 * @param method 方法对象
	 * @param serviceMeta 服务元信息
	 * @return
	 */
	public static MethodMeta getMethodMeta(Method method, ServiceMeta serviceMeta) {
		RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
		//方法信息
		MethodMeta methodData = new MethodMeta();
		String typeString = RpcHelper.getParamTypesStr(method.getParameterTypes());
		String methodIdKey = RpcHelper.getMethodId(serviceMeta.getServiceName(), method.getName(), typeString);
		methodData.setMethodId(methodIdKey);
		methodData.setMethodName(method.getName());
		MethodReturn methodReturn = new MethodReturn();

		//获得Field实例f的泛型类型
		Type[] gType = ReflectUtil.getGenericReturnType(method);
		if (gType != null && gType.length > 0) {
			List<GenericTypeMeta> genericTypeMetas = Lists.newArrayList();
			for (Type type : gType) {
				if (type instanceof ParameterizedType) {
					continue;
				}
				GenericTypeMeta genericTypeMeta = new GenericTypeMeta();
				if (((Class) type).getAnnotation(RpcStruct.class) != null) {
					genericTypeMeta.setStructFlag(1);
					genericTypeMeta.setStructId(DigestUtils.md2Hex(((Class) type).getName()));
				} else {
					genericTypeMeta.setStructFlag(0);
				}
				genericTypeMeta.setGenericType(type);
				genericTypeMeta.setGenericTypeName(type.getTypeName());
				genericTypeMetas.add(genericTypeMeta);
			}
			methodReturn.setGenericTypeMetas(genericTypeMetas);
		}
		methodReturn.setReturnType(method.getReturnType().getName());
		if (method.getReturnType().getAnnotation(RpcStruct.class) != null) {
			methodReturn.setStructFlag(1);
			methodReturn.setStructId(DigestUtils.md2Hex(method.getReturnType().getName()));
		}
		methodData.setMethodReturn(methodReturn);
		if (rpcMethod != null) {
			methodData.setTimeout(rpcMethod.timeout());
		} else {
			HealthCheck healthCheck = method.getAnnotation(HealthCheck.class);
			if (healthCheck == null) {

			}
		}
		Doc rpcMethodDoc = method.getAnnotation(Doc.class);
		if (rpcMethodDoc != null) {
			methodData.setMethodDoc(rpcMethodDoc.value());
			methodData.setAuthor(rpcMethodDoc.author());
			methodData.setCreateTime(rpcMethodDoc.date());
			if (StringUtil.isEmpty(methodData.getAuthor())) {
				methodData.setAuthor(serviceMeta.getAuthor());
				methodData.setCreateTime(serviceMeta.getCreateTime());
			}
		} else {
			methodData.setAuthor(serviceMeta.getAuthor());
			methodData.setCreateTime(serviceMeta.getCreateTime());
		}
		return methodData;
	}

	/**
	 * 判断当前服务是否为proxy服务
	 * @return
	 */
	public static boolean isRpcProxy() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.PROXY.name());
	}

	/**
	 * 判断当前服务是否为配置中心服务
	 * @return
	 */
	public static boolean isRpcConfig() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.RPC_CENTER.name());
	}

	/**
	 * 是否可以对body做编解码
	 * proxy转发的情况下，一般不做编解码，因为多余的编解码会影响效率
	 * 并且如果是pb序列化的byte[] 在proxy不一定能解码
	 * @param msg 消息传递对象
	 * @return boolean
	 */
	public static boolean coderEnable(BaseRpcMessage msg) {
		RpcHeader header = msg.getHeader();
		if (!RpcHelper.isRpcProxy() || RpcHelper.isSendByProxy(header.getClientRole()) || isProxyCoderNamespace(header.getNamespace())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否允许在proxy进行编解码的服务命名空间
	 * @param namespace 服务命名空间
	 * @return boolean
	 */
	public static boolean isProxyCoderNamespace(String namespace) {
		String namespaces = PropertyManager.get(RpcConstants.ENABLE_PROXY_CODER_NAMESPACES);
		return StringUtil.containsAny(namespaces, namespace);
	}

	/**
	 * 指定当前服务的请求和响应可以在proxy做编解码
	 * @param namespace 服务命名空间
	 */
	public static void addProxyCoderNamespace(String namespace) {
		String namespaces = PropertyManager.get(RpcConstants.ENABLE_PROXY_CODER_NAMESPACES);
		namespaces = namespace + "," + namespaces;
		PropertyManager.getProp().setProperty(RpcConstants.ENABLE_PROXY_CODER_NAMESPACES, namespaces);
	}

	/**
	 * 判断当前服务是否为授权中心服务
	 * @return boolean
	 */
	public static boolean isRpcAuth() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.RPC_AUTH.name());
	}

	/**
	 * 判断当前服务是否为注册中心服务
	 * @return boolean
	 */
	public static boolean isRpcRegister() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY);
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.RPC_REGISTRY.name());
	}

	/**
	 * 判断当前服务是否为proxy服务
	 * @return boolean
	 */
	public static boolean isSendByProxy(String serverRole) {
		return StringUtil.equalsIgnoreCase(serverRole, RpcServerType.PROXY.name());
	}

	/**
	 * 判断当前服务是否为治理中心服务
	 * @return boolean
	 */
	public static boolean isRpcCenter() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY);
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.RPC_CENTER.name());
	}

	/**
	 * 判断当前服务是否为业务服务
	 * @return boolean
	 */
	public static boolean isRpcAppServer() {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.BUSINESS_SERVER.name());
	}

	/**
	 * 设置头部ip地址信息
	 * @param address 地址信息
	 * @param header 头部
	 */
	public static void setAddress(InetSocketAddress address, RpcHeader header) {
		log.debug("remote address:{}", address);
		if (StringUtil.isEmpty(header.getClientIp())) {
			header.setClientIp(address.getHostString());
		}
		if (StringUtil.equals(RpcConstants.LOCAHOST, header.getClientIp())) {
			header.setClientIp(NetUtil.getLocalHost());
		}
		header.setClientPort(String.valueOf(address.getPort()));
	}

	public static Map<String, Field> getHeaderFieldMap() {
		return rpcHeaderFields;
	}

	/**
	 *  header 的字段缓存
	 * @param fieldName 字段名
	 */
	public static void addRpcHeaderField(String fieldName, Field field) {
		rpcHeaderFields.put(fieldName, field);
	}

	/**
	 * 缓存rpc 头部字段
	 */
	public static void addHeaderFileds() {
		Field[] fields = RpcHeader.class.getDeclaredFields();
		for (Field field : fields) {
			addRpcHeaderField(field.getName(), field);
		}
	}

	/**
	 * relay 协议解码专用
	 * 将queryString 的参数转换成RpcRequest对象
	 * @param map 请求参数
	 * @return RpcRequest
	 */
	public static RpcRequest convert2RpcRequest(Map<String, String> map) {
		RpcRequest request = new RpcRequest();
		RpcHeader header = new RpcHeader();
		request.setHeader(header);

		String headerPreffix = "header.";
		Map<String, String> paramMap = Maps.newHashMap();
		map.forEach((key, value) -> {
			if (StringUtil.startsWith(key, headerPreffix)) {
				String headerParamName = StringUtil.substringAfter(key, headerPreffix);
				if (rpcHeaderFields.containsKey(headerParamName)) {
					rpcHeaderFields.get(headerParamName).setAccessible(true);
					ReflectUtil.setField(header, rpcHeaderFields.get(headerParamName), value);
				}
			} else {
				paramMap.put(key, value);
			}
		});

		String paramString = StringUtil.convert2QueryString(paramMap);
		if (isRpcProxy()) {
			//报文体的参数，组成queryString，proxy服务的时候，没有method对象
			request.setBody(paramString);
		} else {
			//本地方法调用
			// relay 协议的 请求参数在这里需要进行一次解析,解析成List集合（方法参数可能是多个，放到list存储）
			String methodId = RpcHelper.getMethodId(map);
			Method method = RpcHelper.getMethod(methodId);
			request.setBody(RpcHelper.parseRelay(paramString, method));
		}
		return request;
	}


	/**
	 * 请求参数转换成map，请求参数，如：map.key=value
	 * @param mapName 远程方法参数名
	 * @param request http请求
	 * @return Map
	 */
	public static Map<String, String> parseMapParam(String mapName, Map<String, String> request) {
		Map<String, String> map = new HashMap<>();
		request.forEach((k, v) -> {
			if (StringUtil.startsWith(k, mapName + ".")) {
				String[] params = StringUtil.split(k, ".");
				if (params != null && params.length == 2) {
					String key = StringUtil.substringAfter(k, mapName + ".");
					String value = v;
					map.put(key, value);
				}
			}
		});
		return map;
	}

	/**
	 * 解析结构体的参数
	 * @param request
	 * @return
	 */
	private static Map<String, List<Pair<String, String>>> parseStructParams(Map<String, String> request) {
		Map<String, List<Pair<String, String>>> paramMap = Maps.newHashMap();
		request.forEach((k, v) -> {
			String[] params = StringUtil.split(k, ".");
			if (params != null && params.length == 2) {
				String value = v;
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
				throw new RpcException(RpcError.RPC_FIELD_ERROR, "not support param :" + k);
			}
			//
		});

		return paramMap;
	}

	/**
	 * 判断该类型是否为8大基本类型
	 * @param type
	 * @return
	 */
	public static boolean isSimpleType(Class type) {

		if (type.equals(boolean.class)) {
			return true;
		}
		if (type.equals(int.class)) {
			return true;
		}
		if (type.equals(float.class)) {
			return true;
		}
		if (type.equals(double.class)) {
			return true;
		}
		if (type.equals(short.class)) {
			return true;
		}
		if (type.equals(byte.class)) {
			return true;
		}
		if (type.equals(long.class)) {
			return true;
		}
		return false;
	}

	/**
	 * 数据类型转换
	 * @param value 值
	 * @param type 类型
	 * @return obj
	 */
	public static Object string2Obj(String value, Class type) {
		if (value == null) {
			return null;
		}
		if (type.equals(String.class)) {
			return value;
		}
		if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			return Boolean.parseBoolean(value);
		}
		if (type.equals(Integer.class) || type.equals(int.class)) {
			return Integer.parseInt(value);
		}
		if (type.equals(Float.class) || type.equals(float.class)) {
			return Float.parseFloat(value);
		}
		if (type.equals(Double.class) || type.equals(double.class)) {
			return Double.parseDouble(value);
		}
		if (type.equals(Short.class) || type.equals(short.class)) {
			return Short.parseShort(value);
		}
		if (type.equals(Byte.class)) {
			return Byte.parseByte(value);
		}
		if (type.equals(Long.class) || type.equals(long.class)) {
			return Long.parseLong(value);
		}

		if (Date.class.equals(type)) {
			long time = Long.parseLong(value);
			return new Date(time);
		}

		if (Timestamp.class.equals(type)) {
			long time = Long.parseLong(value);
			return new Timestamp(time);
		}

		if (List.class.equals(type) || ArrayList.class.equals(type)) {
			List<String> list = Lists.newArrayList();
			String[] paramValues = StringUtil.split(value, ",");
			for (String paramValue : paramValues) {
				list.add(paramValue);
			}
			return list;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[Ljava.lang.Integer;")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			Integer[] v = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Integer.valueOf(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[Ljava.lang.Float;")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			Float[] v = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Float.valueOf(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[Ljava.lang.Double;")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			Double[] v = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Double.valueOf(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[Ljava.lang.Long;")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			Long[] v = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Long.valueOf(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[I")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			int[] v = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Integer.parseInt(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[F")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			float[] v = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Float.parseFloat(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[D")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			double[] v = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Double.parseDouble(values[i]);
			}
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type.getName(), "[L")) {
			String[] values = StringUtil.split(value, ",");
			if (values == null) {
				return null;
			}
			long[] v = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				v[i] = Long.parseLong(values[i]);
			}
			return v;
		}

		throw new RpcException(RpcError.NOT_SUPPORT_DATATYPE, "not suppport type:" + type.getName());
	}

	/**
	 * relay 协议的body，必须是string
	 * 转换成queryString
	 * @param response 响应对象
	 * @return String
	 */
	public static String convert2QueryString(BaseRpcMessage response) {

		StringBuilder sb = new StringBuilder();
		RpcHeader header = response.getHeader();
		rpcHeaderFields.forEach((key, value) -> {
			value.setAccessible(true);
			Object v = ReflectUtil.getFieldValue(header, value);
			String str = StringUtil.EMPTY;
			if (v != null) {
				str = v.toString();
			}
			String headerKey = "header." + key;
			sb.append(headerKey).append("=").append(StringUtil.trimToEmpty(str)).append("&");
		});
		if (response.getBody() == null) {
			return sb.toString();
		}
		if (response.getBody().getClass() != String.class) {
			throw new RpcException(RpcError.RELAY_BODY_TYPE_ERROR);
		}
		return sb.append(response.getBody()).toString();

	}


	/**
	 * 解析relay参数 到 method对应的参数对象
	 * @param queryString queryString
	 * @param method 本地方法对象
	 * @return List
	 */
	public static List parseRelay(String queryString, Method method) {
		List body;
		try {
			Map<String, String> paramMap = StringUtil.parse2Map(queryString);
			Class<?>[] types = method.getParameterTypes();
			String[] names = discover.getParameterNames(method);
			Map<String, List<Pair<String, String>>> structParams = parseStructParams(paramMap);

			body = Lists.newArrayList();
			for (int i = 0; i < types.length; i++) {
				RpcStruct struct = types[i].getAnnotation(RpcStruct.class);
				if (struct == null) {
					if (RpcHelper.isMap(types[i])) {
						//Map
						body.add(parseMapParam(names[i], paramMap));
					} else {
						//普通类型，如：8大基本类型，date /timestamp /List
						String value = paramMap.get(names[i]);
						if (StringUtil.isNotEmpty(value)) {
							Object v = string2Obj(value, types[i]);
							body.add(v);
						} else {
							body.add(null);
						}
					}
				} else {
					Object obj = types[i].newInstance();
					List<Pair<String, String>> params = structParams.get(names[i]);
					for (Pair<String, String> param : params) {
						if (StringUtil.isNotEmpty(param.getRight())) {
							Field field = ReflectionUtil.getField(types[i], param.getLeft());
							Object value = string2Obj(param.getRight(), field.getType());
							ReflectionUtil.setFieldValue(obj, param.getLeft(), value);
						}
					}
					body.add(obj);
				}
			}
		} catch (Exception e) {
			String error = ExceptionUtil.stackTraceText(e);
			throw new RpcException(RpcError.RELAY_REQUEST_PARAM_ERROR, error);
		}
		return body;
	}

	/**
	 * 根据数据类型，初始对象
	 * @param type 数据类型
	 */
	public static Object newInstance(String type)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {

		if (type.equals(String.class.getName())) {
			return new String();
		}
		if (type.equals(Integer.class.getName()) || type.equals(int.class.getSimpleName())) {
			return new Integer(0);
		}
		if (type.equals(Float.class.getName()) || type.equals(float.class.getSimpleName())) {
			return new Float(0f);
		}
		if (type.equals(Double.class.getName()) || type.equals(double.class.getSimpleName())) {
			return new Double(0d);
		}
		if (type.equals(Short.class.getName()) || type.equals(short.class.getSimpleName())) {
			return Short.parseShort("0");
		}
		if (type.equals(Byte.class.getName()) || type.equals(byte.class.getSimpleName())) {
			return Byte.parseByte("0");
		}
		if (type.equals(Long.class.getName()) || type.equals(long.class.getSimpleName())) {
			return Long.parseLong("0");
		}
		if (type.equals(Boolean.class.getName()) || type.equals(boolean.class.getSimpleName())) {
			return Boolean.parseBoolean("true");
		}

		if (Date.class.getName().equals(type)) {
			return new Date();
		}

		if (Timestamp.class.getName().equals(type)) {
			return new Timestamp(new Date().getTime());
		}

		if (List.class.getName().equals(type) || ArrayList.class.getName().equals(type)) {
			List<String> list = Lists.newArrayList();
			return list;
		}

		if (StringUtil.equalsIgnoreCase(type, "[Ljava.lang.Integer;")) {
			Integer[] v = new Integer[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[Ljava.lang.Float;")) {
			Float[] v = new Float[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[Ljava.lang.Double;")) {
			Double[] v = new Double[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[Ljava.lang.Long;")) {
			Long[] v = new Long[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[I")) {
			int[] v = new int[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[F")) {
			float[] v = new float[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[D")) {
			double[] v = new double[0];
			return v;
		}

		if (StringUtil.equalsIgnoreCase(type, "[L")) {
			long[] v = new long[0];
			return v;
		}

		Class clz = Class.forName(type);
		return clz.newInstance();
	}

	/**
	 * 优先取同步回来的服务列表
	 * 选择可用的rpc register
	 * @return
	 */
	public static boolean selectRpcRegisterServer() {

		//如果同步集合没有，那么就从本机配置文件指定的注册中心地址选取
		String serversString = PropertyManager.get(RpcConstants.RPC_REGISTER_SERVERS_KEY);
		String[] servers = StringUtil.split(serversString, ";");
		boolean initSuccess = false;

		for (String server : servers) {
			String[] hostPort = StringUtil.split(server, ":");
			//@todo 检查网络
			if (!com.haven.simplej.net.NetUtil.connect(hostPort[0], Integer.parseInt(hostPort[1]))) {
				log.warn("rpc server connect timeout ,server:{}", server);
				continue;
			}
			InvocationContext.setServer(hostPort[0], Integer.parseInt(hostPort[1]));
			initSuccess = true;
			break;
		}
		return initSuccess;
	}

	/**
	 *  判断是否为响应
	 * @return boolean
	 */
	public static boolean isResponse(byte msgType) {
		if (msgType == MsgType.RESPONSE.value) {
			return true;
		}
		return false;
	}

	/**
	 * 获取rpc服务监听的地址
	 * @return String
	 */
	public static String getRpcServerHost() {
		if (isRpcAppServer()) {
			//默认是返回127.0.0.1只允许本机访问
			return PropertyManager.get(RpcConstants.RPC_SERVER_HOST_KEY, RpcConstants.LOCAHOST);
		}
		return PropertyManager.get(RpcConstants.RPC_SERVER_HOST_KEY, RpcConstants.WHOLE_NETWORK);
	}


	/**
	 * 增加coder对象信息
	 * @param coder 编解码器
	 */
	public static void addCoder(Coder coder) {
		log.debug("add coder:{}", coder.getClass().getName());
		customCoderClassMap.put(coder.getClass().getName(), coder.getClass());
		customCoderMap.put(coder.getClass().getName(), coder);
	}

	/**
	 * 获取自定义编解码器
	 * @param coderClassName 编解码器的类名
	 * @return 编解码器对象
	 */
	public static Coder getCoder(String coderClassName) {

		return customCoderMap.get(coderClassName);
	}

	/**
	 * 获取自定义编解码器
	 * @param coderClassName 编解码器的类名
	 * @return 编解码器class
	 */
	public static Class getCoderClass(String coderClassName) {

		return customCoderClassMap.get(coderClassName);
	}
}
