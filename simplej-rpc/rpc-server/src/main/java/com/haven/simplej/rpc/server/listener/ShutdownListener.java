package com.haven.simplej.rpc.server.listener;

import com.haven.simplej.rpc.server.signal.AppSignalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author: havenzhang
 * @date: 2019/11/10 20:49
 * @version 1.0
 */
//@Component
@Slf4j
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {

	@Autowired
	private AppSignalHandler signalHandler;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.debug(".........................ContextClosedEvent.......................");
		signalHandler.shutdown();
	}
}
