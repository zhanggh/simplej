package com.haven.simplej.rpc.proxy.coder;

import com.haven.simplej.rpc.protocol.coder.RpcDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * proxy 专用解码器
 * @author: havenzhang
 * @date: 2018/11/6 22:26
 * @version 1.0
 */
@Slf4j
@Component
public class ProxyDecoder extends RpcDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

	}


}
