package com.haven.simplej.tcp.pool;

import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;

/**
 * tcp socket 对象池
 * @author: havenzhang
 * @date: 2018/9/30 15:21
 * @version 1.0
 */
@Slf4j
public class TcpPoolObjectFactory implements KeyedPooledObjectFactory<String, Socket> {


	@Override
	public PooledObject<Socket> makeObject(String key) throws Exception {
		log.debug("makeObject,key:{}", key);
		String[] params = StringUtil.split(key, ":");
		DefaultPooledObject<Socket> pooledObject;
		if (params != null && params.length == 2) {
			Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
			pooledObject = new DefaultPooledObject<>(socket);
			log.debug("makeObject socket host:{} port:{}", params[0], Integer.parseInt(params[1]));
			return pooledObject;
		}
		return new DefaultPooledObject<>(null);
	}

	@Override
	public void destroyObject(String key, PooledObject<Socket> pooledObject) throws Exception {
		log.debug("destroyObject,key:{} object:{}", key, pooledObject.getObject());
		if (pooledObject.getObject() != null) {
			pooledObject.getObject().close();
		}
	}

	@Override
	public boolean validateObject(String key, PooledObject<Socket> pooledObject) {
		log.debug("validateObject,key:{} object:{}", key, pooledObject.getObject());
		Socket socket = pooledObject.getObject();
		return socket.isConnected() && !socket.isInputShutdown() && !socket.isOutputShutdown();
	}

	@Override
	public void activateObject(String key, PooledObject<Socket> pooledObject) throws Exception {
		log.debug("activateObject,key:{} object:{}", key, pooledObject.getObject());
	}

	@Override
	public void passivateObject(String key, PooledObject<Socket> pooledObject) throws Exception {
		log.debug("passivateObject,key:{} object:{}", key, pooledObject.getObject());

	}
}
