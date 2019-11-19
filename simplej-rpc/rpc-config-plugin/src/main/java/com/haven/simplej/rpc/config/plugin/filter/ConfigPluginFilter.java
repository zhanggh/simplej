package com.haven.simplej.rpc.config.plugin.filter;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.config.plugin.helper.ConfigPluginHelper;
import com.haven.simplej.rpc.config.plugin.manager.RpcPropertyManager;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/10/31 23:11
 * @version 1.0
 */
@Slf4j
@Component
public class ConfigPluginFilter implements RpcFilter {

	private static Method queryNewConfigItem;
	private static Method queryNewConfigFile;
	private static Method getConfigItem;
	private static Method getConfigFile;

	public ConfigPluginFilter() throws NoSuchMethodException {
		//配置中心查询属性的相关方法
		queryNewConfigItem = IConfigService.class.getMethod("queryNewConfigItem", String.class, boolean.class);
		queryNewConfigFile = IConfigService.class.getMethod("queryNewConfigFile", String.class, boolean.class);
		getConfigItem = IConfigService.class.getMethod("getConfigItem", String.class, String.class);
		getConfigFile = IConfigService.class.getMethod("getConfigFile", String.class, String.class);
	}

	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {
		log.debug("-------------ConfigPluginFilter---------------");

		RpcHeader header = request.getHeader();
		Object value = null;
		String methodName = null;
		String methodId = RpcHelper.getMethodId(header);
		if (!RpcHelper.isSendByProxy(header.getClientRole())) {
			//如果请求不是发自proxy，那么先判断缓存是否存在
			if (StringUtil.isNotEmpty(methodId) && ConfigPluginHelper.isConfigMethod(methodId)) {
				String queryNewConfigItemMethodId = RpcHelper.getMethodId(IConfigService.class, queryNewConfigItem);
				String queryNewConfigFileMethodId = RpcHelper.getMethodId(IConfigService.class, queryNewConfigFile);
				String getConfigItemMethodId = RpcHelper.getMethodId(IConfigService.class, getConfigItem);
				String getConfigFileMethodId = RpcHelper.getMethodId(IConfigService.class, getConfigFile);
				List params = (List) request.getBody();

				if (StringUtil.equals(queryNewConfigItemMethodId, header.getMethodId())) {
					String namespace = (String) params.get(0);
					value = RpcPropertyManager.getItemResponse(namespace);
				} else if (StringUtil.equals(queryNewConfigFileMethodId, header.getMethodId())) {
					String namespace = (String) params.get(0);
					value = RpcPropertyManager.getFileResponse(namespace);
				} else if (StringUtil.equals(getConfigItemMethodId, header.getMethodId())) {
					String namespace = (String) params.get(0);
					String key = (String) params.get(1);
					value = RpcPropertyManager.getItemModel(namespace, key);
				} else if (StringUtil.equals(getConfigFileMethodId, header.getMethodId())) {
					String namespace = (String) params.get(0);
					String fileName = (String) params.get(1);
					value = RpcPropertyManager.getFileModel(namespace, fileName);
				}
				Method method = ConfigPluginHelper.getMethod(header.getMethodId());
				methodName = method.getName();
			}
			if (value != null) {
				ResponseHelper.setResponse(value, request.getHeader(), response);
				log.debug("get properties from proxy cache,methodName:{},result:{}", methodName,
						JSON.toJSONString(value));
				return;
			}
		}
		//继续下一个filter
		chain.doFilter(request, response);

	}

	@Override
	public int getOrder() {
		return FilterOrder.CONFIG_LISTEN.order();
	}

}
