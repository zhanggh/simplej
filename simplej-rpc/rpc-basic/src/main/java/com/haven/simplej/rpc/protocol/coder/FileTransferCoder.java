package com.haven.simplej.rpc.protocol.coder;

import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 文件传输编码解码器
 * @author: havenzhang
 * @date: 2018/10/3 18:26
 * @version 1.0
 */
@Slf4j
public class FileTransferCoder {



	/**
	 * 解码
	 * @param stream 报文流
	 * @return RpcRequest
	 */
	public static RpcRequest decode(byte[] stream) {
		RpcRequest request = new RpcRequest();
		RpcHeader header = new RpcHeader();
		request.setHeader(header);


		return request;
	}
}
