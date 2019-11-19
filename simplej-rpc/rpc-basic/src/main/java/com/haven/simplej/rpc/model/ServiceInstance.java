package com.haven.simplej.rpc.model;

import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author haven.zhang
 * @date 2019/2/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceInstance implements Comparable<String>{



	/**
	 * 所在机房
	 */
	private String idc;

	/**
	 * 虚拟集合id
	 */
	private String regionId;


	/**
	 * 服务监听的地址
	 */
	private String host;
	/**
	 * http 目标服务端口
	 */
	private int httpPort;
	/**
	 * 本地proxy http端口
	 */
	private int proxyHttpPort;
	/**
	 * tcp 目标服务端口
	 */
	private int port;

	/**
	 * 本地proxy tcp端口
	 */
	private int proxyPort;

	/**
	 * 节点流量权重
	 */
	private double weight;

	/**
	 * 请求过程中计算后的权重
	 */
	private double currentWeight;

	/**
	 * 服务命名空间（域名）
	 */
	private String namespace;

	/**
	 * 是否与proxy在同一台机器
	 */
	private boolean local;

	public ServiceInstance(String host,int port){
		this.host = host;
		this.port = port;
	}

	@Override
	public int compareTo(String host) {
		return StringUtil.compare(host,this.host);
	}

	public String getMd5(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.idc).append(",");
		sb.append(this.regionId).append(",");
		sb.append(this.host).append(",");
		sb.append(this.httpPort).append(",");
		sb.append(this.proxyHttpPort).append(",");
		sb.append(this.port).append(",");
		sb.append(this.proxyPort).append(",");
		sb.append(this.weight).append(",");

		return DigestUtils.md5Hex(sb.toString());
	}
}
