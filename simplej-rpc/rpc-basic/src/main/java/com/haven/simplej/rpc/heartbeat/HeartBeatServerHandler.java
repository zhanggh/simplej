package com.haven.simplej.rpc.heartbeat;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * tcp网络触达监听器
 * @author: havenzhang
 * @date: 2019/5/13 23:09
 * @version 1.0
 */
@Slf4j
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {


	private int lossConnectCount = 0;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		long readerIdleTime = PropertyManager.getLong(RpcConstants.RPC_READER_IDLE_TIME, 30);
		log.debug("已经{}秒未收到客户端的消息了！", readerIdleTime);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				lossConnectCount++;
				if (lossConnectCount > 2) {
					log.debug("大于两个读空闲时间隔，关闭这个不活跃通道！");
					ctx.channel().close();
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		lossConnectCount = 0;
		log.debug("client says: " + msg.toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}

