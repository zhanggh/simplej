package com.haven.simplej.rpc.protocol.coder;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.MsgType;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.serializer.ProtostuffUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * 报文规范：请求包类型（1个字节）+消息类型（1个字节）+报文长度（4个字节）+序列化类型（4个字节）+头部长度（4个字节）+报文内容+rpcBody(长度) + rpcBody
 * netty 解码器
 * @author: havenzhang
 * @date: 2018/4/26 11:38
 * @version 1.0
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

	/**
	 * 请求类型 0-心跳包，1-业务包
	 */
	private byte msgFlag = 0;
	/**
	 * 消息类型，0-请求，1-响应
	 */
	private byte msgType = 0;

	/**
	 * 报文前面部分的长度
	 */
	private int prefixLen = 10;

	public RpcDecoder() {
		super();
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < prefixLen) { //不够长度丢弃 前4个字节存储报文长度，后面4个字节存储报文序列化类型，1-protostuff ,2-json
			log.error("decode msg error,readableBytes less then 10");
			return;
		}

		RpcHeader header = new RpcHeader();
		BaseRpcMessage msg = new BaseRpcMessage<>();
		msg.setHeader(header);
		//序列化方式
		int serialType = 1;
		try {
			//请求头标志，1-业务请求，0-心跳包
			msgFlag = in.readByte();
			msg.setMsgFlag(msgFlag);
			if (msgFlag == 0) {
				log.debug("heartbeat request...................");
				out.add(msg);
				return;
			}
			msgType = in.readByte();
			msg.setMsgType(msgType);
			//请求流的前面4个字节，代表报文的完整长度
			int dataLength = in.readInt();
			//序列化方式
			serialType = in.readInt();
			//非常重要，指定下一次读取的位置
			in.readerIndex(prefixLen);
			in.markReaderIndex();
			int readable = in.readableBytes();
			if (readable < dataLength) {
				in.resetReaderIndex();
				//读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
				return;
			}
			log.debug("dataLength:{},msgFlag:{},serialType:{},readIndex:{},readable:{}", dataLength, msgFlag,
					serialType, in.readerIndex(), readable);

			//rpcheader的长度
			int headerLen = in.readInt();
			//传输报文数据
			byte[] headerBytes = new byte[headerLen];
			in.readBytes(headerBytes);

			//rpcBody的长度
			int bodyLen = in.readInt();
			//传输报文数据
			byte[] bodyBytes = new byte[bodyLen];
			if (bodyLen > 0) {
				in.readBytes(bodyBytes);
			}

			if (headerLen + bodyLen + 8 != dataLength) {
				log.error("headerLen({}) + bodyLen({}) != dataLength({})", headerLen, bodyLen, dataLength);
				throw new RpcException(RpcError.DECODE_ERROR, "headerLen(" + headerLen + ") + bodyLen(" + bodyLen + ")"
						+ " != " + "dataLength(" + dataLength + ")");
			}

			//反序列化
			if (SerialType.JSON.getValue() == serialType) {
				log.debug("decode header json:{}", new String(headerBytes, RpcConstants.DEFAULT_ENCODE));
				header = JSON.parseObject(headerBytes, RpcHeader.class);
				msg.setHeader(header);
				header.setSerialType(serialType);
				if (RpcHelper.coderEnable(msg)) {
					//当前encoder所在的服务不是proxy服务 或者 客户是proxy的时候,对报文进行解码
					if (msg.getMsgType() == MsgType.REQUEST.value && StringUtils.isNotEmpty(msg.getHeader().getRequestCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getRequestCoderClassName());
						List<Object>  params = coder.decode(bodyBytes);
						msg.setBody(params);
					} else if (msg.getMsgType() == MsgType.RESPONSE.value && StringUtils.isNotEmpty(msg.getHeader().getResponseCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getResponseCoderClassName());
						List<Object> respList = coder.decode(bodyBytes);
						if (CollectionUtil.isNotEmpty(respList)) {
							//响应的时候，List 集合第一个元素就是响应结果对象
							msg.setBody(respList.get(0));
						}
					} else {
						String json = new String(bodyBytes, RpcConstants.DEFAULT_ENCODE);
						msg = JsonDecoder.decode(msg, json);
					}

				} else {
					//如果当前解码的服务是proxy，但是发起rpc请求的客户端却不是proxy自身（比如转发），那么body不解码
					msg.setBytesArray(bodyBytes);
				}
				out.add(msg);
			} else if (SerialType.PROTOSTUFF.getValue() == serialType) {
				header = ProtostuffUtil.deserializer(headerBytes, RpcHeader.class); //将byte数据转化为我们需要的对象
				msg.setHeader(header);
				if (RpcHelper.coderEnable(msg)) {
					//需要做解码的时候
					if (msg.getMsgType() == MsgType.REQUEST.value && StringUtils.isNotEmpty(msg.getHeader().getRequestCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getRequestCoderClassName());
						List<Object>  params = coder.decode(bodyBytes);
						msg.setBody(params);
					} else if (msg.getMsgType() == MsgType.RESPONSE.value && StringUtils.isNotEmpty(msg.getHeader().getResponseCoderClassName())) {
						//如果是自定义编解码器的时候
						Coder coder = RpcHelper.getCoder(msg.getHeader().getResponseCoderClassName());
						List<Object> respList = coder.decode(bodyBytes);
						if (CollectionUtil.isNotEmpty(respList)) {
							//响应的时候，List 集合第一个元素就是响应结果对象
							msg.setBody(respList.get(0));
						}
					} else {
						RpcBody body = ProtostuffUtil.deserializer(bodyBytes, RpcBody.class);
						if (RpcHelper.isResponse(msg.getMsgType())) {
							//客户端接收服务端的响应时，解码
							msg.setBody(body.getBody());
						} else if (!RpcHelper.isRpcProxy()) {
							//服务端接收请求时解码
							//如果respcode是空，则代表是请求对象（约定），那么body就是一个list（元素就是目标远程方法的参数值）
							List tmp = Lists.newArrayList();
							List list = (List) body.getBody();
							list.forEach(e -> {
								if (e instanceof NullValue) {
									tmp.add(null);
								} else {
									tmp.add(e);
								}
							});
							msg.setBody(tmp);
						} else {
							msg.setBody(body.getBody());
						}
					}
				} else {
					//如果当前解码的服务是proxy，但是发起rpc请求的客户端却不是proxy自身（比如转发），那么body不解码
					msg.setBytesArray(bodyBytes);
				}
				out.add(msg);
			} else if (SerialType.RELAY.getValue() == serialType) {
				//relay协议
				String queryString = new String(headerBytes, RpcConstants.DEFAULT_ENCODE);
				Map<String, String> map = StringUtil.parse2Map(queryString);
				msg = RpcHelper.convert2RpcRequest(map);
				header = msg.getHeader();
				out.add(msg);
			} else if (SerialType.FILE_TRANSFER.getValue() == serialType) {
				//请求报文规范：报文长度+序列化方式（4）+报文头长度+报文体长度（参数）+文件流长度+报文头+报文体+文件流
				header = JSON.parseObject(headerBytes, RpcHeader.class);
				msg.setBytesArray(bodyBytes);
				msg.setFileTransfer(true);
				out.add(msg);
			} else {
				log.error("error serial type:{}", serialType);
				throw new RpcException(RpcError.NOT_SUPPORT_SERIAL_TYPE);
			}
		} catch (Exception cause) {
			header.setSerialType(serialType);
			msg.setMsgFlag(msgFlag);
			if (cause instanceof RpcException) {
				RpcException rpcException = (RpcException) cause;
				header.setRespCode(rpcException.getRespCode());
				header.setRespMsg(rpcException.getRespMsg());
			} else {
				header.setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
				header.setRespMsg(ExceptionUtil.stackTraceText(cause));
			}
			throw new RpcException(header.getRespCode(), header.getRespMsg(), header);
		} finally {
			InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
			RpcHelper.setAddress(address, header);
			msg.setMsgFlag(msgFlag);
			header.setSerialType(serialType);
			SequenceUtil.putTraceId(header.getMsgId());
		}

	}
}
