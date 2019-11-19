package com.haven.simplej.db.sql.log;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.center.model.SqlLogModel;
import com.haven.simplej.rpc.center.service.MetricService;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.haven.simplej.time.enums.DateStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * 慢查询记录器
 * @author: havenzhang
 * @date: 2019/1/11 18:42
 * @version 1.0
 */
@Slf4j
public class SlowQuerySqlLogger {


	/**
	 * 前100的慢SQL
	 */
	private static final TreeSet<SqlLogModel> top100 = Sets.newTreeSet();

	/**
	 * 远程测量上报服务
	 */
	private static MetricService metricService;


	/**
	 * 普通SQL日志线程池
	 */
	private static final Executor executor = new ThreadPoolExecutor(2, 4, 10L, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<>(10000), Executors.defaultThreadFactory(), (r, executor) -> System.out.println(
					"sql over list"));
	/**
	 * 慢SQL线程池
	 */
	private static final Executor slowSqlexecutor = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<>(50000), Executors.defaultThreadFactory(), (r, executor) -> System.out.println(
					"slow sql over list"));


	/**
	 * 记录SQL日志
	 * @param sql SQL
	 * @param args 参数
	 * @param cost 执行耗时 毫秒
	 */
	public static void log(String sql, Object[] args, long cost) {
		SqlLogModel sqlLogModel = new SqlLogModel();
		sqlLogModel.setSql(sql);
		String namespace = PropertyManager.get("app.name");
		sqlLogModel.setNamespace(namespace);
		sqlLogModel.setParamValues(JSON.toJSONString(args));
		sqlLogModel.setMsgId(SequenceUtil.getTraceId());
		sqlLogModel.setCurrentTime(System.currentTimeMillis());
		sqlLogModel.setCost(cost);
		//测量服务
		String sqlOutFileName = PropertyManager.get(Constant.SQL_LOG_OUTPUT_FILE_KEY,
				Constant.SQL_LOG_OUTPUT_FILE_DEFAULT);
		if (sqlOutFileName.contains("%s")) {
			String date = DateUtils.getCurrentDate(DateStyle.YYYYMMDD.getValue());
			sqlOutFileName = String.format(sqlOutFileName, date);
		}
		File sqlOutFile = new File(sqlOutFileName);
		executor.execute(() -> {
			try {
				//写本地SQL日志文件
				FileUtils.writeStringToFile(sqlOutFile, JSON.toJSONString(sqlLogModel)+"\n", Constant.DEFAUL_ENCODE, true);

				metricService = SpringContext.getBean("metricService");
				if (metricService != null) {
					//上报至服务治理中心
					String host = PropertyManager.get(Constant.RPC_PROXY_HOST_KEY);
					String port = PropertyManager.get(Constant.RPC_PROXY_PORT_KEY);
					if (StringUtil.isNotEmpty(host) && StringUtil.isNotEmpty(port)) {
						metricService.reportSqlCall(sqlLogModel);
					} else {
						log.debug("rpc proxy not set ,do not report sql execute result");
					}
				}
			} catch (Exception e) {
				log.error("write sql to log error", e);
			}
		});
		/**
		 * 慢SQL登记，单线程登记
		 */
		slowSqlexecutor.execute(() -> logTopSlowSql(sqlLogModel));
	}

	/**
	 * 记录前N个慢SQL，单线程执行
	 * @param sqlLogModel SQL信息
	 */
	public static void logTopSlowSql(SqlLogModel sqlLogModel) {

		long threshold = PropertyManager.getLong(Constant.SLOW_SQL_LOG_COST_TIME_KEY,
				Constant.SLOW_SQL_LOG_COST_TIME_DEFAULT);
		if (sqlLogModel.getCost() < threshold) {
			//小于5毫秒的SQL不记录
			System.out.println("小于5毫秒的SQL不记录,cost:" + sqlLogModel.getCost());
			return;
		}

		top100.add(sqlLogModel);
		String sqlOutFile = PropertyManager.get(Constant.SLOW_SQL_LOG_OUTPUT_FILE_KEY,
				Constant.SLOW_SQL_LOG_OUTPUT_FILE_DEFAULT);
		if (sqlOutFile.contains("%s")) {
			String date = DateUtils.getCurrentDate(DateStyle.YYYYMMDD.getValue());
			sqlOutFile = String.format(sqlOutFile, date);
		}
		if (top100.size() > 100) {
			for (SqlLogModel logModel : top100) {
				try {
					FileUtils.writeStringToFile(new File(sqlOutFile), JSON.toJSONString(sqlLogModel) + "\n",
							Constant.DEFAUL_ENCODE, true);
				} catch (Exception e) {
					log.error("write slow sql to log error", e);
				}

			}
			top100.clear();
		}
	}
}
