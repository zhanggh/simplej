package com.haven.simplej.rpc.server.heathcheck;

import com.haven.simplej.rpc.model.HeathResponse;
import com.haven.simplej.rpc.server.heartbeat.HeartbeatManager;
import com.haven.simplej.rpc.server.signal.AppSignalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务健康度监测服务，提供给外部、人工/监控系统进行计划性的服务存活监测
 * @author: havenzhang
 * @date: 2018/5/6 22:11
 * @version 1.0
 */
@Slf4j
public class HeathCheckServiceImpl implements IHeathCheck {

	@Autowired
	private AppSignalHandler signalHandler;

	@Override
	public HeathResponse healthCheck() {
		return HeartbeatManager.healthCheck();
	}

	@Override
	public boolean shutdown(String user, String password) {
		log.debug(".....................shutdown server.....................");
		new Thread(() -> {
			try {
				signalHandler.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
}
