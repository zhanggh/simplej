package com.haven.simplej.rpc.proxy.coder;

import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.protocol.coder.RpcEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * proxy编码器专用
 * @author: havenzhang
 * @date: 2018/11/6 22:29
 * @version 1.0
 */
@Slf4j
@Component
public class ProxyEncoder extends RpcEncoder {

	@Override
	protected void encode(ChannelHandlerContext ctx, BaseRpcMessage msg, ByteBuf out) {


	}
}
