//package com.haven.simplej.rpc.center.scheduled;
//
//import com.haven.simplej.property.PropertyManager;
//import com.haven.simplej.rpc.annotation.RpcService;
//import com.haven.simplej.rpc.model.ServiceListInfo;
//import com.haven.simplej.rpc.model.UrlInfo;
//import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
//import com.haven.simplej.rpc.model.ServiceInfo;
//
//import com.haven.simplej.time.DateUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * 心跳监听器，监听各个proxy上报的app应用心跳信息
// * @author: havenzhang
// * @date: 2018/5/11 17:22
// * @version 1.0
// */
//@Component
//@Slf4j
//@RpcService(timeout = 100)
//public class ServiceTaskManager {
//
//	@Autowired
//	private InstanceInfoService instanceInfoService;
//
//	@Autowired
//	private ServiceInfoService service;
//	@Autowired
//	private RegisterMysqlImpl registerMysql;
//	@Autowired
//	private UrlInfoService urlInfoService;
//
//	private List<ServiceInfo> serviceInfoList;
//	private List<UrlInfoModel> urlList;
//
//	private String serviceListMd5;
//	private String urlListMd5;
//
//	/**
//	 * 多个rpc server服务共同对所有实例进行检查，按分片处理，instance_id%rpc_server数，
//	 * 实例心跳报告检查
//	 */
//	@Scheduled(cron = "*/5 * * * * ?")
//	public void instanceCheck() {
//		InstanceInfoModel instance = new InstanceInfoModel();
//		instance.setStatus((byte) InstanceStatus.normal.getStatus());
//		List<InstanceInfoModel> instances = instanceInfoService.query(instance);
//		int updateGap = 2 * PropertyManager.getInt(RpcCenterConstant.RPC_SERVICE_HEARBEAT_TIME_GAP, 5000);
//		for (InstanceInfoModel instanceInfoModel : instances) {
//			if (new Date().getTime() - instanceInfoModel.getHeartbeatTime() > updateGap + 1) {
//				InstanceInfoModel model = new InstanceInfoModel();
//				model.setId(instanceInfoModel.getId());
//				model.setStatus((byte) InstanceStatus.invalid.getStatus());
//				model.setUpdateTime(DateUtils.getTimestamp(new Date()));
//				model.setRemark("没有定时心跳，下线处理");
//				ThreadPoolFactory.getHeartbeatExecutor().execute(() -> instanceInfoService.update(instanceInfoModel));
//			}
//		}
//	}
//
//	@Scheduled(cron = "*/5 * * * * ?")
//	public void serviceCheck() {
//		//@TODO一次捞取数据量太大，需要优化(分页)
//		final int updateGap = 2 * PropertyManager.getInt(RpcCenterConstant.RPC_SERVICE_HEARBEAT_TIME_GAP, 5000);
//
//		//service 过期检测
//		ThreadPoolFactory.getHeartbeatExecutor().execute(() -> {
//			ServiceInfoModel req = new ServiceInfoModel();
//			req.setStatus((byte) ServiceStatus.normal.getStatus());
//			List<ServiceInfoModel> services = service.query(req);
//			services.forEach(e -> {
//				if (new Date().getTime() - e.getHeartbeatTime() > updateGap + 1) {
//					ServiceInfoModel model = new ServiceInfoModel();
//					model.setStatus((byte) ServiceStatus.invalid.getStatus());
//					model.setUpdateTime(DateUtils.getTimestamp(new Date()));
//					model.setRemark("没有定时心跳，下线处理");
//					model.setId(e.getId());
//					service.update(e);
//				}
//			});
//
//		});
//		//url心跳检测
//		ThreadPoolFactory.getHeartbeatExecutor().execute(() -> {
//			UrlInfoModel req = new UrlInfoModel();
//			req.setStatus((byte) ServiceStatus.normal.getStatus());
//			List<UrlInfoModel> services = urlInfoService.query(req);
//			services.forEach(e -> {
//				if (new Date().getTime() - e.getHeartbeatTime() > updateGap + 1) {
//					ServiceInfoModel model = new ServiceInfoModel();
//					model.setStatus((byte) ServiceStatus.invalid.getStatus());
//					model.setUpdateTime(DateUtils.getTimestamp(new Date()));
//					model.setRemark("没有定时心跳，下线处理");
//					model.setId(e.getId());
//					urlInfoService.update(e);
//				}
//			});
//		});
//
//
//	}
//
//	@Scheduled(cron = "*/5 * * * * ?")
//	public void serviceChangeMonitor() {
//		//定期捞取所有服务
//		ThreadPoolFactory.getHeartbeatExecutor().execute(() -> {
//			ServiceListInfo serviceListInfo = service.getService(null);
//			if (!serviceListInfo.getMd5().equalsIgnoreCase(serviceListMd5)) {
//				synchronized (registerMysql.getServiceMonitor()) {
//					serviceListMd5 = serviceListInfo.getMd5();
//					registerMysql.getServiceMonitor().notifyAll();
//				}
//			} else {
//				log.debug("service list no change");
//			}
//		});
//
//		ThreadPoolFactory.getHeartbeatExecutor().execute(() -> {
//			List<UrlInfo> services = urlInfoService.getUrlList(null);
//			String md5 = ServiceHelper.urlMd5(services);
//			if (!md5.equalsIgnoreCase(urlListMd5)) {
//				synchronized (registerMysql.getUrlMonitor()) {
//					urlListMd5 = md5;
//					registerMysql.getUrlMonitor().notifyAll();
//				}
//			} else {
//				log.debug("url list no change");
//			}
//		});
//
//	}
//}
