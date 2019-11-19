package com.haven.simplej.rpc.constant;

/**
 * 定义一些字段名
 * @author: havenzhang
 * @date: 2019/1/16 23:40
 * @version 1.0
 */
public class HttpField {
	/**
	 * 目标业务服务提供的协议，http/tcp
	 */
	public static final String protocol = "protocol";
	/**
	 * 通讯编码
	 */
	public static final String encoding = "encoding";
	/**
	 * contentType
	 */
	public static final String contentType = "contentType";
	/**
	 * 长链接字段
	 */
	public static final String keepalive = "keepalive";
	/**
	 * 连接超时时间
	 */
	public static final String connectTimeout = "connectTimeout";
	/**
	 * socket超时
	 */
	public static final String socketTimeout = "socketTimeout";
	/**
	 * 服务命名空间（模块id之类的唯一标志)
	 */
	public static final String namespace = "namespace";
	/**
	 * 服务名称
	 */
	public static final String serviceName = "serviceName";
	/**
	 * 协议版本号
	 */
	public static final String version = "version";
	/**
	 * 服务版本号
	 */
	public static final String serviceVersion = "version";
	/**
	 * 链路跟踪id
	 */
	public static final String msgId = "msgId";
	/**
	 * 调用方法名
	 */
	public static final String methodName = "methodName";
	/**
	 * 调用方法参数类型，如：java.lang.String,java.lang.Integer
	 */
	public static final String methodParamTypes = "methodParamTypes";
	/**
	 * 调用方法返回类型，如：java.lang.String
	 */
	public static final String methodReturnType = "methodReturnType";

	/**
	 * DigestUtils.md2Hex(serverName+methodName)
	 */
	public static final String methodId = "methodId";

	/**
	 * http响应码字段
	 */
	public static final String httpRespCode = "code";

	/**
	 * http响应信息字段
	 */
	public static final String httpRespMsg = "msg";

	/**
	 * 目标url
	 */
	public static final String destUrl = "destUrl";



}
