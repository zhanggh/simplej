package com.haven.simplej.rpc.server.signal;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.enums.ServerState;
import com.haven.simplej.rpc.server.helper.ServerHelper;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author: havenzhang
 * @date: 2018/05/23 23:12
 * @version 1.0
 * 在Linux下支持的信号（具体信号kill -l命令查看）：
 * SEGV, ILL, FPE, BUS, SYS, CPU, FSZ, ABRT, INT, TERM, HUP, USR1, USR2, QUIT, BREAK, TRAP, PIPE
 * 在Windows下支持的信号：
 * SEGV, ILL, FPE, ABRT, INT, TERM, BREAK
 * 运行中可能会抛出异常：
 * java.lang.IllegalArgumentException: Signal already used by VM: USR1
 * 这是因为某些信号可能已经被JVM占用，可以考虑用其它信号代替
 */
@Slf4j
public class AppSignalHandler implements SignalHandler {

	private IServiceRegister serviceRegister;

	private static volatile int state = ServerState.STARTUP_SUCC.state();

	public AppSignalHandler() {
		super();
	}

	public AppSignalHandler(IServiceRegister serviceRegister) {
		this.serviceRegister = serviceRegister;
	}

	@Override
	public void handle(Signal signal) {
		log.info("handle signal:{}，shutdown server", signal.getName());
		shutdown();
	}

	public synchronized void shutdown() {
		log.info("shutdown server ......");
		if(state == ServerState.SHUTDOWN.state()){
			log.debug("................. In closing ....................");
			return;
		}
		state = ServerState.SHUTDOWN.state();
		if (NettyServer.getState() == ServerState.STARTUP_SUCC.state() && !RpcHelper.isRpcRegister() && !RpcHelper.isRpcProxy()) {
			String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
			//向注册中心报告服务关闭
			if (serviceRegister != null) {
				serviceRegister.shutdown(ServiceInfoHelper.getLocalInstance(namespace));
			}
		}

		//关闭所有服务
		ServerHelper.shutdownAll();
	}
}
