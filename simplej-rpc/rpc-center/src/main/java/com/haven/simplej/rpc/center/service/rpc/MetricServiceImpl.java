package com.haven.simplej.rpc.center.service.rpc;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.center.model.SqlLogModel;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.center.service.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 流量监听业务处理类
 * 流量信息的后置处理：
 * 1.链路跟踪
 * 2.流量统计
 * 3.性能分析
 * 4.日志收集
 * 5.监控告警
 * 6.服务健康度更新
 * @author: havenzhang
 * @date: 2018/9/28 17:13
 * @version 1.0
 */
@Service
@Slf4j
public class MetricServiceImpl implements MetricService {


	@Override
	public void reportRpcCall(RpcRequest request, RpcResponse response) {
		log.debug("request header:{}", JSON.toJSONString(request.getHeader(), true));
		log.debug("response header:{}", JSON.toJSONString(response.getHeader(), true));
	}

	@Override
	public void reportSqlCall(SqlLogModel sqlCallModel) {
		log.debug("sqlCallModel:{}", JSON.toJSONString(sqlCallModel, true));

	}
}
