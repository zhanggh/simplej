package com.haven.simplej.rpc.protocol.coder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.sequence.SequenceUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2018/10/8 21:44
 * @version 1.0
 */
@Slf4j
public class JsonDecoder {

	/**
	 * json报文的反序列化
	 * @param msg
	 * @return
	 */
	public static BaseRpcMessage decode(BaseRpcMessage msg, String body) {

		//json解码，要考虑proxy服务的客户端解码和app应用服务端和客户端的解码
		RpcHeader header = msg.getHeader();
		SequenceUtil.putTraceId(header.getMsgId());

		Method method = RpcHelper.getMethod(RpcHelper.getMethodId(msg.getHeader()));
		if (method == null) {
			log.error("can not find method Object by methodId :{},serviceName:{} ,methodName:{}", header.getMethodId()
					, header.getServiceName(), header.getMethodName());
			throw new RpcException(RpcError.METHOD_NOT_FOUND);
		}
		if (RpcHelper.isResponse(msg.getMsgType())) {
			//如果是响应到客户端的时候，解码处理逻辑
			Class returnTypeClz = method.getReturnType();
			Object value = JSON.parseObject(body, returnTypeClz);
			msg.setBody(value);

			if (msg.getBody() instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) msg.getBody();
				Object obj = jsonObject.toJavaObject(returnTypeClz);
				msg.setBody(obj);
			} else if (msg.getBody() instanceof JSONArray) {
				JSONArray params = (JSONArray) msg.getBody();
				List<Type[]> genericTypes = Lists.newArrayList();
				Type[] types = ReflectUtil.getGenericReturnType(method);
				genericTypes.add(types);
				msg.setBody(parseBody(params, new Class[]{method.getReturnType()}, genericTypes));
				params.clear();
			}
		} else {
			//如果是请求对象，解码处理逻辑，请求到达服务端的时候，解码
			Object value = JSON.parseArray(body);
			msg.setBody(value);
			Class<?>[] types = method.getParameterTypes();
			if (types != null && types.length > 0) {
				JSONArray params = (JSONArray) msg.getBody();
				if (types.length != params.size()) {
					log.debug("types.length != params.size()");
					throw new RpcException(RpcError.REQUEST_PARAMS_NOT_MATCH);
				}
				List<Type[]> genericTypes = ReflectUtil.getParamGenericType(method);
				msg.setBody(parseBody(params, types, genericTypes));
				params.clear();
			}
		}
		return msg;
	}


	/**
	 * 解析body
	 * @param params json数组
	 * @param types 要解析成的数据类型
	 * @param genericTypes 泛型
	 * @return List
	 */
	private static List parseBody(JSONArray params, Class<?>[] types, List<Type[]> genericTypes) {
		List bodyList = Lists.newArrayList();
		for (int i = 0; i < types.length; i++) {
			if (params.get(i) == null) {
				bodyList.add(null);
			} else if (params.get(i).getClass() == JSONObject.class) {
				JSONObject jsonObject = (JSONObject) params.get(i);
				if (RpcHelper.isMap(types[i])) {
					if (genericTypes.get(i) != null) {
						//有设置泛型的情况
						Type[] gTypes = genericTypes.get(i);
						Class left = (Class) gTypes[0];
						Class right = (Class) gTypes[1];
						Map map = jsonObject.toJavaObject(Map.class);
						Map map2 = Maps.newHashMap();
						map.forEach((k, v) -> {
							Object newKey;
							if (k.getClass() == JSONObject.class) {
								JSONObject jobj = (JSONObject) k;
								newKey = jobj.toJavaObject(left);
							} else {
								newKey = k;
							}
							Object newValue;
							if (v.getClass() == JSONObject.class) {
								JSONObject jobj = (JSONObject) v;
								newValue = jobj.toJavaObject(right);
							} else {
								newValue = v;
							}
							map2.put(newKey, newValue);
						});
						bodyList.add(map2);
					} else {
						bodyList.add(jsonObject.toJavaObject(types[i]));
					}
				} else {
					bodyList.add(jsonObject.toJavaObject(types[i]));
				}
			} else if (params.get(i).getClass() == JSONArray.class) {
				//如果是数组的时候，处理逻辑
				JSONArray array = (JSONArray) params.get(i);
				if (genericTypes.get(i) != null) {
					Class type = (Class) genericTypes.get(i)[0];
					bodyList.add(array.toJavaList(type));
				} else {
					bodyList.add(params.get(i));
				}
			} else {
				bodyList.add(params.get(i));
			}
		}
		return bodyList;
	}
}
