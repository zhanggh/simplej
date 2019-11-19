package com.haven.simplej.rpc.registry.helper;

import com.haven.simplej.rpc.model.UrlInfo;
import com.haven.simplej.rpc.model.MethodInfo;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.security.DigestUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author: havenzhang
 * @date: 2019/1/14 21:20
 * @version 1.0
 */
@Slf4j
public class ServiceHelper {

	private static final String MD5_PARAM_SPLIT = ":";

	public static String servicesMd5(List<ServiceInfo> services) {
		StringBuilder str = new StringBuilder();
		services.forEach(e -> {
			Set<MethodInfo> methodInfos = e.getMethods();
			TreeSet<MethodInfo> set = new TreeSet<>(methodInfos);
			if (methodInfos != null) {
				set.forEach(s -> {
					str.append(s.getParamsTypes()).append(MD5_PARAM_SPLIT);
					str.append(s.getMethodName()).append(MD5_PARAM_SPLIT);
					str.append(s.getReturnType()).append(MD5_PARAM_SPLIT);
					str.append(s.getVersion()).append(MD5_PARAM_SPLIT);
					str.append(s.getTimeout()).append(MD5_PARAM_SPLIT);
				});
			}
			str.append(e.getNamespace()).append(MD5_PARAM_SPLIT);
			str.append(e.getServiceName()).append(MD5_PARAM_SPLIT);
			str.append(e.getVersion()).append(MD5_PARAM_SPLIT);
			str.append(e.getTimeout()).append(MD5_PARAM_SPLIT);
			List<ServiceInstance> instances = e.getInstances();
			instances.forEach(s -> {
				str.append(s.getIdc()).append(MD5_PARAM_SPLIT);
				str.append(s.getHost()).append(MD5_PARAM_SPLIT);
				str.append(s.getPort()).append(MD5_PARAM_SPLIT);
				str.append(s.getNamespace()).append(MD5_PARAM_SPLIT);
			});
		});
		log.debug("str.toString():{}", str.toString());
		return DigestUtils.md5Hex(str.toString());
	}

	public static String urlMd5(List<UrlInfo> services) {
		StringBuilder str = new StringBuilder();
		services.forEach(e -> {
			str.append(e.getHttpMethod()).append(MD5_PARAM_SPLIT);
			str.append(e.getNamespace()).append(MD5_PARAM_SPLIT);
			str.append(e.getUri()).append(MD5_PARAM_SPLIT);
			str.append(e.getVersion()).append(MD5_PARAM_SPLIT);
			str.append(e.getHeader()).append(MD5_PARAM_SPLIT);
			str.append(e.getTimeout()).append(MD5_PARAM_SPLIT);
			List<ServiceInstance> instances = e.getInstances();
			instances.forEach(s -> {
				str.append(s.getIdc()).append(MD5_PARAM_SPLIT);
				str.append(s.getHost()).append(MD5_PARAM_SPLIT);
				str.append(s.getPort()).append(MD5_PARAM_SPLIT);
				str.append(s.getNamespace()).append(MD5_PARAM_SPLIT);
				str.append(s.getNamespace()).append(MD5_PARAM_SPLIT);
			});
		});
		log.debug("str.toString():{}", str.toString());
		return DigestUtils.md5Hex(str.toString());
	}
}
