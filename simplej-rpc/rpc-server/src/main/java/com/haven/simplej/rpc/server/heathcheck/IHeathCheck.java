package com.haven.simplej.rpc.server.heathcheck;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.model.HeathResponse;

/**
 * 健康度监测服务,每一个rpc server都会默认有一个健康度监测的服务，由本框架提供
 * 注意：是每一个rpc server都会默认实现了健康度监测的服务功能（框架实现）
 * @author: havenzhang
 * @date: 2018/5/6 22:09
 * @version 1.0
 */
@Doc(value = "健康度监测服务,每一个rpc server都会默认有一个健康度监测的服务，由本框架提供",author = "havenzhang")
@RpcService(timeout = 1000)
public interface IHeathCheck {



	/**
	 * 框架默认提供的服务健康监测服务，比如当我们需要监测某个服务是否正常/存活的时候，
	 * 可以直接telnet服务端口或者发送任意请求
	 * @return
	 */
	@Doc("rpc服务默认的健康监测接口方法,框架默认提供的服务健康监测服务，比如当我们需要监测某个服务是否正常/存活的时候")
	@RpcMethod
	HeathResponse healthCheck();

	@RpcMethod
	@Doc("接收远程关闭服务的命令")
	boolean shutdown(@RpcParam @Doc("用户名") String user,@RpcParam @Doc("用户密码")  String password);
}
