package com.haven.simplej.rpc.validate;

import com.google.common.collect.Maps;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 参数校验器，@TODO 有待优化
 * @author: havenzhang
 * @date: 2019/1/14 23:10
 * @version 1.0
 */
public class RpcParamValidator {

	/**
	 * 执行方法注解缓存
	 */
	private static final Map<Method, Annotation[][]> methodParamAnnotaionMap = Maps.newConcurrentMap();
	/**
	 * 字段缓存
	 */
	private static final Map<Field, RpcParam> fieldAnnotationMap = Maps.newConcurrentMap();

	public static void validate(Method method, Object[] args) {
		if (args == null || args.length == 0) {
			return;
		}
		if (method == null) {
			throw new RpcException(RpcError.METHOD_NOT_FOUND);
		}
		Annotation[][] paramsAnnotations = methodParamAnnotaionMap.get(method);
		if (paramsAnnotations == null) {
			paramsAnnotations = method.getParameterAnnotations();
			if (paramsAnnotations == null) {
				return;
			}
			methodParamAnnotaionMap.putIfAbsent(method, paramsAnnotations);
		}
		int argIndex = 0;
		for (Annotation[] paramsAnnotation : paramsAnnotations) {
			if(paramsAnnotation == null){
				continue;
			}
			for (Annotation annotation : paramsAnnotation) {
				if (annotation.annotationType().equals(RpcStruct.class)) {
					List<Field> fields = BeanUtil.getFields(args[argIndex].getClass());
					for (Field field : fields) {
						RpcParam param;
						if (fieldAnnotationMap.containsKey(field)) {
							param = fieldAnnotationMap.get(field);
						} else {
							param = field.getAnnotation(RpcParam.class);
						}
						Object fieldValue = ReflectionUtil.getFieldValue(args[argIndex], field);
						paramValid(param, fieldValue, field.getName());
					}

				} else if (annotation.annotationType().equals(RpcParam.class)) {
					RpcParam param = (RpcParam) annotation;
					String note = param.alias();
					if (StringUtil.isEmpty(note)) {
						note = " of the" + argIndex;
					}
					paramValid(param, args[argIndex], note);
				}
			}
			argIndex++;
		}

	}

	private static void paramValid(RpcParam param, Object value, String fieldName) {

		if(param == null){
			return;
		}
		if (param.required() && value == null) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "param " + fieldName + " can not be null");
		}

		if (param.required() && value != null && value.getClass().equals(String.class)) {
			if (StringUtil.isNotEmpty(param.regular())) {
				if (StringUtil.match((String) value, param.regular())) {
					throw new RpcException(RpcError.RPC_FIELD_ERROR, "param " + fieldName + " should match the " +
							"regular：" + param.regular());
				}
			}
			String string = (String) value;
			int len = StringUtil.length(string);
			if (param.maxLength() != 0) {
				if (param.minLength() > len || len > param.maxLength()) {
					throw new RpcException(RpcError.RPC_FIELD_ERROR,
							"param " + fieldName + " length should between [" + param.minLength() + " ," + param.maxLength() + "]");
				}
			} else {
				if (param.minLength() > len) {
					throw new RpcException(RpcError.RPC_FIELD_ERROR,
							"param " + fieldName + " length should grater " + "then " + param.minLength());
				}
			}
		}
	}

	/**
	 * rpc报文头参数校验
	 * @param header rpc 头部
	 */
	public static void headerValid(RpcHeader header) {

		if (StringUtil.isEmpty(header.getNamespace())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "rpc header param namespace can not be empty");
		}
		if (StringUtil.isEmpty(header.getServerVersion())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "rpc header param serverVersion can not be empty");
		}
		if (StringUtil.isEmpty(header.getMsgId())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "rpc header param msgId can not be empty");
		}
		if (StringUtil.isEmpty(header.getClientIp())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "rpc header param clientIp can not be empty");
		}
		if (StringUtil.isEmpty(header.getClientNamespace())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "rpc header param clientNamespace can not be empty");
		}

		if (StringUtil.isEmpty(header.getMethodId()) && (StringUtil.isEmpty(header.getServiceName()) && StringUtil.isEmpty(header.getMethodName()) && StringUtil.isEmpty(header.getMethodParamTypes()))) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR,
					"rpc header param methodId or serviceName and methodName" + " can not  be  empty");
		}
	}
}
