package com.haven.simplej.rpc.protocol.coder;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.MsgType;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.model.RpcBody;
import com.haven.simplej.serializer.ProtostuffUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 报文规范：请求包类型（1个字节）+消息类型（1个字节）+报文长度（4个字节）+序列化类型（4个字节）+头部长度（4个字节）+报文内容+rpcBody(长度) + rpcBody
 * netty消息编码器
 * @author: havenzhang
 * @date: 2018/4/26 11:39
 * @version 1.0
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<BaseRpcMessage> {


	public RpcEncoder() {
		super();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, BaseRpcMessage msg, ByteBuf out) {
		//rpc头部的字节数组
		byte[] rpcHeaderBytes;
		//rpc体的字节数组
		byte[] rpcBodyBytes = new byte[0];
		try {
			int serialType = msg.getHeader().getSerialType();
			if (SerialType.JSON.getValue() == serialType) {
				rpcHeaderBytes = JSON.toJSONBytes(msg.getHeader()); //使用fastJson将对象转换为byte
				if (RpcHelper.coderEnable(msg)) {
					//当前encoder所在的服务不是proxy服务 或者 客户是proxy的时候
					if (msg.getMsgType() == MsgType.REQUEST.value && StringUtils.isNotEmpty(msg.getHeader().getRequestCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getRequestCoderClassName());
						rpcBodyBytes = coder.encode((List<Object>) msg.getBody());
					} else if (msg.getMsgType() == MsgType.RESPONSE.value && StringUtils.isNotEmpty(msg.getHeader().getResponseCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getResponseCoderClassName());
						rpcBodyBytes = coder.encode((List<Object>) msg.getBody());
					} else if (msg.getBody() != null) {
						rpcBodyBytes = JSON.toJSONBytes(msg.getBody()); //使用fastJson将对象转换为byte
					}
				} else {
					//proxy服务，做转发的时候，不需要做编解码
					if (msg.getBytesArray() != null) {
						rpcBodyBytes = msg.getBytesArray();
					}
				}
			} else if (SerialType.PROTOSTUFF.getValue() == serialType) {
				rpcHeaderBytes = ProtostuffUtil.serializer(msg.getHeader()); //使用ProtostuffUtil将对象转换为byte
				if (RpcHelper.coderEnable(msg)) {
					//proxy服务,并且rpc发起方不是proxy的时候，报文体数据在BytesArray字段中获取，原因
					RpcBody body = new RpcBody();
					if (msg.getMsgType() == MsgType.REQUEST.value && StringUtils.isNotEmpty(msg.getHeader().getRequestCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getRequestCoderClassName());
						rpcBodyBytes = coder.encode((List<Object>) msg.getBody());
					} else if (msg.getMsgType() == MsgType.RESPONSE.value && StringUtils.isNotEmpty(msg.getHeader().getResponseCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getResponseCoderClassName());
						rpcBodyBytes = coder.encode((List<Object>) msg.getBody());
					} else if (msg.getBody() != null) {
						//默认使用框架提供的编解码器
						body.setBody(msg.getBody());
						rpcBodyBytes = ProtostuffUtil.serializer(body); //使用ProtostuffUtil
					}
				} else {
					// 将对象转换为byte
					if (msg.getBytesArray() != null) {
						rpcBodyBytes = msg.getBytesArray();
					}
				}
			} else if (SerialType.RELAY.getValue() == serialType) {
				//注意： relay 协议,此时，不管是编码器还是解码器，body都是relay参数的原串，对不body进一步编解码
				String str = RpcHelper.convert2QueryString(msg);
				rpcHeaderBytes = str.getBytes(RpcConstants.DEFAULT_ENCODE);
			} else if (SerialType.FILE_TRANSFER.getValue() == serialType) {
				//文件传输的时候，头部是用json序列化，文件流不做序列化
				rpcHeaderBytes = JSON.toJSONBytes(msg.getHeader()); //使用fastJson将对象转换为byte
				rpcBodyBytes = msg.getBytesArray();
			} else {
				log.error("error serial type:{}", msg.getHeader().getSerialType());
				rpcHeaderBytes = RpcError.NOT_SUPPORT_SERIAL_TYPE.getErrorMsg().getBytes();
			}
			//写数据
			int totalLen = rpcHeaderBytes.length + rpcBodyBytes.length + 8;
			write(out, msg, totalLen, rpcHeaderBytes, rpcBodyBytes);
		} catch (Throwable cause) {
			log.error("encode error:{}", cause);
			if (cause instanceof RpcException) {
				RpcException rpcException = (RpcException) cause;
				msg.getHeader().setRespCode(rpcException.getRespCode());
				msg.getHeader().setRespMsg(rpcException.getRespMsg());
			} else {
				msg.getHeader().setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
				msg.getHeader().setRespMsg(ExceptionUtil.stackTraceText(cause));
			}
			throw new RpcException(msg.getHeader().getRespCode(), msg.getHeader().getRespMsg(), msg.getHeader());
		}
	}

	/**
	 * 写数据
	 */
	protected void write(ByteBuf out, BaseRpcMessage msg, int totalLen, byte[] rpcHeaderBytes, byte[] rpcBodyBytes) {
		//写数据
		out.writeByte(msg.getMsgFlag()); //请求包类型，1-业务包，0-心跳包
		out.writeByte(msg.getMsgType()); //数据包类型，0-请求包，1-响应包
		out.writeInt(totalLen); //先将消息长度写入，也就是消息头，8是两个字节长度
		out.writeInt(msg.getHeader().getSerialType()); //把序列化类型写入
		out.writeInt(rpcHeaderBytes.length); //把头部的长度写入
		if (rpcHeaderBytes.length > 0) {
			out.writeBytes(rpcHeaderBytes); //报文头header的数据
		}
		out.writeInt(rpcBodyBytes.length); //把body的长度写入
		if (rpcBodyBytes.length > 0) {
			out.writeBytes(rpcBodyBytes); //报文body的数据
		}
	}
}
