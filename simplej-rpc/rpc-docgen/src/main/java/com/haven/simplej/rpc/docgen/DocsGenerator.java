package com.haven.simplej.rpc.docgen;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haven.simplej.rpc.annotation.*;
import com.haven.simplej.rpc.docgen.util.ClassUtil;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Set;

/**
 * 文档生成器
 * @author: havenzhang
 * @date: 2019/1/15 17:39
 * @version 1.0
 */
@Slf4j
public class DocsGenerator {

	/**
	 * 参数名称解析器
	 */
	private static final DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();

	private static final Set<ServiceMeta> SERVICE_META_LIST = Sets.newHashSet();
	private static final Set<RpcModelMeta> modelList = Sets.newHashSet();

	private static String basePackage;

	public static Set<ServiceMeta> getServiceMetaList() {
		return SERVICE_META_LIST;
	}

	public static Set<RpcModelMeta> getModelList() {
		return modelList;
	}

	public static String getBasePackage() {
		return basePackage;
	}

	/**
	 * 扫描指定包路径下的所有类
	 * @param basePackage 包名
	 * @return
	 */
	public static void scanPackage(String basePackage) {
		DocsGenerator.basePackage = basePackage;
		Set<Class> classList = ClassUtil.getClasses(basePackage);
		for (Class aClass : classList) {
			if (!aClass.isInterface()) {
				RpcStruct rpcStruct = (RpcStruct) aClass.getAnnotation(RpcStruct.class);
				if (rpcStruct != null) {
					modelList.add(parseRpcModel(aClass));
				}
			} else {
				RpcService rpcService = (RpcService) aClass.getAnnotation(RpcService.class);
				if (rpcService != null) {
					SERVICE_META_LIST.add(parseServiceClass(aClass, rpcService));
				}
			}
		}
		log.debug("SERVICE_META_LIST:{}", JSON.toJSONString(SERVICE_META_LIST, true));
	}


	/**
	 * 解析rpc 数据对象类元信息
	 * @param model 结构类
	 * @return RpcModelMeta
	 */
	public static RpcModelMeta parseRpcModel(Class model) {

		Doc doc = (Doc) model.getAnnotation(Doc.class);
		RpcModelMeta rpcModelMeta = new RpcModelMeta();
		rpcModelMeta.setModelId(DigestUtils.md2Hex(model.getName()));
		if (doc != null) {
			rpcModelMeta.setModelDoc(doc.value());
			rpcModelMeta.setAuthor(doc.author());
			rpcModelMeta.setCreateTime(doc.date());
		}
		rpcModelMeta.setModelName(model.getName());
		List<ParamMeta> params = Lists.newArrayList();
		Field[] fields = model.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			ParamMeta paramMeta = new ParamMeta();
			paramMeta.setDataType(field.getType().getName());
			paramMeta.setName(field.getName());
			paramMeta.setListFlag(RpcHelper.isList(field.getType()) ? 1 : 0);
			paramMeta.setMapFlag(RpcHelper.isMap(field.getType()) ? 1 : 0);
			paramMeta.setArrayFlag(field.getType().isArray() ? 1 : 0);
			RpcParam rpcParam = field.getAnnotation(RpcParam.class);
			if (rpcParam != null) {
				paramMeta.setRequired(rpcParam.required());
				paramMeta.setMaxLen(rpcParam.maxLength());
				paramMeta.setMinLen(rpcParam.minLength());
			}
			RpcStruct rpcStruct = field.getAnnotation(RpcStruct.class);
			if (rpcStruct != null) {
				paramMeta.setRequired(rpcStruct.required());
			}
			Doc rpcDoc = field.getAnnotation(Doc.class);
			if (rpcDoc != null) {
				paramMeta.setParamDoc(rpcDoc.value());
			}
			params.add(paramMeta);
		}
		rpcModelMeta.setParamMetaList(params);
		return rpcModelMeta;
	}

	/**
	 * 解析service 类元信息，如方法，参数
	 * @param serviceClz 接口类
	 * @param rpcService rpcservice 注解
	 * @return ServiceMeta
	 */
	public static ServiceMeta parseServiceClass(Class serviceClz, RpcService rpcService) {
		ServiceMeta serviceMeta = new ServiceMeta();
		String serviceName = rpcService.serviceName();
		if (StringUtil.isEmpty(serviceName)) {
			serviceName = serviceClz.getName();
		}
		serviceMeta.setServiceId(DigestUtils.md2Hex(serviceName + rpcService.version()));
		serviceMeta.setServiceName(serviceName);
		serviceMeta.setVersion(rpcService.version());
		serviceMeta.setTimeout(rpcService.timeout());
		Doc rpcDoc = (Doc) serviceClz.getAnnotation(Doc.class);
		if (rpcDoc != null) {
			serviceMeta.setServiceDoc(rpcDoc.value());
			serviceMeta.setAuthor(rpcDoc.author());
			serviceMeta.setCreateTime(rpcDoc.date());
		}
		//解析方法，当前类定义的方法
		Method[] methods = serviceClz.getDeclaredMethods();
		if (methods == null || methods.length == 0) {
			return null;
		}
		for (Method method : methods) {
			MethodMeta methodMeta = RpcHelper.getMethodMeta(method,serviceMeta);
			String[] names = discover.getParameterNames(method);
			Class[] types = method.getParameterTypes();
			List<ParamMeta> params;
			params = Lists.newArrayList();
			//方法参数的注解
			Annotation[][] annotations = method.getParameterAnnotations();
			if (names != null && names.length > 0) {
				//解析参数
				for (int i = 0; i < names.length; i++) {
					Annotation[] paramAnnotation = annotations[i];
					params.addAll(RpcHelper.parseParams(new Pair<>(names[i], types[i]), paramAnnotation));
				}
				methodMeta.getParams().addAll(params);
			}

			serviceMeta.getMethods().add(methodMeta);
		}
		SERVICE_META_LIST.add(serviceMeta);
		return serviceMeta;
	}


}
