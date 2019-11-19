package com.haven.simplej.sequence;

import com.google.common.collect.Maps;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.concurrent.jsr166e.LongAdder;
import com.vip.vjtools.vjkit.net.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public class SequenceUtil extends com.vip.vjtools.vjkit.id.IdUtil {

	/**
	 * 请求跟踪的链路id
	 */
	private static final String TRACE_ID = "traceId";
	/**
	 * 进程号
	 */
	private static String pid;
	/**
	 * 本机ip后两段，如：10.168.10.129 => 10129
	 */
	private static String ipSuffix;
	/**
	 * 累计器初始值
	 */
	private static long initValue = 0;

	static {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		pid = name.split("@")[0];
		String[] values = NetUtil.getLocalHost().split("\\.");
		ipSuffix = values[2] + values[3];
	}

	/**
	 * 默认的累加器key
	 */
	private static final String DEFAULT_BUS_KEY = "default_key";
	/**
	 * 计数器
	 * 每个业务对应一个累加器
	 */
	private static Map<String, LongAdder> counterMap = Maps.newConcurrentMap();

	private static ThreadLocal<Map<Long, Long>> threadCounter = ThreadLocal.withInitial(() -> new HashMap<>());

	/**
	 * 生成唯一id，计算速度要高于UUID
	 * 如果只是想要得到一个唯一的id，比如链路id，可以采用该方法
	 * @return String id
	 */
	public static String generateId() {
		return generateId("");
	}

	/**
	 * 生成唯一id，在高并发的情况下，计算速度要高于UUID
	 * 如果只是想要得到一个唯一的id，比如链路id，可以采用该方法
	 * @param salt 加入盐计算
	 * @return String id
	 */
	public static String generateId(String salt) {
		long time = System.currentTimeMillis();
		long i = 0L;
		if (!threadCounter.get().containsKey(time)) {
			threadCounter.get().put(time, i);
		} else {
			i = threadCounter.get().get(time);
			threadCounter.get().put(time, ++i);
		}

		StringBuilder idString = new StringBuilder(30);
		idString.append(StringUtils.trimToEmpty(salt)).append(ipSuffix).append(pid).append(Thread.currentThread().getId()).append(i).append(time);
		return idString.toString();
	}

	/**
	 * 产生线性递增的id
	 * @return long id值
	 */
	public static long increaseId() {

		return increaseId(DEFAULT_BUS_KEY);
	}

	/**
	 * 产生线性递增的id
	 * @param busKey 业务key，每一个业务都应该有一个独立的key，粒度越细也好
	 * @return long id值
	 */
	public static long increaseId(String busKey) {

		if (!counterMap.containsKey(busKey)) {
			synchronized (busKey.intern()) {
				if (!counterMap.containsKey(busKey)) {
					System.out.println("lock " + busKey);
					counterMap.put(busKey, new LongAdder());
					counterMap.get(busKey).add(initValue);
				}
			}
		}
		counterMap.get(busKey).increment();
		return counterMap.get(busKey).longValue();
	}

	/**
	 * 初始化累加器起始值
	 * @param value 起始值
	 */
	public static void setStartValue(long value) {
		initValue = value;
	}

	/**
	 * 指定当前请求执行的链路id
	 * @param traceId 自定义的id
	 * @return 链路id
	 */
	public static String putTraceId(String traceId) {
		String msgId = MDC.get(TRACE_ID);
		if (StringUtil.isEmpty(msgId)) {
			if (StringUtil.isEmpty(traceId)) {
				msgId = SequenceUtil.generateId();
			} else {
				msgId = traceId;
			}
		}
		MDC.put(TRACE_ID, msgId);
		return msgId;
	}

	/**
	 * 获取已经设置了的
	 * @return
	 */
	public static String getTraceId(){
		return putTraceId(null);
	}
}
