package com.haven.simplej.rpc.model;

import lombok.Data;

import java.util.Map;


/**
 * rpc 请求头部
 * @author: havenzhang
 * @date: 2018/4/26 11:43
 * @version 1.0
 */
@Data
public class RpcHeader {

	public RpcHeader(){
		super();
	}

	public RpcHeader(String msgId){
		super();
		this.msgId = msgId;
	}

	/**
	 * 消息id，必填
	 */
	private String msgId;
	/**
	 * 消息报文版本号，必填
	 */
	private String version;

	/**
	 * 报文加密算法，可选
	 */
	private String encryptAlgorithm;
	/**
	 * 报文签名算法，必填
	 */
	private String signAlgorithm;
	/**
	 * 报文序列化方式 protobuff/json，必填
	 */
	private int serialType;

	/**
	 * 额外的信息，可选
	 */
	private Map<String, String> addition;
	/**
	 * 响应码，响应的时候必填，底层框架根据该字段判断是否为响应还是请求
	 * 当该字段有值的时候，代表可以响应给客户端了，责任链停止并退出
	 */
	private String respCode;
	/**
	 * 响应消息，必填
	 */
	private String respMsg;

	/**
	 * 服务名，当methodId不为空的时候，可选
	 */
	private String serviceName;

	/**
	 * 用于唯一标志一个method对象的id
	 */
	private String methodId;

	/**
	 * 执行方法，当methodId不为空的时候，可选
	 */
	private String methodName;

	/**
	 * service版本号，当methodId不为空的时候，可选
	 */
	private String serverVersion;

	/**
	 * 超时时间，默认是不限制，可选
	 */
	private long timeout;

	/**
	 * 目标服务的命名空间（或者域名），框架自动填
	 */
	private String namespace;

	/**
	 * 是否长链接，可选，建议填上
	 */
	private boolean keepalive;

	/**
	 * 是否为同步请求
	 */
	private boolean syncRequest = true;
	/**
	 * 编码，默认是utf-8，可选
	 */
	private String encode;

	/**
	 * 调用方法参数类型，如：java.lang.String,java.lang.Integer
	 * ，当methodId不为空的时候，可选
	 */
	private String methodParamTypes;

	/**
	 * 调用方法返回类型，如：java.lang.String
	 * 请求的时候，不需要填，返回的时候会填上该字段
	 */
	private String methodReturnType;


	/**
	 * 客户端服务域名（命名空间），主要用于跟踪链路，框架自动填
	 */
	private String clientNamespace;


	/**
	 * 客户端ip，框架自动填
	 */
	private String clientIp;
	/**
	 * 客户端请求出口端口
	 */
	private String clientPort;

	/**
	 * 服务端ip，框架自动填,在proxy服务转发的时候补上
	 */
	private String serverIp;

	/**
	 * 服务端端口，框架自动填,在proxy服务转发的时候补上
	 */
	private String serverPort;

	/**
	 * 请求生成时间，客户端框架自动生成
	 */
	private long requestTime;

	/**
	 * proxy转发请求的当前时间，proxy服务自动生成
	 */
	private long proxyDispatchTime;
	/**
	 * 请求响应回到proxy的当前时间
	 */
	private long proxyResponseTime;

	/**
	 * 通讯模式，P2P- 点对点，默认的模式
	 * BROADCAST - 广播
	 * 选填，如果需要广播，则填上：BROADCAST
	 */
	private String requestMode;

	/**
	 * 客户端所在idc，选填，不填的情况，默认是default，
	 * proxy服务会根据该参数进行优先本机房路由
	 */
	private String clientIdc;

	/**
	 * 客户端所属集合id，选填，不填的时候，默认是：default
	 * proxy服务会根据该参数进行优先本集合路由
	 */
	private String clientRegionId;

	/**
	 * 是否为模拟结果
	 */
	private boolean mockResponse;

	/**
	 * 响应延时（也就是请求的完整耗时）
	 */
	private long delayed;


	/**
	 * 是否快速失败，响应的时候填上，默认是false
	 */
	private boolean fallback;

	/**
	 * 发起调用的服务角色是：proxy？还是普通app,默认是app
	 * 必填
	 */
	private String clientRole;

	/**
	 * proxy服务生成的签名，当开启权限控制的时候，不管是客户端应用还是服务端应用，
	 * 都需要对proxy的请求和响应进行验签
	 */
	private String proxySign;

	/**
	 * 客户应用的请求签名信息或者服务端应用的响应签名信息,
	 * 签名内容，request body序列化后的byte[]
	 */
	private String sign;

	/**
	 * 自定义的请求参数编解码器名，框架会通过类名查找编解码器对象进行编解码
	 * 非必须，主要提供给有需要使用自定义协议的调用方和服务方进行自定义协议约定
	 */
	private String requestCoderClassName;

	/**
	 * 自定义的响应结果的编解码器名称
	 * 非必须，同上
	 */
	private String responseCoderClassName;

}
