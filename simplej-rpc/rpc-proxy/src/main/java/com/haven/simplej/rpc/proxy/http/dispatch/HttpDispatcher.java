package com.haven.simplej.rpc.proxy.http.dispatch;

import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.rpc.proxy.http.model.HttpResponse;

/**
 * http转发器
 * @author: havenzhang
 * @date: 2019/5/15 22:41
 * @version 1.0
 */
public interface HttpDispatcher {

	HttpResponse dispatch(HttpRequest request);
}
