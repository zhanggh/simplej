package com.haven.simplej.rpc.condition;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.RpcServerType;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否为proxy服务
 * @author: havenzhang
 * @date: 2018/10/1 21:40
 * @version 1.0
 */
@Slf4j
public class IsRpcProxyServer implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		return StringUtil.equalsIgnoreCase(serverType, RpcServerType.PROXY.name());
	}
}
