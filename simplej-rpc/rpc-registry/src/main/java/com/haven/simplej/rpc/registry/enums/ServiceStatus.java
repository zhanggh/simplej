package com.haven.simplej.rpc.registry.enums;

import lombok.Getter;

/**
 * 服务状态
 * 服务状态，1-正常，0-未启用，2-关闭（无效）
 * @author: havenzhang
 * @date: 2018/5/4 17:35
 * @version 1.0
 */
@Getter
public enum ServiceStatus {

	unavailable(0),normal(1),invalid(2);

	int status;

	ServiceStatus(int status){
		this.status = status;
	}
}
