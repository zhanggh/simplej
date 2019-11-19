package com.haven.simplej.http;

import lombok.Data;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author: havenzhang
 * @date: 2019/5/16 22:47
 * @version 1.0
 */
@Data
public class HttpBuilder {
	private int connectTimeout;
	private int socketTimeout;
	private String mimetype;
	private String encoding;
	private boolean keepAlive;
	private PoolingHttpClientConnectionManager poolManager;

	public HttpExecuter build(){
		HttpExecuter executer = new HttpExecuter();
		executer.setConnectTimeout(this.connectTimeout);
		executer.setEncoding(this.encoding);
		executer.setSocketTimeout(this.socketTimeout);
		executer.setContentType(this.mimetype);
		executer.setPoolManager(this.poolManager);
		executer.setKeepAlive(this.keepAlive);
		return executer;
	}

	public HttpBuilder setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public HttpBuilder setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
		return this;
	}

	public HttpBuilder setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public HttpBuilder setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public HttpBuilder setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public HttpBuilder setPoolManager(PoolingHttpClientConnectionManager poolManager) {
		this.poolManager = poolManager;
		return this;
	}


}
