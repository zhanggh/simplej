package com.haven.simplej.rpc.protocol.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author: havenzhang
 * @date: 2018/10/1 18:40
 * @version 1.0
 */
@Slf4j
public class RpcChannelFutureListener implements ChannelFutureListener {
	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		log.debug("----------------operationComplete-----------------");
	}
}
