package com.haven.simplej.rpc.center.sequence;

import com.google.common.collect.Maps;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.center.service.ISequenceService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.haven.simplej.time.enums.DateStyle;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列号管理器，基础工具设施
 * @author: havenzhang
 * @date: 2018/6/10 23:42
 * @version 1.0
 */
public class SequenceManager {

	private static ISequenceService sequenceService;

	private static ThreadLocal<AtomicLong> counter = ThreadLocal.withInitial(() -> new AtomicLong(100000));

	/**
	 * 默认的步长
	 */
	private static final int DEFAULT_STEP = 3000;
	/**
	 * 当前实例下某个业务的唯一id
	 */
	private static Map<String, Integer> businessIdMap = Maps.newConcurrentMap();

	private static ISequenceService getSequenceService() {
		if (sequenceService == null) {
			synchronized (ISequenceService.class) {
				if (sequenceService == null) {
					sequenceService = ServiceProxy.create().setInterfaceClass(ISequenceService.class).build();
				}
			}
		}
		return sequenceService;
	}

	/**
	 * 幂等接口
	 * 业务需要用到seq的时候，应提前注册好seq信息
	 * @param busTypeKey 业务key
	 * @param step 步长
	 */
	public static void registerSeq(String busTypeKey, int step) {
		String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		int businessId = getSequenceService().register(namespace, busTypeKey, step);
		businessIdMap.put(busTypeKey, businessId);
	}

	/**
	 * 生成唯一序列号
	 * YYYYMMDDHHMMSS + businessId+ 6位自增序列号
	 * @return String
	 */
	public static String getSequenceNo(String busTypeKey) {
		if (!businessIdMap.containsKey(busTypeKey)) {
			registerSeq(busTypeKey, DEFAULT_STEP);
		}
		int businessId = businessIdMap.get(busTypeKey);

		long current = counter.get().incrementAndGet();
		if (current > 999999) {
			synchronized (counter) {
				if (current > 999999) {
					counter.get().set(100000);
				}
			}
			current = counter.get().incrementAndGet();
		}
		StringBuilder seq = new StringBuilder();
		seq.append(DateUtils.dateToString(new Date(), DateStyle.YYYYMMDDHHMMSS.getValue()));
		seq.append(StringUtil.leftPad(String.valueOf(businessId), 5, '0'));
		seq.append(current);
		return seq.toString();
	}

	/**
	 * 业务key与当前计数器的缓存
	 */
	private static final Map<String, AtomicLong> seqenceNoMap = Maps.newConcurrentMap();
	/**
	 * 最大序列号
	 */
	private static final Map<String, Long> maxSequenceNoMap = Maps.newConcurrentMap();

	/**
	 * 基本递增序列号
	 * @param busTypeKey  业务key
	 * @return 序列号
	 */
	public static long getNextNo(String busTypeKey) {
		AtomicLong current = getSequence(busTypeKey, false);
		long max = maxSequenceNoMap.get(busTypeKey);
		long value = current.get();
		if (value > max - 1) {
			current = getSequence(busTypeKey, true);
		}
		return current.getAndIncrement();
	}

	public static AtomicLong getSequence(String busTypeKey, boolean remote) {
		String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		int sequenceStep = PropertyManager.getInt(RpcConstants.SEQUENCE_STEP, 3000);
		AtomicLong current = seqenceNoMap.get(busTypeKey);
		if (current == null || remote) {
			synchronized (busTypeKey.intern()) {
				current = seqenceNoMap.get(busTypeKey);
				if (current == null) {
					long startNo = getSequenceService().getNextShortSeqNo(namespace, busTypeKey, sequenceStep);
					current = new AtomicLong(startNo);
					seqenceNoMap.put(busTypeKey, current);
					maxSequenceNoMap.put(busTypeKey, startNo + sequenceStep);
				}
			}
		}
		return current;
	}
}
